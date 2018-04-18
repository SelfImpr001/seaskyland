/*
 * @(#)com.cntest.fxpt.anlaysis.customize.anlaysis.Calcluator.java	1.0 2015年4月23日:上午8:32:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.anlaysis;

import java.util.HashMap;
import java.util.List;

import com.cntest.fxpt.anlaysis.bean.StudentCjContainer;
import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisTask;
import com.cntest.fxpt.anlaysis.customize.bean.Formula;
import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.anlaysis.uitl.CalculateHelper;
import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月23日 上午8:32:13
 * @version 1.0
 */
public class Calcluator {
	private AnalysisTask analysisTask;
	private StudentCjContainer studentCj;

	public Calcluator(AnalysisTask analysisTask) {
		this.analysisTask = analysisTask;
	}

	public void analysis() {
		AbstractStudentCjFilter filter = analysisTask.getFilter();
		studentCj = analysisTask.getContext().getStudentCjContainer(filter);
		calculateAtp();
	}

	private void calculateAtp() {
		List<AnalysisTestpaper> analysisTestpapers = analysisTask
				.getAnalysisTestpapers();
		HashMap<String, Object> result = new HashMap<>();
		for (AnalysisTestpaper atp : analysisTestpapers) {
			atp = analysisTask.getContext().getAnalysisTestpaperContainer()
					.get(atp.getId());
			if (atp == null) {
				continue;
			}
			SubjectScoreContainer cjc = studentCj.getSubjectScoreContainer(atp,
					analysisTask.getContext().getStatRankFilter(atp));
			if (cjc.isEmpty()) {
				continue;
			}
			CalculateHelper ch = cjc.getCalculateHelper();

			HashMap<String, Object> values = new HashMap<>();
			result.put(atp.getName(), values);
			List<Formula> formulas = analysisTask.getFormulas();
			for (Formula f : formulas) {
				double value = new FormulaCalcluator(cjc, ch, f).getValue();
				values.put(f.getName(), value);
			}
		}
		analysisTask.getAnalysisResult().put(analysisTask.getKey(), result);
	}
}
