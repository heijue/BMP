package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
import android.view.View;

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
public class ViewEmailBean implements Serializable{
	
	private EmailBean bean ;	// 邮件对象
	private View v;				// view
	
	public EmailBean getBean() {
		return bean;
	}
	public void setBean(EmailBean bean) {
		this.bean = bean;
	}
	public View getV() {
		return v;
	}
	public void setV(View v) {
		this.v = v;
	}
}
 