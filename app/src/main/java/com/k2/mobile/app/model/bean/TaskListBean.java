package com.k2.mobile.app.model.bean;

import java.io.Serializable;

public class TaskListBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private BaseInfoBean baseInfoBean;

	private ExtendInfoBean extendInfoBean;

	public BaseInfoBean getBaseInfoBean() {
		return baseInfoBean;
	}

	public void setBaseInfoBean(BaseInfoBean baseInfoBean) {
		this.baseInfoBean = baseInfoBean;
	}

	public ExtendInfoBean getExtendInfoBean() {
		return extendInfoBean;
	}

	public void setExtendInfoBean(ExtendInfoBean extendInfoBean) {
		this.extendInfoBean = extendInfoBean;
	}

}
