package com.zk.node;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.zk.session.watcher.ZookeeperWatcher;

/**
 * 节点是否存在检测
 * 
 * @author think
 *
 */
public class ZookeeperNodeCheck {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zk = null;

	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181", 5000, //
				new ZookeeperWatcher(connectedSemaphore));
		connectedSemaphore.await();

		final String path = "/zk-test-ephemeral-";

		/**
		 * path：检查节点路径 watch：是否复用zookeeper中的默认watcher
		 * 
		 * 主要作用还是用于监听节点的变化
		 * 
		 */
		final Watcher watcher = new Watcher() {
			public void process(WatchedEvent event) {
				try {
					if (EventType.NodeCreated == event.getType()) {
						System.out.println("Node(" + event.getPath()
								+ ")Created");
						zk.exists(path,this );
					} else if (EventType.NodeDeleted == event.getType()) {
						System.out.println("Node(" + event.getPath()
								+ ")Deleted");
						zk.exists(path,this );
					} else if (EventType.NodeDataChanged == event.getType()) {
						System.out.println("Node(" + event.getPath()
								+ ")DataChanged");
						zk.exists(path,this );
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		Stat stat = zk.exists(path,watcher );
		
		System.out.println(stat);

		zk.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL);

		zk.setData(path, "123".getBytes(), -1);
		zk.delete( path, 1);
		Thread.sleep(Integer.MAX_VALUE);
	}
}
