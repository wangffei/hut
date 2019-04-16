package com.interfaces;

import org.jboss.com.sun.net.httpserver.HttpExchange;

import java.util.Map ;

public interface HttpServlet{
	void service(HttpExchange exchange , Map<String , String> param) throws Exception;
}