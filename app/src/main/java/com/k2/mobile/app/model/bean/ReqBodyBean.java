package com.k2.mobile.app.model.bean;    
  
public class ReqBodyBean {
    // "ReqBody":{"InvokeFunctionCode":"100001","InvokeParameter":"[\"80083625\",\"123\"]"}}
	private String invokeFunctionCode; // 方法名
	private String invokeParameter;    // 参数
	
//	private InvokeParameterBean  invokeParameter;
	
	public String getInvokeFunctionCode() {
		return invokeFunctionCode;
	}
	public void setInvokeFunctionCode(String invokeFunctionCode) {
		this.invokeFunctionCode = invokeFunctionCode;
	}
	public String getInvokeParameter() {
		return invokeParameter;
	}
	public void setInvokeParameter(String invokeParameter) {
		this.invokeParameter = invokeParameter;
	}
	
}
 