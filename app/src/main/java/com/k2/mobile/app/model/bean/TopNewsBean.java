package com.k2.mobile.app.model.bean;    
  
public class TopNewsBean {
	
	private String formInstanceCode; 	// 流程实例编号
	private String newsImg;				// 图片地址
	private String newsSummary;			// 新闻摘要
	private String newsTitle;			// 新闻标题
	private String procSetID;			// 流程实例类型ID
	private String procVerID;			// 流程实例版本
	
	public String getProcSetID() {
		return procSetID;
	}
	public void setProcSetID(String procSetID) {
		this.procSetID = procSetID;
	}
	public String getProcVerID() {
		return procVerID;
	}
	public void setProcVerID(String procVerID) {
		this.procVerID = procVerID;
	}
	public String getFormInstanceCode() {
		return formInstanceCode;
	}
	public void setFormInstanceCode(String formInstanceCode) {
		this.formInstanceCode = formInstanceCode;
	}
	public String getNewsImg() {
		return newsImg;
	}
	public void setNewsImg(String newsImg) {
		this.newsImg = newsImg;
	}
	public String getNewsSummary() {
		return newsSummary;
	}
	public void setNewsSummary(String newsSummary) {
		this.newsSummary = newsSummary;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
}
 