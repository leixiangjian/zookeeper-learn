package com.zk.session;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.ZooKeeper;

import com.zk.session.watcher.ZookeeperWatcher;

public class ZookeeperConstructor {
	//通过计数的方式来计算线程的任务是否完成，完成就减一，当计数器值到达0时，它表示所有的线程已经完成了任务，然后在闭锁上等待的线程就可以恢复执行任务
	public static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	public static void main(String[] args) throws IOException {
		/**参数说明：
		 * connectString:服务器列表
		 * sessionTimeOut:会话的超时时间，单位“毫秒”
		 * watcher:事件监听器
		 * sessionId:会话Id
		 * sessionPwd:会话密钥
		 * canBeReadonly:是否支持只读模式，如果集群中过半机器出现问题，其他的节点是否可以提供只读
		 * 
		 */
		ZooKeeper zookeeper = new ZooKeeper("192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
				5000, 
				new ZookeeperWatcher(connectedSemaphore));
		System.out.println(zookeeper.getState());
		try {
			connectedSemaphore.await();
		} catch (InterruptedException e) {
		}
		System.out.println("ZooKeeper session established.");
	}

}
