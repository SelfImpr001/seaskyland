/*
 * @(#)com.cntest.fxpt.anlaysis.customize.bean.AnalysisTask.java	1.0 2015年4月22日:上午10:39:43
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.bean;

import java.util.List;

import com.cntest.fxpt.anlaysis.customize.parse.DimesionResolver;
import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 上午10:39:43
 * @version 1.0
 */
public class AnalysisTask {

	private IExamContext context;
	private AnalysisResult analysisResult;
	private List<FilterWhereValue> filterWhereValues;
	private List<AnalysisTestpaper> analysisTestpapers;
	private List<Formula> formulas;

	public IExamContext getContext() {
		return context;
	}

	public List<FilterWhereValue> getFilterWhereValues() {
		return filterWhereValues;
	}

	public List<AnalysisTestpaper> getAnalysisTestpapers() {
		return analysisTestpapers;
	}

	public List<Formula> getFormulas() {
		return formulas;
	}

	public AnalysisResult getAnalysisResult() {
		return analysisResult;
	}

	public AnalysisTask setContext(IExamContext context) {
		this.context = context;
		return this;
	}

	public AnalysisTask setAnalysisResult(AnalysisResult analysisResult) {
		this.analysisResult = analysisResult;
		return this;
	}

	public AnalysisTask setAnalysisTestpapers(
			List<AnalysisTestpaper> analysisTestpapers) {
		this.analysisTestpapers = analysisTestpapers;
		return this;
	}

	public AnalysisTask setFormulas(List<Formula> formulas) {
		this.formulas = formulas;
		return this;
	}

	public AnalysisTask setFilterWhereValues(
			List<FilterWhereValue> filterWhereValues) {
		this.filterWhereValues = filterWhereValues;
		return this;
	}

	public AbstractStudentCjFilter getFilter() {
		if (filterWhereValues == null) {
			return null;
		}
		DimesionResolver resolver = new DimesionResolver();
		return resolver.parse(filterWhereValues);
	}

	@Override
	public String toString() {
		if (filterWhereValues == null) {
			return "All";
		}

		StringBuffer sb = new StringBuffer();

		for (FilterWhereValue f : filterWhereValues) {
			sb.append(f.toString());
			sb.append("-");
		}

		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	public String getKey() {
		StringBuffer sb = new StringBuffer();
		if (filterWhereValues == null) {
			sb.append("All");
		} else {
			for (FilterWhereValue f : filterWhereValues) {
				sb.append(f.getDimensionAttributeValue());
				sb.append(".");
			}
			if (sb.length() > 1) {
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		return sb.toString();
	}

	public void copy(AnalysisTask at) {
		context = at.context;
		analysisResult = at.analysisResult;
		filterWhereValues = at.filterWhereValues;
		analysisTestpapers = at.analysisTestpapers;
		formulas = at.formulas;
	}

}
