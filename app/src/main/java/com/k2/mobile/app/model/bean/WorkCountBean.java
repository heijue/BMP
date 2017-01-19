package com.k2.mobile.app.model.bean;    

import java.io.Serializable;

/**
 * @Title: WorkCountBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 工作待办实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author congmin.jin	
 * @date 2015-07-07 15:52:30
 * @version V1.0
 */ 
public class WorkCountBean implements Serializable{
	
	private String count;//待办数量
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	
	 
}
 