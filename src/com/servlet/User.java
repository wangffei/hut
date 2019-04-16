package com.servlet ;

import com.google.gson.Gson;
import com.interfaces.HttpServlet ;
import com.dao.UserDao ;
import com.util.Config ;

import org.jboss.com.sun.net.httpserver.HttpExchange;
import org.jboss.com.sun.net.httpserver.Headers;

import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Map ;
import java.util.Date ;
import java.io.BufferedWriter ;
import java.io.OutputStreamWriter ;

public class User implements HttpServlet {
	public void service(HttpExchange exchange , Map<String , String> param) throws Exception{
		String method = param.get("method");
		if(method.equals("xf")){
			xfMethod(exchange , param) ;
		}
		if(method.equals("kb")){
			kbMethod(exchange , param) ;
		}
		if(method.equals("time")){
			getTime(exchange , param) ;
		}
		if(method.equals("cj")){
			cjMethod(exchange , param) ;
		}
		if(method.equals("sx")){
			sxMethod(exchange , param) ;
		}
		if(method.equals("dj")){
			djMethod(exchange , param) ;
		}
		if(method.equals("zl")){
			zlMethod(exchange , param) ;
		}
		if(method.equals("kjs")){
			kjsMethod(exchange , param) ;
		}
		if(method.equals("clasdetail")){
			cdlMethod(exchange , param) ;
		}
		//考试查询
		if(method.equals("ks")){
			ksMethod(exchange , param) ;
		}
		//个人信息查询
		if(method.equals("grxx")){
			grxxMethod(exchange , param) ;
		}
		//推出登陆
		if("userquit".equals(method)){
			quitMethod(exchange , param) ;
		}
	}

	//退出登录
	public void quitMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		Config.username = null ;
		Config.password = null ;
		String data = "{'fun':function(){window.location='login.html'}}" ;
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//个人信息
	public void grxxMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		String data = Config.grzl ;
		if(data == null){
			data = UserDao.getGrxx() ;
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//考试查询
	public void ksMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		String data = null ;

		data = UserDao.ksDetail() ;

		if(data.contains("code") || data == null || data.trim().equals("")){
			if(Config.ks != null){
				data = Config.ks ;
			}
		}

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//教室详情
	public void cdlMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		Integer zc = Integer.parseInt(param.get("zc")) ;
		Integer day = Integer.parseInt(param.get("day")) ;
		String classroom = URLDecoder.decode(param.get("classroom") , "utf-8") ;
		String data = UserDao.getRoomDetail(zc , day , classroom) ;
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//空教室数据获取
	public void kjsMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		Integer zc = Integer.parseInt(param.get("zc")) ;
		Integer day = Integer.parseInt(param.get("day")) ;
		Integer clas = Integer.parseInt(param.get("clas")) ;
		String data = UserDao.getKjs(zc , day , clas) ;
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//获取周历
	public void zlMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		Map data = null ;
		String flush = param.get("flush") ;
		if(flush == null || !flush.equals("flush")){
			if(Config.zl == null){
				data = UserDao.getZl() ;
			}else{
				data = Config.zl ;
			}
		}else if(flush != null && flush.equals("flush")){
			data = UserDao.getZl() ;
		}
		if(data.get("code") != null){
			if(Config.zl != null){
				data = Config.zl ;
			}
		}else{
			//保存当前所在哪一个学期 ，用于判断缓存课表是否有效
			Date date = new Date() ;
			Calendar cld = Calendar.getInstance() ;
			cld.setTime(date) ;
			Integer month = cld.get(Calendar.MONTH) + 1 ;
			String str = "" ;
			if(month >= 6){
				Integer year = cld.get(Calendar.YEAR) ;
				str =  year+"-"+(year+1)+"-1" ;
			}else{
				Integer year = cld.get(Calendar.YEAR) ;
				str = (year-1)+"-"+year+"-2" ;
			}
			if(!data.get("now").equals(str)){
				data = UserDao.getZl() ;
			}
		}
		String result = "var result="+(new Gson().toJson(data))+";" ;
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "text/javascript;charset=utf-8");
		exchange.sendResponseHeaders(200 , result.getBytes("utf-8").length);
		writer.write(result);
		writer.close() ;
	}

	//获取等级考试成绩
	public void djMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		String data = null ;
		String flush = param.get("flush") ;
		if(flush == null || !flush.equals("flush")){
			if(Config.dj == null){
				data = UserDao.getDj() ;
			}else{
				data = Config.dj ;
			}
		}else if(flush != null && flush.equals("flush")){
			data = UserDao.getDj() ;
		}
		if(data.contains("code")){
			if(Config.dj != null){
				data = Config.dj ;
			}
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//实习安排
	public void sxMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		String data = null ;
		String flush = param.get("flush") ;
		if(flush == null || !flush.equals("flush")){
			if(Config.sx == null){
				data = UserDao.getSx() ;
			}else{
				data = Config.sx ;
			}
		}else if(flush != null && flush.equals("flush")){
			data = UserDao.getSx() ;
		}
		if(data.contains("code")){
			if(Config.sx != null){
				data = Config.sx ;
			}
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//成绩查询
	public void cjMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		String data = null ;
		String flush = param.get("flush") ;
		if(flush == null || !flush.equals("flush")){
			if(Config.cj == null){
				data = UserDao.getCj() ;
			}else{
				data = Config.cj ;
			}
		}else if(flush != null && flush.equals("flush")){
			data = UserDao.getCj() ;
		}
		if(data.contains("code")){
			if(Config.cj != null){
				data = Config.cj ;
			}
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//获取服务器时间
	public void getTime(HttpExchange exchange , Map<String , String> param) throws Exception{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "text/javascript;charset=utf-8");
		String data = "var date_now = new Date("+new Date().getTime()+"); " ;
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//学分查询
	public void xfMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		String data = null ;
		String flush = param.get("flush") ;
		if(flush == null || !flush.equals("flush")){
			if(Config.xf == null){
				data = UserDao.xfInfo() ;
			}else{
				data = Config.xf ;
			}
		}else if(flush != null && flush.equals("flush")){
			data = UserDao.xfInfo() ;
		}
		if(data.contains("code")){
			if(Config.xf != null){
				data = Config.xf ;
			}
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}

	//课表查询
	public void kbMethod(HttpExchange exchange , Map<String , String> param) throws Exception{
		String data = null ;
		Map map = null ;
		String flush = param.get("flag") ;
		if(flush == null || !flush.equals("flush")){
			if(Config.kb != null){
				map = Config.kb ;
			}else{
				data = UserDao.getKb() ;
			}
		}else if(flush != null && flush.equals("flush")) {
			data = UserDao.getKb();
		}
		if(map != null){
			String now = "" ;
			Date date = new Date() ;
			//保存当前所在哪一个学期 ，用于判断缓存课表是否有效
			Calendar cld = Calendar.getInstance() ;
			cld.setTime(date) ;
			Integer month = cld.get(Calendar.MONTH) + 1 ;
			if(month >= 6){
				Integer year = cld.get(Calendar.YEAR) ;
				now = year+"-"+(year+1)+"-1" ;
			}else{
				Integer year = cld.get(Calendar.YEAR) ;
				now = (year-1)+"-"+year+"-2" ;
			}
			if(!map.get("now").equals(now)){
				data = UserDao.getKb() ;
			}else{
				data = new Gson().toJson(map) ;
			}
		}
		if(data.contains("code")){
			if(Config.kb != null){
				data = new Gson().toJson(Config.kb) ;
			}
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody() , "utf-8"));
		Headers header = exchange.getResponseHeaders() ;
		header.set("Content-Type" , "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200 , data.getBytes("utf-8").length);
		writer.write(data);
		writer.close() ;
	}
}