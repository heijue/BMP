package com.k2.mobile.app.model.bean;

import java.io.Serializable;
import java.util.List;


/**
 * Group's Object
 * 
 * @author ZYB2
 * 
 */
public class GroupBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// items
	public final static String GROUP = "Group";// Group

	public final static String GROUPS = "Groups";// Groups

	public final static String TYPE = "Type";// Type

	public final static String LABEL = "Label";// Label

	public final static String COLLAPSED = "Collapsed";// Collapsed

	// values
	private String type;

	private String label;

	private boolean collapsed;

	private List<ItemsBean> itemList;

	private HeaderBean headerBean;

	private List<RowsBean> rowsBeanList;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	public List<ItemsBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemsBean> itemList) {
		this.itemList = itemList;
	}

	public HeaderBean getHeaderBean() {
		return headerBean;
	}

	public void setHeaderBean(HeaderBean headerBean) {
		this.headerBean = headerBean;
	}

	public List<RowsBean> getRowsBeanList() {
		return rowsBeanList;
	}

	public void setRowsBeanList(List<RowsBean> rowsBeanList) {
		this.rowsBeanList = rowsBeanList;
	}

}
