/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * @Title: ReqMessage.java
 * @Package com.oppo.mo.model.bean
 * @Description: 公用JSON解析实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-03-12 15:57:00
 * @version V1.0
 */
public class ResPublicBean implements Serializable {

	private String isNeedErase;		// 是否擦除
	private String success;			// 成功标识
	private String timeOut;			// 超时时间
	
	private String result;
	private String message;
	private String messageDetails;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(String messageDetails) {
		this.messageDetails = messageDetails;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getIsNeedErase() {
		return isNeedErase;
	}

	public void setIsNeedErase(String isNeedErase) {
		this.isNeedErase = isNeedErase;
	}

}
