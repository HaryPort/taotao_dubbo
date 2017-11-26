package com.taotao.pageHelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {

	@Test
	public void testPageHelper() throws Exception{
		
		//1. 在mybatis config 中配置分頁插件
		
		//2. 在執行查询之前配置分页插件, 使用PageHelper的静态方法
		PageHelper.startPage(1, 10);
		
		//3. 执行查询
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		
		TbItemMapper mapper = applicationContext.getBean(TbItemMapper.class);
		//创建TbItemExample对象
		TbItemExample example = new TbItemExample();
		List<TbItem> list = mapper.selectByExample(example);
		
		//4. 取分页信息, 使用PageInfo对象取
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println(pageInfo.getTotal());
		
		System.out.println(pageInfo.getPages());
		System.out.println(list.size());
		
	}
	
	

}
