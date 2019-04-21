package com.servlet ;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import com.dao.LoginDao;
import com.wf.inter.HttpServlet;
import com.wf.inter.HttpServletRequest;
import com.wf.inter.HttpServletResponse;
import com.wf.zj.RequestMapper;

@RequestMapper(path="/login.do")
public class LoginServlet implements HttpServlet{
	public void service(HttpServletRequest request , HttpServletResponse response) throws Exception{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream() , "utf-8")) ;
		String username = request.getParameter("username") ;
		String password = request.getParameter("password");
		String result = LoginDao.doLogin(username , password) ;
		writer.write(result);
		writer.close() ;
	}
}