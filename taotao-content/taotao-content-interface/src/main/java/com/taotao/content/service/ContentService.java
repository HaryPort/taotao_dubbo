package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	
	TaotaoResult addContent(TbContent content);

	EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId);

	TaotaoResult updateContent(TbContent content);

	void deleteContent(Long id);
	
	List<TbContent> getContentByCid(Long cid);

}
