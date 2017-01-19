/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.db.table;

import java.io.Serializable;

/**
 * @Title Email.java
 * @Package com.oppo.mo.model.db
 * @Description Email实体类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-05-11 10:38:00
 * @version V1.0
 */
public class Email implements Serializable{

	private String id;				// 主键ID
	private String userAccount;		// 用户账号
	private String convertToTask ;	// 是否可以转换为任务
	private String mailCode;		// 发件Code
	private String isImportant ;	// 是否非常重要
	private String mailSize;		// 邮件大小
	private String mailSubject ;	// 邮件标题
	private String receiptTime ;	// 收件时间
	private String isRead;			// 是否阅读过 
	private String senderName;		// 发件人名称
	private String mailFolderType;	// 邮箱文件夹类型,1.有编号 默认收件箱 和 自定义收件箱 的根据邮箱编号来 2.发件箱  3.草稿箱 4.废纸篓
	private String folderCode;		// 文件夹code
	private String mailFrom;		// 废纸缕数据来
	
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	public String getFolderCode() {
		return folderCode;
	}
	public void setFolderCode(String folderCode) {
		this.folderCode = folderCode;
	}
	public String getMailCode() {
		return mailCode;
	}
	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getConvertToTask() {
		return convertToTask;
	}
	public void setConvertToTask(String convertToTask) {
		this.convertToTask = convertToTask;
	}
	public String getIsImportant() {
		return isImportant;
	}
	public void setIsImportant(String isImportant) {
		this.isImportant = isImportant;
	}
	public String getMailSize() {
		return mailSize;
	}
	public void setMailSize(String mailSize) {
		this.mailSize = mailSize;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getReceiptTime() {
		return receiptTime;
	}
	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getMailFolderType() {
		return mailFolderType;
	}
	public void setMailFolderType(String mailFolderType) {
		this.mailFolderType = mailFolderType;
	}
}