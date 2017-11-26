package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	@Test
	public void testJedis() throws Exception{
		//创建一个jedis对象, 需要制定服务的ip和端口号
		Jedis jedis = new Jedis("192.168.158.99", 6379);
		
		//直接操作数据库
		jedis.set("jedis_key", "1234");
		
		String result = jedis.get("jedis_key");
		System.out.println(result);
		
		//关闭jedis
		jedis.close();
	}
	
	@Test
	public void testJedisPool() throws Exception{
		//创建一个数据库连接池对象(单利), 需要指定的服务ip和端口号
		JedisPool jedisPool = new JedisPool("192.168.158.99", 6379);
		//从连接池中获取一个连接
		Jedis jedis = jedisPool.getResource();
		
		//使用jedis操作数据库(方法级别使用)
		String result = jedis.get("jedis_key");
		System.out.println(result);
		
		//关闭jedis连接
		jedis.close();
		
		//关闭连接池
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() throws Exception{
		//创建一个JedisCluster对象, 构造参数Set类型, 集合中每个元素是HostAndPort类型
		Set<HostAndPort> nodes = new HashSet<>();
		//向集合中添加节点
		nodes.add(new HostAndPort("192.168.158.99", 7001));
		nodes.add(new HostAndPort("192.168.158.99", 7002));
		nodes.add(new HostAndPort("192.168.158.99", 7003));
		nodes.add(new HostAndPort("192.168.158.99", 7004));
		nodes.add(new HostAndPort("192.168.158.99", 7005));
		nodes.add(new HostAndPort("192.168.158.99", 7006));
		
		//JedisCluster操作Redis, 自带连接池. JedisCluster可以是单例的.
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("cluster-key", "hello Jedis cluster");
		
		String result = cluster.get("cluster-key");
		System.out.println(result);
		//系统关闭前, 关闭JedisCluster
		cluster.close();
	}

}
