package com.k2.mobile.app.model.bean;    

import java.io.Serializable;

/**
 * @Title: HomeMenuBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 菜单实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-04-25 11:09:30
 * @version V1.0
 */ 
public class HomeMenuBean implements Serializable{
	
	private String menuCode ;	// 菜单编号
	private int menuIcons ;		// 菜单图片
	private String menuNmae ;	// 菜单名称
//	private String 	isImportant;// 重要，不允许删除
	private String parentCode;	// 父菜单编号
	private int stauts;			// 当前状态
	private String menuType;	// 是否可删除
	private int isChoose;		// 是否选中
	private String iD;
	private String menuChsName;
	private String menuLevel;
	private String icoURL;
	private String orderID;
	private String belongType;
	private String isBlank;
	private String noShortcut;
	private String remark;
	private String menuUrl;
	
	public String getiD() {
		return iD;
	}
	public void setiD(String iD) {
		this.iD = iD;
	}
	public String getMenuChsName() {
		return menuChsName;
	}
	public void setMenuChsName(String menuChsName) {
		this.menuChsName = menuChsName;
	}
	public String getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(String menuLevel) {
		this.menuLevel = menuLevel;
	}
	public String getIcoURL() {
		return icoURL;
	}
	public void setIcoURL(String icoURL) {
		this.icoURL = icoURL;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getBelongType() {
		return belongType;
	}
	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}
	public String getIsBlank() {
		return isBlank;
	}
	public void setIsBlank(String isBlank) {
		this.isBlank = isBlank;
	}
	public String getNoShortcut() {
		return noShortcut;
	}
	public void setNoShortcut(String noShortcut) {
		this.noShortcut = noShortcut;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public int getIsChoose() {
		return isChoose;
	}
	public void setIsChoose(int isChoose) {
		this.isChoose = isChoose;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public int getStauts() {
		return stauts;
	}
	public void setStauts(int stauts) {
		this.stauts = stauts;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public int getMenuIcons() {
		return menuIcons;
	}
	public void setMenuIcons(int menuIcons) {
		this.menuIcons = menuIcons;
	}
	public String getMenuNmae() {
		return menuNmae;
	}
	public void setMenuNmae(String menuNmae) {
		this.menuNmae = menuNmae;
	}
//	public String getIsImportant() {
//		return isImportant;
//	}
//	public void setIsImportant(String isImportant) {
//		this.isImportant = isImportant;
//	}
	
}
 