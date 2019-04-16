package com.dao;

import java.util.Map;
import java.util.Set;

import java.net.URLDecoder ;

import org.jboss.com.sun.net.httpserver.HttpExchange;

import com.util.Email;

public class jianyiDao {
	
	public void presend(HttpExchange exchange , Map<String , String> param) throws Exception{
		
		StringBuffer sf = new StringBuffer();
		sf.append(URLDecoder.decode(param.get("strtext") , "utf-8"));
		param.remove("strtext");
		Set<String> keySet = param.keySet(); 
		if(!keySet.isEmpty()){
			
			for (String string : keySet) {
				sf.append("<img src='");
				sf.append(param.get(string));
				sf.append("'/>");
			}
		}
		
		if(sf == null || "".equals(sf.toString().trim())){
			return;
		}
		//发送邮件，封装好了,传过去字符串就行。这个邮件是只包含文本的
		Email.send(sf.toString());
	}
}
