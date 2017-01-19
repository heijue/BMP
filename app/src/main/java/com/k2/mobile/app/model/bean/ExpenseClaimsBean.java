package com.k2.mobile.app.model.bean;    
/**
 * @Title: ExpenseClaimsBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 我的报销实体类
 * @Company: K2
 * 
 * @author linqijun
 * @date 2015-03-31 14:33:49
 * @version V1.0
 */  
public class ExpenseClaimsBean {

	private String userAccount; // 用户工号
	private String deviceId;	// 设备ID
	private String dateFrom;	// 开始时间
	private String dateTo;		// 结束时间
	
	private String header_id;	// ID
	private String report_submitted_date; // 提报时间
	private String total;		// 报销金额
	private String status;		// 状态
	
	public String getHeader_id() {
		return header_id;
	}
	public void setHeader_id(String header_id) {
		this.header_id = header_id;
	}
	public String getReport_submitted_date() {
		return report_submitted_date;
	}
	public void setReport_submitted_date(String report_submitted_date) {
		this.report_submitted_date = report_submitted_date;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
}
 