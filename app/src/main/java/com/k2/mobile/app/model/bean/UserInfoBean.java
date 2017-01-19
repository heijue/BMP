/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * @Title: User.java
 * @Package com.oppo.mo.model.bean
 * @Description: User实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class UserInfoBean implements Serializable {

	/** @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么） */
	private static final long serialVersionUID = 1L;

	// email
	private String email;
	// 头像路径
	private String fileUrl;
	// 用户地址
	private String address;
	// 职位
	private String jobDesc;
	// 用户手机号码
	private String mobilePhone;
	// 办公室电话
	private String officeTel;
	// 部门
	private String realityOrgName;
	// 性别
	private String sex;
	// 工号
	private String userAccount;
	// 中文名
	private String userChsName;
	// 英文名
	private String userEnName;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getOfficeTel() {
		return officeTel;
	}
	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}
	public String getRealityOrgName() {
		return realityOrgName;
	}
	public void setRealityOrgName(String realityOrgName) {
		this.realityOrgName = realityOrgName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getUserChsName() {
		return userChsName;
	}
	public void setUserChsName(String userChsName) {
		this.userChsName = userChsName;
	}
	public String getUserEnName() {
		return userEnName;
	}
	public void setUserEnName(String userEnName) {
		this.userEnName = userEnName;
	}
}
