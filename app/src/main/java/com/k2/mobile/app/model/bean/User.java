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
public class User implements Serializable {

	/** @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么） */
	private static final long serialVersionUID = 1L;

	//"{\"UserId\":\"760c234b-9711-451d-96f2-07c5ecc1e4a3\",\"UserName\":\"ariesly\",\"Address\":null,\"Email\":\"\",\"FirstName\":\"Ariesly\",\"LastName\":\"Mao\",\"Phone\":\"131234567890\",\"Remark\":\"\",\"Sex\":0,\"Status\":0}"
	// 用户ID
	private String userId;
	// 用户名
	private String userName;
	// 用户地址
	private String address;
	// 用户头像
	private String fileUrl;
	// 用户email
	private String email;
	
	// 工号
	private String userAccount;
	// 姓名
	private String userChsName;
	// 手机
	private String mobilePhone;
	// 公司电话
	private String officeTel;
	// 部门
	private String orgName;
	private String realityOrgName;
	// 联系地址
	private String OfficeAddress;
	// 职位
	private String jobDesc;
	// 英文名
	private String userEnName;
	
	public String getUserEnName() {
		return userEnName;
	}

	public void setUserEnName(String userEnName) {
		this.userEnName = userEnName;
	}

	public String getRealityOrgName() {
		return realityOrgName;
	}

	public void setRealityOrgName(String realityOrgName) {
		this.realityOrgName = realityOrgName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOfficeAddress() {
		return OfficeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		OfficeAddress = officeAddress;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	private String firstName;
	
	private String lastName;
	// 电话
	private String phone;
	// 备注
	private String remark;
	// 性别
	@Override
	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", address='" + address + '\'' +
				", fileUrl='" + fileUrl + '\'' +
				", email='" + email + '\'' +
				", userAccount='" + userAccount + '\'' +
				", userChsName='" + userChsName + '\'' +
				", mobilePhone='" + mobilePhone + '\'' +
				", officeTel='" + officeTel + '\'' +
				", orgName='" + orgName + '\'' +
				", realityOrgName='" + realityOrgName + '\'' +
				", OfficeAddress='" + OfficeAddress + '\'' +
				", jobDesc='" + jobDesc + '\'' +
				", userEnName='" + userEnName + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", phone='" + phone + '\'' +
				", remark='" + remark + '\'' +
				", sex='" + sex + '\'' +
				", status=" + status +
				'}';
	}private String sex;
	// 状态
	private int status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
