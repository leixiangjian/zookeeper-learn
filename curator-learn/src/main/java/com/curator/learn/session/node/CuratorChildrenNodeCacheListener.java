package com.curator.learn.session.node;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.zookeeper.CreateMode;

public class CuratorChildrenNodeCacheListener {
	static Logger log = Logger.getLogger(CuratorCrtNode.class);
	static {
		log.addAppender(new ConsoleAppender(new PatternLayout(
				PatternLayout.TTCC_CONVERSION_PATTERN)));
	}
	static CuratorFramework client = CuratorFrameworkFactory
			.builder()
			.connectString(
					"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181")
			.sessionTimeoutMs(5000)
			.retryPolicy(new ExponentialBackoffRetry(10000, 3)).build();
	static ExecutorService tp = Executors.newFixedThreadPool(1);
	static CountDownLatch latch = new CountDownLatch(2);

	public static void main(String[] args) throws Exception {
		client.start();
		try {
			client.create().creatingParentsIfNeeded()
					.withMode(CreateMode.PERSISTENT)
					.inBackground(new BackgroundCallback() {
						public void processResult(CuratorFramework client,
								CuratorEvent event) throws Exception {
							log.info("Event[code:" + event.getResultCode()
									+ ",type:" + event.getType() + "]");
							latch.countDown();
						}
					}, tp).forPath("/zk-test-ephemeral-", "init".getBytes());
			@SuppressWarnings("resource")
			PathChildrenCache cache = new PathChildrenCache(client,
					"/zk-test-ephemeral-", true);
			cache.start(StartMode.POST_INITIALIZED_EVENT);
			cache.getListenable().addListener(new PathChildrenCacheListener() {

				public void childEvent(CuratorFramework client,
						PathChildrenCacheEvent event) throws Exception {
					switch (event.getType()) {
					case CHILD_ADDED:
						log.info("CHILD_ADDED,"+event.getData());
						break;
					case CHILD_UPDATED:
						log.info("CHILD_UPDATED,"+event.getData());
						break;
					case CHILD_REMOVED:
						log.info("CHILD_REMOVED,"+event.getData());
						break;
					default:
						break;
					}
					latch.countDown();
				}

			}, Executors.newFixedThreadPool(1));
			client.create().withMode(CreateMode.EPHEMERAL).forPath("/zk-test-ephemeral-/c2");
		} catch (Exception e) {
			log.error("创建节点异常", e);
		}
		latch.await();
		log.info("创建节点成功");
	}

}
