/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.common.exception;


/**
 * @Title DataException.java
 * @Package com.oppo.mo.common.exception
 * @Description 数据异常处理类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class DataException extends BaseException {
	
	private static final long serialVersionUID = 1L;
	
	public DataException(String msg) {
		super(msg);
		this.printStackTrace();
	}
	
	public DataException(Throwable ex) {
		super(ex);
		this.printStackTrace();
	}
	
	public DataException(String msg,Throwable ex) {
		super(msg,ex);
		this.printStackTrace();
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
}
