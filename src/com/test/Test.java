package com.test ;

import com.util.Config ;
import com.wf.server.Server;

public class Test{
	public static void main(String[] args) throws Exception{
		Config.init() ;
		Server server = new Server(9264, "com.servlet") ;
		server.init() ;
	}
}