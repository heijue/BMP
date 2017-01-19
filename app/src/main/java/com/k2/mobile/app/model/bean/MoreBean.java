package com.k2.mobile.app.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * More's Object
 * 
 * @author ZYB2
 * 
 */
public class MoreBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// item
	public final static String MORE = "More";// More
	
	// value
	private List<ItemsBean> itemList;

	public List<ItemsBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemsBean> itemList) {
		this.itemList = itemList;
	}

}
