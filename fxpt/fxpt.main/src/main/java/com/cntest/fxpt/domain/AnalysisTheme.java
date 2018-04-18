/*
 * @(#)com.cntest.fxpt.domain.Analysistheme.java	1.0 2015年6月11日:上午11:26:01
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 上午11:26:01
 * @version 1.0
 */
public class AnalysisTheme {
	private Long id;
	private String name;
	private int type;
	private String sql;
	private int analysisThemeLevel;
	private boolean available;
	private List<AnalysisDataClassifyPlan> analysisDataClassifyPlans;
	private List<AnalysisResultSaveToTable> analysisResultSaveToTables;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getAnalysisThemeLevel() {
		return analysisThemeLevel;
	}

	public void setAnalysisThemeLevel(int analysisThemeLevel) {
		this.analysisThemeLevel = analysisThemeLevel;
	}

	public List<AnalysisDataClassifyPlan> getAnalysisDataClassifyPlans() {
		return analysisDataClassifyPlans;
	}

	public void setAnalysisDataClassifyPlans(
			List<AnalysisDataClassifyPlan> analysisDataClassifyPlans) {
		this.analysisDataClassifyPlans = analysisDataClassifyPlans;
	}

	public List<AnalysisResultSaveToTable> getAnalysisResultSaveToTables() {
		return analysisResultSaveToTables;
	}

	public void setAnalysisResultSaveToTables(
			List<AnalysisResultSaveToTable> analysisResultSaveToTables) {
		this.analysisResultSaveToTables = analysisResultSaveToTables;
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
		return name + "--><type:" + type + ">--><analysisThemeLevel:"
				+ analysisThemeLevel + ">"
				+ analysisDataClassifyPlans.toString();
	}

}
