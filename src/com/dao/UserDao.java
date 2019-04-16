package com.dao ;

import com.util.Unicode ;
import com.login.Cookie ;
import com.login.Login  ;
import com.util.Config  ;
import com.util.URLMethod ;

import java.util.Map ;
import java.util.HashMap ;
import java.util.List ;
import java.util.ArrayList ;
import java.util.Date ;
import java.util.Calendar ;
import java.util.List ;
import java.util.ArrayList ;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.URLEncoder ;

import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class UserDao{
	//学分数据解析
	public static String xfInfo(){
		//判断登录是否有效
		boolean success = Login.loginAgin() ;
		if(success == false){
			return "{code:-1}" ;
		}
		//获取学分页请求的关键参数
		String url = "http://218.75.197.123:83/jsxsd/xxwcqk/xxwcqk_idxOnlb.do" ;
		Map<String , String> header = new HashMap<>() ;
		header.put("Host" , "218.75.197.123:83") ;
		header.put("cookie" , Cookie.cookie) ;
		header.put("User-Agent" , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36") ;
		Map<String , StringBuffer> result = URLMethod.doGet(url , header) ;
		if(result.get("data") != null && result.get("data").toString().startsWith("http://192.168.0.1")){
			url = "http://172.16.65.75/jsxsd/xxwcqk/xxwcqk_idxOnlb.do" ;
			result = URLMethod.doGet(url , header) ;
		}
		String code = result.get("code") == null ? "" : result.get("code").toString().trim() ;
		if(!"200".equals(code)){
			return "{code:-1}" ;
		}
		Document document = Jsoup.parse(result.get("data").toString());
		Elements input = document.getElementById("Form1").getElementsByTag("input") ;
		String name1 = input.get(0).attr("name");
		String key1 = input.get(0).val() ;
		String name2 = input.get(1).attr("name") ;
		String key2 = input.get(1).val() ;
		String data = name1+"="+key1+"&"+name2+"="+key2 ;
		//访问学分页
		url = "http://218.75.197.123:83/jsxsd/xxwcqk/xxwcqkOnkclb.do" ;
		header.put("Referer" , "http://218.75.197.123:83/jsxsd/xxwcqk/xxwcqk_idxOnlb.do");
		try{
			result = URLMethod.doPost(url , data , header) ;
		}catch(Exception e){
			System.out.println(e);
		}
		if(result.get("data") != null && result.get("data").toString().startsWith("http://192.168.0.1")){
			url = "http://172.16.65.75/jsxsd/xxwcqk/xxwcqkOnkclb.do" ;
			try{
				result = URLMethod.doPost(url , data , header) ;
			}catch(Exception e){
				System.out.println(e) ;
			}
		}
		code = result.get("code") == null ? "" : result.get("code").toString().trim() ;
		if(!code.equals("200")){
			return "{code:-1}" ;
		}
		Map<String , List<Map>> map = new HashMap<>() ;
		map.put("yixiu" , new ArrayList<Map>()) ;
		map.put("zhengxiu" , new ArrayList<Map>()) ;
		map.put("meixiu" , new ArrayList<Map>()) ;
		//解析数据
		document = Jsoup.parse(result.get("data").toString()) ;
		Elements eles = document.getElementsByClass("Nsb_r_list") ;
		Elements tr = eles.get(1).getElementsByTag("tr") ;
		for(Element e : tr){
			Elements tds = e.getElementsByTag("td") ;
			if(tds.size() != 0){
				if("".equals(tds.get(0).attr("colspan").trim())){
					String classname = Unicode.gbEncoding(tds.get(1).text()) ;
					String isstudy = Unicode.gbEncoding(tds.get(5).text()) ;
					String xuefen = tds.get(2).text() ;
					if(!classname.trim().equals("") && !isstudy.trim().equals("") && !xuefen.trim().equals("")) {
						Map<String , String> m = new HashMap<>();
						m.put("classname" , classname) ;
						m.put("isstudy" , isstudy) ;
						m.put("xuefen" , xuefen) ;
						if(isstudy.trim().equals(Unicode.gbEncoding("待修读"))){
							map.get("meixiu").add(m) ;
						}else if(isstudy.trim().equals(Unicode.gbEncoding("修读中"))){
							map.get("zhengxiu").add(m) ;
						}else{
							map.get("yixiu").add(m) ;
						}
					}
				}
			}
		}
		String dd = new Gson().toJson(map).toString() ;
		Config.xf = dd ;
		return dd ;
	}
	//课表数据解析
	public static String getKb(){
		//判断登录是否有效
		boolean success = Login.loginAgin() ;
		if(success == false){
			return "{code:-1}" ;
		}
		//生成一个数据结构用来存储课表数据（暂时为空）
		Map<String , Object> map = new HashMap<>() ;
		//保存当前时间字段 ， 用于后续周数计算使用
		Date date = new Date() ;
		//保存当前所在哪一个学期 ，用于判断缓存课表是否有效
		Calendar cld = Calendar.getInstance() ;
		cld.setTime(date) ;
		Integer month = cld.get(Calendar.MONTH) + 1 ;
		if(month >= 6){
			Integer year = cld.get(Calendar.YEAR) ;
			map.put("now" , year+"-"+(year+1)+"-1") ;
		}else{
			Integer year = cld.get(Calendar.YEAR) ;
			map.put("now" , (year-1)+"-"+year+"-2") ;
		}
		Map<Integer , Map> kb = new HashMap<>() ;
		for(int i = 1 ; i <= 30 ; i++){
			Map<Integer , Map> temp = new HashMap<>() ;
			for(int j = 1 ; j <= 7 ; j++){
				temp.put(j , new HashMap()) ;
			}
			kb.put(i , temp);
		}
		map.put("kb" , kb) ;
		Map header = new HashMap() ;
		//爬学生学期的总课表
		header = new HashMap<>() ;
		header.put("Host" , "218.75.197.123:83") ;
		header.put("cookie" , Cookie.cookie) ;
		header.put("User-Agent" , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36") ;
		Map result = URLMethod.doGet("http://218.75.197.123:83/jsxsd/xskb/xskb_list.do" , header) ;
		if(result.get("data") != null && result.get("data").toString().startsWith("http://192.168.0.1")){
			String url = "http://172.16.65.75/jsxsd/xskb/xskb_list.do" ;
			result = URLMethod.doGet(url , header) ;
		}
		String code = result.get("code") == null ? "" : result.get("code").toString().trim() ;
		if(!"200".equals(code)){
			return "{code:-1}" ;
		}
		Document document = Jsoup.parse(result.get("data").toString());
		Elements classes = document.getElementById("kbtable").getElementsByTag("tr") ;
		//从第一节课解析到第5节课
		for(int i=1 ; i<classes.size() - 1 ; i++){
			Elements es = classes.get(i).getElementsByTag("td") ;
			//从星期一解析到星期七
			for(int j=0 ; j<es.size() ; j++){
				Element e = es.get(j).getElementsByClass("kbcontent").get(0) ;
				String[] value = e.html().split("<br>");
				for(int k=0 ; k<value.length ; k++){
					value[k] = value[k].replaceAll("<.*?>" , "").trim() ;
				}
				if(value.length > 5){
					String[] temp = new String[4] ;
					System.arraycopy(value , 0 , temp , 0 , 4) ;
					decodeKb(map , temp , i , j+1) ;
					System.arraycopy(value , 5 , temp , 0 , 4) ;
					decodeKb(map , temp , i , j+1) ;
				}else{
					decodeKb(map , value , i , j+1) ;
				}
			}
		}
		Map<Object , Object> sx = new HashMap() ;
		decodeSx(sx , classes.get(classes.size()-1).getElementsByTag("td").get(0).text()) ;
		String s = new Gson().toJson(sx).toString() ;
		Config.sx = s ;
		Config.kb = map ;
		return new Gson().toJson(map).toString();
	}
	//解析实习安排
	private static void decodeSx(Map<Object , Object> map , String data){
		String[] strs = data.split(";") ;
		for(int i=0 ; i<strs.length ; i++){
			String[] tips = strs[i].split(" ") ;
			String zs = "" ;
			String teacher = "" ;
			if(tips.length == 2){
				zs = tips[1] ;
			}else if(tips.length == 3){
				zs = tips[2] ;
				teacher = tips[1] ;
			}

			Map temp = new HashMap() ;
			temp.put("classname" , Unicode.gbEncoding(tips[0])) ;
			temp.put("teacher" , Unicode.gbEncoding(teacher)) ;
			temp.put("fenbu" , zs) ;
			map.put(i , temp) ;
		}
	}
	//解析课表的方法
	private static void decodeKb(Map<String , Object> map , String[] data , int i , int j){
		if(data == null || data.length < 3){
			return ;
		}
		//flag表示课程是如何分布 ， 1普通模式  2单周模式  3双周模式
		int flag = 1 ;
		if(data[2].contains("(单周)")){
			flag = 2 ;
		}else if(data[2].contains("(双周)")){
			flag = 3 ;
		}else{
			flag = 1 ;
		}
		String zc = data[2].replaceAll("[\u4e00-\u9fa5]" , "").replace("(" , "").replace(")" , "");
		//保存这门课程分布的周数
		List<Integer> list = new ArrayList<>() ;
		if(zc.contains(",")){
			String[] strs = zc.split(",") ;
			for(String s : strs){
				if(!"".equals(s) && s.contains("-")){
					String[] num = s.split("-" , 2) ;
					int start = Integer.parseInt(num[0].trim()) ;
					int end   = Integer.parseInt(num[1].trim()) ;
					for(int m = start ; m <= end ; m++){
						if(flag == 1){
							list.add(m) ;
						}else{
							if(flag == 3 && m % 2 == 0){
								list.add(m) ;
							}else if(flag == 2 && m % 2 != 0){
								list.add(m) ;
							}
						}
					}
				}else if(!"".equals(s)){
					list.add(Integer.parseInt(s)) ;
				}
			}
		}else{
			if(zc.contains("-")){
				String[] num = zc.split("-" , 2) ;
				int start = Integer.parseInt(num[0].trim()) ;
				int end   = Integer.parseInt(num[1].trim()) ;
				for(int m = start ; m <= end ; m++){
					if(flag == 1){
						list.add(m) ;
					}else{
						if(flag == 3 && m % 2 == 0){
							list.add(m) ;
						}else if(flag == 2 && m % 2 != 0){
							list.add(m) ;
						}
					}
				}
			}else{
				list.add(Integer.parseInt(zc)) ;
			}
		}
		Map<String , Object> msg = new HashMap<String , Object>() ;
		msg.put("classname" , Unicode.gbEncoding(data[0])) ;
		msg.put("teacher" , Unicode.gbEncoding(data[1])) ;
		if(data.length == 4){
			msg.put("classroom" , Unicode.gbEncoding(data[3])) ;
		}else if(data.length == 3){
			msg.put("classroom" , "") ;
		}
		msg.put("fenbu" , list);
		for(Integer m : list){
			((Map)((Map)((Map)map.get("kb")).get(m)).get(j)).put(i , msg) ;
		}
	}

	//成绩数据解析
	public static String getCj(){
		//判断登录是否有效
		boolean success = Login.loginAgin() ;
		if(success == false){
			return "{code:-1}" ;
		}
		String url = "http://218.75.197.123:83/jsxsd/kscj/cjcx_list" ;
		Map<String , String> header = new HashMap() ;
		header.put("Host" , "218.75.197.123:83") ;
		header.put("cookie" , Cookie.cookie) ;
		header.put("User-Agent" , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36") ;
		Map result = URLMethod.doGet(url , header) ;
		if(result.get("data") != null && result.get("data").toString().startsWith("http://192.168.0.1")){
			url = "http://172.16.65.75/jsxsd/kscj/cjcx_list" ;
			result = URLMethod.doGet(url , header) ;
		}
		String code = result.get("code") == null ? "" : result.get("code").toString() ;
		if(!code.equals("200")){
			return "{code:-1}" ;
		}
		Map<String , Map> map = new HashMap<>() ;
		Document document = Jsoup.parse(result.get("data").toString()) ;
		Elements tr = document.getElementById("dataList").getElementsByTag("tr") ;
		for(int i=1 ; i<tr.size() ; i++){
			Elements eles = tr.get(i).getElementsByTag("td") ;
			Map m = map.get(eles.get(1).text()) ;
			String nj = "" ;
			if(m == null){
				m = new HashMap();
				if(Config.username != null){
					Integer year = Integer.parseInt("20"+Config.username.substring(0 , 2)) ;
					if(eles.get(1).text().contains(year+"-"+(year+1)+"-1")){
						nj = "大一上" ;
					}else if(eles.get(1).text().contains(year+"-"+(year+1)+"-2")){
						nj = "大一下" ;
					}else if(eles.get(1).text().contains((year+1)+"-"+(year+2)+"-1")){
						nj = "大二上" ;
					}else if(eles.get(1).text().contains((year+1)+"-"+(year+2)+"-2")){
						nj = "大二下" ;
					}else if(eles.get(1).text().contains((year+2)+"-"+(year+3)+"-1")){
						nj = "大三上" ;
					}else if(eles.get(1).text().contains((year+3)+"-"+(year+4)+"-1")){
						nj = "大四上" ;
					}else if(eles.get(1).text().contains((year+3)+"-"+(year+4)+"-2")){
						nj = "大四下" ;
					}
					m.put("nj" , Unicode.gbEncoding(nj)) ;
				}
			}
			Map temp = new HashMap() ;
			temp.put("classname" , Unicode.gbEncoding(eles.get(3).text())) ;
			temp.put("score" , Unicode.gbEncoding(eles.get(4).text())) ;
			temp.put("xuefen" , eles.get(6).text()) ;
			temp.put("jidian" , eles.get(8).text()) ;
			temp.put("ksxz" , Unicode.gbEncoding(eles.get(11).text())) ;
			temp.put("kcsx" , Unicode.gbEncoding(eles.get(12).text())) ;
			temp.put("kcxz" , Unicode.gbEncoding(eles.get(13).text())) ;
			m.put(i , temp) ;
			map.put(eles.get(1).text() , m) ;
		}
		String d = new Gson().toJson(map) ;
		Config.cj = d  ;
		return d ;
	}

	public static String getSx(){
		getKb() ;
		if(Config.sx == null){
			return "{code:-2}";
		}
		return Config.sx ;
	}

	//等级考试成绩查询
	public static String getDj(){
		//判断登录是否有效
		boolean success = Login.loginAgin() ;
		if(success == false){
			return "{code:-1}" ;
		}
		String url = "http://218.75.197.123:83/jsxsd/kscj/djkscj_list" ;
		Map<String , String> header = new HashMap() ;
		header.put("Host" , "218.75.197.123:83") ;
		header.put("cookie" , Cookie.cookie) ;
		header.put("User-Agent" , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36") ;
		Map result = URLMethod.doGet(url , header) ;
		if(result.get("data") != null && result.get("data").toString().startsWith("http://192.168.0.1")){
			url = "http://172.16.65.75/jsxsd/kscj/djkscj_list" ;
			result = URLMethod.doGet(url , header) ;
		}
		String code = result.get("code") == null ? "" : result.get("code").toString() ;
		if(!code.equals("200")){
			return "{code:-1}" ;
		}
		Map map = new HashMap() ;
		Document document = Jsoup.parse(result.get("data").toString()) ;
		Elements tr = document.getElementById("dataList").getElementsByTag("tr");
		for(int i=2 ; i<tr.size() ; i++){
			Elements e = tr.get(i).getElementsByTag("td") ;
			Map m = new HashMap() ;
			m.put("name" , e.get(1).text()) ;
			m.put("bishi" , e.get(2).text()) ;
			m.put("jishi" , e.get(3).text()) ;
			m.put("zcj" , e.get(4).text()) ;
			m.put("time" , e.get(8).text()) ;
			map.put(i , m) ;
		}
		String d = new Gson().toJson(map) ;
		Config.dj = d ;
		return d ;
	}

	public static Map getZl(){
		//判断登录是否有效
		boolean success = Login.loginAgin();
		if (success == false) {
			Map m = new HashMap();
			m.put("code", -1);
			return m;
		}
		String url = "http://218.75.197.123:83/jsxsd/jxzl/jxzl_query" ;
		Map<String , String> header = new HashMap() ;
		header.put("Host" , "218.75.197.123:83") ;
		header.put("cookie" , Cookie.cookie) ;
		header.put("User-Agent" , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36") ;
		Map result = URLMethod.doGet(url , header) ;
		if(result.get("data") == null || result.get("data").toString().startsWith("http://192.168.0.1")){
			url = "http://172.16.65.75/jsxsd/jxzl/jxzl_query" ;
			result = URLMethod.doGet(url , header) ;
		}
		String code = result.get("code") == null ? "" : result.get("code").toString() ;
		if(!code.equals("200")){
			Map r = new HashMap() ;
			r.put("code" , "-1") ;
			return  r;
		}
		Map map = new HashMap<>() ;
		//保存当前所在哪一个学期 ，用于判断缓存课表是否有效
		Date date = new Date() ;
		Calendar cld = Calendar.getInstance() ;
		cld.setTime(date) ;
		Integer month = cld.get(Calendar.MONTH) + 1 ;
		if(month >= 6){
			Integer year = cld.get(Calendar.YEAR) ;
			map.put("now" , year+"-"+(year+1)+"-1") ;
		}else{
			Integer year = cld.get(Calendar.YEAR) ;
			map.put("now" , (year-1)+"-"+year+"-2") ;
		}
		Document document = Jsoup.parse(result.get("data").toString()) ;
		Elements tr = document.getElementById("kbtable").getElementsByTag("tr") ;
		for(int i=1 ; i<tr.size() - 1 ; i++){
			Elements td = tr.get(i).getElementsByTag("td") ;
			Map m = new HashMap<>();
			for(int j=1 ; j<td.size() - 1 ; j++){
				m.put(j, td.get(j).attr("title").replace("年", "-").replace("月", "-")) ;
			}
			map.put(i , m) ;
		}
		Config.zl = map ;
		return map ;
	}

	//空教室数据处理
	public static String getKjs(Integer zc , Integer day , Integer clas){
		//定义一个Map保存结果
		Map result = new HashMap() ;
		//保存正在上课的教室
		List taking = new ArrayList() ;
		//保存空教室
		List empty = new ArrayList() ;
		result.put("taking" , taking) ;
		result.put("empty" , empty) ;
		Map map = Config.schKb ;
		while(true){
			Object[] objs = map.keySet().toArray() ;
			if(objs.length == 0){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue ;
			}
			for(Object o : objs){
				Map m = (Map)((Map)map.get(o)).get("kb") ;
				Map temp = ((Map)((Map)((Map)m.get(zc)).get(day)).get(clas)) ;
				if(temp.get("classes") != null){
					taking.add(temp) ;
				}else{
					empty.add(temp) ;
				}
			}
			break ;
		}
		return new Gson().toJson(result) ;
	}
	//教室详情页
	public static String getRoomDetail(Integer zc , Integer day , String classroom){
		Map map = Config.schKb ;
		Object[] objs = map.keySet().toArray() ;
		String data = "{code: -1}" ;
		for(Object o : objs){
			if(((String)((Map)map.get(o)).get("classroom")).trim().equals(classroom.trim())){
				Map result = (Map)((Map)((Map)((Map)map.get(o)).get("kb")).get(zc)).get(day) ;
				data = new Gson().toJson(result) ;
			}
		}
		return data ;
	}

	//考试查询
	public static String ksDetail() throws Exception{
		String url = "http://218.75.197.122:84/" ;
		//保存进入成绩查询页的cookie
		String cookie = "" ;
		Map header = new HashMap() ;
		header.put("Host" , "218.75.197.122:84") ;
		header.put("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36") ;
		header.put("Referer" , "http://jwc.hut.edu.cn/") ;
		Map result = URLMethod.doGet(url , header) ;
		if(result.get("data") != null && result.get("data").toString().startsWith("http://192.168.0.1")){
			return "{code:-3}" ;
		}
		cookie = result.get("cookie") == null ? "" : result.get("cookie").toString().replace("; ; HttpOnly" , "") ;
		String code = result.get("code") == null ? "" : result.get("code").toString() ;
		if(!"200".equals(code)){
			return  "{code:-1}";
		}
		Document document = Jsoup.parse(result.get("data").toString()) ;
		String key = document.getElementById("csrf").val() ;
		url = "http://218.75.197.122:84/search/exam/"+Config.username ;
		header.put("Referer" , "http://218.75.197.122:84/") ;
		header.put("Cookie" , cookie) ;
		header.put("X-Requested-With" , "XMLHttpRequest") ;
		result = URLMethod.doPost(url , "_csrf="+ URLEncoder.encode(key , "utf-8" ) , header) ;
		if(result.get("data") != null && result.get("data").toString().startsWith("http://192.168.0.1")){
			return "{code:-3}" ;
		}else if(result.get("data") == null){
			return "{code:-1}" ;
		}
		Config.ks = result.get("data").toString() ;
		return result.get("data").toString() ;
	}

	//个人信息解析
	public static String getGrxx(){
		//判断登录是否有效
		boolean success = Login.loginAgin() ;
		if(success == false){
			return "{code:-1}" ;
		}
		String url = "http://218.75.197.123:83/jsxsd/grxx/xsxx" ;
		Map<String , String> header = new HashMap() ;
		header.put("Host" , "218.75.197.123:83") ;
		header.put("cookie" , Cookie.cookie) ;
		header.put("User-Agent" , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36") ;
		Map result = URLMethod.doGet(url , header) ;
		if(result.get("data") != null && result.get("data").toString().startsWith("http://192.168.0.1")){
			url = "http://172.16.65.75/jsxsd/grxx/xsxx" ;
			result = URLMethod.doGet(url , header) ;
		}
		String code = result.get("code") == null ? "" : result.get("code").toString() ;
		if(!code.equals("200")){
			return "{code:-1}" ;
		}
		//保存数据
		Map map = new HashMap() ;
		//解析html代码
		Document document = Jsoup.parse(result.get("data").toString()) ;
		Elements elements = document.getElementById("xjkpTable").getElementsByTag("tr") ;
		map.put("yx", elements.get(2).select("td").get(0).text().split("：")[1]) ;
		map.put("zy", elements.get(2).select("td").get(1).text().split("：")[1]) ;
		map.put("bj", elements.get(2).select("td").get(3).text().split("：")[1]) ;
		map.put("xm", elements.get(3).select("td").get(1).text()) ;
		map.put("sex", elements.get(3).select("td").get(3).text()) ;
		map.put("birthday", elements.get(4).select("td").get(1).text()) ;
		map.put("idcard", elements.get(47).select("td").get(3).text()) ;
		map.put("xh" , Config.username) ;
		String d = new Gson().toJson(map) ;
		Config.grzl = d ;
		return d;
	}
}