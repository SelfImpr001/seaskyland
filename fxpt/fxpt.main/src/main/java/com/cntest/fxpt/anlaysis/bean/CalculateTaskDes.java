/*
 * @(#)com.cntest.fxpt.anlaysis.bean.CalculateTaskDescription.java	1.0 2015年6月12日:下午3:39:43
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import java.util.List;

import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.domain.AnalysisResultSaveToTable;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月12日 下午3:39:43
 * @version 1.0
 */
public class CalculateTaskDes {
	private List<FilterDes> filterDesesDes;
	private Container<String, Object> studentRangeDeses;
	private Container<String, AnalysisResultSaveToTable> saveTableContainer;

	public List<FilterDes> getFilterDesesDes() {
		return filterDesesDes;
	}

	public void setFilterDesesDes(List<FilterDes> filterDesesDes) {
		this.filterDesesDes = filterDesesDes;
	}

	public Container<String, Object> getStudentRangeDeses() {
		return studentRangeDeses;
	}

	public void setStudentRangeDeses(Container<String, Object> studentRangeDeses) {
		this.studentRangeDeses = studentRangeDeses;
	}

	public Container<String, AnalysisResultSaveToTable> getSaveTableContainer() {
		return saveTableContainer;
	}

	public void setSaveTableContainer(
			Container<String, AnalysisResultSaveToTable> saveTableContainer) {
		this.saveTableContainer = saveTableContainer;
	}

}
