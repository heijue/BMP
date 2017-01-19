/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * @Title: Employee.java
 * @Package com.oppo.mo.model.bean
 * @Description: Employee实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class Employee implements Serializable {

	/** @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么） */
	private static final long serialVersionUID = 1L;

	//"{\"EmployeeId\":\"94fa3da9-3f67-4ced-9745-cd3fbd457333\",\"LoginId\":\"760c234b-9711-451d-96f2-07c5ecc1e4a3\",
			//\"FullName\":\"ariesly Mao\",\"Gender\":0,\"IsSupervisor\":false,\"Language\":null,\"LastStatus\":1,\"ProjectId\":\"b83b89ad-c3f0-4bb1-b979-9db528380988\",\"ProjectName\":\"TCW\"}"
		
	private String EmployeeId;
	
	private String LoginId;
	
	private String FullName;
	
	private int Gender;
	
	private boolean IsSupervisor;
	
	private String Language;
	
	private int LastStatus;
	
	private String ProjectId;
	
	private String ProjectName;
	
	private String CurProjectId;
	
	private String CurProjectName;
	
	private boolean CanManager;

	public String getCurProjectId() {
		return CurProjectId;
	}

	public void setCurProjectId(String curProjectId) {
		CurProjectId = curProjectId;
	}

	public String getCurProjectName() {
		return CurProjectName;
	}

	public void setCurProjectName(String curProjectName) {
		CurProjectName = curProjectName;
	}

	public boolean isCanManager() {
		return CanManager;
	}

	public void setCanManager(boolean canManager) {
		CanManager = canManager;
	}

	public String getEmployeeId() {
		return EmployeeId;
	}

	public void setEmployeeId(String employeeId) {
		EmployeeId = employeeId;
	}

	public String getLoginId() {
		return LoginId;
	}

	public void setLoginId(String loginId) {
		LoginId = loginId;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public int getGender() {
		return Gender;
	}

	public void setGender(int gender) {
		Gender = gender;
	}

	public boolean isIsSupervisor() {
		return IsSupervisor;
	}

	public void setIsSupervisor(boolean isSupervisor) {
		IsSupervisor = isSupervisor;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public int getLastStatus() {
		return LastStatus;
	}

	public void setLastStatus(int lastStatus) {
		LastStatus = lastStatus;
	}

	public String getProjectId() {
		return ProjectId;
	}

	public void setProjectId(String projectId) {
		ProjectId = projectId;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	
}
