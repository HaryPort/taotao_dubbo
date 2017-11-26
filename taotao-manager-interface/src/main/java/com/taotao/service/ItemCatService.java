package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;

/**
 * 产品分类
 * @author Administrator
 *
 */
public interface ItemCatService {
	
	public List<EasyUITreeNode> getItemCatList(long parentId);

}
