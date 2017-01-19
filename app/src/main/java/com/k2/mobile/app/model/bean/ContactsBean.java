package com.k2.mobile.app.model.bean;    

import java.io.Serializable;

/**
 * @Title: Contacts.java
 * @Package com.oppo.mo.model.bean
 * @Description: Contacts实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun
 * @date 2015-04-3 16:53:00
 * @version V1.0
 */  
public class ContactsBean implements Serializable{
	// 主键ID
	private int id;
	// 当前用户工号
	private String userAccount;
	// 更新时间
	private String updateTime;
	// 工号
    private String userCode;
    // 中文名字
    private String userChsName;
    // 英文名
    private String userEnName;
    // 部门名称
    private String orgName;
    // 拼音		
    private String alphabetical;
    
    // 邮箱
    private String email;
    // 职位 
    private String jobDesc;
    // 移动电话
    private String mobilePhone;
    // 办公电话
    private String officeTel;
    // 性别
    private String sex;
    // 头像路径
    private String fileUrl;
    // 部门名称
    private String realityOrgName;
    
    //[{\"ID\":3,\"Alphabetical\":\"HPX\",\"UserCode\":\"Collin Huang\",\"UserAccount\":\"Collin Huang\",
    //\"UserChsName\":\"黄平显\",\"UserEnName\":null,\"UserFullName\":\"黄平显\",\"Email\":null,
    // \"MobilePhone\":null,\"OfficeTel\":null,\"HomeTel\":null,\"HomeAddress\":null,
    //\"OfficeAddress\":null,\"Sex\":\"男\",\"BirthDay\":null,\"NativePlace\":null,\"BeginWorkTime\":null,
    //\"GraduatedSchool\":null,\"GraduatedTime\":null,\"Profession\":null,\"Status\":\"1\",\"Type\":\"1\",
    // \"OrgCode\":\"120002\",\"OrgName\":\"服务部\",\"OrgFullName\":\"K2-华南区-服务部\",
    // \"RealityOrgCode\":\"120002\",\"RealityOrgName\":\"服务部\",\"realityOrgFullName\":\"K2-华南区-服务部\",
    // \"PositionCode\":\"1000002\",\"JobDesc\":\"安卓工程师\",\"IsStaff\":true,\"JobLevel\":\"4\",
    // \"JobLevelDesc\":\"4级\"}]"
    
    public String getRealityOrgName() {
		return realityOrgName;
	}
	public void setRealityOrgName(String realityOrgName) {
		this.realityOrgName = realityOrgName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getOfficeTel() {
		return officeTel;
	}
	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUserChsName() {
		return userChsName;
	}
	public void setUserChsName(String userChsName) {
		this.userChsName = userChsName;
	}
	public String getUserEnName() {
		return userEnName;
	}
	public void setUserEnName(String userEnName) {
		this.userEnName = userEnName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getAlphabetical() {
		return alphabetical;
	}
	public void setAlphabetical(String alphabetical) {
		this.alphabetical = alphabetical;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}	
}
 