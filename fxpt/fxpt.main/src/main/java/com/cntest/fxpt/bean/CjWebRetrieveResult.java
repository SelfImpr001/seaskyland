/*
 * @(#)com.cntest.fxpt.etl.domain.WebRetrieveResult.java	1.0 2014年5月19日:下午4:51:50
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.List;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.TestPaper;

/**
 * <Pre>
 * 从web页面获取etl需要的数据
 * </Pre>
 * 
 * @author 刘海林 2014年5月19日 下午4:51:50
 * @version 1.0
 */
public class CjWebRetrieveResult extends WebRetrieveResult {
	private List<Item> items;
	private TestPaper testPaper;
	private List<AnalysisTestpaper> analysisTestpapers;
	
	public CjWebRetrieveResult(WebRetrieveResult wrr) {

		setFileDir(wrr.getFileDir());
		setSchemeType(wrr.getSchemeType());
		setFileName(wrr.getFileName());
		setSheetName(wrr.getSheetName());
		setSuffix(wrr.getSuffix());
		setFileRealName(wrr.getFileRealName());
		setExamId(wrr.getExamId());
		setTestPaperId(wrr.getTestPaperId());
		setTableName(wrr.getTableName());
		setOmrStr(wrr.getOmrStr());
		setScoreStr(wrr.getScoreStr());
		setWebFieldRelation(wrr.getWebFieldRelation());
		setDataScource(wrr.getDataScource());
		setHeadRowIndex(wrr.getHeadRowIndex());
		setOverlayImport(wrr.isOverlayImport());
		setUserName(wrr.getUserName());
		setImportType(wrr.getImportType());
		setFileParentDir(wrr.getFileParentDir());
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public TestPaper getTestPaper() {
		return testPaper;
	}

	public List<AnalysisTestpaper> getAnalysisTestpapers() {
		return analysisTestpapers;
	}

	public void setTestPaper(TestPaper testPaper) {
		this.testPaper = testPaper;
	}

	public void setAnalysisTestpapers(List<AnalysisTestpaper> analysisTestpapers) {
		this.analysisTestpapers = analysisTestpapers;
	}

}
