package com.taotao.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestSpringActiveMQ {
	
	private ApplicationContext applicationContext;

	@Test
	public void testJmsTemplate() throws Exception{
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		
		//2. 从容器中获得JmsTemplate对象
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		
		//3. 从容器中获得Destination对象
		Destination destination = (Destination) applicationContext.getBean("test-queue");
		
		//4. 发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createTextMessage("test spring activemq send queue...");
				return message;
			}
		});
		
	}

}
