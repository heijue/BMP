package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * ProcBaseInfo's Object
 * 
 * @author ZYB2
 * 
 */
public class ProcBaseInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// item
	public final static String PROCBASEINFO = "ProcBaseInfo";// ProcBaseInfo

	// value
	private GroupBean groupBean;

	public GroupBean getGroupBean() {
		return groupBean;
	}

	public void setGroupBean(GroupBean groupBean) {
		this.groupBean = groupBean;
	}

}
