/*
 * @(#)com.cntest.fxpt.anlaysis.customize.bean.AnalysisResource.java	1.0 2015年4月22日:上午9:50:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.bean;

import java.util.List;

import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 上午9:50:04
 * @version 1.0
 */
public class AnalysisResource {
	private Long examId;
	private List<Formula> formulas;
	private List<AnalysisTestpaper> analysisTestpapers;
	private AnalysisFilterWhere analysisFilterWhere;

	public Long getExamId() {
		return examId;
	}

	public List<Formula> getFormulas() {
		return formulas;
	}

	public AnalysisFilterWhere getAnalysisFilterWhere() {
		return analysisFilterWhere;
	}

	public List<AnalysisTestpaper> getAnalysisTestpapers() {
		return analysisTestpapers;
	}

	public void setAnalysisTestpapers(List<AnalysisTestpaper> analysisTestpapers) {
		this.analysisTestpapers = analysisTestpapers;
	}

	public void setExamId(Long examId) {
		this.examId = examId;
	}

	public void setFormulas(List<Formula> formulas) {
		this.formulas = formulas;
	}

	public void setAnalysisFilterWhere(AnalysisFilterWhere analysisFilterWhere) {
		this.analysisFilterWhere = analysisFilterWhere;
	}

}
