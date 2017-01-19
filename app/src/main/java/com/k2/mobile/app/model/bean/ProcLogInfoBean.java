package com.k2.mobile.app.model.bean;

import java.io.Serializable;

public class ProcLogInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// item
	public final static String PROCLOGINFO = "ProcLogInfo";// ProcLogInfo

	// value
	private GroupBean groupBean;

	public GroupBean getGroupBean() {
		return groupBean;
	}

	public void setGroupBean(GroupBean groupBean) {
		this.groupBean = groupBean;
	}
}
