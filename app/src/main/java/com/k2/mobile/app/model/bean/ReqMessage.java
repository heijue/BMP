/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * @Title: ReqMessage.java
 * @Package com.oppo.mo.model.bean
 * @Description: ErrorMsg实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-03-12 15:57:00
 * @version V1.0
 */
public class ReqMessage implements Serializable {

	private String resCode;   // 服务端返回标标识，1,成功，其他为错误
	private String message;   // 消息体内容
	private String resMsg;    // 处理结果提示
	
	public String getResCode() {
		return resCode;
	}
	
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getResMsg() {
		return resMsg;
	}
	
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
}
