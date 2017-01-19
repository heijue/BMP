package com.k2.mobile.app.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Data's Object
 * 
 * @author ZYB2
 * 
 */
public class DataBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// item
	public final static String DATA = "Data";// Data

	// value
	private List<ItemsBean> itemList;

	public List<ItemsBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemsBean> itemList) {
		this.itemList = itemList;
	}

}
