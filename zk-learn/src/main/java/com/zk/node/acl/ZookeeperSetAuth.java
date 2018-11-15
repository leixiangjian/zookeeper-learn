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
 * Ȩ�޿���
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
		 * ACL�ı�ʾ����Ϊ��scheme:id:permission
		 * Ȩ����4��ģʽ��
		 * world��Ĭ�Ϸ�ʽ��һֱ��ŵ�Ȩ�޿���ģʽ������ģʽ���Կ���Ϊ�����Digest
		 * digest�����û���:�������ַ�ʽ��֤����Ҳ��ҵ��ϵͳ����õ�
		 * ip��ʹ��Ip��ַ��֤
		 * super�������û�ģʽ���ڳ����û�ģʽ�¿��Զ�ZK������в���
		 */
		
		//����ͨ���˷�ʽ���ýڵ��acl��ʽ
//		List<ACL> acls = new ArrayList<ACL>(2);     
		  
//		Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin123"));  
//		ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);  
//		  
//		Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("guest:guest123"));  
//		ACL acl2 = new ACL(ZooDefs.Perms.READ, id2); 
		
		//Ҳ����ͨ���˷�ʽ���ýڵ�acl��֤�ķ�ʽ
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
