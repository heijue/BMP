package com.k2.mobile.app.utils;
/**
 * @Title NumberHelper.java
 * @Package com.oppo.mo.utils
 * @Description 数据格式处理类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-18 16:59:00
 * @version V1.0
 */
public class NumberHelper {

	public static String LeftPad_Tow_Zero(int str) {
		java.text.DecimalFormat format = new java.text.DecimalFormat("00");
		return format.format(str);
	}
}
