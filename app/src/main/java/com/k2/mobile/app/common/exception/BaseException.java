/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.common.exception;

import com.k2.mobile.app.utils.LogUtil;

/**
 * @Title BaseException.java
 * @Package com.oppo.mo.common.exception
 * @Description 异常处理基类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class BaseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	/** 自定义出错信息 */
	private String strMsg = null;
	
	/** 异常类 */
	private Throwable e = null;
	
	public BaseException() {
		super();
	}
	
	public BaseException(String msg) {
		super(msg);
		strMsg = msg;
		
		this.printStackTrace();
	}
	
	public BaseException(Throwable ex) {
		super(ex);
		e = ex;
		
		this.printStackTrace();
	}
	
	public BaseException(String msg,Throwable ex) {
		super(msg,ex);
		strMsg = msg;
		e = ex;
		
		this.printStackTrace();
	}
	
	/**
	 * 打印错误信息
	 */
	@Override
	public void printStackTrace() {
		if(strMsg != null) {
			LogUtil.e(e.getMessage(), e);
			LogUtil.saveCrashInfo2File(strMsg, e);
		} else {
			LogUtil.saveCrashInfo2File(e);
		}
		// System.err.println(strMsg);
		
		//super.printStackTrace();
	}

}
