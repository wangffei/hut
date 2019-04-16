package com.util ;

import java.util.Map ;
import java.util.HashMap ;
import java.util.Set ;
import java.util.List ;

import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.OutputStream ;

import java.net.URL ;
import java.net.HttpURLConnection ;
/**
 * 爬虫方法封装
 */
public class URLMethod{
    //post方法封装
    public static Map<String , StringBuffer> doPost(String path , String data , Map<String , String> header)
            throws Exception{
        if(path == null || path.trim().equals("")){
            throw new Exception("path参数为空");
        }
        if(data == null || data.trim().equals("")){
            throw new Exception("data参数不能为空");
        }
        StringBuffer buf = new StringBuffer() ;
        OutputStream out = null ;
        BufferedReader reader = null ;
        Map<String , StringBuffer> result = new HashMap<>() ;

        try{
            URL url = new URL(path);
            //HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection con = (HttpURLConnection)url.openConnection() ;
            con.setInstanceFollowRedirects(false);
            //设置请求方式
            con.setRequestMethod("POST");
            con.setDoOutput(true);//使用 URL 连接进行输出
            con.setDoInput(true);
            con.setConnectTimeout(500);
            con.setReadTimeout(1500);
            //con.setRequestTimtout(2000);
            //设置访问
            if(header != null){
                Object[] keys1 = header.keySet().toArray() ;
                for(Object obj : keys1){
                    con.setRequestProperty((String)obj , header.get(obj));
                }
            }
            con.connect();
            //写post数据
            out = con.getOutputStream() ;
            out.write(data.getBytes()) ;
            //保存响应码
            int code = con.getResponseCode() ;
            result.put("code" , new StringBuffer(code+""));
            //记录cookie
            Map<String , List<String>> headerFields = con.getHeaderFields() ;
            Set<String> keys = headerFields.keySet();
            for (String str : keys) {
                if ("Set-Cookie".equalsIgnoreCase(str)) {
                    StringBuffer cookie = new StringBuffer();
                    for (String k : headerFields.get(str)) {
                        cookie.append(k.replace("path=/" , "").replace("Path=/" , ""));
                    }
                    result.put("cookie", cookie);
                }
            }
            if(result.get("cookie") == null){
                result.put("cookie", new StringBuffer(""));
            }
            //判断是否发生重定向
            if(con.getResponseCode() == 301 || con.getResponseCode() == 302){
                //获取重定向地址
                String location = con.getHeaderField("Location") ;
                if(location.startsWith("http://192.168.0.1")){
                    result.put("data" , new StringBuffer(location.trim())) ;
                    return result ;
                }
                HttpURLConnection connection = (HttpURLConnection) new URL(location).openConnection() ;
                //设置请求方式
                connection.setRequestMethod("GET");
                //设置访问头
                connection.setRequestProperty("User-Agent" , header.get("User-Agent"));
                connection.setRequestProperty("Referer" , header.get("Referer"));
                connection.setRequestProperty("cookie" , result.get("cookie").toString());
                con.connect();
                //读数据
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream() , "utf-8"));

                String line =  null;
                while((line = reader.readLine()) != null){
                    buf.append(line) ;
                }
                result.put("data" , buf) ;
            }else if(con.getResponseCode() == 200){
                //读数据
                reader = new BufferedReader(new InputStreamReader(con.getInputStream() , "utf-8"));

                String line = "" ;
                while((line = reader.readLine()) != null){
                    buf.append(line) ;
                }
                result.put("data" , buf) ;
            }
        } catch(Exception e){
            System.out.println(e);
        } finally{
            if(out != null){
                try{
                    out.close() ;
                }catch(Exception e){
                    System.out.println(e);
                }
            }
            if(reader != null){
                try{
                    reader.close() ;
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        return result ;
    }
    //get方法封装
    public static Map<String , StringBuffer> doGet(String path , Map<String , String> header){
        StringBuffer buf = new StringBuffer() ;
        BufferedReader reader = null ;
        Map<String , StringBuffer> result = new HashMap<>() ;

        try{
            URL url = new URL(path);
            //HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection con = (HttpURLConnection)url.openConnection() ;
            con.setInstanceFollowRedirects(false);
            //设置请求方式
            con.setRequestMethod("GET");
            //设置访问
            if(header != null){
                Object[] keys1 = header.keySet().toArray() ;
                for(Object obj : keys1){
                    con.setRequestProperty((String)obj , header.get(obj));
                }
            }
            con.setConnectTimeout(500);
            con.setReadTimeout(1500);
            con.connect();
            //保存响应码
            int code = con.getResponseCode() ;
            result.put("code" , new StringBuffer(code+""));
            //记录cookie
            Map<String , List<String>> headerFields = con.getHeaderFields() ;
            Set<String> keys = headerFields.keySet();
            for (String str : keys) {
                if ("Set-Cookie".equalsIgnoreCase(str)) {
                    StringBuffer cookie = new StringBuffer();
                    for (String k : headerFields.get(str)) {
                        cookie.append(k.replace("path=/" , "").replace("Path=/" , ""));
                    }
                    result.put("cookie", cookie);
                }
            }
            if(result.get("cookie") == null){
                result.put("cookie", new StringBuffer(""));
            }

            if(code == 302 || code == 301){
                //获取重定向地址
                String location = con.getHeaderField("Location") ;
                if(location.startsWith("http://192.168.0.1")){
                    result.put("data" , new StringBuffer(location)) ;
                    return result ;
                }
            }else if(code == 200){
                //读数据
                reader = new BufferedReader(new InputStreamReader(con.getInputStream() , "utf-8"));

                String line = "" ;
                while((line = reader.readLine()) != null){
                    buf.append(line) ;
                }
                result.put("data" , buf) ;
            }
        } catch(Exception e){
            System.out.println(e);
        } finally{
            if(reader != null){
                try{
                    reader.close() ;
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        return result ;
    }
}