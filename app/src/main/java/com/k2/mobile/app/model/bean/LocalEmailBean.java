package com.k2.mobile.app.model.bean;    

import java.io.Serializable;

/**
 * @Title: EmailBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 我的邮件实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author liangzy	
 * @date 2015-04-08 15:52:30
 * @version V1.0
 */ 
public class LocalEmailBean implements Serializable{
	
	// 主键ID
	private int id;
	private String senderCode ;		// 发件人Code
	// 联系人
	private String code;			// 唯一编码
	private String displayName;		// 显示名称
	private String enName;		// 英文名
	private String displayOrgName;	// 部门
	public String getSenderCode() {
		return senderCode;
	}
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
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
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getDisplayOrgName() {
		return displayOrgName;
	}
	public void setDisplayOrgName(String displayOrgName) {
		this.displayOrgName = displayOrgName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	 
}
 