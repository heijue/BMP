package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @ClassName: DormRepairBean
 * @Description: 宿舍报修详情实体
 * @author linqijun
 * @date 2015-7-20 09:56:46
 *
 */
public class DormRepairDetailBean implements Serializable{
	 
	private String acceptTime;				// 上门预受理时间
	private String actualAmount;			// 实际自费费用
	private String assessmentTime;			// 评价时间
	private String assignedTime;			// 指派时间
	private List<DormRepairAttachmentBean> attachmentList;			// 附件集合
	private List<DormRepairUseMaterialBean> repairUseMaterList;		// 维修材料集合
	private String finishHashAttach;		// 维修结束是否有附件
	private String finishTime;				// 维修完成时间
	private String id;						// id
	private String isSubmit;				// 1提交 0保存
	private String planAmount;				// 预计自费费用
	private String planFinishTime;			// 承诺完成时间
	private String problemIsSolved;			// 问题是否解决
	private String repairAddress;			// 报修地点
	private String repairCode;				// 编号
	private String repairHashAttach;		// 报修是否有附件
	private String repairItems;				// 报修物品
	private String repairNumber;			// 报修单号
	private String repairStatus;			// 报修单状态
	private String repairSubmitTime;		// 报修时间
	private String repairUserCode;			// 报修人工号
	private String repairUserMobilePhone;	// 报修人联系电话
	private String repairUserName;			// 报修人姓名
	private String repairUserOrgName;		// 报修人部门
	private String repairreason;			// 报修原因
	private String serviceScore;			// 服务评分

	private String repairerMobilePhone;		// 维修员电话
    private String repairerUserName;		// 维修员名称
    private String acceptUpdateTime;		// 预受理操作时间
    private String isRepair;				// 维修状态 1是完成 0未完成
    private String repairRemark;			// 维修备注
    private String assessmentContent;		// 评价内容
    
	public String getAssessmentContent() {
		return assessmentContent;
	}
	public void setAssessmentContent(String assessmentContent) {
		this.assessmentContent = assessmentContent;
	}
	public String getRepairRemark() {
		return repairRemark;
	}
	public void setRepairRemark(String repairRemark) {
		this.repairRemark = repairRemark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRepairerMobilePhone() {
		return repairerMobilePhone;
	}
	public void setRepairerMobilePhone(String repairerMobilePhone) {
		this.repairerMobilePhone = repairerMobilePhone;
	}
	public String getRepairerUserName() {
		return repairerUserName;
	}
	public void setRepairerUserName(String repairerUserName) {
		this.repairerUserName = repairerUserName;
	}
	public String getAcceptUpdateTime() {
		return acceptUpdateTime;
	}
	public void setAcceptUpdateTime(String acceptUpdateTime) {
		this.acceptUpdateTime = acceptUpdateTime;
	}
	public String getIsRepair() {
		return isRepair;
	}
	public void setIsRepair(String isRepair) {
		this.isRepair = isRepair;
	}
	public String getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}
	public String getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}
	public String getAssessmentTime() {
		return assessmentTime;
	}
	public void setAssessmentTime(String assessmentTime) {
		this.assessmentTime = assessmentTime;
	}
	public String getAssignedTime() {
		return assignedTime;
	}
	public void setAssignedTime(String assignedTime) {
		this.assignedTime = assignedTime;
	}
	public List<DormRepairAttachmentBean> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<DormRepairAttachmentBean> attachmentList) {
		this.attachmentList = attachmentList;
	}
	public List<DormRepairUseMaterialBean> getRepairUseMaterList() {
		return repairUseMaterList;
	}
	public void setRepairUseMaterList(
			List<DormRepairUseMaterialBean> repairUseMaterList) {
		this.repairUseMaterList = repairUseMaterList;
	}
	public String getFinishHashAttach() {
		return finishHashAttach;
	}
	public void setFinishHashAttach(String finishHashAttach) {
		this.finishHashAttach = finishHashAttach;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getiD() {
		return id;
	}
	public void setiD(String iD) {
		this.id = iD;
	}
	public String getIsSubmit() {
		return isSubmit;
	}
	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}
	public String getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(String planAmount) {
		this.planAmount = planAmount;
	}
	public String getPlanFinishTime() {
		return planFinishTime;
	}
	public void setPlanFinishTime(String planFinishTime) {
		this.planFinishTime = planFinishTime;
	}
	public String getProblemIsSolved() {
		return problemIsSolved;
	}
	public void setProblemIsSolved(String problemIsSolved) {
		this.problemIsSolved = problemIsSolved;
	}
	public String getRepairAddress() {
		return repairAddress;
	}
	public void setRepairAddress(String repairAddress) {
		this.repairAddress = repairAddress;
	}
	public String getRepairCode() {
		return repairCode;
	}
	public void setRepairCode(String repairCode) {
		this.repairCode = repairCode;
	}
	public String getRepairHashAttach() {
		return repairHashAttach;
	}
	public void setRepairHashAttach(String repairHashAttach) {
		this.repairHashAttach = repairHashAttach;
	}
	public String getRepairItems() {
		return repairItems;
	}
	public void setRepairItems(String repairItems) {
		this.repairItems = repairItems;
	}
	public String getRepairNumber() {
		return repairNumber;
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
	public String getRepairSubmitTime() {
		return repairSubmitTime;
	}
	public void setRepairSubmitTime(String repairSubmitTime) {
		this.repairSubmitTime = repairSubmitTime;
	}
	public String getRepairUserCode() {
		return repairUserCode;
	}
	public void setRepairUserCode(String repairUserCode) {
		this.repairUserCode = repairUserCode;
	}
	public String getRepairUserMobilePhone() {
		return repairUserMobilePhone;
	}
	public void setRepairUserMobilePhone(String repairUserMobilePhone) {
		this.repairUserMobilePhone = repairUserMobilePhone;
	}
	public String getRepairUserName() {
		return repairUserName;
	}
	public void setRepairUserName(String repairUserName) {
		this.repairUserName = repairUserName;
	}
	public String getRepairUserOrgName() {
		return repairUserOrgName;
	}
	public void setRepairUserOrgName(String repairUserOrgName) {
		this.repairUserOrgName = repairUserOrgName;
	}
	public String getRepairreason() {
		return repairreason;
	}
	public void setRepairreason(String repairreason) {
		this.repairreason = repairreason;
	}
	public String getServiceScore() {
		return serviceScore;
	}
	public void setServiceScore(String serviceScore) {
		this.serviceScore = serviceScore;
	}
	
}
 