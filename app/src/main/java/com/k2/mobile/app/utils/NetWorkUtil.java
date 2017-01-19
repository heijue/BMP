/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

/**
 * @Title NetWorkUtil.java
 * @Package com.oppo.mo.utils
 * @Description 网络工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class NetWorkUtil {

	private static String LOG_TAG = "NetWorkUtil";

	public static Uri uri = Uri.parse("content://telephony/carriers");

	/**
	 * 判断是否有网络连�?
	 */
	public static boolean isNetworkAvailable(Context context) {
		try {
			return isWifiDataEnable(context)||isMobileDataEnable(context);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
//		ConnectivityManager connectivity = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//		if (connectivity == null) {
//		} else {
//			NetworkInfo[] info = connectivity.getAllNetworkInfo();
//			if (info != null) {
//				for (int i = 0; i < info.length; i++) {
//					if (info[i].isAvailable()) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
	}

	/**
	 * 判断网络是否为漫�?
	 */
	public static boolean isNetworkRoaming(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null
					&& info.getType() == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				if (tm != null && tm.isNetworkRoaming()) {
					return true;
				} else {
				}
			} else {
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isMobileDataEnable(Context context) throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isMobileDataEnable = false;
	
		isMobileDataEnable = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

		return isMobileDataEnable;
	}

	
	/**
	 * 判断wifi 是否可用
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isWifiDataEnable(Context context) throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isWifiDataEnable = false;
		isWifiDataEnable = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		return isWifiDataEnable;
	}

//	/**
//	 * 设置Mobile网络�?��
//	 * 
//	 * @param context
//	 * @param enabled
//	 * @throws Exception
//	 */
//	public static void setMobileDataEnabled(Context context, boolean enabled)
//			throws Exception {
//		APNManager apnManager=APNManager.getInstance(context);
//		List<APN> list = apnManager.getAPNList();
//		if (enabled) {
//			for (APN apn : list) {
//				ContentValues cv = new ContentValues();
//				cv.put("apn", apnManager.matchAPN(apn.apn));
//				cv.put("type", apnManager.matchAPN(apn.type));
//				context.getContentResolver().update(uri, cv, "_id=?",
//						new String[] { apn.apnId });
//			}
//		} else {
//			for (APN apn : list) {
//				ContentValues cv = new ContentValues();
//				cv.put("apn", apnManager.matchAPN(apn.apn) + "mdev");
//				cv.put("type", apnManager.matchAPN(apn.type) + "mdev");
//				context.getContentResolver().update(uri, cv, "_id=?",
//						new String[] { apn.apnId });
//			}
//		}
//	}

//	public static boolean isNetWorkAvailble(Context context) throws Exception{
//		return isWifiDataEnable(context)||isMobileDataEnable(context);
//	}
	
}
