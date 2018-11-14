package com.zk.node;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.zk.session.watcher.ZookeeperWatcher;

/**
 * 删除节点
 * 
 * @author think
 *
 */
public class ZookeeperDelNode {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;
	public static void main(String[] args) throws Exception {
    	String path = "/zk-test-ephemeral-";
    	zk = new ZooKeeper("192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181", 
				5000, //
				new ZookeeperWatcher(connectedSemaphore));
    	connectedSemaphore.await();

    	zk.create( path, "你好".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL );
    	
    	/**
    	 * 參數：
    	 * path：節點路徑
    	 * version:節點版本，-1與0一致
    	 * 
    	 */
    	zk.delete( path, -1 );
    	
    	Thread.sleep( Integer.MAX_VALUE );
	}

}
