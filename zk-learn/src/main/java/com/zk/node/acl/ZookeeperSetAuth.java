package com.zk.node.acl;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * 权限控制
 * 
 * @author think
 *
 */
public class ZookeeperSetAuth {

	public static void main(String[] args) throws Exception {
		ZooKeeper zookeeper = new ZooKeeper(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
				50000, null);
		/**
		 * ACL的表示方法为：scheme:id:permission
		 * 权限有4种模式：
		 * world：默认方式，一直最开放的权限控制模式。这种模式可以看做为特殊的Digest
		 * digest：即用户名:密码这种方式认证，这也是业务系统中最常用的
		 * ip：使用Ip地址认证
		 * super：超级用户模式，在超级用户模式下可以对ZK任意进行操作
		 */
		
		//可以通过此方式设置节点的acl方式
//		List<ACL> acls = new ArrayList<ACL>(2);     
		  
//		Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin123"));  
//		ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);  
//		  
//		Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("guest:guest123"));  
//		ACL acl2 = new ACL(ZooDefs.Perms.READ, id2); 
		
		//也可以通过此方式设置节点acl认证的方式
//		zookeeper.addAuthInfo("digest", "foo:true".getBytes());
		zookeeper.addAuthInfo("ip", "192.168.3.24".getBytes());
		String path = "/zk-test-ephemeral-";
		zookeeper.create(path, "init".getBytes(), Ids.CREATOR_ALL_ACL,
				CreateMode.EPHEMERAL);

		ZooKeeper zookeeper2 = new ZooKeeper(
				"192.168.3.22:2181,192.168.3.33:2181,192.168.3.44:2181,192.168.3.55:2181",
				50000, null);

		try{
			byte[] bytes = zookeeper2.getData(path, true, new Stat());
			System.out.println(new String(bytes));
		}catch(Exception e){
			e.printStackTrace();
		}

		Thread.sleep(Integer.MAX_VALUE);
	}

}
