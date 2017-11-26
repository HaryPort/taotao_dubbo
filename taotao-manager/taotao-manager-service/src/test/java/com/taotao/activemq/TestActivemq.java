package com.taotao.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActivemq {
	
	@Test
	public void testQueueProducer() throws Exception{
		//1. 创建一个连接工厂对象ConnectionFactory. 需要指定mq服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.158.102:61616");
		//2. 使用connectionFactory 创建一个连接
			Connection connection = connectionFactory.createConnection();
		//3. 开启连接, 调用start方法
			connection.start();
		//4. 使用connection对象创建一个会话session
			//a. 第一个参数: 是否开启事务, 分布式事务, 一般不使用, 保证事务的最终一致, 可以使用消息队列queue.
						//如果第一个参数为true, 第二个参数自动忽略
						//如果第一个参数为false, 第二个参数为消息的应答模式, 一般是自动应答模式
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5. 使用session 创建一个目的地对象destination, 一种是topic, 一种是queue
			// 参数含义: 消息队列的名称
			Queue queue = session.createQueue("test-queue");
		//6. 使用session 创建一个producer 对象
			MessageProducer producer = session.createProducer(queue);
		//7. 创建一个TextMessage 对象
			/*TextMessage textMessage = new ActiveMQTextMessage();
			textMessage.setText("hello activemq");*/
			TextMessage textMessage = session.createTextMessage("hello activemq");
		//8. 发送消息
			producer.send(textMessage);
		//9. 关闭资源
			producer.close();
			session.close();
			connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws Exception{
		//1. 创建一个连接工厂对象
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.158.102:61616");
		//2. 使用一个连接工厂创建一个连接
			Connection connection = connectionFactory.createConnection();
		//3. 开启连接
			connection.start();
		//4. 使用一个连接对象创建一个session对象
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5. 使用session 创建一个Destination对象, Destination 应该和消息的发送端一致
			Queue queue = session.createQueue("test-queue");
		//6. 使用session创建一个consumer 对象
			MessageConsumer consumer = session.createConsumer(queue);
		//7. 向Consumer对象中设置一个MessageListener对象, 用来接收消息
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					//8. 取消息的内容
					//9. 打印消息的内容
					if(message instanceof TextMessage){
						TextMessage textMessage = (TextMessage) message;
						try {
							System.out.println(textMessage.getText());
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
			
		//系统等待
		/*while(true) {
			Thread.sleep(100);
		}*/
		System.in.read();
		//10. 关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicProducer() throws Exception{
		//1. 创建一个连接工厂对象, 
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.158.102:61616");
		//2. 创建一个连接对象
			Connection connection = connectionFactory.createConnection();
		//3. 开启连接
			connection.start();
		//4. 创建session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5. 创建destination
			Topic topic = session.createTopic("test-topic");
		//6. 创建一个producer 对象
			MessageProducer messageProducer = session.createProducer(topic);
		//7. 创建一个消息
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText("Hello Topic");
		//8. 发送消息
			messageProducer.send(textMessage);
		//9. 关闭资源
			messageProducer.close();
			session.close();
			connection.close();
	}
	
	@Test
	public void testTopicComsumer() throws Exception{
		//1. 创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.158.102:61616");
	//2. 使用一个连接工厂创建一个连接
		Connection connection = connectionFactory.createConnection();
	//3. 开启连接
		connection.start();
	//4. 使用一个连接对象创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	//5. 使用session 创建一个Destination对象, Destination 应该和消息的发送端一致
		Topic topic = session.createTopic("test-topic");
	//6. 使用session创建一个consumer 对象
		MessageConsumer consumer = session.createConsumer(topic);
	//7. 向Consumer对象中设置一个MessageListener对象, 用来接收消息
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				//8. 取消息的内容
				//9. 打印消息的内容
				if(message instanceof TextMessage){
					TextMessage textMessage = (TextMessage) message;
					try {
						System.out.println(textMessage.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
	//系统等待
	/*while(true) {
		Thread.sleep(100);
	}*/
	System.out.println("消费者3");
	System.in.read();
	//10. 关闭资源
	consumer.close();
	session.close();
	connection.close();
	}

}
