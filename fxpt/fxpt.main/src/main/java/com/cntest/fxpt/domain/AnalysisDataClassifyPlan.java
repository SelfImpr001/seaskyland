/*
 * @(#)com.cntest.fxpt.domain.Analysisdataclassifyplan.java	1.0 2015年6月11日:上午11:27:11
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 上午11:27:11
 * @version 1.0
 */
public class AnalysisDataClassifyPlan {
	private Long id;
	private AnalysisTheme analysisTheme;
	private String name;
	private String classifyName;
	private String objectName;
	private String saveToDBFieldName;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getSaveToDBFieldName() {
		return saveToDBFieldName;
	}

	public void setSaveToDBFieldName(String saveToDBFieldName) {
		this.saveToDBFieldName = saveToDBFieldName;
	}

	@Override
	public String toString() {
		return name + "--><classifyName:" + classifyName + ">-<objectName:"
				+ objectName + ">-<saveToDBFieldName:" + saveToDBFieldName
				+ ">";
	}

}
