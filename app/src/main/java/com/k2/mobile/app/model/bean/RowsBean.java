package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * Row's Object
 * 
 * @author ZYB2
 * 
 */
public class RowsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// items
	public final static String ROWS = "Rows";// Rows

	// values
	private DataBean dataBean;

	private MoreBean moreBean;

	public DataBean getDataBean() {
		return dataBean;
	}

	public void setDataBean(DataBean dataBean) {
		this.dataBean = dataBean;
	}

	public MoreBean getMoreBean() {
		return moreBean;
	}

	public void setMoreBean(MoreBean moreBean) {
		this.moreBean = moreBean;
	}

}
