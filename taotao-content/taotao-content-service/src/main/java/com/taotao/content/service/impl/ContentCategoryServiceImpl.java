package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{
	
	@Autowired
	private TbContentCategoryMapper contentCatetoryMapper;
	

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {

		TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
		
		//設置查詢條件
		Criteria c = tbContentCategoryExample.createCriteria();
		c.andParentIdEqualTo(parentId);
		
		List<TbContentCategory> list = contentCatetoryMapper.selectByExample(tbContentCategoryExample);
		
		List<EasyUITreeNode> nodes = new ArrayList<EasyUITreeNode>();
		for(TbContentCategory category : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(category.getId());
			node.setText(category.getName());
			node.setState(category.getIsParent()?"closed":"open");
			
			nodes.add(node);
		}
		
		return nodes;
	}


	@Override
	public TaotaoResult addContentCategory(Long parentId, String name) {

		// 创建POJO对象
		TbContentCategory contentCategory = new TbContentCategory();
		// 补全对象
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//状态: 可选值, 1(正常)  2(删除)
		contentCategory.setStatus(1);
		// 排序状态, 默认为1
		contentCategory.setSortOrder(1);
		//叶子节点
		contentCategory.setIsParent(Boolean.FALSE);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		// 插入到数据库
		contentCatetoryMapper.insert(contentCategory);
		
		//判断父节点的状态
		TbContentCategory parent = contentCatetoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) {
			//如果父节点为叶子节点, 则应该改为父节点
			parent.setIsParent(Boolean.TRUE);
			
			//更新父节点
			contentCatetoryMapper.updateByPrimaryKey(parent);
		}
		
		//	返回对象
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public TaotaoResult updateContentCategory(Long id, String name) {
		TbContentCategory contentCategory = contentCatetoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(name);
		
		contentCatetoryMapper.updateByPrimaryKey(contentCategory);
		
		return TaotaoResult.ok(contentCategory);
	}


	@Override
	public TaotaoResult deleteContentCategory(Long id) {
		TbContentCategory contentCategory = contentCatetoryMapper.selectByPrimaryKey(id);
		cursiveDeleteContentCategory(id, contentCategory);
		
		return TaotaoResult.ok(contentCategory);
	}

	private void cursiveDeleteContentCategory(Long id, TbContentCategory contentCategory) {
		if(contentCategory.getIsParent()) {
			updateContentCategoryStatus(contentCategory);
			
			TbContentCategoryExample contentCategoryExample = encapsulateContentCategoryExample(id);
			List<TbContentCategory> list = contentCatetoryMapper.selectByExample(contentCategoryExample);
			
			for(TbContentCategory value : list) {
				cursiveDeleteContentCategory(value.getId(), value);
			}
		}else {//不是Parent 的 目录, 可以直接删除
			updateContentCategoryStatus(contentCategory);
		}
	}


	private void updateContentCategoryStatus(TbContentCategory contentCategory) {
		contentCategory.setStatus(2);
		contentCatetoryMapper.updateByPrimaryKeySelective(contentCategory);
	}

	private TbContentCategoryExample encapsulateContentCategoryExample(Long id) {
		TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
		TbContentCategoryExample.Criteria criteria = contentCategoryExample.createCriteria();
		criteria.andParentIdEqualTo(id);
		return contentCategoryExample;
	}

}
