package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;

/**
 * 搜索查询库商品dao
 * 
 * @author Administrator
 *
 */
@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery query) throws Exception {
		// 根据对象进行查询
		QueryResponse response = solrServer.query(query);

		// 取查询结果
		SolrDocumentList list = response.getResults();
		// 取查询总记录数
		long numFound = list.getNumFound();
		SearchResult result = new SearchResult();
		result.setRecordCount(numFound);

		// 商品列表的list
		List<SearchItem> itemList = new ArrayList<SearchItem>();
		// 封装查询结果到SearchItem中, 把结果添加到SearchResult中
		for (SolrDocument document : list) {
			SearchItem item = new SearchItem();
			item.setCategory_name((String) document.get("item_category_name"));
			item.setId((String) document.get("id"));
			String image = (String) document.get("item_image");
			if(StringUtils.isNotBlank(image)) {
				image = image.split(",")[0];
			}
			item.setImage(image);
			item.setPrice((long) document.get("item_price"));
			item.setSell_point((String) document.get("item_sell_point"));

			// 取高亮显示
			Map<String, Map<String, List<String>>> map = response.getHighlighting();
			List<String> highlightList = map.get(document.get("id")).get("item_title");
			String itemTile = "";
			if (!CollectionUtils.isEmpty(highlightList)) {
				itemTile = highlightList.get(0);
			} else {
				itemTile = (String) document.get("item_title");
			}
			item.setTitle(itemTile);

			// 添加到商品列表
			itemList.add(item);
		}

		result.setItemList(itemList);
		return result;
	}

}
