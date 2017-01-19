package com.k2.mobile.app.model.bean;

import java.io.Serializable;

public class ItemsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static String ITEMS = "Items";// Items

	// items
	public final static String NAME = "Name";// Name

	public final static String VALUE = "Value";// Value

	public final static String LABEL = "Label";// Label

	public final static String VISIBLE = "Visible";// Visible

	public final static String EDITABLE = "Editable";// Editable

	public final static String DETAILSURL = "DetailsUrl";// DetailsUrl

	public final static String FORMAT = "Format";// Format

	// values
	private String name;

	private String value;

	private String label;

	private boolean visible;

	private boolean editable;

	private String detailsUrl;

	private String format;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getDetailsUrl() {
		return detailsUrl;
	}

	public void setDetailsUrl(String detailsUrl) {
		this.detailsUrl = detailsUrl;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
