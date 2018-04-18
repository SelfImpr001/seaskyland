/*
 * @(#)com.cntest.fxpt.etl.domain.WebRetrieveResult.java	1.0 2014年5月19日:下午4:51:50
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.List;

import javax.sql.DataSource;

/**
 * <Pre>
 * 从web页面获取etl需要的数据
 * </Pre>
 * 
 * @author 刘海林 2014年5月19日 下午4:51:50
 * @version 1.0
 */
public class WebRetrieveResult {
	private String userName;
	private String fileDir;
	private String fileParentDir;//项目的上级目录
	private int schemeType;
	private String fileName;
	private String fileRealName;
	private String sheetName;
	private String suffix;
	private Long examId;
	private Long testPaperId;
	private String tableName;
	private String omrStr;
	private String scoreStr;
	private List<WebFieldRelation> webFieldRelation;
	private DataSource dataScource;
	private int headRowIndex = 0;
	private boolean isOverlayImport = false;
	//导入操作类型（覆盖导入，追加导入）
	private int importType;
	//excel名称
	private String realName;
	private boolean validate;//是否是验证

	
	public boolean isValidate() {
		return validate;
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	/*
	 * 项目上级目录
	 */
	public String getFileParentDir() {
		return fileParentDir;
	}
	/*
	 * 项目上级目录
	 */
	public void setFileParentDir(String fileParentDir) {
		this.fileParentDir = fileParentDir;
	}

	public String getFileRealName() {
		return fileRealName;
	}

	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getImportType() {
		return importType;
	}

	public void setImportType(int importType) {
		this.importType = importType;
	}

	public String getFilePath() {
		return this.fileDir + this.fileName;
	}

	public int getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(int schemeType) {
		this.schemeType = schemeType;
	}

	public String getOmrStr() {
		return omrStr;
	}

	public void setOmrStr(String omrStr) {
		this.omrStr = omrStr;
	}

	public String getScoreStr() {
		return scoreStr;
	}

	public void setScoreStr(String scoreStr) {
		this.scoreStr = scoreStr;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getExamId() {
		return examId;
	}

	public void setExamId(Long examId) {
		this.examId = examId;
	}

	public Long getTestPaperId() {
		return testPaperId;
	}

	public void setTestPaperId(Long testPaperId) {
		this.testPaperId = testPaperId;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<WebFieldRelation> getWebFieldRelation() {
		return webFieldRelation;
	}

	public void setWebFieldRelation(List<WebFieldRelation> webFieldRelation) {
		this.webFieldRelation = webFieldRelation;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public DataSource getDataScource() {
		return dataScource;
	}

	public void setDataScource(DataSource dataScource) {
		this.dataScource = dataScource;
	}

	public int getHeadRowIndex() {
		return headRowIndex;
	}

	public void setHeadRowIndex(int headRowIndex) {
		this.headRowIndex = headRowIndex;
	}

	public boolean isOverlayImport() {
		return isOverlayImport;
	}

	public void setOverlayImport(boolean isOverlayImport) {
		this.isOverlayImport = isOverlayImport;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
