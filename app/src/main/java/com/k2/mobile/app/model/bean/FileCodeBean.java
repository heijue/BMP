package com.k2.mobile.app.model.bean;    
/**
 * @Title: ResTaskBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 我的任务实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-03-27 15:57:00
 * @version V1.0
 */ 
public class FileCodeBean {

	private String id;					// 任务ID
	private String workListItemTitle;	// 任务名称
	private String createDatetime ;		// 发起时间
	private String processingType;		// 执行
	private String beginDatetime;		// 开始时间
	private String workListItemStatus;	// 任务完成状态
	private String finishDatetime;		// 结束时间
	private String workCreateType;		// 任务创建类型
	
	private String lastDueTime;			// 完成时间
	private String actionerChsName;		// 指派人
	private String field1;				// 描述
	
	public String getLastDueTime() {
		return lastDueTime;
	}
	public void setLastDueTime(String lastDueTime) {
		this.lastDueTime = lastDueTime;
	}
	public String getActionerChsName() {
		return actionerChsName;
	}
	public void setActionerChsName(String actionerChsName) {
		this.actionerChsName = actionerChsName;
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getWorkCreateType() {
		return workCreateType;
	}
	public void setWorkCreateType(String workCreateType) {
		this.workCreateType = workCreateType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWorkListItemTitle() {
		return workListItemTitle;
	}
	public void setWorkListItemTitle(String workListItemTitle) {
		this.workListItemTitle = workListItemTitle;
	}
	public String getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}
	public String getProcessingType() {
		return processingType;
	}
	public void setProcessingType(String processingType) {
		this.processingType = processingType;
	}
	public String getBeginDatetime() {
		return beginDatetime;
	}
	public void setBeginDatetime(String beginDatetime) {
		this.beginDatetime = beginDatetime;
	}
	public String getWorkListItemStatus() {
		return workListItemStatus;
	}
	public void setWorkListItemStatus(String workListItemStatus) {
		this.workListItemStatus = workListItemStatus;
	}
	public String getFinishDatetime() {
		return finishDatetime;
	}
	public void setFinishDatetime(String finishDatetime) {
		this.finishDatetime = finishDatetime;
	}
}
 