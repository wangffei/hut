package com.servlet ;

import com.interfaces.HttpServlet ;
import com.html.WriteHtml ;

import org.jboss.com.sun.net.httpserver.HttpExchange;
import org.jboss.com.sun.net.httpserver.Headers;

import java.io.OutputStream ;

import java.util.Map ;
import com.dao.ImageDao ;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageServlet implements HttpServlet {
	public void service(HttpExchange exchange , Map<String , String> param) throws Exception{
		String method = param.get("method");
		if("zl".equals(method)){
			zlMethod(exchange , param) ;
		}
	}

	public void zlMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		BufferedImage img = ImageDao.drawZl() ;
		OutputStream out = exchange.getResponseBody() ;
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "image/png");
		exchange.sendResponseHeaders(200 , 0);
		ImageIO.write(img, "png",out) ;
		out.close() ;
	}
}