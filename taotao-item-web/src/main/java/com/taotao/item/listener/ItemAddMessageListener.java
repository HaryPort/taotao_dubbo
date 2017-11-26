package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class ItemAddMessageListener implements MessageListener {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;

	@Override
	public void onMessage(Message message) {
		Writer out = null;
		try {
			//1. 从消息中取出商品id
			TextMessage textMessage = (TextMessage) message;
			String strId = textMessage.getText();
			Long itemId = Long.parseLong(strId);
			
			//等待事务提交
			Thread.sleep(1000);
			//2. 根据商品id查询商品信息和商品描述
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
			//3. 使用freemarker生成静态页面
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			//3.1 创建模版
			Template template = configuration.getTemplate("item.ftl");
			//3.2 加载模版
			//3.3 准备模版需要的数据
			Map<Object, Object> data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);
			
			//3.4 创建输出的目录及文件名
			out = new FileWriter(new File(HTML_OUT_PATH + strId + ".html"));
			
			//3.5 生成静态页面
			template.process(data, out);
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			try {
				if(out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
