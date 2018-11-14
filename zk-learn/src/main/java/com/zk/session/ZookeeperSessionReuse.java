package com.zk.session;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.ZooKeeper;

import com.zk.session.watcher.ZookeeperWatcher;

/**
 * 会话复用 通过sessionId和sessionPwd
 * 
 * @author think
 *
 */
public class ZookeeperSessionReuse {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static CountDownLatch connectedSemaphore2 = new CountDownLatch(1);

	public static void main(String[] args) throws IOException, InterruptedException {
		ZooKeeper zookeeper = new ZooKeeper(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
				5000, new ZookeeperWatcher(connectedSemaphore));

		System.out.println(zookeeper.getState());
		try {
			connectedSemaphore.await();
		} catch (InterruptedException e) {
		}
		System.out.println("ZooKeeper session established.");
		long sessionId = zookeeper.getSessionId();
		byte[] sessionPwd = zookeeper.getSessionPasswd();

		zookeeper = new ZooKeeper(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
				5000, new ZookeeperWatcher(connectedSemaphore2), sessionId, sessionPwd);
		try {
			connectedSemaphore2.await();
		} catch (InterruptedException e) {
		}
		System.out.println("ZooKeeper session2 established.");

	}

}
