/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * @Title DeviceTool.java
 * @Package com.oppo.mo.utils
 * @Description 设备工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class DeviceUtil
{
	private static DeviceUtil ins;
	/**
	 * 串号
	 */
	public String imsi;
	public String imei;
	/**
	 * wifi的mac地址
	 */
	public String mac;
	
	/**
	 * APP的包名称
	 */
	public String appname;
	/**
	 * / 手机型号
	 */
	public String device;
	/**
	 * 版本�?
	 */
	public int versioncode;
	public String versionname="";
	/**
	 * 随机�?
	 */
	public String guid;
	/**
	 * 手机的sdk版本
	 */
	public String sdk;
	 /**
	  * sdk名称
	  */
	public String sdkname;
	
	/**
	 * 手机的sdk版本
	 */
	public int sdk_int;

	public static DeviceUtil init(Context context)
	{
		if (ins == null)
		{
			ins = new DeviceUtil();
			/**
			 * 获取手机的号�?
			 */
			TelephonyManager phone = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (phone != null)//号码等于空就获取手机的串�?
			{
				ins.imsi = phone.getSubscriberId();
				ins.imei = phone.getDeviceId();
			}
			/**
			 * 获取wifi
			 */
			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			if (wifi.getConnectionInfo() != null)//是wifi上网就获取mac地址
			{
				ins.mac = wifi.getConnectionInfo().getMacAddress();
			}
			/**
			 * 获取随机�?
			 */
			ins.guid = "TIME" + System.currentTimeMillis();
			if (ins.imei != null)
			{
				ins.guid = "IMEI" + ins.imei;
			}
			//如果串号为空clientcode就取随机�?
			if (ins.imsi != null)
			{
				ins.guid = "IMSI" + ins.imsi;
			}
			ins.device = android.os.Build.MODEL;
			ins.sdk  = android.os.Build.VERSION.SDK;
			ins.sdk_int = android.os.Build.VERSION.SDK_INT;
			ins.sdkname = "Android" + android.os.Build.VERSION.RELEASE;
			ins.appname=context.getPackageName();
			 
			try
			{
				PackageInfo info = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				 
				ins.versioncode=info.versionCode;
				ins.versionname=info.versionName;
			} catch (Throwable e)
			{
				 e.getStackTrace();
			}
		}
	
		return ins;
	}
	/**
	 * 获取手机的MAC地址
	 */
	public static String getMacAddr(Context context){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
}
