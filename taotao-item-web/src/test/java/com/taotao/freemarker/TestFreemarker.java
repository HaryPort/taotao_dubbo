package com.taotao.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreemarker {
	
	@Test
	public void testFreemarker() throws Exception{
		//1. 创建一个模版文件
		
		//2. 创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//2.1 Configuration 需要设置模版所在的路径
		configuration.setDirectoryForTemplateLoading(new File("E:\\Eclipse_workspace_project\\taotao-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		//2.2 Configuration 需要设置模版的字符编码
		configuration.setDefaultEncoding("UTF-8");
		//3. 使用Configuration对象加载一个模版文件, 需要制定模版文件的文件名
		//Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("studentList.ftl");
		//4. 创建一个数据集合, 可以是Pojo, 或者是Map, 推荐使用map
		Map<String, Object> map = new HashMap<>();
		map.put("hello", "hello freemarker");
		Student student = new Student(123, "小名", 22, "上海");
		Student student1 = new Student(456, "大名堂", 40, "南京");
		List<Student> list = new ArrayList<>();
		list.add(student);
		list.add(student1);
		map.put("studentList", list);
		map.put("date", new Date());
		map.put("val", "hello ftl");
		//5. 创建一个Writer对象, 输出文件的路径和文件名
		Writer out = new FileWriter("E:/out/studentList.html");
		//6. 使用模版对象的process 方法输出对象
		template.process(map, out);
		//7. 关闭流
		out.close();
	}

}
