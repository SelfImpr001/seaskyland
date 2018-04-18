/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.BuildTask.java	1.0 2015年6月12日:下午3:09:01
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.util.ArrayList;
import java.util.List;

import com.cntest.fxpt.anlaysis.bean.CalculateTaskDes;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.domain.AnalysisTheme;
import com.cntest.fxpt.service.AnalysisThemeService;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月12日 下午3:09:01
 * @version 1.0
 */
public class BuildTask {
	public List<CalculateTaskDes> build(IExamContext ec) {
		AnalysisThemeService analysisThemeService = (AnalysisThemeService) SpringContext
				.getBean("AnalysisThemeService");
		List<AnalysisTheme> analysisThemes = analysisThemeService.list(ec
				.getExam().getLevelCode());

		ArrayList<CalculateTaskDes> result = new ArrayList<>();
		for (AnalysisTheme analysisTheme : analysisThemes) {
			List<CalculateTaskDes> taskList = new CalculateTaskBuild().build(
					analysisTheme, ec);
			result.addAll(taskList);
		}
		return result;
	}
}
