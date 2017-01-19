package com.k2.mobile.app.model.bean;    

public class PushBean {
	
	// "SN":"35_22","Destination":"K2:DENALLIX\\Administrator","Folio":"个人工作日报20151123-300890","ProcessName":"WorkDaily",
	// "Title":"你收到一个新的任务","Body":"johnj-个人工作日报20151123-300890","ClientID":"cc8b89dc104463721b31ee4d30110f09"
	private String SN;
	private String destination;
	private String folio;
	private String processName;
	private String title;
	private String body;
	private String clientID;
	
	public String getSN() {
		return SN;
	}
	public void setSN(String sN) {
		SN = sN;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	
}
 