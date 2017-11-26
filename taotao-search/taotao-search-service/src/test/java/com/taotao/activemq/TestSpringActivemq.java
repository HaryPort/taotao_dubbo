package com.taotao.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringActivemq {

	private ApplicationContext applicationContext;

	@Test
	public void testSrpingActivemq() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		applicationContext.getBean("");
		System.in.read();
	}
}
