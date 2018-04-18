/*
 * @(#)com.cntest.fxpt.anlaysis.event.produce.CalculateTaskFactory.java	1.0 2015年7月21日:下午2:30:37
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.produce;

import java.util.List;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.CalculateTaskDes;
import com.cntest.fxpt.anlaysis.bean.FilterDes;
import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.anlaysis.filter.EmptyFilter;
import com.cntest.fxpt.anlaysis.filter.StudentAttrFilter;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年7月21日 下午2:30:37
 * @version 1.0
 */
public class CalculateTaskFactory {
	CalculateTask task = new CalculateTask();

	public CalculateTask build(IExamContext ec, CalculateTaskDes taskDes,
			AnalysisTestpaper testpaper, int wl) {
		setExamContext(ec);
		setTestpaper(testpaper);
		setSaveTableContainer(taskDes);
		setFindStudentFilter(taskDes, wl);
		setStudentRangeValues(taskDes, wl);
		return task;
	}

	private void setExamContext(IExamContext context) {
		task.setContext(context);
	}

	private void setTestpaper(AnalysisTestpaper testpaper) {
		task.setAnalysisTestpaper(testpaper);
	}

	private void setSaveTableContainer(CalculateTaskDes taskDes) {
		task.setSaveTableContainer(taskDes.getSaveTableContainer());
	}

	private void setFindStudentFilter(CalculateTaskDes taskDes, int wl) {
		task.setFindStudentFilter(createFilter(taskDes, wl));
	}

	private AbstractStudentCjFilter createFilter(CalculateTaskDes taskDes,
			int wl) {
		List<FilterDes> filterDeses = taskDes.getFilterDesesDes();
		AbstractStudentCjFilter filter = createWLFilter(wl);
		for (FilterDes filterDes : filterDeses) {
			filter.next(new StudentAttrFilter(filterDes.getAttrName(),
					filterDes.getValue()));
		}
		return filter;
	}

	private AbstractStudentCjFilter createWLFilter(int wl) {
		AbstractStudentCjFilter filter = null;
		if (wl == 0) {
			filter = new EmptyFilter();
		} else {
			filter = new StudentAttrFilter("wl", wl + "");
		}
		return filter;
	}

	private void setStudentRangeValues(CalculateTaskDes taskDes, int wl) {
		Container<String, Object> studentRangeValues = new Container<>();
		studentRangeValues.putAll(taskDes.getStudentRangeDeses());
		studentRangeValues.put("wl", wl);
		task.setStudentRangeValues(studentRangeValues);
	}
}
