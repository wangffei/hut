package com.login ;

import java.util.Map ;
import java.util.HashMap ;
import java.util.Date ;

import java.net.URLEncoder ;

import com.util.URLMethod ;
import com.util.Base64Util ;
import com.util.Config ;
import com.login.Cookie ;

public class Login{
	public static Boolean doLogin(String username , String password) throws Exception{
		Map<String , String> map = new HashMap<>() ;
		String url = "http://218.75.197.123:83/jsxsd/xk/LoginToXk" ;
		map.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        map.put("Host" , "218.75.197.123:83");
        map.put("Content-Type" , "application/x-www-form-urlencoded") ;
        map.put("Referer" , "http://218.75.197.123:83/jsxsd/") ;
        String param = URLEncoder.encode(Base64Util.encodeBytes(username.getBytes())+"%%%"+Base64Util.encodeBytes(password.getBytes()) , "utf-8") ;
		Map<String , StringBuffer> result = URLMethod.doPost(url , "userAccount="+username+"&userPassword="+password+"&encoded="+param , map) ;
		if(result.get("data") != null && result.get("data").toString().startsWith("http://192.168")){
			url = "http://172.16.65.75/jsxsd/xk/LoginToXk" ;
			map.put("Referer" , "http://172.16.65.75/jsxsd/") ;
			result = URLMethod.doPost(url , "userAccount="+username+"&userPassword="+password+"&encoded="+param , map) ;
		}
		String code = result.get("code") == null ? "" : result.get("code").toString().trim() ;
		if(code.equals("301") || code.equals("302")){
			Cookie.cookie = result.get("cookie").toString();
			Cookie.cookieStart = new Date().getTime() ;
			return true ;
		}
		return false ;
	}

	public static Boolean loginAgin(){
		if(!Cookie.checkCookie()){
			boolean success = false ;
			try{
				success = Login.doLogin(Config.username , Config.password) ;
			}catch(Exception e){
				System.out.println(e);
			}
			if(!success){
				try{
					success = Login.doLogin(Config.username , Config.password) ;
				}catch(Exception e){
					System.out.println(e);
				}
				if(!success){
					return false ;
				}
			}
		}
		return true ;
	}
}