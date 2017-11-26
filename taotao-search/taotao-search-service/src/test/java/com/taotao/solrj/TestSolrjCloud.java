package com.taotao.solrj;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrjCloud {
	
	@Test
	public void testSolrCloudAddDocument() throws Exception{
		//创建一个CloudServer对象, 构造方法中需要制定zookeeper 的 地址列表
		CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.158.101:2181,192.168.158.101:2182,192.168.158.101:2183");
		//需要设置一个默认的collection
		cloudSolrServer.setDefaultCollection("collection2");
		
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		
		//向文档中添加域
		document.addField("id", "test001");
		document.addField("item_title", "test商品");
		document.addField("item_price", 1000);
		
		//把文档写入索引库
		cloudSolrServer.add(document);
		
		//提交
		cloudSolrServer.commit();
	}
	
	@Test
	public void testDeleteDocumentById() throws Exception{
		CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.158.101:2181,192.168.158.101:2182,192.168.158.101:2132");
		cloudSolrServer.setDefaultCollection("collection2");
		cloudSolrServer.deleteById("test001");
		cloudSolrServer.commit();
	}

}
