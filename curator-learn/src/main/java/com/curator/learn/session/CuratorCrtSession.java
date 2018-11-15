package com.curator.learn.session;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 创建会话
 * 
 * @author think
 *
 */
public class CuratorCrtSession {

	public static void main(String[] args) throws InterruptedException {
		
		/**
		 * 重试策略
		 * RetryPolicy参数：
		 * sleeper:初始sleep时间
		 * retryCount:已经重试的次数
		 * elapsedTimeMs:从第一次重试开始已经花费的时间，单位为毫秒
		 * 
		 */
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		
		/** 
		 * 创建一个客户端
		 * 参数说明：
		 * connectString:服务器连接地址
		 * sessionTimeoutMs:会话超时时间,单位毫秒，默认60000ms
		 * connectionTimeoutMs:连接创建超时时间，单位毫秒,默认是15000ms
		 * 
		 */
		CuratorFramework client = CuratorFrameworkFactory.newClient(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181", 5000, 3000,
				retryPolicy);
		client.start();
		
		Thread.sleep(Integer.MAX_VALUE);
		
	}

}
