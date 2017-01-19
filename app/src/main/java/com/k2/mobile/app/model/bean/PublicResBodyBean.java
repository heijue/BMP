package com.k2.mobile.app.model.bean;    
  
public class PublicResBodyBean {

	private String resultInt;		// 状态码为1时返回的处理结果  整形
	private String resultString;	// 状态码为1时返回的处理结果  字符串  --目前只用这个，其它两个暂时不用
	private String resultObject;	// 状态码为1时返回的处理结果  对象
	
	public String getResultInt() {
		return resultInt;
	}
	public void setResultInt(String resultInt) {
		this.resultInt = resultInt;
	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	public String getResultObject() {
		return resultObject;
	}
	public void setResultObject(String resultObject) {
		this.resultObject = resultObject;
	}
	
}
 