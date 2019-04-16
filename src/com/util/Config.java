package com.util ;

import java.io.BufferedReader ;
import java.io.FileReader ;
import java.io.ByteArrayOutputStream ;
import java.io.ByteArrayInputStream ;
import java.io.ObjectInputStream ;
import java.io.FileInputStream ;
import java.util.Map ;

import java.nio.* ;
import java.nio.channels.FileChannel;  

public class Config{
	//用户名
	public static String username = null ;
	//密码
	public static String password = null ;
	//课表
	public static Map kb = null ;
	//学分
	public static String xf = null ;
	//成绩
	public static String cj = null ;
	//实习安排
	public static String sx = null ;
	//等级考试查询
	public static String dj = null ;
	//保存学校周历
	public static Map zl = null ;
	//保存代理服务器
	public static Map schKb = null ;
	//考试查询数据保存
	public static String ks = null;
	//个人资料
	public static String grzl = null ;

	//重置所有参数
	public static void reset(){
		username = null ;
		password = null ;
		kb = null ;
		xf = null ;
		cj = null ;
		sx = null ;
		dj = null ;
		zl = null ;
		grzl = null ;
	}
	

	//在初始化时将配置文件全部读入内存
	public static void init(){
		BufferedReader reader = null ;
		try{
			reader = new BufferedReader(new FileReader("user.conf")) ;
			String line = "" ;
			while((line = reader.readLine()) != null){
				line = line.trim() ;
				if(line.startsWith("username")){
					String[] strs = line.split("=") ;
					username = strs[1].trim();
				}else if(line.startsWith("password")){
					String[] strs = line.split("=") ;
					password = strs[1].trim() ;
				}
			}
		}catch(Exception e){
			System.out.println(e) ;
		}finally{
			if(reader != null){
				try{
					reader.close() ;
				}catch(Exception e){
					System.out.println(e) ;
				}
			}
		}
		ObjectInputStream in = null ;
		FileChannel ch = null ;
		try{
			FileInputStream fin = new FileInputStream("all.source") ;
			ch = fin.getChannel() ;
			int capacity = 1024;
			ByteBuffer bf = ByteBuffer.allocate(capacity);  
			ByteArrayOutputStream out = new ByteArrayOutputStream() ;
			int len = -1 ;
			while((len = ch.read(bf)) != -1){
				bf.clear() ;
				out.write(bf.array() , 0 , len) ;
			}
			in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())) ;
			Object obj = in.readObject() ;
			schKb = (Map)obj ;
		}catch(Exception e){
			System.out.println(e) ;
		}finally{
			if(in != null){
				try{
					in.close() ;
				}catch(Exception e){
					System.out.println(e) ;
				}		
			}
			if(ch != null){
				try{
					ch.close() ;
				}catch(Exception e){
					System.out.println(e) ;
				}
			}
		}
		System.out.println("加载完成") ;
	}
}