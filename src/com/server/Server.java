package com.server ;

import org.jboss.com.sun.net.httpserver.Headers;
import org.jboss.com.sun.net.httpserver.HttpExchange;
import org.jboss.com.sun.net.httpserver.HttpHandler;
import org.jboss.com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream ;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.OutputStreamWriter ;
import java.io.BufferedWriter ;
import java.net.URL ;

import com.util.Config ;
import com.servlet.* ;

public class Server implements Runnable{
    public void run(){
        HttpServer http = null ;
        try {
            http = HttpServer.create(new InetSocketAddress(9264) , 0 ) ;
        } catch(Exception e){
            System.exit(-1);
        }
        http.createContext("/", new HttpHandler() {

            @Override
            public void handle(HttpExchange exchange) throws IOException {
                //url解析
                String urlString = exchange.getRequestURI().toString();
//                System.out.println(exchange.getRequestBody());
           
                BufferedReader read = new BufferedReader(new InputStreamReader(exchange.getRequestBody())) ;
                String line = "" ;
                urlString = urlString.contains("?") ? urlString+"&" : urlString + "?" ;
                
                while((line = read.readLine()) != null){
                	urlString += line ;
                }
                String url = "";
                String parames = "";
                if (urlString.contains("?")) {
                	String[] strs = urlString.split("\\?");
                    url = strs[0];
                    if(strs.length > 1){
                    	parames = strs[1];
                    }
                } else {
                    url = urlString;
                }
                Map<String , String> map = getParames(parames) ;
                //路由表
                if(url.equals("/login.do")){
                    try{
                        new LoginServlet().service(exchange , map);
                    }catch(Exception e){
                        System.out.println(e);
                    }
                    return ;
                }else if(url.equals("/user.do")){
                    try{
                        new User().service(exchange , map) ;
                    }catch(Exception e){
                        System.out.println(e) ;
                    }
                    return ;
                }else if(url.equals("/html")){
                    try{
                        new HtmlServlet().service(exchange , map) ;
                    }catch(Exception e){
                        System.out.println(e) ;
                    }
                    return ;                
                }else if(url.equals("/image")){
                    try{
                        new ImageServlet().service(exchange , map) ;
                    }catch(Exception e){
                        System.out.println(e) ;
                    }
                    return ;     
                }else if(url.equals("/huthelper")){            //抓取工大助手的数据
                     try{
                        new HutServlet().service(exchange , map) ;
                    }catch(Exception e){
                        System.out.println(e) ;
                    }
                    return ;    
                }else if(url.equals("/jianyi")){            //邮件发送
                    try{
                       new jianyiServlet().service(exchange , map) ;
                   }catch(Exception e){
                       System.out.println(e) ;
                   }
                   return ;    
               }

                //若用户第一次登陆就转发到login.html页面去
                if(Config.username == null || Config.password == null){
                    exchange.sendResponseHeaders(200, 0);
                    OutputStream out = exchange.getResponseBody();
                    InputStream in = new FileInputStream("./WebRoot/login.html") ;
                    byte[] data = new byte[1024];
                    int len = 0;
                    while ((len = in.read(data)) != -1) {
                        out.write(data, 0, len);
                    }
                    out.close();
                    in.close() ;
                    return ;
                }
                //文件读取
                exchange.sendResponseHeaders(200, 0);
                OutputStream out = exchange.getResponseBody();
                InputStream in = new FileInputStream("./WebRoot"+url) ;
                byte[] data = new byte[1024];
                int len = 0;
                while ((len = in.read(data)) != -1) {
                    out.write(data, 0, len);
                }
                out.close();
                in.close() ;
                return;
            }

            public Map<String , String> getParames(String parames) {
                Map<String , String> map = new HashMap<>() ;
                if (!parames.equals("")) {
                    String[] strs = null ;
                    if(!parames.contains("&")){
                        strs = new String[]{parames};
                    }else{
                        strs = parames.split("&");
                    }
                    for (String string : strs) {
                        if (string.contains("=")) {
                        	String[] strs1 = string.split("=") ;
                            if(strs1.length > 1){
                            	String k = strs1[0];
                                String v = strs1[1];
                                map.put(k, v);
                            }
                        }
                    }
                }
                return map;
            }
        });
        http.start();
    }
}
