package com.dao ;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HutDao{
	private static String xh = "1740****119" ;         //将以此学号对hut电费查询接口进行访问
	//该学号对应的登陆令牌
	private static String userkey = "8c53c79c80d6d5a27995423461a***********" ; //hut助手的登陆令牌

	public static String dfcxDao(String build , String room , String area){
		String path = getUrl(build, room, xh, Integer.parseInt(area));
		InputStream in = null ;
		String buf = "" ;
		try{
			URL url = new URL(path) ;
			in = url.openStream() ;
			byte[] data = new byte[1024] ;
			int len = -1 ;
			while((len = in.read(data)) != -1){
				buf += new String(data , 0 , len , "utf-8") ;
			}
		}catch(Exception e){
			return "{code:-1}" ;
		} finally{
			if(in != null){
				try{
					in.close() ;
				}catch(Exception e){
					System.out.println(e) ;
				}
			}
		}
		return buf.toString() ;
	}






	/**
	 * 
	 * @param a 宿舍楼号
	 * @param b 宿舍门牌号
	 * @param c 学生学号
	 * @param d 校区
	 * @return
	 */
	private static String getUrl(String a , String b , String c , Integer d){
		if(d != 1 && d != 2){
			throw new RuntimeException("校区不存在") ;
		}
		String str = a+b+c+userkey+d ;
		long[] data = oneStep(str) ;
		List<Long> list = wordsToBytes(data) ;
		String key = bytesToHex(list);
		String url = "https://api.huthelper.cn/api/v3/get/power_e/"+d+"/"+a+"/"+b+"/"+c+"/"+userkey+"/"+key ;
		return url ;
	}


	//获取第一步处理后的数组
	private static long[] oneStep(String str){
		byte[] e = str.getBytes() ;
		int[] n = byteToWords(e) ;
		int o = 8*e.length ;
		int[] i = new int[80] ;
		long s = 1732584193 ;
		long c = - 271733879 ;
		long u = -1732584194 ;
		long l = 271733878 ;
		long f = -1009589776 ;
		n[o >> 5] |= 128 << 24  - o % 32 ;
		n[15 + (o + 64 >>> 9 << 4)] = o ;
		for (int p = 0; p < n.length; p += 16) {
			long m = s
                , h = c
                , d = u
                , y = l
                , v = f;
			for (int b = 0; b < 80; b++) {
				if (b < 16)
					i[b] = n[p + b];
				else {
					long g = (int)(i[b - 3] ^ i[b - 8] ^ i[b - 14] ^ i[b - 16]);
					i[b] = (g <= Integer.MAX_VALUE ? (int)g << 1 : (int)(g << 1)) | (g <= Integer.MAX_VALUE ? (int)g >>> 31 : (int)(g >>> 31)) ;
				}
				long a1 = s <= Integer.MAX_VALUE ? (int)s << 5 :  (int) (s << 5) ; 
				long a2 = i[b] & 0x0FFFFFFFFl; 
				long a6 = s <= Integer.MAX_VALUE ? (int)s >>> 27 : (int)((s & 0x0FFFFFFFFl ) >> 27) ;
				long a3 = (int)(c & u | ~c & l) ;
				long a4 = (int)(c ^ u ^ l) ;
				long a5 = (int)(c & u | c & l | u & l) ;
				long w = (a1 | a6) + f + a2 + (b < 20 ? 1518500249l + a3 : b < 40 ? 1859775393l + a4 : b < 60 ? a5 - 1894007588l : a4 - 899497514l);
				f = l;
				l = u;
				u = c <= Integer.MAX_VALUE ?  ((int)c << 30) | ((int)c >>> 2) : (int)(c << 30 | (c & 0x0FFFFFFFFl) >>> 2) ;
				c = s ;
				s = w <= Integer.MAX_VALUE ? (int)w : w ;
			}
			s += m;
			c += h;
			u += d;
			l += y;
			f += v ;
		}
		return new long[]{s , c , u , l , f} ;
	}
	
	private static int[] byteToWords(byte[] e){
		int[] t = new int[32] ;
		for(int n=0 ,r=0 ; n<e.length ; n++ , r+=8){
			t[r >>> 5] |= e[n] << 24 - r%32 ;
		}
		return t ;
	}
	
	private static List<Long> wordsToBytes(long[] e){
		List<Long> t = new ArrayList<Long>() ; 
		for (int n = 0; n < 32 * e.length; n += 8)
            t.add(e[n >>> 5] >>> 24 - n % 32 & 255);
        return t ;
	}
	
	private static String bytesToHex(List<Long> e){
		StringBuffer buff = new StringBuffer() ;
		for (Long l : e) {
			buff.append(Integer.toHexString((int) (l >>> 4)));
			buff.append(Integer.toHexString((int) (15 & l))) ;
		}
		return buff.toString() ;
	}

}
