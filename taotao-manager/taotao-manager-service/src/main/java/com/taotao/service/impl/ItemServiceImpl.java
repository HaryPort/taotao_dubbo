package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.IDUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

/**
 * 商品管理服务
 * @author Harry
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource(name="itemAddTopic")
	private Destination destination;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	
	@Value("${TIME_EXPIRE}")
	private Integer TIME_EXPIRE;
	
	@Override
	public TbItem getItemById(long itemId) {
		//查询数据库之前先查缓存
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
			if(StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//如果缓存中没有,  查询数据库
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		
		//查询结果添加缓存
		try {
			//1. 把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
			//2. 设置过期时间, 提高缓存的利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE", TIME_EXPIRE);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		
		//1. 设置分页信息
		PageHelper.startPage(page, rows);
		
		//2. 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		
		//取查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		
		//返回结果
		return result;
	}

	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		
		//1. 生成商品的id
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//2. 补全item的属性
		//商品状态: 1-正常, 2-下架, 3-删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		
		itemMapper.insert(item);
		
		//3. 创建商品描述表对应的pojo
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		//4. 补全pojo的属性
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		
		itemDescMapper.insert(itemDesc);
		
		//向activemq发送商品添加消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createTextMessage(itemId + "");
				return message;
			}
		});
		
		//5. 返回结果
		return TaotaoResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		//查询数据库之前先查缓存
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
			if(StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		//查询结果添加缓存
		try {
			//1. 把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
			//2. 设置过期时间, 提高缓存的利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC", TIME_EXPIRE);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}
