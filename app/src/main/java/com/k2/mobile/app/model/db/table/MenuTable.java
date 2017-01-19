/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.db.table;

import java.io.Serializable;

/**
 * @Title Menu.java
 * @Package com.oppo.mo.model.db
 * @Description 常用菜单实体类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-05-11 10:38:00
 * @version V1.0
 */
public class MenuTable implements Serializable{

	private int id;			// 主键ID
	private String menuCode ;	// 菜单编号
	private String menuNmae ;	// 菜单名称
	private String userAccount;	// 用户账号
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuNmae() {
		return menuNmae;
	}
	public void setMenuNmae(String menuNmae) {
		this.menuNmae = menuNmae;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
}