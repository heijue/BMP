package com.k2.mobile.app.model.bean;    
/**
 * @Title: TaskBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 我的任务实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-03-27 15:57:00
 * @version V1.0
 */ 
public class TaskBean {
	
	private String deviceId; 				// 设备编号
	private String taskId;					// 任务ID
	private String userAccount;				// 用户ID
	private String taskType;				// 任务类型
	
	private String userId;			// 用户ID
	private String taskTitle;		// 任务标题
	private String executeName;		// 指派人
	private String creatDueTime;	// 创建时间
	private String lastDueTime;		// 截止时间
	private String content;			// 任务描述
	
	/* 邮件转为任务 */
	private String mailCode;		// 邮件编号
	private String userName;		// 用户名
	private String title;			// 任务标题
	private String endTime;			// 结束时间
	
	public String getMailCode() {
		return mailCode;
	}
	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreatDueTime() {
		return creatDueTime;
	}
	public void setCreatDueTime(String creatDueTime) {
		this.creatDueTime = creatDueTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getExecuteName() {
		return executeName;
	}
	public void setExecuteName(String executeName) {
		this.executeName = executeName;
	}
	public String getLastDueTime() {
		return lastDueTime;
	}
	public void setLastDueTime(String lastDueTime) {
		this.lastDueTime = lastDueTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
}
 