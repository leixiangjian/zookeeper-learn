package com.curator.learn.session.node;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.zookeeper.CreateMode;

public class CuratorCrtNode {
	static Logger log = Logger.getLogger(CuratorCrtNode.class);
	static {
		log.addAppender(new ConsoleAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN)));
	}

	public static void main(String[] args) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(10000, 3);
		CuratorFramework client = null;
		try {
			client = CuratorFrameworkFactory
					.newClient(
							"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
							5000, 3000, retryPolicy);
			client.start();
		} catch (Exception e) {
			log.error("连接异常", e.getCause());
		}
		log.info("创建连接成功");
		try {

			client.create().creatingParentsIfNeeded()
					.withMode(CreateMode.EPHEMERAL)
					.forPath("/zk-test-ephemeral-/c2", "xx".getBytes());
			log.info("创建节点成功");
			Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e) {
			log.error("创建节点异常", e);
		}
	}

}
