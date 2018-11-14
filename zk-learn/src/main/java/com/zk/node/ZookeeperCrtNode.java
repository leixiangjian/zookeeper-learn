package com.zk.node;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

import com.zk.session.watcher.ZookeeperWatcher;

/**
 * �������ݽڵ�
 * 
 * @author think
 *
 */
public class ZookeeperCrtNode {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	public static void main(String[] args) throws Exception {
		// ͨ��ͬ����ʽ�������ڵ�
		ZooKeeper zookeeper = new ZooKeeper(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
				5000, //
				new ZookeeperWatcher(connectedSemaphore));
		connectedSemaphore.await();

		/**
		 * ����˵��: 
		 * path�����ݽڵ�·�� 
		 * data[]:���ݽڵ����� 
		 * acl:�ڵ��ACL���ԣ� ALL = READ | WRITE |
		 * CREATE | DELETE | ADMIN;
		 * createMode:�ڵ�����,���Է�Ϊ4�У��־ã��־�˳����ʱ����ʱ˳��
		 * 
		 * �첽��������������:
		 * StringCallback ���ص�����
		 * ctx:�ص�ʱ���������
		 * 
		 */
		String path1 = zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Success create znode: " + path1);

		String path2 = zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("Success create znode: " + path2);

		// ͨ���첽��ʽ�������ڵ�
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
	 * rc��0�ɹ���-4���ӶϿ���-110�ڵ��Ѿ����ڣ�-112�Ự����
	 */
	public void processResult(int rc, String path, Object ctx, String name) {
		System.out.println("Create path result: [" + rc + ", " + path + ", "
				+ ctx + ", real path name: " + name);
	}
}
