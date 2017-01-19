package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
/**
 * 
 * @ClassName: DormRepairBean
 * @Description: 宿舍报修列表实体
 * @author linqijun
 * @date 2015-7-20 09:56:46
 *
 */
public class DormRepairBean implements Serializable{
	// id
	private String id;
	// 编号    		
    private String repairCode ;
    // 报修单号    		
    private String repairNumber ;
    // 报修单状态    		
    private String repairStatus ;
    // 报修物品
    private String repairItems;
    // 报修人工号    		
    private String repairUserCode ;
    // 报修人姓名    		
    private String repairUserName ;
    // 报修地点    		
    private String repairAddress ;
    // 报修时间    		
    private String repairSubmitTime ;
    // 维修员编号    		
    private String repairerAccount ;
    // 维修员名称    		
    private String repairerUserName ;
    // 1提交 0保存
    private int isSubmit ;
    // 页面传过来的图片id集合json串
    private String attachIds ;
    // 维修人员的名字
    private String repairerChsName ;
    // 报修开始时间
    private String startDate ;
    // 维修完成时间	
    private String finishTime;
    // 维修材料集合Json串
    private String repairUseMaterListJson ;
        
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRepairCode() {
		return repairCode;
	}
	public void setRepairCode(String repairCode) {
		this.repairCode = repairCode;
	}
	public String getRepairNumber() {
		return repairNumber;
	}
	public String getRepairItems() {
		return repairItems;
	}
	public void setRepairItems(String repairItems) {
		this.repairItems = repairItems;
	}
	public void setRepairNumber(String repairNumber) {
		this.repairNumber = repairNumber;
	}
	public String getRepairStatus() {
		return repairStatus;
	}
	public void setRepairStatus(String repairStatus) {
		this.repairStatus = repairStatus;
	}
	public String getRepairUserCode() {
		return repairUserCode;
	}
	public void setRepairUserCode(String repairUserCode) {
		this.repairUserCode = repairUserCode;
	}
	public String getRepairUserName() {
		return repairUserName;
	}
	public void setRepairUserName(String repairUserName) {
		this.repairUserName = repairUserName;
	}
	public String getRepairAddress() {
		return repairAddress;
	}
	public void setRepairAddress(String repairAddress) {
		this.repairAddress = repairAddress;
	}
	public String getRepairSubmitTime() {
		return repairSubmitTime;
	}
	public void setRepairSubmitTime(String repairSubmitTime) {
		this.repairSubmitTime = repairSubmitTime;
	}
	public String getRepairerAccount() {
		return repairerAccount;
	}
	public void setRepairerAccount(String repairerAccount) {
		this.repairerAccount = repairerAccount;
	}
	public String getRepairerUserName() {
		return repairerUserName;
	}
	public void setRepairerUserName(String repairerUserName) {
		this.repairerUserName = repairerUserName;
	}
	public int getIsSubmit() {
		return isSubmit;
	}
	public void setIsSubmit(int isSubmit) {
		this.isSubmit = isSubmit;
	}
	public String getAttachIds() {
		return attachIds;
	}
	public void setAttachIds(String attachIds) {
		this.attachIds = attachIds;
	}
	public String getRepairerChsName() {
		return repairerChsName;
	}
	public void setRepairerChsName(String repairerChsName) {
		this.repairerChsName = repairerChsName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getRepairUseMaterListJson() {
		return repairUseMaterListJson;
	}
	public void setRepairUseMaterListJson(String repairUseMaterListJson) {
		this.repairUseMaterListJson = repairUseMaterListJson;
	}
}
 