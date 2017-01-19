package com.k2.mobile.app.model.bean;    
  
public class ReqHeaderBean {

	// ReqHeader":{"UserAccount":"80083625","TokenID":"TokenID","Mac":"110029-a889de9",
	// "SectetKey":"123","Sign":"456","DeviceType":"PC","ClientIP":"0.0.0.1","OperatingSystemType":"OA","OperatingSystemVer":"1.0"},
	private String userAccount;
	private String tokenID;
	private String mac;
	private String sectetKey;
	private String sign;
	private String deviceType;
	private String clientIP;
	private String operatingSystemType;
	private String operatingSystemVer;
	private String mask;
	private String locale;

	public String getLocale() { return locale; }
	public void setLocale(String locale) { this.locale = locale; }
	public String getMask() {
		return mask;
	}
	public void setMask(String mask) {
		this.mask = mask;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getTokenID() {
		return tokenID;
	}
	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSectetKey() {
		return sectetKey;
	}
	public void setSectetKey(String sectetKey) {
		this.sectetKey = sectetKey;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getOperatingSystemType() {
		return operatingSystemType;
	}
	public void setOperatingSystemType(String operatingSystemType) {
		this.operatingSystemType = operatingSystemType;
	}
	public String getOperatingSystemVer() {
		return operatingSystemVer;
	}
	public void setOperatingSystemVer(String operatingSystemVer) {
		this.operatingSystemVer = operatingSystemVer;
	}
	
}
 