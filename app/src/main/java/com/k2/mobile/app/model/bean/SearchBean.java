/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * @Title: SearchBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 搜索实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun
 * @date 2015-03-31 20:13:00
 * @version V1.0
 */
public class SearchBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String deviceId;	// 设备ID
	private String searchType;	// 搜索范围
	private String userAccount;	// 当前用户
	private String index;		// 关键字
	
	private String Id;			// 员工ID,数据库记录的行ID
	private String employeesNo ;// 员工编号
	private String userChsName;		// 员工名称
	private String orgName;		// 部门
	private String userEnName;
	private String realityOrgName;
	
	private String term;		// 要搜索的文本
	private String module;		// 搜索的模块
	private String pageNo;		// 分页索引
	private String pageSize;	// 每页行数
	private String reader;		// 读者或收件人
	
//	private String 
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getReader() {
		return reader;
	}
	public void setReader(String reader) {
		this.reader = reader;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getEmployeesNo() {
		return employeesNo;
	}
	public void setEmployeesNo(String employeesNo) {
		this.employeesNo = employeesNo;
	}
	 
	public String getUserChsName() {
		return userChsName;
	}
	public void setUserChsName(String userChsName) {
		this.userChsName = userChsName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
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
	
	
}
