package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品分类管理
 */
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService{
	
	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {

		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		
		//设置parentid
		criteria.andParentIdEqualTo(parentId);
		
		//根据父节点的id查询子节点列表
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		
		//转换成EasyUITreeNode列表
		List<EasyUITreeNode> nodes = new ArrayList<>();
		
		for(TbItemCat category: list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(category.getId());
			node.setText(category.getName());
			//如果该节点下有字节点, 则设置为closed
			node.setState(category.getIsParent()?"closed":"open");
			
			//添加节点列表
			nodes.add(node);
		}
		
		return nodes;
	}

}
