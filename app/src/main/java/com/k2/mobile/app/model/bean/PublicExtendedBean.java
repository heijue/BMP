package com.k2.mobile.app.model.bean;    

import java.util.List;
  
public class PublicExtendedBean {
	
	// 设备ID
	private String deviceId;
	// 工号
	private String userAccount;
	// 邮箱编号
	private String mailFolderCode;
	// 分页
	private String pageIndex;
	// 每页显示条数
	private String pageSize;
	// 年月份
	private String month;
	// ID
	private String cid;
	// 用户名
	private String user;
	// 用户密码
	private String pwd;
	// 成功标识
	private String success;
	// 用户ID
	private String userId;
	// 修改类型
	private String updateType;
	// 修改的内容
	private String content;
	// 任务类型
	private String taskType;
	// 关键字
	private String searchText; 
	// 验证码
	private String checkCode;
	
	/******************** 邮件 **********************************/
	// 发件人
	private String senderName;
	// 邮件编号
	private String mailCode;
	// 邮件编号
	private String folderCode;
	// 收件人集合
	private List<EmailBean> recipent;
	// 抄送人集合
	private List<EmailBean> cc;
	// 密送人集合
	private List<EmailBean> bcc;
	// 邮件标题
	private String subject;
	// 是否重要
	private String isImportantRe;
	// 是否加密
	private String isEncryptionRe;
	// 是否需要回执
	private String needReceiptRe;
	// 邮箱详情类型
	private String mailType;
	// 邮件状态 草稿1 发送2
	private String mailStatus;
	// 邮件编号集合
	private List<String> mailCodes;
	// 邮件标记已读
	private String isRead;
	// 要搜索的文本
	private String term;
	// 搜索的模块
	private String module;
	// 分页页码
	private String pageNo;
	// 搜索的类型
	private String searchType;
	// 用户的usercode(读者或收件人)
	private String reader;
	private String mailFrom;
	
	/******************** 邮件end **********************************/
	// 新闻代码
	private String formInstanceCode;
	// 菜单代码
	private String menuCode;
	private String menuType;
	// 最后一条数据的ID
	private String lastId;	
	
	/********************* 程序自动升级 ****************************/
	private String appName;				// 程序更新名称
	private String appPlatform;			// 平台类型
	private String sign;				// 签名
	
	/*********************** 问题反馈 *************************/
	private String flag;			// 类型(0:我反馈的问题列表，1:待处理的问题列表)
	private String code;			// 问题代码
	private String userName;		// 反馈人姓名
	private String userDepart;		// 反馈人部门
	private String userPhone;		// 反馈人电话
	private String categoryId;		// 问题类别ID
	private String categoryName;	// 问题类别名称
	private String summary;			// 问题摘要
	private String desc;			// 问题描述
	private List<String> attachIds;	// 附件id集合
	private String isSubmit;		// 是否提交（0:保存，1:提交）
	private String sourceCode; 		// 原问题反馈代码
	private String opFlag;			// 类型（0:关闭,1:删除）
	private String isHandled;		// 是否已解决(0:未解决,1:是)
	private String appraise;		// 评价内容
	private String score;			// 满意度评价分数
	private String classId;			// 问题类型ID
	private String className;		// 问题类型名称
	private String ptime;			// 承诺完成时间
	private String handleType;		// 处理类型（0:完成处理,1:无法解决,2：直接处理）
	private String remark;			// 备注
	
	private String userCode; //用户工号
	
	public String getLastId() {
		return lastId;
	}
	public void setLastId(String lastId) {
		this.lastId = lastId;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	public String getHandleType() {
		return handleType;
	}
	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPtime() {
		return ptime;
	}
	public void setPtime(String ptime) {
		this.ptime = ptime;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getAppraise() {
		return appraise;
	}
	public void setAppraise(String appraise) {
		this.appraise = appraise;
	}
	public String getIsHandled() {
		return isHandled;
	}
	public void setIsHandled(String isHandled) {
		this.isHandled = isHandled;
	}
	public String getOpFlag() {
		return opFlag;
	}
	public void setOpFlag(String opFlag) {
		this.opFlag = opFlag;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserDepart() {
		return userDepart;
	}
	public void setUserDepart(String userDepart) {
		this.userDepart = userDepart;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<String> getAttachIds() {
		return attachIds;
	}
	public void setAttachIds(List<String> attachIds) {
		this.attachIds = attachIds;
	}
	public String getIsSubmit() {
		return isSubmit;
	}
	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getReader() {
		return reader;
	}
	public void setReader(String reader) {
		this.reader = reader;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppPlatform() {
		return appPlatform;
	}
	public void setAppPlatform(String appPlatform) {
		this.appPlatform = appPlatform;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	// 流程实例ID
	private String workListItemCode;
	// 审批意见
	private String approveComment;
	// 选中节点
	private String activtyId;
	// 流程实例ID
	private String procSetID;
	
	public String getProcSetID() {
		return procSetID;
	}
	public void setProcSetID(String procSetID) {
		this.procSetID = procSetID;
	}
	public String getApproveComment() {
		return approveComment;
	}
	public void setApproveComment(String approveComment) {
		this.approveComment = approveComment;
	}
	public String getActivtyId() {
		return activtyId;
	}
	public void setActivtyId(String activtyId) {
		this.activtyId = activtyId;
	}
	public String getWorkListItemCode() {
		return workListItemCode;
	}
	public void setWorkListItemCode(String workListItemCode) {
		this.workListItemCode = workListItemCode;
	}
	public String getFormInstanceCode() {
		return formInstanceCode;
	}
	public void setFormInstanceCode(String formInstanceCode) {
		this.formInstanceCode = formInstanceCode;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	
	public List<String> getMailCodes() {
		return mailCodes;
	}
	public void setMailCodes(List<String> mailCodes) {
		this.mailCodes = mailCodes;
	}
	
	public String getMailType() {
		return mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	
	public String getFolderCode() {
		return folderCode;
	}
	public void setFolderCode(String folderCode) {
		this.folderCode = folderCode;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getMailCode() {
		return mailCode;
	}
	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}
	public List<EmailBean> getRecipent() {
		return recipent;
	}
	public void setRecipent(List<EmailBean> recipent) {
		this.recipent = recipent;
	}
	public List<EmailBean> getCc() {
		return cc;
	}
	public void setCc(List<EmailBean> cc) {
		this.cc = cc;
	}
	public List<EmailBean> getBcc() {
		return bcc;
	}
	public void setBcc(List<EmailBean> bcc) {
		this.bcc = bcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	 
	public String getIsImportantRe() {
		return isImportantRe;
	}
	public void setIsImportantRe(String isImportantRe) {
		this.isImportantRe = isImportantRe;
	}
	public String getIsEncryptionRe() {
		return isEncryptionRe;
	}
	public void setIsEncryptionRe(String isEncryptionRe) {
		this.isEncryptionRe = isEncryptionRe;
	}
	public String getNeedReceiptRe() {
		return needReceiptRe;
	}
	public void setNeedReceiptRe(String needReceiptRe) {
		this.needReceiptRe = needReceiptRe;
	}
	public String getMailStatus() {
		return mailStatus;
	}
	public void setMailStatus(String mailStatus) {
		this.mailStatus = mailStatus;
	}
	
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getUpdateType() {
		return updateType;
	}
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMailFolderType() {
		return mailFolderType;
	}
	public void setMailFolderType(String mailFolderType) {
		this.mailFolderType = mailFolderType;
	}
	// 邮箱文件夹类型 
	private String mailFolderType;
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getMailFolderCode() {
		return mailFolderCode;
	}
	public void setMailFolderCode(String mailFolderCode) {
		this.mailFolderCode = mailFolderCode;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	
	
}
 