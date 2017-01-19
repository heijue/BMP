package com.k2.mobile.app.utils;    

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
/**
 * @Title SystemUtil.java
 * @Package com.oppo.mo.utils
 * @Description 系统相关工具类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-05-26 15:37:00
 * @version V1.0
 */
public class SystemUtil {

	/**
	 * 获取当前程序的版本号 
	 * @param context 上下文
	 * @return String 版本号
	 */
	public static String getVersionName(Context context) throws Exception{
		// 获取packagemanager的实例 
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
	    return packInfo.versionName; 
	}
	
	/**
	 * 获取当前程序的版本号 
	 * @param context 上下文
	 * @return int 版本号
	 */
	public static int getVersionCode(Context context) throws Exception{
		// 获取packagemanager的实例 
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
	    return packInfo.versionCode; 
	}
}
 