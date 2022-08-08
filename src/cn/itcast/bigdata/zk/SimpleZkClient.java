package cn.itcast.bigdata.zk;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

public class SimpleZkClient {

	private static final String connectString = "rendong01:2181,rendong02:2181,rendong03:2181";
	private static final int sessionTimeout = 2000;

	ZooKeeper zkClient = null;

	@Before
	public void init() throws Exception {
		zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				// 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
				System.out.println("收到事件通知后的回调函数	>>>	" + event.getType() + "---" + event.getPath());
				try {
					zkClient.getChildren("/", true);
					System.out.println("监听 / 的getChildren");
				} catch (Exception e) {
				}
			}
		});

	}

	/**
	 * 数据的增删改查
	 * 
	 * @throws InterruptedException
	 * @throws KeeperException
	 */

	// 创建数据节点到zk中
	@Test
	public void testCreate() throws KeeperException, InterruptedException {
		// 参数1：要创建的节点的路径 参数2：节点大数据 参数3：节点的权限 参数4：节点的类型
		System.out.println("start creating");
		try {
			String nodeCreated = zkClient.create("/eclipse", "hellozk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		} finally {
			System.out.println("end creating");
			//上传的数据可以是任何类型，但都要转成byte[]
		}
	}

	//判断znode是否存在
	@Test	
	public void testExist() throws Exception{
		System.out.println("-----start check exist---------");
		Stat stat = zkClient.exists("/eclipse", false);
		System.out.println(stat==null?"not exist":"exist");
		
		
	}
	
	// 获取子节点
	@Test
	public void getChildren() throws Exception {
		List<String> children = zkClient.getChildren("/", true);
		for (String child : children) {
			System.out.println(child);
		}
		Thread.sleep(Long.MAX_VALUE);
	}

	//获取znode的数据
	@Test
	public void getData() throws Exception {
		
		byte[] data = zkClient.getData("/eclipse", false, null);
		System.out.println(new String(data));
		
	}
	
	//删除znode
	@Test
	public void deleteZnode() throws Exception {
		
		//参数2：指定要删除的版本，-1表示删除所有版本
		zkClient.delete("/eclipse", -1);
		
		
	}
	//删除znode
	@Test
	public void setData() throws Exception {
		
		zkClient.setData("/eclipse", "imissyou angelababy".getBytes(), -1);
		
		byte[] data = zkClient.getData("/eclipse", false, null);
		System.out.println(new String(data));
		
	}
	
	
}
