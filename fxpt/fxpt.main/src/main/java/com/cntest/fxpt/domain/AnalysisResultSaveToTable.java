/*
 * @(#)com.cntest.fxpt.domain.AnalysisResultSaveToTable.java	1.0 2015年6月12日:下午2:16:41
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月12日 下午2:16:41
 * @version 1.0
 */
public class AnalysisResultSaveToTable {
	private Long id;
	private AnalysisTheme analysisTheme;
	private String tableName;
	private String classifyName;
	private boolean available;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AnalysisTheme getAnalysisTheme() {
		return analysisTheme;
	}

	public void setAnalysisTheme(AnalysisTheme analysisTheme) {
		this.analysisTheme = analysisTheme;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "【tableName:" + tableName + ";classifyName:" + classifyName
				+ "】";
	}

}
