package com.k2.mobile.app.model.bean;

import java.io.Serializable;

public class TaskListItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String processName;

	private String folio;

	private String displayName;

	private String startDate;

	private String staffIcon;

	private String sN;

	private String formId;
	
	private String destination;
	
	private String activityId;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStaffIcon() {
		return staffIcon;
	}

	public void setStaffIcon(String staffIcon) {
		this.staffIcon = staffIcon;
	}

	public String getsN() {
		return sN;
	}

	public void setsN(String sN) {
		this.sN = sN;
	}

	public String getDestination() {
		return destination==null?"":destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	

}
