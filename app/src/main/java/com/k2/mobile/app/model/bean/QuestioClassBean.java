/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * @Title: QuestioClassBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: QuestioClassBean实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun
 * @date 2015-06-17 17:15:00
 * @version V1.0
 */
public class QuestioClassBean implements Serializable {

	/** @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么） */
	private static final long serialVersionUID = 1L;
	
	private String id; 
	private String questionClassName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestionClassName() {
		return questionClassName;
	}
	public void setQuestionClassName(String questionClassName) {
		this.questionClassName = questionClassName;
	}
	
}
