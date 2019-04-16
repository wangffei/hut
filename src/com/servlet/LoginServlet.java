package com.servlet ;

import com.interfaces.HttpServlet ;

import org.jboss.com.sun.net.httpserver.HttpExchange;

import java.util.Map ;
import java.io.BufferedWriter ;
import java.io.OutputStreamWriter ;

import com.dao.LoginDao ;

public class LoginServlet implements HttpServlet{
	public void service(HttpExchange exchange , Map<String , String> param) throws Exception{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8")) ;
		String username = param.get("username") ;
		String password = param.get("password");
		String result = LoginDao.doLogin(username , password) ;
		System.out.println(result) ;
		exchange.sendResponseHeaders(200 , result.getBytes("utf-8").length);
		writer.write(result);
		writer.close() ;
	}
}