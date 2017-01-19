package com.k2.mobile.app.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * TaskList's Object
 * 
 * @author ZYB2
 * 
 */
public class ExtendInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// base
	public final static String EXTENDINFO = "ExtendInfo";// ExtendInfo

	private List<ItemsBean> itemList;

	public List<ItemsBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemsBean> itemList) {
		this.itemList = itemList;
	}
}
