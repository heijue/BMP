/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * @Title: CurUser.java
 * @Package com.oppo.mo.model.bean
 * @Description: CurUser实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class CurUser implements Serializable {

	/** @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么） */
	private static final long serialVersionUID = 1L;
	
	public  String USER_ID;
	public  String USER_NAME;
	public  String USER_ADDRESS;
	public  String USER_EMAIL;
	public  String USER_FIRST_NAME;
	public  String USER_LAST_NAME;
	public  String USER_PHONE;
	public  String USER_REMARK;
	public  int USER_SEX;
	public  int USER_STATUS;
	
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getUSER_NAME() {
		return USER_NAME;
	}
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}
	public String getUSER_ADDRESS() {
		return USER_ADDRESS;
	}
	public void setUSER_ADDRESS(String uSER_ADDRESS) {
		USER_ADDRESS = uSER_ADDRESS;
	}
	public String getUSER_EMAIL() {
		return USER_EMAIL;
	}
	public void setUSER_EMAIL(String uSER_EMAIL) {
		USER_EMAIL = uSER_EMAIL;
	}
	public String getUSER_FIRST_NAME() {
		return USER_FIRST_NAME;
	}
	public void setUSER_FIRST_NAME(String uSER_FIRST_NAME) {
		USER_FIRST_NAME = uSER_FIRST_NAME;
	}
	public String getUSER_LAST_NAME() {
		return USER_LAST_NAME;
	}
	public void setUSER_LAST_NAME(String uSER_LAST_NAME) {
		USER_LAST_NAME = uSER_LAST_NAME;
	}
	public String getUSER_PHONE() {
		return USER_PHONE;
	}
	public void setUSER_PHONE(String uSER_PHONE) {
		USER_PHONE = uSER_PHONE;
	}
	public String getUSER_REMARK() {
		return USER_REMARK;
	}
	public void setUSER_REMARK(String uSER_REMARK) {
		USER_REMARK = uSER_REMARK;
	}
	public int getUSER_SEX() {
		return USER_SEX;
	}
	public void setUSER_SEX(int uSER_SEX) {
		USER_SEX = uSER_SEX;
	}
	public int getUSER_STATUS() {
		return USER_STATUS;
	}
	public void setUSER_STATUS(int uSER_STATUS) {
		USER_STATUS = uSER_STATUS;
	}
	
}
