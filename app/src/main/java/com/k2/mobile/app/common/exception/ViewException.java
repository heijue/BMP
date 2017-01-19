/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.common.exception;


/**
 * @Title ViewException.java
 * @Package com.oppo.mo.common.exception
 * @Description UI异常处理类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class ViewException extends BaseException {
	
	private static final long serialVersionUID = 1L;
	
	public ViewException(String msg) {
		super(msg);
		this.printStackTrace();
	}
	
	public ViewException(Throwable ex) {
		super(ex);
		this.printStackTrace();
	}
	
	public ViewException(String msg,Throwable ex) {
		super(msg,ex);
		this.printStackTrace();
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
}
