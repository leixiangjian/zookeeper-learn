package com.zk.node.data;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.zk.session.watcher.ZookeeperWatcher;

public class ZookeeperGetNodeData {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static String path = "/zk-test-ephemeral-";

	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
				5000, new ZookeeperWatcher(connectedSemaphore));
		connectedSemaphore.await();
		// ͬ����ʽ
		zk.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL);
		
		//stat:��Ҫ����һ������Ϊ�յľɵ�stat�������ñ������ڷ���ִ�й����У������Է������Ӧ����stat�����滻
		Stat stat = new Stat();
		System.out.println(new String(zk.getData(path, true, stat)));
		System.out.println(stat.getCzxid() + "," + stat.getMzxid() + ","
				+ stat.getVersion());

		// �첽��ʽ
		zk.getData(path, true, new IDataCallback(), null);
		Thread.sleep(Integer.MAX_VALUE);
	}

}

class IDataCallback implements AsyncCallback.DataCallback {
	public void processResult(int rc, String path, Object ctx, byte[] data,
			Stat stat) {
		System.out.println(rc + ", " + path + ", " + new String(data));
		System.out.println(stat.getCzxid() + "," + stat.getMzxid() + ","
				+ stat.getVersion());
	}
}
