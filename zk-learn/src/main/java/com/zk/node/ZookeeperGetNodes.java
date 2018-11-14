package com.zk.node;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.zk.session.watcher.ZookeeperWatcher;

/**
 * 查询子节点
 * 
 * @author think
 *
 */
public class ZookeeperGetNodes {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	public static void main(String[] args) throws Exception {
		//同步方式获得子节点
		ZooKeeper zk = new ZooKeeper("192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181", 
				5000, //
				new ZookeeperWatcher(connectedSemaphore));
        connectedSemaphore.await();
        //注意临时节点下不能创建叶子节点
        zk.create("/zk-test-ephemeral-", "".getBytes(), 
        		  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create("/zk-test-ephemeral-"+"/c1", "".getBytes(), 
        		  Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.create("/zk-test-ephemeral-"+"/c2", "".getBytes(), 
      		  Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        
        List<String> childrenList = zk.getChildren("/zk-test-ephemeral-", true);
        System.out.println(childrenList);
        
		//异步方式获得子节点
        zk.getChildren("/zk-test-ephemeral-", true, new IChildren2Callback(), null);
        Thread.sleep( Integer.MAX_VALUE );
	}

}

class IChildren2Callback implements AsyncCallback.Children2Callback{
	public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("Get Children znode result: [response code: " + rc + ", param path: " + path
                + ", ctx: " + ctx + ", children list: " + children + ", stat: " + stat);
    }
}
