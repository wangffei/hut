package com.test ;

import com.server.Server ;
import com.util.Config ;

public class Test{
	public static void main(String[] args){
		Config.init() ;
		new Thread(new Server()).start() ;
	}
}