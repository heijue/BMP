package com.k2.mobile.app.model.bean;

import java.io.Serializable;
import java.util.List;

public class ActionsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// item
	public final static String ACTIONS = "Actions";// Actions

	// value
	private List<ItemsBean> itemList;

	public List<ItemsBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemsBean> itemList) {
		this.itemList = itemList;
	}

}
