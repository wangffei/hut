package com.servlet ;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import com.html.WriteHtml;
import com.wf.inter.HttpServletRequest;
import com.wf.inter.HttpServletResponse;
import com.wf.zj.RequestMapper;

@RequestMapper(path="/html")
public class HtmlServlet {
	
	@RequestMapper(path="jsDetail")
	public void jsMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String zc = request.getParameter("zc") ;
		String day = request.getParameter("day") ;
		String classroom =  request.getParameter("classroom") ;
		StringBuffer buf = WriteHtml.getJsDetailHTML(zc , day , classroom) ;
		String data = buf.toString() ;
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream() , "utf-8"));
		response.setContentTyoe("text/html;charset=utf-8");
		writer.write(data);
		writer.close() ;
	}
}