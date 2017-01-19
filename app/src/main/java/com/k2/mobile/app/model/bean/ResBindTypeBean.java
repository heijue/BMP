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
public class ResBindTypeBean implements Serializable {

	private String email;   // 邮件
	private String phone;   // 电话
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
