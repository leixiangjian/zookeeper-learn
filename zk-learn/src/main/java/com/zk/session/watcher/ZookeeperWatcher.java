package com.zk.session.watcher;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZookeeperWatcher implements Watcher {
	CountDownLatch connectedSemaphore = null;
	public ZookeeperWatcher(CountDownLatch connectedSemaphore){
		this.connectedSemaphore = connectedSemaphore;
	}
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event£º" + event);
        if (KeeperState.SyncConnected == event.getState()) {
        	connectedSemaphore.countDown();
        }

	}

}
