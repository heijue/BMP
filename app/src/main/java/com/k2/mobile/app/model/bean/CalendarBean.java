package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
import java.util.List;
  
public class CalendarBean implements Serializable{

	private String week; 		// 周几
	private String year;		// 年
	private String month; 		// 月
	private String day;			// 日
	private List<ResCalendarBean> resBean;
		
	public List<ResCalendarBean> getResBean() {
		return resBean;
	}

	public void setResBean(List<ResCalendarBean> resBean) {
		this.resBean = resBean;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	
	
}
 