package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
  
public class FeedbackListBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String appraiseDatetime;			// 评价时间
	private String appraiseScore;				// 满意度评价分数
	private String createDatetime;				// 反馈时间
	private String creatorDepartment;			// 创建人部门
	private String creatorName;					// 创建人姓名
	private String creatorPhoneNum;				// 创建人联系电话
	private String handleFactTime;				// 实际处理时间
	private String handleResult;				// 处理结果0 未解决，1解决
	private String questionAbstract;			// 问题摘要
	private String questionCategoryName;		// 反馈类别名称
	private String questionDescription;			// 问题描述
	private String status;						// 状态
	private String remark;						// 备注
	
	private String attachmentList;				// 图片集
	private String questionCategorylist;		// 反馈类别集
	private String questionClasslist;			// 问题类别集
	
	private String questionFeedbackNumber;		// 问题编号
	private String creatorCode;					// 反馈人工号
	private String reviewerName;				// 指派人
	private String reviewDatetime;				// 指派时间
	private String handlerName;					// 处理人
	private String handleEstimateTime;			// 问题预处理时间
	private String finishEstimateTime;			// 承诺完成时间
	private String appraiseContent;				// 评价内容
	private String questionCategoryID;			// 问题大类ID
	private String questionClassID;				// 问题小类ID
	private String questionClassName;			// 问题类型名称
	
	public String getQuestionFeedbackNumber() {
		return questionFeedbackNumber;
	}

	public void setQuestionFeedbackNumber(String questionFeedbackNumber) {
		this.questionFeedbackNumber = questionFeedbackNumber;
	}

	public String getCreatorCode() {
		return creatorCode;
	}

	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getReviewDatetime() {
		return reviewDatetime;
	}

	public void setReviewDatetime(String reviewDatetime) {
		this.reviewDatetime = reviewDatetime;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getHandleEstimateTime() {
		return handleEstimateTime;
	}

	public void setHandleEstimateTime(String handleEstimateTime) {
		this.handleEstimateTime = handleEstimateTime;
	}

	public String getFinishEstimateTime() {
		return finishEstimateTime;
	}

	public void setFinishEstimateTime(String finishEstimateTime) {
		this.finishEstimateTime = finishEstimateTime;
	}

	public String getAppraiseContent() {
		return appraiseContent;
	}

	public void setAppraiseContent(String appraiseContent) {
		this.appraiseContent = appraiseContent;
	}

	public String getQuestionCategoryID() {
		return questionCategoryID;
	}

	public void setQuestionCategoryID(String questionCategoryID) {
		this.questionCategoryID = questionCategoryID;
	}

	public String getQuestionClassID() {
		return questionClassID;
	}

	public void setQuestionClassID(String questionClassID) {
		this.questionClassID = questionClassID;
	}

	public String getQuestionClassName() {
		return questionClassName;
	}

	public void setQuestionClassName(String questionClassName) {
		this.questionClassName = questionClassName;
	}

	public String getAttachmentList() {
		return attachmentList;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setAttachmentList(String attachmentList) {
		this.attachmentList = attachmentList;
	}
	public String getQuestionCategorylist() {
		return questionCategorylist;
	}
	public void setQuestionCategorylist(String questionCategorylist) {
		this.questionCategorylist = questionCategorylist;
	}
	public String getQuestionClasslist() {
		return questionClasslist;
	}
	public void setQuestionClasslist(String questionClasslist) {
		this.questionClasslist = questionClasslist;
	}
	public String getAppraiseDatetime() {
		return appraiseDatetime;
	}
	public void setAppraiseDatetime(String appraiseDatetime) {
		this.appraiseDatetime = appraiseDatetime;
	}
	public String getAppraiseScore() {
		return appraiseScore;
	}
	public void setAppraiseScore(String appraiseScore) {
		this.appraiseScore = appraiseScore;
	}
	public String getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}
	public String getCreatorDepartment() {
		return creatorDepartment;
	}
	public void setCreatorDepartment(String creatorDepartment) {
		this.creatorDepartment = creatorDepartment;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCreatorPhoneNum() {
		return creatorPhoneNum;
	}
	public void setCreatorPhoneNum(String creatorPhoneNum) {
		this.creatorPhoneNum = creatorPhoneNum;
	}
	public String getHandleFactTime() {
		return handleFactTime;
	}
	public void setHandleFactTime(String handleFactTime) {
		this.handleFactTime = handleFactTime;
	}
	public String getHandleResult() {
		return handleResult;
	}
	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}
	public String getQuestionAbstract() {
		return questionAbstract;
	}
	public void setQuestionAbstract(String questionAbstract) {
		this.questionAbstract = questionAbstract;
	}
	public String getQuestionCategoryName() {
		return questionCategoryName;
	}
	public void setQuestionCategoryName(String questionCategoryName) {
		this.questionCategoryName = questionCategoryName;
	}
	public String getQuestionDescription() {
		return questionDescription;
	}
	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
 