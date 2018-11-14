package com.zk.node.data;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.zk.session.watcher.ZookeeperWatcher;

public class ZookeeperSetNodeData {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static String path = "/zk-test-ephemeral-";

	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
				5000, new ZookeeperWatcher(connectedSemaphore));
		connectedSemaphore.await();

		// 同步方式
		zk.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL);
		Stat stat = new Stat();
		zk.getData(path, true, stat);
		stat = zk.setData(path, "456".getBytes(),stat.getVersion() );
		System.out.println(stat.getCzxid() + "," + stat.getMzxid() + ","
				+ stat.getVersion());
		
		Stat stat2 = zk.setData(path, "456".getBytes(), stat.getVersion());
		System.out.println(stat2.getCzxid() + "," + stat2.getMzxid() + ","
				+ stat2.getVersion());
		
		// 异步方式
		zk.setData( path, "456".getBytes(), -1, new IStatCallback(), null );
		
		Thread.sleep( Integer.MAX_VALUE );
	}

}

class IStatCallback implements AsyncCallback.StatCallback{
	public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (rc == 0) {
            System.out.println("SUCCESS");
        }
    }
}
