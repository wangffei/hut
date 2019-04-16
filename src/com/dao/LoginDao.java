package com.dao ;

import com.login.Login ;
import com.util.Config;

public class LoginDao{
	public static String doLogin(String username , String password){
		boolean success = false ;
		try{
			success = Login.doLogin(username , password) ;
		}catch(Exception e){
			System.out.println(e);
		}
		if(success == true){
			Config.reset();
			Config.username = username ;
			Config.password = password ;
			return "OK" ;
		}else{
			return "NO" ;
		}
	}
}