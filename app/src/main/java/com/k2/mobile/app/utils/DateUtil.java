/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Title DateUtil.java
 * @Package com.oppo.mo.utils
 * @Description 日期工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class DateUtil {

	/**
	 * 时间字符串格式化（dd-MM-yyyy HH:mm:ss)
	 * 
	 * @param time
	 * @return
	 */
	public static String datetimeFormat1(String time) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter1 = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");
		try {
			date = formatter.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String changeTime = formatter1.format(date);
		return changeTime;
	}
	
	/**
	 * 时间字符串格式化（MM.dd hh:mm)
	 * 
	 * @param time
	 * @return
	 * @throws ParseException 
	 */
	public static String datetimeFormat(String time) throws ParseException {
		if(null == time){
			return null;
		}
		/*
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = formatter.parse(time);
		
		StringBuilder sb = new StringBuilder();
		sb.append(date.getMonth()<10 ? ("0" + date.getMonth()) : (date.getMonth() + ""));
		sb.append(".");
		sb.append(date.getDay()<10 ? ("0" + date.getDay()) : (date.getDay() + ""));
		sb.append(" ");
		sb.append(date.getHours()<10 ? ("0" + date.getHours()) : (date.getHours() + ""));
		sb.append(":");
		sb.append(date.getMinutes()<10 ? ("0" + date.getMinutes()) : (date.getMinutes() + ""));

		*/
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatter = s.format(date);
		
		StringBuilder sb = new StringBuilder();
		
		String[] times = formatter.split(" ");
		if(null != times && times.length > 1){
			String[] ymd = times[0].split("-");
			if(null != ymd && ymd.length > 2){
				sb.append(ymd[1].length()<2 ? "0" + ymd[1] : ymd[1]);
				sb.append(".");
				sb.append(ymd[2].length()<2 ? "0" + ymd[2] : ymd[2]);
			}
			
			sb.append(" ");
			
			String[] hms = times[1].split(":");
			if(null != hms && hms.length > 2){
				sb.append(hms[0].length()<2 ? "0" + hms[0] : hms[0]);
				sb.append(":");
				sb.append(hms[1].length()<2 ? "0" + hms[1] : hms[1]);
			}
		}
		
		return sb.toString();
	}

	/**
	 * 时间字符串格式化（yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	public static String datetimeFormat2(String time) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = formatter.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String changeTime = formatter1.format(date);
		return changeTime;
	}

	/**
	 * 设置缺省时间
	 * 
	 * @param time
	 * @return
	 */
	public static String setDefaultTime(String time) {
		if (time.equals("2014-01-01 00:00:00")) {
			time = "----";
		}
		return time;
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getDownloadDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * @return yyyyMMdd_HH-mm-ss
	 */
	public static String getPhotoDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
		return sdf.format(new Date());
	}
	/**
	 * @return yyyy-MM-dd
	 */
	public static String getDate_hms() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(new Date());
	}
	/**
	 * @return yyyy-MM-dd
	 */
	public static String getDate_hms(String time) {
		if(null == time || "".equals(time.trim())){
			return null;
		}
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = ymd.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return sdf.format(date);
	}
	/**
	 * @return yyyy-MM-dd
	 */
	public static String getDate_ymd() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	
	/**
	 * @return yyyy-MM-dd
	 */
	public static String getDate_ymd(String time) {
		if(null == time || "".equals(time.trim())){
			return null;
		}
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = ymd.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return sdf.format(date);
	}
	
	/**
	 * @return yyyy-MM-dd
	 */
	public static String getDate_ymdhm(String time) {
		if(null == time || "".equals(time.trim())){
			return null;
		}
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = ymd.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return sdf.format(date);
	}
	// /**
	// * 字符串转换成日期
	// * @param str
	// * @return date
	// */
	// public static Date stringToDateTimeFormat(String str) {
	//
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// Date date = null;
	// try {
	// date = format.parse(str);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// return date;
	// }

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static String stringToDateTimeFormat(String str) {
		String tmp = str.substring(0, 19);
		return tmp.replace("T", " ");
	}

	public static String dateTimeStringFormat(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return format.format(date);
	}

	/**
	 * 根据时区改变时间
	 * 
	 * @param time
	 * @return changeTime
	 */
	public static String chageServerTime(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strDate = formatter.parse(time, pos);
		long utcTime = strDate.getTime();
		long localOffset = strDate.getTimezoneOffset() * 60000;
		long localTime = utcTime - localOffset;
		Date date = new Date(localTime);
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String changeTime = formatter1.format(date);
		return changeTime;
	}

	/**
	 * 根据时区还原�?时区时间
	 * 
	 * @param time
	 * @return changeTime
	 */
	public static String recoveryServerTime(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strDate = formatter.parse(time, pos);
		long utcTime = strDate.getTime();
		long localOffset = strDate.getTimezoneOffset() * 60000;
		long localTime = utcTime + localOffset;
		Date date = new Date(localTime);
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String changeTime = formatter1.format(date);
		return changeTime;
	}

	/**
	 * 解析字符串，获取当天是几号
	 * 
	 * @param time
	 * @return changeTime
	 */
	public static int getDay(String str){
		int day = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sdf.parse(str);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date); 
			day = cal.get(Calendar.DATE); //日
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return day;
	}
	
	// 把日期转为字符串 
    public static String ConverToString(Date date) { 
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");          
        return df.format(date); 
    } 
    // 把字符串转为日期 
    public static Date ConverToDate(String strDate) throws Exception { 
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss"); 
        return df.parse(strDate); 
    } 
    // 时间对比（天）
    public static boolean timeComparison(String oldTime, String newTime) {
//    	System.out.println("oldTime = "+oldTime+" , newTime = "+newTime);
    	if(null == oldTime){
    		return false;
    	}
    	// 转换时间格式
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d1 = sdf.parse(oldTime);
			Date d2 = sdf.parse(newTime);
			//一个月比较
			if(Math.abs(((d1.getTime() - d2.getTime()))) <= 30) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
    	return false;
    }
    /**
	 * 时间比较大小
	 * 
	 * @param t1、t2 字符串时间,
	 * @return int 比较结果-1:时间格式不正确,0:时间相等,1:t1时间大于t2, 2:t1时间小于t2, 
	 */
    public static int timeComparingSize(String t1, String t2){
    	
    	DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar c1=java.util.Calendar.getInstance();
    	Calendar c2=java.util.Calendar.getInstance();
    	
    	try
    	{
	    	c1.setTime(df.parse(t1));
	    	c2.setTime(df.parse(t2));
    	}catch(ParseException e){
    		return -1; // 格式出错
    	}
    	
    	int result = c1.compareTo(c2);
    	if(result == 0){
    		return 0;  // t1相等t2
    	} else if  (result < 0) {
    		return 2;	// t1小于t2
    	} else {
    		return 1;	// t1大于t2
    	}
    }
}
