package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;

	@Override
	public TaotaoResult addContent(TbContent content) {
		// 补全 pojo的属性
		content.setCreated(new Date());
		content.setUpdated(new Date());

		contentMapper.insert(content);
		
		//同步缓存
		//删除缓存的同步信息
		jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());

		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId) {

		// 1. 设置分页信息
		PageHelper.startPage(page, rows);

		// 2. 执行查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		
		List<TbContent> list = contentMapper.selectByExample(example);

		// 取查询结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);

		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());

		// 返回结果
		return result;

	}

	@Override
	public TaotaoResult updateContent(TbContent content) {
		TbContent oriContent = contentMapper.selectByPrimaryKey(content.getId());
		oriContent.setContent(content.getContent());
		oriContent.setPic(content.getPic());
		oriContent.setPic2(content.getPic2());
		oriContent.setSubTitle(content.getSubTitle());
		oriContent.setTitle(content.getTitle());
		oriContent.setTitleDesc(content.getTitleDesc());
		oriContent.setUpdated(new Date());
		
		contentMapper.updateByPrimaryKey(oriContent);
		
		//同步缓存
		//删除缓存的同步信息
		jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
		
		return TaotaoResult.ok();
	}

	@Override
	public void deleteContent(Long id) {
		TbContent tbContent = contentMapper.selectByPrimaryKey(id);
		contentMapper.deleteByPrimaryKey(id);
		
		//同步缓存
		//删除缓存的同步信息
		jedisClient.hdel(INDEX_CONTENT, tbContent.getCategoryId().toString());
	}

	@Override
	public List<TbContent> getContentByCid(Long cid) {
		
		//1. 先查询缓存,
		//添加缓存, 不能影响正常业务逻辑
		try {
			//查询缓存
			String json = jedisClient.hget(INDEX_CONTENT, String.valueOf(cid));
			
			//查询到结果, 把json 转成List 返回
			if(StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//查询不到, 走下面的流程
		
		//2. 如果缓存没有, 查询数据库
		TbContentExample example = new TbContentExample();
		Criteria c = example.createCriteria();
		//设置查询条件
		c.andCategoryIdEqualTo(cid);
		
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		
		//3. 查询数据后的结果添加缓存
		try {
			jedisClient.hset(INDEX_CONTENT, String.valueOf(cid), JsonUtils.objectToJson(list));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//4. 返回结果
		return list;
	}

}
