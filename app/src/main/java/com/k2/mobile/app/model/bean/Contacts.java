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
public class Contacts implements Serializable{
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
    // 部门
    private String realityOrgName;
    //发件人类型
    private String type;
    
    /// 记录ID
    private String iD;
    /// 用户全名
    private String userFullName ;
    /// 邮箱
    private String email ;
    /// 手机号码
    private String mobilePhone ;
    /// 办公电话
    private String officeTel ;
    /// 固定电话
    private String homeTel ;
    /// 住址
    private String homeAddress ;
    /// 办公地址
    private String officeAddress ;
    /// 性别
    private String sex ;
    /// 生日
    private String birthDay ;
    /// 学位
    private String nativePlace ;
    /// 开始工作时间
    private String beginWorkTime ;
    /// 毕业学校
    private String graduatedSchool ;
    /// 毕业时间
    private String graduatedTime ;
    /// 专业
    private String profession ;
    /// 状态
    private String status ;
    /// 组织编码
    private String orgCode ;    
    /// 组织全称
    private String orgFullName ;
    /// 部门编码
    private String realityOrgCode ;
    /// 部门全称
    private String realityOrgFullName ;
    /// 职位代码
    private String PositionCode ;
    /// 职位名称
    private String jobDesc;
    /// 是否在职：0离职；1在职
    private String isStaff;
    /// 职级
    private String jobLevel;
    /// 职级名称
    private String jobLevelDesc;
    
	public String getiD() {
		return iD;
	}
	public void setiD(String iD) {
		this.iD = iD;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getHomeTel() {
		return homeTel;
	}
	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getBeginWorkTime() {
		return beginWorkTime;
	}
	public void setBeginWorkTime(String beginWorkTime) {
		this.beginWorkTime = beginWorkTime;
	}
	public String getGraduatedSchool() {
		return graduatedSchool;
	}
	public void setGraduatedSchool(String graduatedSchool) {
		this.graduatedSchool = graduatedSchool;
	}
	public String getGraduatedTime() {
		return graduatedTime;
	}
	public void setGraduatedTime(String graduatedTime) {
		this.graduatedTime = graduatedTime;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgFullName() {
		return orgFullName;
	}
	public void setOrgFullName(String orgFullName) {
		this.orgFullName = orgFullName;
	}
	public String getRealityOrgCode() {
		return realityOrgCode;
	}
	public void setRealityOrgCode(String realityOrgCode) {
		this.realityOrgCode = realityOrgCode;
	}
	public String getRealityOrgFullName() {
		return realityOrgFullName;
	}
	public void setRealityOrgFullName(String realityOrgFullName) {
		this.realityOrgFullName = realityOrgFullName;
	}
	public String getPositionCode() {
		return PositionCode;
	}
	public void setPositionCode(String positionCode) {
		PositionCode = positionCode;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public String getIsStaff() {
		return isStaff;
	}
	public void setIsStaff(String isStaff) {
		this.isStaff = isStaff;
	}
	public String getJobLevel() {
		return jobLevel;
	}
	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}
	public String getJobLevelDesc() {
		return jobLevelDesc;
	}
	public void setJobLevelDesc(String jobLevelDesc) {
		this.jobLevelDesc = jobLevelDesc;
	}
	public String getRealityOrgName() {
		return realityOrgName;
	}
	public void setRealityOrgName(String realityOrgName) {
		this.realityOrgName = realityOrgName;
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
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
	
}
 