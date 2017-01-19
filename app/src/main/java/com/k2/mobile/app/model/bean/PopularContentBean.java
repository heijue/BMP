package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
  
public class PopularContentBean implements Serializable{
 
	 private String id;   
	 private String popularCode;
	 private String popularTitle;
	 private String popularSummary; 
	 private String popularContents; 
	 private String typeCode;   
	 private String orderID;     
	 private String isEnable; 
	 private String createUser;  
	 private String createDate;  
	 private String modifyUser;  
	 private String modifyDate;
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPopularCode() {
		return popularCode;
	}
	public void setPopularCode(String popularCode) {
		this.popularCode = popularCode;
	}
	public String getPopularTitle() {
		return popularTitle;
	}
	public void setPopularTitle(String popularTitle) {
		this.popularTitle = popularTitle;
	}
	public String getPopularSummary() {
		return popularSummary;
	}
	public void setPopularSummary(String popularSummary) {
		this.popularSummary = popularSummary;
	}
	public String getPopularContents() {
		return popularContents;
	}
	public void setPopularContents(String popularContents) {
		this.popularContents = popularContents;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	 
}
 