package com.k2.mobile.app.model.bean;

import java.io.Serializable;
import java.util.List;

public class BizInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// item
	public final static String BIZINFO = "BizInfo";// BizInfo

	// value
	private List<GroupBean> groupBeanList;

	public List<GroupBean> getGroupBeanList() {
		return groupBeanList;
	}

	public void setGroupBeanList(List<GroupBean> groupBeanList) {
		this.groupBeanList = groupBeanList;
	}

}
