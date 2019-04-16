package com.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.com.sun.net.httpserver.Headers;
import org.jboss.com.sun.net.httpserver.HttpExchange;

import com.dao.jianyiDao;
import com.interfaces.HttpServlet;

public class jianyiServlet  implements HttpServlet  {
	public void service(HttpExchange exchange , Map<String , String> param) throws Exception{
		
		jianyiDao dao = new jianyiDao();
		try{
			dao.presend(exchange, param);
		}catch(Exception e){
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
			Headers header = exchange.getResponseHeaders() ;
			header.set("Content-Type" , "text/html;charset=utf-8");
			exchange.sendResponseHeaders(500, 0);
			writer.write("faild");
			writer.flush();
			writer.close() ;
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "text/html;charset=utf-8");
		exchange.sendResponseHeaders(200, 0);
		writer.write("{msg:ok}");
		writer.flush();
		writer.close() ;
	}
}
