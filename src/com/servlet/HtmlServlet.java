package com.servlet ;

import com.interfaces.HttpServlet ;
import com.html.WriteHtml ;

import org.jboss.com.sun.net.httpserver.HttpExchange;
import org.jboss.com.sun.net.httpserver.Headers;

import java.util.Map ;

import java.net.URLDecoder ;
import java.io.BufferedWriter ;
import java.io.OutputStreamWriter ;

public class HtmlServlet implements HttpServlet {
	public void service(HttpExchange exchange , Map<String , String> param) throws Exception{
		String method = param.get("method");
		if("jsDetail".equals(method)){
			jsMethod(exchange , param) ;
		}
	}

	public void jsMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		String zc = param.get("zc") ;
		String day = param.get("day") ;
		String classroom =  URLDecoder.decode(param.get("classroom") , "utf-8") ;
		StringBuffer buf = WriteHtml.getJsDetailHTML(zc , day , classroom) ;
		String data = buf.toString() ;
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "text/html;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}
}