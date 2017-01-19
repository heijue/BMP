/*
 * Copyright (c) 2015. OPPO Co,. Ltd.
 */  
package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
 
/**
 * @Title: HTTPOutput.java
 * @Package com.oppo.mo.bean
 * @Description: TODO
 * @Company: K2
 * 
 * @author Jason.wu
 * @date 2015-01-29 15:37:49
 * @version V1.0
 */
public class HTTPOutput implements Serializable {

	/** 接口加密数据 */
	private String message;
	
	/** TOKEN */
	private String token;

	/** 客户端使用语言版本 */
	private String reqLang;
	/** 用户校验URL */
	private String url;
	/** 设备类型 */
	private String deviceType;
	
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @return the reqLang
	 */
	public String getReqLang() {
		return reqLang;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @param reqLang the reqLang to set
	 */
	public void setReqLang(String reqLang) {
		this.reqLang = reqLang;
	}

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
     * 获取message信息
     * */
	public String getMessage() {
		return message;
	}
	/**
     * 设置message信息
     * */
	public void setMessage(String message) {
		this.message = message;
	}

}
 