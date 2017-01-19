package com.k2.mobile.app.model.bean;    

import java.io.Serializable;

/**
 * @Title: EmailBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 我的邮件实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-04-08 15:52:30
 * @version V1.0
 */ 
public class EmailBean implements Serializable{
	
	private String id;				// 邮件ID
	private String convertToTask ;	// 是否可以转换为任务
	private String isImportant ;	// 是否非常重要
	private String mailCode ;		// 发件Code
	private String mailSize;		// 邮件大小
	private String mailSubject ;	// 邮件标题
	private String receiptTime ;	// 收件时间
	private String mailSendTime;	// 收邮件时间
	private String senderCode ;		// 发件人Code
	private String isRead;			// 是否阅读过 
	private String senderName;		// 发件人名称
	
	private String folderType;		// 邮箱文件夹类型 1.自定义文件夹 2.收件箱   
	private String mailCount;		// 未读邮件的数量
	private String folderCode;		// 邮箱编号
	private String folderName;		// 邮箱名称
	private String mailFolderType;	// 邮箱文件夹类型,1.有编号 默认收件箱 和 自定义收件箱 的根据邮箱编号来 2.发件箱  3.草稿箱 4.废纸篓
	// 联系人
	private String code;			// 唯一编码
	private String displayName;		// 显示名称
	private String enName;			// 英文名
	private String type;			// 地址薄类别
	private String mailContent;		// 内容
	private String recipient;		// 收件人名称
	private String createTime;		// 邮件被删除时间
	private String displayOrgName;	// 部门
	
	private String recipientJson;	// 所有收件人的JSON字符串
	private String magicField;		// 用户名和工号
	private String mailFrom;		// 废纸缕数据来
	
	private String cc;				// 抄送
	private String ccCode;			// 抄送联系人工号
	/** 邮件搜索返回结果字段 **/
	private String created_By;
	private String creation_Date;
	private String moduleid;
	private String title;
	
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getCcCode() {
		return ccCode;
	}
	public void setCcCode(String ccCode) {
		this.ccCode = ccCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	public String getCreated_By() {
		return created_By;
	}
	public void setCreated_By(String created_By) {
		this.created_By = created_By;
	}
	public String getCreation_Date() {
		return creation_Date;
	}
	public void setCreation_Date(String creation_Date) {
		this.creation_Date = creation_Date;
	}
	public String getModuleid() {
		return moduleid;
	}
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMagicField() {
		return magicField;
	}
	public void setMagicField(String magicField) {
		this.magicField = magicField;
	}
	public String getRecipientJson() {
		return recipientJson;
	}
	public void setRecipientJson(String recipientJson) {
		this.recipientJson = recipientJson;
	}
	public String getDisplayOrgName() {
		return displayOrgName;
	}
	public void setDisplayOrgName(String displayOrgName) {
		this.displayOrgName = displayOrgName;
	}
	public String getMailContent() {
		return mailContent;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getMailSendTime() {
		return mailSendTime;
	}
	public void setMailSendTime(String mailSendTime) {
		this.mailSendTime = mailSendTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMailFolderType() {
		return mailFolderType;
	}
	public void setMailFolderType(String mailFolderType) {
		this.mailFolderType = mailFolderType;
	}
	public String getFolderType() {
		return folderType;
	}
	public void setFolderType(String folderType) {
		this.folderType = folderType;
	}
	public String getMailCount() {
		return mailCount;
	}
	public void setMailCount(String mailCount) {
		this.mailCount = mailCount;
	}
	public String getFolderCode() {
		return folderCode;
	}
	public void setFolderCode(String folderCode) {
		this.folderCode = folderCode;
	}
	public String getFolderName() {
		return folderName;
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
	public String getMailCode() {
		return mailCode;
	}
	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
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
	public String getSenderCode() {
		return senderCode;
	}
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
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
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	
	
	 
}
 