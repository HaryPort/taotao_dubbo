package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;

/**
 * 图片上传
 * @author Administrator
 *
 */
@Controller
public class PictureController {
	
	@Value("${IMAGE_SERVER_URL}")
	private String image_server_url;

	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String picUpload(MultipartFile uploadFile) {
		//接收上传的文件
		
		//取扩展名
		String originalFilename = uploadFile.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		
		Map<String, Object> result = new HashMap<>();
		//上传到图片服务器
		try {
			FastDFSClient client = new FastDFSClient("classpath:fastdfs/Client.conf");
			String url = client.uploadFile(uploadFile.getBytes(), extName);
			
			//响应上产图片  url
			url = image_server_url + url;
			
			//响应的上传图片的URL
			result.put("error", 0);
			result.put("url", url);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", 1);
			result.put("message", "图片上传失败");
		}
		
		return JsonUtils.objectToJson(result);
	}
}
