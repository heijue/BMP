/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.common.exception;


/**
 * @Title HttpException.java
 * @Package com.oppo.mo.common.exception
 * @Description HTTP异常处理类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class HttpException extends BaseException {
	private static final long serialVersionUID = 1L;

    private int exceptionCode;
    
	public HttpException(String msg) {
		super(msg);
		this.printStackTrace();
	}
	
	public HttpException(Throwable ex) {
		super(ex);
		this.printStackTrace();
	}
	
	public HttpException(String msg,Throwable ex) {
		super(msg,ex);
		this.printStackTrace();
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
	

    /**
     * @param exceptionCode The http response status code, 0 if the http request error and has no response.
     */
    public HttpException(int exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    /**
     * @param exceptionCode The http response status code, 0 if the http request error and has no response.
     * @param detailMessage
     */
    public HttpException(int exceptionCode, String detailMessage) {
        super(detailMessage);
        this.exceptionCode = exceptionCode;
    }

    /**
     * @param exceptionCode The http response status code, 0 if the http request error and has no response.
     * @param detailMessage
     * @param throwable
     */
    public HttpException(int exceptionCode, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.exceptionCode = exceptionCode;
    }

    /**
     * @param exceptionCode The http response status code, 0 if the http request error and has no response.
     * @param throwable
     */
    public HttpException(int exceptionCode, Throwable throwable) {
        super(throwable);
        this.exceptionCode = exceptionCode;
    }

    /**
     * @return The http response status code, 0 if the http request error and has no response.
     */
    public int getExceptionCode() {
        return exceptionCode;
    }
}
