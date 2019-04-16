package com.wf.test ;

import com.wf.util.MusicParams ;

import java.util.Map ;
import java.util.HashMap ;

public class Test1{
	public static void main(String[] args) {
		String data = "";
		String url = "{\"hlpretag\":\"<span class='s-fc7'>\",\"hlposttag\":\"</span>\",\"#/download\":\"\",\"s\":\"xuezhiqian\",\"type\":\"1\",\"offset\":\"0\",\"total\":\"true\",\"limit\":\"30\",\"csrf_token\":\"\"}" ;
		Map<String , String> map = new HashMap<>() ;
		map.put("User-Agent" , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"); 
		try{
			data = MusicParams.paramsKey(url);
			data = MusicParams.doPost("https://music.163.com/weapi/cloudsearch/get/web?csrf_token=token=" , data , map) ;
		} catch(Exception e){
			System.out.println(e);
		}
		System.out.println(data);
	}
}