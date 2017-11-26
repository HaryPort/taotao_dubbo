package com.taotao.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;

/**
 * 商品数据导入索引库
 * 
 * @author Administrator
 *
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Autowired
	private SolrServer solrServer;

	@Override
	public TaotaoResult importItemsToIndex() {

		// 1. 先查询所有商品的数据
		List<SearchItem> list = searchItemMapper.getItemList();

		// 2. 遍历商品数据, 添加到索引库
		for(SearchItem searchItem : list) {

			// 2.1 创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			// 2.2 向文档中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			// 2.3 把文档写入索引库
			try {
				solrServer.add(document);
			} catch (SolrServerException e) {
				e.printStackTrace();
				return TaotaoResult.build(500, "solr server met some exception...");
			} catch (IOException e) {
				e.printStackTrace();
				return TaotaoResult.build(500, "solr server met some exception...");
			}
			
		}

		// 3. 提交
		try {
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "solr server met some exception...");
		} catch (IOException e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "solr server met some exception...");
		}

		// 4. 返回添加成功
		return TaotaoResult.ok();
	}

}
