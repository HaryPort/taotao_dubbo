package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;

/**
 * 搜索服务controller
 * 
 * @author Administrator
 *
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;

	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;

	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page,
			Model model) throws Exception{
		// 调用服务, 执行查询
		SearchResult result = null;
		// 把查询条件进行转码, 解决get乱码问题
		queryString = new String(queryString.getBytes("ISO8859-1"), "UTF-8");

		result = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
		// 把结果传递给页面
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("itemList", result.getItemList());
		model.addAttribute("page", page);

		// 返回逻辑视图
		return "search";
	}

}
