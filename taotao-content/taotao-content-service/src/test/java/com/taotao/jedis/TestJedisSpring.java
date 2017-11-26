package com.taotao.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {
	
	private ApplicationContext applicationContext;

	@Test
	public void testJedisClientPool() throws Exception{
		
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		
		//获取JedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		
		//使用JedisClient 对象 操作redis
		jedisClient.set("jedisClient1", "mytest1");
		
		
		String result = jedisClient.get("jedisClient1");
		System.out.println(result);
		
	}

}
