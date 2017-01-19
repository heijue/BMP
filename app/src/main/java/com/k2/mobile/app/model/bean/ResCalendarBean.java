package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
  
public class ResCalendarBean implements Serializable{

	private String id;					// ID
	private String detail;				// 内容
	private String startTime;			// 开始时间
	private String endTime;				// 结束时间
	private String eventType;			// 事件类型
	private String title;				// 标题
	private String userAccount;			// 用户账号
	private String userCode;			// 用户代码
	private String workListItemCode;	// 任务代码
	private String remark;				// 备注
	
	private String deviceId;			// 设备编号
	private String userId;				// 当前用户工号
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getWorkListItemCode() {
		return workListItemCode;
	}
	public void setWorkListItemCode(String workListItemCode) {
		this.workListItemCode = workListItemCode;
	}
}
 