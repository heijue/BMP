/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.other;

import com.k2.mobile.app.model.bean.HTTPOutput;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;

/**
 * @Title FormatData.java
 * @Package com.oppo.mo.model.http
 * @Description 接口数据封装类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class FormatData {
	
	public static String TAG = "FormatData";
	
	/**
	 * @Title: submitRequest
	 * @Description: TODO
	 * @param @param strReqBody
	 * @param @param strReqClientID
	 * @param @param strLang
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static String submitRequest(String strReqBody, String strReqClientID, String strReqLang) {

		HTTPOutput entity = new HTTPOutput();
		entity.setMessage(strReqBody);
		entity.setToken(strReqClientID);
		entity.setReqLang(strReqLang);
		
		String jsonStr = FastJSONUtil.getEntityToJson(entity.getClass());
		LogUtil.i("jsonStr: "+jsonStr);
		
		return jsonStr;
	}
	
	/**
	 * @Title: submitRequest
	 * @Description: TODO
	 * @param @param strReqBody
	 * @param @param strReqClientID
	 * @param @param strLang
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static <T> String submitRequest(T entity) {

		String jsonStr = FastJSONUtil.getEntityToJson(entity);
		LogUtil.i("jsonStr: "+jsonStr);
		
		return jsonStr;
	}
	
	// Login
	public static String loginRequest(String strUserName, String strPassword) {
		String jsonStr = "{\"UserName\":\"" + strUserName + "\", \"Password\":\""
				+ strPassword + "\"}";
		LogUtil.i("jsonStr: "+jsonStr);
		
		return jsonStr;
	}
	
}
