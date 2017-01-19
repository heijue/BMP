package com.k2.mobile.app.model.bean;    
  
public class PublicResHeaderBean {
	private String stateCode;				// 返回的状态码 1是正常，其它都是不正常
	private String returnMsg;				// 根据状态码返回的消息内容
	private String sectetKey;				// DES的密钥
	private String isException;				// 服务器是否有异常，1是 0否
	private String exceptionStackTrace;		// 异常的堆栈信息
	private String exceptionMsg;			// 异常的消息内容
	
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getSectetKey() {
		return sectetKey;
	}
	public void setSectetKey(String sectetKey) {
		this.sectetKey = sectetKey;
	}
	public String getIsException() {
		return isException;
	}
	public void setIsException(String isException) {
		this.isException = isException;
	}
	public String getExceptionStackTrace() {
		return exceptionStackTrace;
	}
	public void setExceptionStackTrace(String exceptionStackTrace) {
		this.exceptionStackTrace = exceptionStackTrace;
	}
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	
	
}
 