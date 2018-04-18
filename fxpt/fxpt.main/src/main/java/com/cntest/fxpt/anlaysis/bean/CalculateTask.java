/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.CalculateTask.java	1.0 2014年11月25日:上午10:45:41
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.anlaysis.uitl.OrgProxy;
import com.cntest.fxpt.domain.AnalysisResultSaveToTable;
import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 上午10:45:41
 * @version 1.0
 */
public class CalculateTask {
	// 初始化必须实例化的
	private Object obj;
	private IExamContext context;
	private AnalysisTestpaper analysisTestpaper;
	private Container<String, Object> studentRangeValues = new Container<>();
	private AbstractStudentCjFilter findStudentFilter;
	private Container<String, AnalysisResultSaveToTable> saveTableContainer;
	private String zfResultTableName;
	private String itemResultTableName;
	private String itemGroupTableName;
	private String segmentTableName;

	// 计算
	private SubjectScoreContainer ssc;
	private int bkrs;

	private Container<Integer, CalculateResult> calculateResult = new Container<>();

	public Container<Integer, CalculateResult> getCalculateResult() {
		return calculateResult;
	}

	public CalculateResult getCalculateResult(int wl) {
		CalculateResult result = calculateResult.get(wl);
		if (result == null) {
			result = new CalculateResult();
			calculateResult.put(wl, result);
		}
		return result;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public void setContext(IExamContext context) {
		this.context = context;
	}

	public void setAnalysisTestpaper(AnalysisTestpaper analysisTestpaper) {
		this.analysisTestpaper = analysisTestpaper;
	}

	public Container<String, AnalysisResultSaveToTable> getSaveTableContainer() {
		return saveTableContainer;
	}

	public void setSaveTableContainer(
			Container<String, AnalysisResultSaveToTable> saveTableContainer) {
		this.saveTableContainer = saveTableContainer;
	}

	public Object getObj() {
		return obj;
	}

	public IExamContext getContext() {
		return context;
	}

	public void setSubjectScoreContainer(SubjectScoreContainer ssc) {
		this.ssc = ssc;
	}

	public SubjectScoreContainer getSubjectScoreContainer() {
		return ssc;
	}

	public AnalysisTestpaper getAnalysisTestpaper() {
		return analysisTestpaper;
	}

	public int getBkrs() {
		return bkrs;
	}

	public void setBkrs(int bkrs) {
		this.bkrs = bkrs;
	}

	public void setCalculateTaskWith(CalculateTask calculateTask) {
		this.obj = calculateTask.obj;
		this.analysisTestpaper = calculateTask.analysisTestpaper;
		this.context = calculateTask.context;
		ssc = null;
		calculateResult = new Container<>();
		this.studentRangeValues = calculateTask.studentRangeValues;
		this.findStudentFilter = calculateTask.findStudentFilter;
		this.segmentTableName = calculateTask.segmentTableName;
		this.zfResultTableName = calculateTask.zfResultTableName;
		this.itemResultTableName = calculateTask.itemResultTableName;
		this.itemGroupTableName = calculateTask.itemGroupTableName;
		this.saveTableContainer = calculateTask.saveTableContainer;
	}

	@Override
	public String toString() {

		String text = "";
		if (obj != null) {
			OrgProxy op = new OrgProxy(obj);
			text = op.getName();
		} else {
			for (Object object : studentRangeValues.toList()) {
				text += object.toString() + ",";
			}
		}

		String subject = "";
		if (analysisTestpaper != null) {
			String wl = "";
			if (analysisTestpaper.getPaperType() == 1) {
				wl = "(理)";
			} else if (analysisTestpaper.getPaperType() == 2) {
				wl = "(文)";
			} else {
				wl = "";
			}
			subject = analysisTestpaper.getName() + wl;
		}

		return context.getExam().getName() + "--->" + text + "--->" + subject;
	}

	public AbstractStudentCjFilter getFindStudentFilter() {
		return findStudentFilter;
	}

	public void setFindStudentFilter(AbstractStudentCjFilter findStudentFilter) {
		this.findStudentFilter = findStudentFilter;
	}

	public Container<String, Object> getStudentRangeValues() {
		return studentRangeValues;
	}

	public void putStudentRangeValue(String rangeName, Object rangeValue) {
		studentRangeValues.put(rangeName, rangeValue);
	}

	public void setStudentRangeValues(
			Container<String, Object> studentRangeValues) {
		this.studentRangeValues = studentRangeValues;
	}

	public String getZfResultTableName() {
		return zfResultTableName;
	}

	public void setZfResultTableName(String zfResultTableName) {
		this.zfResultTableName = zfResultTableName;
	}

	public String getItemResultTableName() {
		return itemResultTableName;
	}

	public void setItemResultTableName(String itemResultTableName) {
		this.itemResultTableName = itemResultTableName;
	}

	public String getItemGroupTableName() {
		return itemGroupTableName;
	}

	public void setItemGroupTableName(String itemGroupTableName) {
		this.itemGroupTableName = itemGroupTableName;
	}

	public String getSegmentTableName() {
		return segmentTableName;
	}

	public void setSegmentTableName(String segmentTableName) {
		this.segmentTableName = segmentTableName;
	}

}
