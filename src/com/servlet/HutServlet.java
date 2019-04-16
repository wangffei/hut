package com.servlet ;

import com.interfaces.HttpServlet ;
import com.html.WriteHtml ;

import org.jboss.com.sun.net.httpserver.HttpExchange;
import org.jboss.com.sun.net.httpserver.Headers;

import java.util.Map ;

import java.net.URLDecoder ;
import java.io.BufferedWriter ;
import java.io.OutputStreamWriter ;

import com.dao.HutDao ;

public class HutServlet implements HttpServlet {
	public void service(HttpExchange exchange , Map<String , String> param) throws Exception{
		String method = param.get("method");
		if("dfcx".equals(method)){
			dfcxMethod(exchange , param) ;
		}
	}

	public void dfcxMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		String build = param.get("build") ;
		String room = param.get("room") ;
		String area = param.get("area") ;
		String data = "" ;
		if(build == null || room == null || area == null){
			data = "{code:-1}" ;
		}else{
			data = HutDao.dfcxDao(build , room , area) ; 
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "text/html;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}
}