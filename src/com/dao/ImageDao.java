package com.dao ;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.util.ChineseDateFormat;

import com.dao.UserDao ;
import com.util.Config ;

public class ImageDao{
	//画周历
	public static BufferedImage drawZl(){
		Map map = Config.zl ;
		if(map == null){
			map = UserDao.getZl() ;
		}
		int width = 600 ;
		//计算横条要画多少条
		int count = map.size() - 1 ;
		//每个日期矩形的边长
		int rectWidth = (width - 100) / 7 ;
		//图片高度
		int height = rectWidth * count + 120 ;
		//创建一个画板
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB) ;
		//拿到画笔
		Graphics g = img.createGraphics() ;
		//画背景图片
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		//画头部信息栏
		g.setColor(new Color(151 , 158 , 171));
		g.fillRect(0, 0, width, 80);
		//画竖排的线
		g.setColor(new Color(205 , 205 , 205));
		g.drawLine(1, 0, 1, height);
		g.drawLine(60, 0, 60, height - 40);
		g.drawLine(100, 0, 100, height - 40);
		g.drawLine(100+rectWidth, 40, 100+rectWidth, height - 40);
		g.drawLine(100+2*rectWidth, 40, 100+2*rectWidth, height - 40);
		g.drawLine(100+3*rectWidth, 40, 100+3*rectWidth, height - 40);
		g.drawLine(100+4*rectWidth, 40, 100+4*rectWidth, height - 40);
		g.drawLine(100+5*rectWidth, 40, 100+5*rectWidth, height - 40);
		g.drawLine(100+6*rectWidth, 40, 100+6*rectWidth, height - 40);
		g.drawLine(width-1, 0, width-1, height);
		//画横排的线
		g.drawLine(0, 0, width, 0);
		g.drawLine(100, 40, width, 40);
		g.drawLine(0 , 80, width, 80);
		for(int i=1 ; i<=count ; i++){
			g.drawLine(0, 80+i*rectWidth, width, 80+i*rectWidth);
		}
		g.drawLine(0, height-1, width, height-1);
		//写入头部信息
		g.setColor(Color.BLACK);
		g.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 16));
		g.drawString("月份", 10, 40);
		g.drawString("周次", 62, 40);
		g.drawString("星期", width/2 + 30, 25);
		String[] xq = new String[]{"一" , "二" , "三" , "四" , "五" , "六" , "七"} ;
		for(int i=0 ; i<7 ; i++){
			g.drawString(xq[i], 100+i*rectWidth+25, 65);
		}
		//画日历
		Font font1 = new Font("楷体", Font.LAYOUT_RIGHT_TO_LEFT, 18) ;
		Font font2 = new Font("楷体", Font.LAYOUT_RIGHT_TO_LEFT, 14) ;
		g.setFont(font1);
		for(int i=1 ; i<=count ; i++){
			Map m = (Map)map.get(i) ;
			g.drawString(i+"" , 75  , 80+(i-1)*rectWidth+40);
			g.drawString(((String)m.get(1)).split("-")[1]+"月" , 15  , 80+(i-1)*rectWidth+40);
			for(int j=1 ; j<=m.size(); j++){
				String str = (String)m.get(j) ;
				Integer year = Integer.parseInt(str.split("-")[0]) ;
				Integer month = Integer.parseInt(str.split("-")[1]) ;
				Integer day = Integer.parseInt(str.split("-")[2]) ;
				String cnDate = new ChineseDateFormat().getLunarDate(year, month, day, false) ;
				cnDate = cnDate.length() > 3 ? cnDate.substring(0 , 3 ) : cnDate ;
				g.drawString(day+"", 100+(j-1)*rectWidth+25, 80+(i-1)*rectWidth+30);
				g.setFont(font2);
				g.drawString(cnDate, 100+(j-1)*rectWidth+20, 80+(i-1)*rectWidth+50);
				g.setFont(font1);
			}
		}
		g.drawString("放假啦！！！", width/2 - 40, count*rectWidth+110);
		return img ;
	}
}