/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * @Title: CurEmployee.java
 * @Package com.oppo.mo.model.bean
 * @Description: CurEmployee实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class CurEmployee implements Serializable {

	/** @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么） */
	private static final long serialVersionUID = 1L;
	
	public  String EMPLOYEE_ID;
	public  String EMPLOYEE_LOGIN_ID;
	public  String EMPLOYEE_FULL_NAME;
	public  int EMPLOYEE_GENDER;
	public  boolean EMPLOYEE_IS_SUPERVISOR;
	public  String EMPLOYEE_LANGUAGE;
	public  int EMPLOYEE_LAST_STATUS;
	public  String EMPLOYEE_PROJECT_ID;
	public  String EMPLOYEE_PROJECT_NAME;
	public  String EMPLOYEE_PROJECT_ID_SELECTED;
	public  String EMPLOYEE_PROJECT_NAME_SELECTED;

	public  boolean EMPLOYEE_CAN_MANAGER;

	public String getEMPLOYEE_ID() {
		return EMPLOYEE_ID;
	}

	public void setEMPLOYEE_ID(String eMPLOYEE_ID) {
		EMPLOYEE_ID = eMPLOYEE_ID;
	}

	public String getEMPLOYEE_LOGIN_ID() {
		return EMPLOYEE_LOGIN_ID;
	}

	public void setEMPLOYEE_LOGIN_ID(String eMPLOYEE_LOGIN_ID) {
		EMPLOYEE_LOGIN_ID = eMPLOYEE_LOGIN_ID;
	}

	public String getEMPLOYEE_FULL_NAME() {
		return EMPLOYEE_FULL_NAME;
	}

	public void setEMPLOYEE_FULL_NAME(String eMPLOYEE_FULL_NAME) {
		EMPLOYEE_FULL_NAME = eMPLOYEE_FULL_NAME;
	}

	public int getEMPLOYEE_GENDER() {
		return EMPLOYEE_GENDER;
	}

	public void setEMPLOYEE_GENDER(int eMPLOYEE_GENDER) {
		EMPLOYEE_GENDER = eMPLOYEE_GENDER;
	}

	public boolean isEMPLOYEE_IS_SUPERVISOR() {
		return EMPLOYEE_IS_SUPERVISOR;
	}

	public void setEMPLOYEE_IS_SUPERVISOR(boolean eMPLOYEE_IS_SUPERVISOR) {
		EMPLOYEE_IS_SUPERVISOR = eMPLOYEE_IS_SUPERVISOR;
	}

	public String getEMPLOYEE_LANGUAGE() {
		return EMPLOYEE_LANGUAGE;
	}

	public void setEMPLOYEE_LANGUAGE(String eMPLOYEE_LANGUAGE) {
		EMPLOYEE_LANGUAGE = eMPLOYEE_LANGUAGE;
	}

	public int getEMPLOYEE_LAST_STATUS() {
		return EMPLOYEE_LAST_STATUS;
	}

	public void setEMPLOYEE_LAST_STATUS(int eMPLOYEE_LAST_STATUS) {
		EMPLOYEE_LAST_STATUS = eMPLOYEE_LAST_STATUS;
	}

	public String getEMPLOYEE_PROJECT_ID() {
		return EMPLOYEE_PROJECT_ID;
	}

	public void setEMPLOYEE_PROJECT_ID(String eMPLOYEE_PROJECT_ID) {
		EMPLOYEE_PROJECT_ID = eMPLOYEE_PROJECT_ID;
	}

	public String getEMPLOYEE_PROJECT_NAME() {
		return EMPLOYEE_PROJECT_NAME;
	}

	public void setEMPLOYEE_PROJECT_NAME(String eMPLOYEE_PROJECT_NAME) {
		EMPLOYEE_PROJECT_NAME = eMPLOYEE_PROJECT_NAME;
	}

	public String getEMPLOYEE_PROJECT_ID_SELECTED() {
		return EMPLOYEE_PROJECT_ID_SELECTED;
	}

	public void setEMPLOYEE_PROJECT_ID_SELECTED(String eMPLOYEE_PROJECT_ID_SELECTED) {
		EMPLOYEE_PROJECT_ID_SELECTED = eMPLOYEE_PROJECT_ID_SELECTED;
	}

	public String getEMPLOYEE_PROJECT_NAME_SELECTED() {
		return EMPLOYEE_PROJECT_NAME_SELECTED;
	}

	public void setEMPLOYEE_PROJECT_NAME_SELECTED(
			String eMPLOYEE_PROJECT_NAME_SELECTED) {
		EMPLOYEE_PROJECT_NAME_SELECTED = eMPLOYEE_PROJECT_NAME_SELECTED;
	}

	public boolean isEMPLOYEE_CAN_MANAGER() {
		return EMPLOYEE_CAN_MANAGER;
	}

	public void setEMPLOYEE_CAN_MANAGER(boolean eMPLOYEE_CAN_MANAGER) {
		EMPLOYEE_CAN_MANAGER = eMPLOYEE_CAN_MANAGER;
	}
	
}
