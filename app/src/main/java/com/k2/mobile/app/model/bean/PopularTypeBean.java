package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
  
public class PopularTypeBean implements Serializable{
 
	 private String id;    		
	 private String typeCode; 		
     private String typeName; 
     private String parentCode;
     private String typeLevel;
     private String ico;
     private String remark;
     
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getTypeLevel() {
		return typeLevel;
	}
	public void setTypeLevel(String typeLevel) {
		this.typeLevel = typeLevel;
	}
	public String getIco() {
		return ico;
	}
	public void setIco(String ico) {
		this.ico = ico;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
 