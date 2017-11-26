package com.taotao.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class TestSolrj {
	
	@Test
	public void testAddDocument() throws Exception{
		//创建一个solrServer对象,创建一个HttpSolrServer 对象
		
		//需要指定 solr服务url
		SolrServer server = new HttpSolrServer("http://192.168.158.101:8080/solr/collection1");
		
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		
		//像文档中添加域, 必须有id域 , 域的名称必须在schema.xml中定义
		document.addField("id", "test001");
		document.addField("item_title", "测试商品1");
		document.addField("item_price", 1000);
		//把文档对象写入索引库
		server.add(document);
		//提交
		server.commit();
	}
	
	@Test
	public void testDeleteDocumentById() throws Exception{
		SolrServer server = new HttpSolrServer("http://192.168.158.101:8080/solr/collection1");
		
		server.deleteById("test001");
		server.commit();
	}
	
	@Test
	public void testDeleteDocumentByQueue() throws Exception{
		SolrServer server = new HttpSolrServer("http://192.168.158.101:8080/solr/collection1");
		
		server.deleteByQuery("id:123");
		
		server.commit();
	}
	
	@Test
	public void testSearchDocment() throws Exception{
		//创建一个solrServer对象
		SolrServer server = new HttpSolrServer("http://192.168.158.101:8080/solr/collection1");
		//创建一个solrQuery对象
		SolrQuery query = new SolrQuery();
		
		//设置查询条件, 过滤条件, 分页条件, 排序条件, 高亮
		//query.set("q", "*:*"); //query.setQuery("*:*");
		query.setQuery("手机");
		//分页条件
		query.setStart(30);
		query.setRows(10);
		//设置默认搜索域
		query.set("df", "item_keywords");
		//设置高亮
		query.setHighlight(true);
		//设置高亮显示的域
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		//执行查询, 得到一个response对象
		QueryResponse response = server.query(query);
		
		//取查询结果
		SolrDocumentList solrDocumentList = response.getResults();
		//取查询结果的总记录数
		System.out.println("查询结果的总记录数: " + solrDocumentList.getNumFound());
		
		for(SolrDocument document : solrDocumentList) {
			System.out.println(document.get("id"));
			
			//取高亮 显示
			Map<String, Map<String, List<String>>>  map = response.getHighlighting();
			List<String> list = map.get(document.get("id")).get("item_title");
			String itemTitle = "";
			if(!CollectionUtils.isEmpty(list)) {
				itemTitle = list.get(0);
			}else {
				itemTitle = (String) document.get("item_title");
			}
			System.out.println(itemTitle);
			System.out.println(document.get("item_sell_point"));
			System.out.println(document.get("item_price"));
			System.out.println(document.get("item_image"));
			System.out.println(document.get("item_category_name"));
			System.out.println("-------------------------------");
		}
		
	}

}
