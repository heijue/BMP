package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
/**
 * 
 * @ClassName: DormRepairBean
 * @Description: 宿舍报修附件实体
 * @author linqijun
 * @date 2015-7-20 09:56:46
 *
 */
public class DormRepairAttachmentBean implements Serializable{
	 
	private String fileCode;			// 文件唯一CODE
	private String fileName;			// 文件名称
	private String filePath;			// 文件路径
	private String 	fileSize;		    // 文件大小
	private String fileType;			// 文件类型
	private String iD;					// id
	public String getFileCode() {
		return fileCode;
	}
	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getiD() {
		return iD;
	}
	public void setiD(String iD) {
		this.iD = iD;
	}	 
}
 