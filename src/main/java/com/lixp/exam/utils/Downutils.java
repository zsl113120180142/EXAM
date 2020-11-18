package com.lixp.exam.utils;

import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * 下载文件防止文件名中文乱码
 */
public class Downutils {
		public static String filenameEncoding(String filename,String agent) throws IOException{
		
			if(agent.contains("Firefox")){
				
				BASE64Encoder base64Encoder=new BASE64Encoder();
				
				filename="=?utf-8?B?"+base64Encoder.encode(filename.getBytes("utf-8"))+"?=";
			
			}else if(agent.contains("MSIE")){
			
				filename=URLEncoder.encode(filename,"utf-8");
			
			}else{
				
				filename=URLEncoder.encode(filename, "utf-8");
			}
		
			return filename;
		}

}
