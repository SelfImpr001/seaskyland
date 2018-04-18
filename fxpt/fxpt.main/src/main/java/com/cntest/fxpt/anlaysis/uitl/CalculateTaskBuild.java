/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.CalcluateTaskBuild.java	1.0 2015年6月11日:下午1:16:31
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.cntest.fxpt.anlaysis.bean.CalculateTaskDes;
import com.cntest.fxpt.anlaysis.bean.FilterDes;
import com.cntest.fxpt.anlaysis.bean.StudentCj;
import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.anlaysis.filter.StudentAttrFilter;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.domain.AnalysisDataClassifyPlan;
import com.cntest.fxpt.domain.AnalysisResultSaveToTable;
import com.cntest.fxpt.domain.AnalysisTheme;
import com.cntest.fxpt.domain.ExamStudent;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 下午1:16:31
 * @version 1.0
 */
public class CalculateTaskBuild {
	private HashMap<String, Integer> hasDimensionValue = new HashMap<>();
	private ArrayList<CalculateTaskDes> taskList = new ArrayList<>();
	private Container<String, AnalysisResultSaveToTable> saveTableContainer = new Container<>();

	public List<CalculateTaskDes> build(AnalysisTheme analysisTheme,
			IExamContext ec) {
		resolve(analysisTheme, ec);
		return taskList;
	}

	private void resolve(AnalysisTheme analysisTheme, IExamContext ec) {
		List<StudentCj> studentCjs = ec.getStudentCjContainer().toList();
		for (StudentCj cj : studentCjs) {
			ExamStudent student = cj.getStudent();
			ResolveAnalysisThemeWithStudent r = new ResolveAnalysisThemeWithStudent(
					analysisTheme, student);
			createTaskDes(analysisTheme, r);
		}
	}

	private void createTaskDes(AnalysisTheme analysisTheme,
			ResolveAnalysisThemeWithStudent r) {
		if (r.isAvailable() && !isHasTaskAndIfNoAdd(r)) {
			addAnalysisLevel(analysisTheme, r.getStudentRangeValues());
			CalculateTaskDes task = new CalculateTaskDes();
			task.setStudentRangeDeses(r.getStudentRangeValues());
			task.setFilterDesesDes(r.getFilter());
			task.setSaveTableContainer(getSaveTableContainer(analysisTheme));
			taskList.add(task);
		}
	}

	private void addAnalysisLevel(AnalysisTheme analysisTheme,
			Container<String, Object> studentRangeDeses) {
		int level = analysisTheme.getAnalysisThemeLevel();
		if (level < 1000000) {
			studentRangeDeses.put("objType", level);
		}
	}

	private Container<String, AnalysisResultSaveToTable> getSaveTableContainer(
			AnalysisTheme analysisTheme) {

		if (saveTableContainer.isEmpty()) {
			List<AnalysisResultSaveToTable> analysisResultSaveToTables = analysisTheme
					.getAnalysisResultSaveToTables();
			for (AnalysisResultSaveToTable table : analysisResultSaveToTables) {
				table.setAnalysisTheme(null);
				saveTableContainer.put(table.getClassifyName(), table);
			}
		}
		return saveTableContainer;
	}

	private boolean isHasTaskAndIfNoAdd(ResolveAnalysisThemeWithStudent r) {
		boolean result = hasDimensionValue.get(r.key()) != null;
		if (!result) {
			hasDimensionValue.put(r.key(), 1);
		}
		return result;
	}

	private AbstractStudentCjFilter createStudentFilter(String attrName,
			String attrValue) {
		return new StudentAttrFilter(attrName, attrValue);
	}

	private class ResolveAnalysisThemeWithStudent {
		private AnalysisTheme analysisTheme;
		private ExamStudent student;
		private Container<String, Object> studentRangeValues = new Container<>();
		private ArrayList<FilterDes> filterDeses = new ArrayList<>();
		private StringBuffer keyBuffer = new StringBuffer();
		private boolean available = true;

		public ResolveAnalysisThemeWithStudent(AnalysisTheme analysisTheme,
				ExamStudent student) {
			this.analysisTheme = analysisTheme;
			this.student = student;
			resolveAnalysisDataClassifyPlans();
		}

		public String key() {
			return keyBuffer.toString();
		}

		public List<FilterDes> getFilter() {
			return filterDeses;
		}

		public Container<String, Object> getStudentRangeValues() {
			return studentRangeValues;
		}

		public boolean isAvailable() {
			return available;
		}

		private void resolveAnalysisDataClassifyPlans() {
			for (AnalysisDataClassifyPlan p : analysisTheme
					.getAnalysisDataClassifyPlans()) {
				String studentAttributeValue = getStudentAttributeValue(p
						.getClassifyName());
				if (studentAttributeValue == null) {
					available = false;
					break;
				}
				keyBuffer = createKeyBuffer(studentAttributeValue);
				createFilter(p, studentAttributeValue);
				studentRangeValues = createStudentRangeValues(p,
						studentAttributeValue);
			}
		}

		private String getStudentAttributeValue(String attrName) {
			String studentAttributeValue = "";
			try {
				studentAttributeValue = BeanUtils
						.getProperty(student, attrName);
			} catch (Exception e) {
			}
			return studentAttributeValue;
		}

		private StringBuffer createKeyBuffer(String studentAttributeValue) {
			keyBuffer.append(".").append(studentAttributeValue);
			return keyBuffer;
		}

		private FilterDes createFilter(AnalysisDataClassifyPlan p,
				String studentAttributeValue) {
			FilterDes filterDes = new FilterDes();
			filterDes.setAttrName(p.getObjectName());
			filterDes.setValue(studentAttributeValue);
			filterDeses.add(filterDes);
			return filterDes;
		}

		private Container<String, Object> createStudentRangeValues(
				AnalysisDataClassifyPlan p, String studentAttributeValue) {
			studentRangeValues.put(p.getSaveToDBFieldName(),
					studentAttributeValue);
			return studentRangeValues;
		}
	}

}
