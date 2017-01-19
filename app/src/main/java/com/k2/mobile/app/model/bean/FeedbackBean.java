package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
import java.util.List;
  
public class FeedbackBean implements Serializable{

	private String questionFeedbackCode; 		// 问题反馈代码
	private String createDatetime;				// 反馈时间
	private String questionAbstract;			// 问题摘要
	private String questionCategoryName;		// 类别
	private String status;						// 状态
	private String id;							// 类别ID

	private String creatorPhoneNum;				// 联系人电话号码
	private String questionDescription;			// 问题描述 
	private List<String> attachmentList;		// 图片集合
	private String handlerName;					// 解决人
	private String handleEstimateTime;			// 预处理时间
	private String finishEstimateTime;			// 承诺解决时间
	private String handleFactTime;				// 实际解决时间
	private String remark;						// 备注
	private String isHandled;					// 是否已解决(0:否,1:是)
	private String appraiseScore;				// 满意度评价打分
	private String appraiseContent;				// 评价内容
	private String appraiseDatetime;			// 评价时间
	
	private String flag;						// 成功返回值
	private List<FileBean> data;				// 文件上传成功返回集合
	
	public String getCreatorPhoneNum() {
		return creatorPhoneNum;
	}
	public void setCreatorPhoneNum(String creatorPhoneNum) {
		this.creatorPhoneNum = creatorPhoneNum;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	 
	public List<FileBean> getData() {
		return data;
	}
	public void setData(List<FileBean> data) {
		this.data = data;
	}
	public String getQuestionDescription() {
		return questionDescription;
	}
	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}
	public List<String> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<String> attachmentList) {
		this.attachmentList = attachmentList;
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
	public String getHandleFactTime() {
		return handleFactTime;
	}
	public void setHandleFactTime(String handleFactTime) {
		this.handleFactTime = handleFactTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIsHandled() {
		return isHandled;
	}
	public void setIsHandled(String isHandled) {
		this.isHandled = isHandled;
	}
	public String getAppraiseScore() {
		return appraiseScore;
	}
	public void setAppraiseScore(String appraiseScore) {
		this.appraiseScore = appraiseScore;
	}
	public String getAppraiseContent() {
		return appraiseContent;
	}
	public void setAppraiseContent(String appraiseContent) {
		this.appraiseContent = appraiseContent;
	}
	public String getAppraiseDatetime() {
		return appraiseDatetime;
	}
	public void setAppraiseDatetime(String appraiseDatetime) {
		this.appraiseDatetime = appraiseDatetime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestionFeedbackCode() {
		return questionFeedbackCode;
	}
	public void setQuestionFeedbackCode(String questionFeedbackCode) {
		this.questionFeedbackCode = questionFeedbackCode;
	}
	public String getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
 