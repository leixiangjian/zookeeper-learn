package com.curator.learn.session.node;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.zookeeper.CreateMode;

public class CuratorAsynProcess {
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
	static CountDownLatch latch = new CountDownLatch(1);
	public static void main(String[] args) throws Exception {
		client.start();
		try {
			client.create().creatingParentsIfNeeded()
					.withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
						
						public void processResult(CuratorFramework client, CuratorEvent event)
								throws Exception {
							log.info("Event[code:"+event.getResultCode()+",type:"+event.getType()+"]");
							latch.countDown();
						}
					},tp)
					.forPath("/zk-test-ephemeral-", "init".getBytes());
		} catch (Exception e) {
			log.error("创建节点异常", e);
		}
		latch.await();
		log.info("创建节点成功");
	}

}
