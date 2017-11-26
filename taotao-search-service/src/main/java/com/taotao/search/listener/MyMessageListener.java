package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接收activemq 发送的消息
 * @author Administrator
 *
 */
public class MyMessageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		//1. 接收到消息
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				System.out.println(textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
