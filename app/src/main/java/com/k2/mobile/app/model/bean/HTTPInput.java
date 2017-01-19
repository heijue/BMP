/*
 * Copyright (c) 2015. OPPO Co,. Ltd.
 */  
package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
 
/**
 * @Title: HTTPInput.java
 * @Package com.oppo.mo.bean
 * @Description: TODO
 * @Company: K2
 * 
 * @author Jason.wu
 * @date 2015-01-29 15:37:49
 * @version V1.0
 */
public class HTTPInput implements Serializable {

	/** 
	 * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 1L;

	/** 接口加密数据 */
	private String resBody;

	/** 中间件返回错误代码 */
	private String resCode;

	/** 中间件返回错误信息 */
	private String resMsg;

	/**
	 * @return the resBody
	 */
	public String getResBody() {
		return resBody;
	}

	/**
	 * @param resBody the resBody to set
	 */
	public void setResBody(String resBody) {
		this.resBody = resBody;
	}

	/**
	 * @return the resCode
	 */
	public String getResCode() {
		return resCode;
	}

	/**
	 * @param resCode the resCode to set
	 */
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	/**
	 * @return the resMsg
	 */
	public String getResMsg() {
		return resMsg;
	}

	/**
	 * @param resMsg the resMsg to set
	 */
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

}
 