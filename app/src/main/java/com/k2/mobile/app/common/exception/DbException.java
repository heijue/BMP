/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.common.exception;


/**
 * @Title DbException.java
 * @Package com.oppo.mo.common.exception
 * @Description SQLite异常处理类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class DbException extends BaseException {
	private static final long serialVersionUID = 1L;
	
	public DbException() {}
	
	
	public DbException(String msg) {
		super(msg);
		this.printStackTrace();
	}
	
	public DbException(Throwable ex) {
		super(ex);
		this.printStackTrace();
	}
	
	public DbException(String msg,Throwable ex) {
		super(msg,ex);
		this.printStackTrace();
	}
	
}
