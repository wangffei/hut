package com.servlet ;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import com.dao.HutDao;
import com.wf.inter.HttpServletRequest;
import com.wf.inter.HttpServletResponse;
import com.wf.zj.RequestMapper;

@RequestMapper(path="/huthelper")
public class HutServlet {
	
	@RequestMapper(path="dfcx")
	public void dfcxMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String build = request.getParameter("build") ;
		String room = request.getParameter("room") ;
		String area = request.getParameter("area") ;
		String data = "" ;
		if(build == null || room == null || area == null){
			data = "{code:-1}" ;
		}else{
			data = HutDao.dfcxDao(build , room , area) ; 
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream() , "utf-8"));
		response.setContentTyoe("text/html;charset=utf-8");
		writer.write(data);
		writer.close() ;
	}
}