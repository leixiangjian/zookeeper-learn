package com.curator.learn.session;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * �����Ự
 * 
 * @author think
 *
 */
public class CuratorCrtSession {

	public static void main(String[] args) throws InterruptedException {
		
		/**
		 * ���Բ���
		 * RetryPolicy������
		 * sleeper:��ʼsleepʱ��
		 * retryCount:�Ѿ����ԵĴ���
		 * elapsedTimeMs:�ӵ�һ�����Կ�ʼ�Ѿ����ѵ�ʱ�䣬��λΪ����
		 * 
		 */
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		
		/** 
		 * ����һ���ͻ���
		 * ����˵����
		 * connectString:���������ӵ�ַ
		 * sessionTimeoutMs:�Ự��ʱʱ��,��λ���룬Ĭ��60000ms
		 * connectionTimeoutMs:���Ӵ�����ʱʱ�䣬��λ����,Ĭ����15000ms
		 * 
		 */
		CuratorFramework client = CuratorFrameworkFactory.newClient(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181", 5000, 3000,
				retryPolicy);
		client.start();
		
		Thread.sleep(Integer.MAX_VALUE);
		
	}

}
