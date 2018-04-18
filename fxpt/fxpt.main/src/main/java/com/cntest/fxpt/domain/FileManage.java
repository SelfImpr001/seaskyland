/*
 * @(#)com.cntest.fxpt.domain.Subject.java	1.0 2014年5月14日:下午1:25:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import java.util.Date;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 下午1:25:49
 * @version 1.0
 */
public class FileManage {
	private Long fileId;
	private Long examId;
	/*
	 * 导入文件类型（0--组织，1--学生，2--细目表，3--成绩）
	 */
	private Long fileType;
	private Date importTime;
	//导入文件的原始名称
	private String fileName;
	/*
	 * 文件保存的名称
	 */
	private String filePath;
	/*
	 * 操作者
	 */
	private String importer;
	
	
	/*
	 * 操作者
	 */
	public String getImporter() {
		return importer;
	}
	/*
	 * 操作者
	 */
	public void setImporter(String importer) {
		this.importer = importer;
	}
	
	
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public Long getExamId() {
		return examId;
	}
	public void setExamId(Long examId) {
		this.examId = examId;
	}
	/*
	 * 导入文件类型（0--组织，1--学生，2--细目表，3--成绩）
	 */
	public Long getFileType() {
		return fileType;
	}
	/*
	 * 导入文件类型（0--组织，1--学生，2--细目表，3--成绩）
	 */
	public void setFileType(Long fileType) {
		this.fileType = fileType;
	}
	public Date getImportTime() {
		return importTime;
	}
	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/*
	 * 文件保存的名称
	 */
	public String getFilePath() {
		return filePath;
	}
	/*
	 * 文件保存的名称
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
