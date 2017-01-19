package com.k2.mobile.app.model.bean;    
  
public class AutoUpdateProcedureBean {

    private String versionCode ; 	// 程序代码版本
    private String versionName ; 	// 应用版本名称
    private String fileSize ;		// 文件大小
    private String fileName ;		// 文件名
    private String downloadUrl ;	// 下载路径
    private String description ;	// 更新描述
    private String isNeedUpdate;	// 是否强制性更新
    
	public String getIsNeedUpdate() {
		return isNeedUpdate;
	}
	public void setIsNeedUpdate(String isNeedUpdate) {
		this.isNeedUpdate = isNeedUpdate;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
 