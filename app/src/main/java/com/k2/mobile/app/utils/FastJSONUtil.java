/*
 * Copyright (c) 2015. OPPO Co,. Ltd.
 */  
package com.k2.mobile.app.utils;    

import com.alibaba.fastjson.JSON;
 
/**
 * @Title: FastJsonUtil.java
 * @Package com.oppo.mo.utils
 * @Description: TODO
 * @Company: K2
 * 
 * @author Jason
 * @date 2015年2月3日 下午2:23:41
 * @version V1.0
 */
public class FastJSONUtil {
	/**
	 * @Title: getEntityToJson
	 * @Description: 封装实体转JSON字符串
	 * @param @param entity
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static <T> String getEntityToJson(T entity) {
		String strTmp = null;
		if (entity != null) {

			// 解析json对象
			try {
				strTmp = JSON.toJSONString(entity);
			}
			catch (Exception e) {
				//LogUtil.saveCrashInfo2File(e);
				e.printStackTrace();
			}
		}
		return strTmp;
	}
	
	/**
	 * @Title: getJSONToEntity
	 * @Description: 封装JSON字符串映射实体方法
	 * @param @param jsonData
	 * @param @param entity
	 * @param @return    设定文件
	 * @return T    返回类型
	 * @throws
	 */

	public static <T> T getJSONToEntity(String jsonData, Class<T> entity) {
		T infoVO = null;

		try {
			if (!"".equals(jsonData)) {
				
				// 替换 \" 为 "
				jsonData = jsonData.replace("\\\"", "\"");
				// 替换 \\ 为 \
				jsonData = jsonData.replace("\\\\", "\\");
				//jsonData = jsonData.substring(1, jsonData.length()-1);
				
				// 解析json实体
				infoVO = JSON.parseObject(jsonData, entity);
			}
		}
		catch (Exception e) {
			//LogUtil.saveCrashInfo2File(e);
			e.printStackTrace();
		}
		
		return infoVO;
	}
	
}
 