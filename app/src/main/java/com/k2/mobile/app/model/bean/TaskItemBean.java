package com.k2.mobile.app.model.bean;

import java.io.Serializable;

public class TaskItemBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// values
	private ProcBaseInfoBean procBaseInfo;

	private ProcLogInfoBean procLogInfo;

	private BizInfoBean bizInfo;

	private ActionsBean action;

	public ProcBaseInfoBean getProcBaseInfo() {
		return procBaseInfo;
	}

	public void setProcBaseInfo(ProcBaseInfoBean procBaseInfo) {
		this.procBaseInfo = procBaseInfo;
	}

	public ProcLogInfoBean getProcLogInfo() {
		return procLogInfo;
	}

	public void setProcLogInfo(ProcLogInfoBean procLogInfo) {
		this.procLogInfo = procLogInfo;
	}

	public BizInfoBean getBizInfo() {
		return bizInfo;
	}

	public void setBizInfo(BizInfoBean bizInfo) {
		this.bizInfo = bizInfo;
	}

	public ActionsBean getAction() {
		return action;
	}

	public void setAction(ActionsBean action) {
		this.action = action;
	}

}
