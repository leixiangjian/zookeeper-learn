package com.zk.session;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.ZooKeeper;

import com.zk.session.watcher.ZookeeperWatcher;

public class ZookeeperConstructor {
	//ͨ�������ķ�ʽ�������̵߳������Ƿ���ɣ���ɾͼ�һ����������ֵ����0ʱ������ʾ���е��߳��Ѿ����������Ȼ���ڱ����ϵȴ����߳̾Ϳ��Իָ�ִ������
	public static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	public static void main(String[] args) throws IOException {
		/**����˵����
		 * connectString:�������б�
		 * sessionTimeOut:�Ự�ĳ�ʱʱ�䣬��λ�����롱
		 * watcher:�¼�������
		 * sessionId:�ỰId
		 * sessionPwd:�Ự��Կ
		 * canBeReadonly:�Ƿ�֧��ֻ��ģʽ�������Ⱥ�й�������������⣬�����Ľڵ��Ƿ�����ṩֻ��
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
