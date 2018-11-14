package com.zk.node;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

import com.zk.session.watcher.ZookeeperWatcher;

/**
 * 创建数据节点
 * 
 * @author think
 *
 */
public class ZookeeperCrtNode {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	public static void main(String[] args) throws Exception {
		// 通过同步方式来创建节点
		ZooKeeper zookeeper = new ZooKeeper(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
				5000, //
				new ZookeeperWatcher(connectedSemaphore));
		connectedSemaphore.await();

		/**
		 * 参数说明: 
		 * path：数据节点路径 
		 * data[]:数据节点内容 
		 * acl:节点的ACL策略， ALL = READ | WRITE |
		 * CREATE | DELETE | ADMIN;
		 * createMode:节点类型,可以分为4中，持久，持久顺序，临时，临时顺序
		 * 
		 * 异步处理多出两个参数:
		 * StringCallback ：回调对象
		 * ctx:回调时传输的内容
		 * 
		 */
		String path1 = zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Success create znode: " + path1);

		String path2 = zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("Success create znode: " + path2);

		// 通过异步方式来创建节点
		zookeeper.create("/zk-Asyn-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				new IStringCallback(), "I am context.");

		zookeeper.create("/zk-Asyn-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				new IStringCallback(), "I am context.");

		zookeeper.create("/zk-Asyn-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
				new IStringCallback(), "I am context.");
		
		Thread.sleep(Integer.MAX_VALUE);
	}

}

class IStringCallback implements AsyncCallback.StringCallback {
	/**
	 * rc：0成功，-4连接断开，-110节点已经存在，-112会话过期
	 */
	public void processResult(int rc, String path, Object ctx, String name) {
		System.out.println("Create path result: [" + rc + ", " + path + ", "
				+ ctx + ", real path name: " + name);
	}
}
