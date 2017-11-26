<html>
	<head>
		<meta charset="UTF-8">
		<title>测试页面</title>
	</head>
	
	<body>
		<table border="1">
			<tr>
				<th>序号</th>	
				<th>学号</th>	
				<th>姓名</th>
				<th>年龄</th>
				<th>住址</th>
			</tr>
		
		<#list studentList as student>
			<#if student_index % 2 == 0>
				<tr bgcolor="red">
			<#else>
				<tr bgcolor="blue">
			</#if>
				<td>${student_index}</td>
				<td>${student.id}</td>
				<td>${student.name}</td>
				<td>${student.age}</td>
				<td>${student.address}</td>
			</tr>
			
		</#list>
		</table>
		
		日期类型处理: ${date?string('yyyy/mm/dd HH:mm:ss')} <br/>
		
		null的处理1: ${val!"默认值"} <br/>
		null的处理2: ${val!} <br/>
		
		使用if判断null值:
		<#if val??>
			val 是有值的.
		<#else>
			val 的值为null...
		</#if>
		
		<br/>
		<#include "hello.ftl">
	</body>

</html>