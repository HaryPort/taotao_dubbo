package com.taotao.item.controller;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 网页静态化的Controller
 * @author Administrator
 *
 */
@Controller
public class HtmlGenController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	/**
	 * 测试 Spring 和 Freemarker 的整合
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception{
		//1. 生成静态页面
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		Template template = configuration.getTemplate("hello.ftl");
		Map<Object, Object> dataModel = new HashMap<>();
		dataModel.put("hello", "hello freemarker test");
		Writer out = new FileWriter("E:/out/hello.html");
		template.process(dataModel, out);
		out.close();
		
		//2. 返回结果
		return "OK";
	}

}
