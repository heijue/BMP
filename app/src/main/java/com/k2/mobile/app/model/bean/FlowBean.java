package com.k2.mobile.app.model.bean;    

import com.k2.mobile.app.controller.core.BaseApp;

import java.io.Serializable;

/**
 * @Title FlowBean.java
 * @Package com.oppo.mo.model.bean
 * @Description 流程类实体
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-04-22 16:02:00
 * @version V1.0
 */
public class FlowBean implements Serializable{
	
    private String SN;				// 任务编号
    private String destination;		// 审批人帐号
    private String activityName;	// 环节编号
    private String assignedDate;	// 审批时间
    private String displayName;		// 显示名称
    private String folio;			// 流程主题
    private String procDispName;	// 流程名称
    private String ProcInstID;		// 实例编号
    private String startDate;		// 发起时间
    private int classType;			// 操作类型
    private String opentype;        // 打开类型
    private String linkUrl;		    // 详情地址
    private String processFullName; // 流程全名
    private String bsCode;			// 业务编码
    
    
	public String getSN() {
		return SN;
	}
	public void setSN(String sN) {
		SN = sN;
	}
	public int getClassType() {
		return classType;
	}
	public void setClassType(int classType) {
		this.classType = classType;
	}
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getAssignedDate() {
		return assignedDate;
	}
	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getProcDispName() {
		return procDispName;
	}
	public void setProcDispName(String procDispName) {
		this.procDispName = procDispName;
	}
	public String getProcInstID() {
		return ProcInstID;
	}
	public void setProcInstID(String procInstID) {
		ProcInstID = procInstID;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getOpentype() {
		return opentype;
	}
	public void setOpentype(String opentype) {
		this.opentype = opentype;
	}
	public String getLinkurl() {
//		int exist = linkUrl.indexOf("?");
//		String seg = "?u=";
//		if ( exist != -1 ){
//			seg = "&u=";
//		}
//		return linkUrl + seg + BaseApp.token;
		return linkUrl;
	}
	public void setLinkurl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getProcessFullName() {
		return processFullName;
	}
	public void setProcessFullName(String processFullName) {
		this.processFullName = processFullName;
	}
	public String getBsCode() {
		return bsCode;
	}
	public void setbsCode(String bsCode) {
		this.bsCode = bsCode;
	} 
}
 