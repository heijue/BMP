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
public class DormRepairUseMaterialBean implements Serializable{
	
	private String materialName ;		// 材料名称
    private String iD ;					// 主键ID
    private String repairCode ;			// RepairCode
    private String materialCode ;		// 物料Code
    private String materialDisplayCode ;// 物料编号
    private String materialCount ;		// 数量
    private String IsOwn ;				// 是否自费
    private String materialUnitPrice ;	// 单价
    private String useType ;			// 类型
    private String createTime ;  		// CreateTime
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getiD() {
		return iD;
	}
	public void setiD(String iD) {
		this.iD = iD;
	}
	public String getRepairCode() {
		return repairCode;
	}
	public void setRepairCode(String repairCode) {
		this.repairCode = repairCode;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialDisplayCode() {
		return materialDisplayCode;
	}
	public void setMaterialDisplayCode(String materialDisplayCode) {
		this.materialDisplayCode = materialDisplayCode;
	}
	public String getMaterialCount() {
		return materialCount;
	}
	public void setMaterialCount(String materialCount) {
		this.materialCount = materialCount;
	}
	public String getIsOwn() {
		return IsOwn;
	}
	public void setIsOwn(String isOwn) {
		IsOwn = isOwn;
	}
	public String getMaterialUnitPrice() {
		return materialUnitPrice;
	}
	public void setMaterialUnitPrice(String materialUnitPrice) {
		this.materialUnitPrice = materialUnitPrice;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
 }
 