package com.taotao.fastdfs;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.taotao.controller.PageController;
import com.taotao.utils.FastDFSClient;

public class TestFastDFS {
	
	@Test
	public void testUploadFile() {
		
		//1. 向工程中添加jar
		
		//2. 创建一个配置文件, 配置tracker 服务器地址
		
		//3. 加载配置文件, TrackerClient对象
		try {
			ClientGlobal.init("E:/Eclipse_workspace_project/taotao-manager-web/src/main/resources/fastdfs/Client.conf");
			
			TrackerClient client = new TrackerClient();
			TrackerServer tackerServer = client.getConnection();
			StorageServer storageServer = null;
			StorageClient client2 = new StorageClient(tackerServer, storageServer);
			
			String [] paths = client2.upload_file("C:/Users/Administrator/Desktop/1.png", "png", null);
			for(String path: paths) {
				System.out.println(path);
			}
		} catch (IOException | MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//4. 使用TrackerClient对象获取TrackerServer对象
		
		//5. 创建一个StorageServer引用, null就可以
		
		//6. 创建一个storageClient 对象, 需要TrackerServer, StorageServer对象
		
		//7. 使用storageClient上传文件
		
	}
	
	@Test
	public void testFastDFSClient() throws Exception{
		FastDFSClient client = new FastDFSClient("E:/Eclipse_workspace_project/taotao-manager-web/src/main/resources/fastdfs/Client.conf");
		String str = client.uploadFile("C:/Users/Administrator/Desktop/2.jpg", "jpg");
		System.out.println(str);
	}

}
