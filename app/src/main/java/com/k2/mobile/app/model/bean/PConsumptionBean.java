package com.k2.mobile.app.model.bean;    
/**
 * @Title: PConsumptionBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 个人消费BEAN
 * @Company: K2
 * 
 * @author linqijun
 * @date 2015-03-30 10:45:49
 * @version V1.0
 */ 
public class PConsumptionBean {

	private String deviceId;	// 设备ID
	private String userId;		// 用户ID
	private String pageNo;		// 页数
	private String pageSize;	// 每页行数
	
	private String balance;			// 当前金额
	private String consume_money;	// 消费金额
	private String consume_time;	// 消费时间
	private String deposit_name;	// 消费类型
	private String machine_no;		// 卡机号
	private String node_name;		// 食堂类型
	
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getConsume_money() {
		return consume_money;
	}
	public void setConsume_money(String consume_money) {
		this.consume_money = consume_money;
	}
	public String getConsume_time() {
		return consume_time;
	}
	public void setConsume_time(String consume_time) {
		this.consume_time = consume_time;
	}
	public String getDeposit_name() {
		return deposit_name;
	}
	public void setDeposit_name(String deposit_name) {
		this.deposit_name = deposit_name;
	}
	public String getMachine_no() {
		return machine_no;
	}
	public void setMachine_no(String machine_no) {
		this.machine_no = machine_no;
	}
	public String getNode_name() {
		return node_name;
	}
	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
}
 