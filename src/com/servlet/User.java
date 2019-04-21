package com.servlet ;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.dao.UserDao;
import com.google.gson.Gson;
import com.util.Config;
import com.wf.inter.HttpServletRequest;
import com.wf.inter.HttpServletResponse;
import com.wf.zj.RequestMapper;

@RequestMapper(path="/user.do")
public class User {

	//退出登录
	@RequestMapper(path="userquit")
	public void quitMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		Config.username = null ;
		Config.password = null ;
		String data = "{'fun':function(){window.location='login.html'}}" ;
		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}

	//个人信息
	@RequestMapper(path="grxx")
	public void grxxMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String data = Config.grzl ;
		if(data == null){
			data = UserDao.getGrxx() ;
		}
		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}

	//考试查询
	@RequestMapper(path="ks")
	public void ksMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String data = null ;
		
		data = UserDao.ksDetail() ;

		if(data.contains("code") || data == null || data.trim().equals("")){
			if(Config.ks != null){
				data = Config.ks ;
			}
		}

		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}

	//教室详情
	@RequestMapper(path="clasdetail")
	public void cdlMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		Integer zc = Integer.parseInt(request.getParameter("zc")) ;
		Integer day = Integer.parseInt(request.getParameter("day")) ;
		String classroom = request.getParameter("classroom") ;
		String data = UserDao.getRoomDetail(zc , day , classroom) ;
		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}

	//空教室数据获取
	@RequestMapper(path="kjs")
	public void kjsMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		Integer zc = Integer.parseInt(request.getParameter("zc")) ;
		Integer day = Integer.parseInt(request.getParameter("day")) ;
		Integer clas = Integer.parseInt(request.getParameter("clas")) ;
		String data = UserDao.getKjs(zc , day , clas) ;
		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}

	//获取周历
	@RequestMapper(path="zl")
	public void zlMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		Map data = null ;
		String flush = request.getParameter("flush") ;
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
		response.setContentTyoe("text/javascript;charset=utf-8");
		response.getWriter().print(result);
	}

	//获取等级考试成绩
	@RequestMapper(path="dj")
	public void djMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String data = null ;
		String flush = request.getParameter("flush") ;
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
		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}

	//实习安排
	@RequestMapper(path="sx")
	public void sxMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String data = null ;
		String flush = request.getParameter("flush") ;
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
		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}

	//成绩查询
	@RequestMapper(path="cj")
	public void cjMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String data = null ;
		String flush = request.getParameter("flush") ;
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
		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}

	//获取服务器时间
	@RequestMapper(path="time")
	public void getTime(HttpServletRequest request , HttpServletResponse response) throws Exception{
		response.setContentTyoe("text/javascript;charset=utf-8");
		String data = "var date_now = new Date("+new Date().getTime()+"); " ;
		response.getWriter().print(data);
	}

	//学分查询
	@RequestMapper(path="xf")
	public void xfMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String data = null ;
		String flush = request.getParameter("flush") ;
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
		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}

	//课表查询
	@RequestMapper(path="kb")
	public void kbMethod(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String data = null ;
		Map map = null ;
		String flush = request.getParameter("flag") ;
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
		response.setContentTyoe("application/json;charset=utf-8");
		response.getWriter().print(data);
	}
}