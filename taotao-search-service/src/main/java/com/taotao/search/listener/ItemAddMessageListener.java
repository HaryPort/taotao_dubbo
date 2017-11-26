package com.taotao.search.listener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;

/**
 * 监听商品添加时间
 * @author Administrator
 *
 */
public class ItemAddMessageListener implements MessageListener{
	
	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		//1. 从消息中取商品id
		TextMessage textMessage = (TextMessage) message;
		
		try {
			//2. 根据商品id查询数据, 取商品信息
			String text = textMessage.getText();
			long itemId = Long.parseLong(text);
			//等待事务提交
			Thread.sleep(1000);
			SearchItem searchItem = searchItemMapper.getItemById(itemId);
			
			//3. 创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			//4. 向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			//5. 把文档对象写入索引库
			solrServer.add(document);
			//6. 提交
			solrServer.commit();
		} catch (JMSException | InterruptedException | SolrServerException | IOException e) {
			e.printStackTrace();
		}
		
	}

}
