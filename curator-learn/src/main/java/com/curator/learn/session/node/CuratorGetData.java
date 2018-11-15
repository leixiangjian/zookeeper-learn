package com.curator.learn.session.node;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CuratorGetData {
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

	public static void main(String[] args) throws Exception {
		client.start();
		try {
			client.create().creatingParentsIfNeeded()
					.withMode(CreateMode.EPHEMERAL)
					.forPath("/zk-test-ephemeral-", "init".getBytes());
		} catch (Exception e) {
			log.error("创建节点异常", e);
		}
		Stat stat = new Stat();

		byte[] data = client.getData().storingStatIn(stat)
				.forPath("/zk-test-ephemeral-");
		log.info(new String(data));

	}

}
