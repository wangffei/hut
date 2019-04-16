package com.login ;

import java.util.Date ;
/**
 * 项目一些登陆参数保存
 */
public class Cookie{
	//登陆后c将ookie保存
	public static String cookie = "" ;
	//cookie创建的时间
	public static Long cookieStart = 0L ;
	//cookie有效间隔 默认设置为15分钟
	public static int cookieSpace = 1000*60*15 ;

	//判断cookie是否有效
	public static boolean checkCookie(){
		Long now = new Date().getTime() ;
		if(now - cookieStart > cookieSpace){
			return false ;
		}else{
			return true ;
		}
	}
}