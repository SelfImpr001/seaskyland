/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.LoadExamData.java	1.0 2014年11月24日:下午5:01:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import java.util.List;

import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.bean.StudentCjContainer;
import com.cntest.fxpt.anlaysis.bean.TempItemScore;
import com.cntest.fxpt.anlaysis.bean.TempTotalScore;
import com.cntest.fxpt.anlaysis.service.ILoadExamData;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.StatisticSetting;
import com.cntest.fxpt.service.IAnalysisTestPaperService;
import com.cntest.fxpt.service.ICjService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.service.IItemService;
import com.cntest.fxpt.service.IStatisticSettingService;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午5:01:47
 * @version 1.0
 */
public class LoadExamData implements ILoadExamData {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.anlaysis.service.ILoadExamData#loadExam()
	 */
	@Override
	public Exam loadExam(Long examId) {
		IExamService s = SpringContext.getBean("IExamService");
		return s.findById(examId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.service.ILoadExamData#loadAnlaysisTestPaper()
	 */
	@Override
	public void loadAnlaysisTestPaper(
			Container<Long, AnalysisTestpaper> analysisTestpaperContainer,
			Exam exam) {
		IAnalysisTestPaperService s = SpringContext
				.getBean("IAnalysisTestPaperService");
		IItemService itemService = SpringContext.getBean("IItemService");
		List<AnalysisTestpaper> list = s.listAllWith(exam.getId());

		for (AnalysisTestpaper at : list) {
			analysisTestpaperContainer.put(at.getId(), at);
			List<Item> items = itemService
					.listByAnlaysisTestPaperId(at.getId());
			at.setItems(items);
		}
	}

	@Override
	public StudentCjContainer loadStudent(Exam exam, Param... param) {
		IExamStudentService service = SpringContext
				.getBean("IExamStudentService");
		return service.listStudentWith(exam.getId(), param);
	}

	@Override
	public List<ExamStudent> loadStudentList(Exam exam, Param... params) {
		IExamStudentService service = SpringContext
				.getBean("IExamStudentService");
		return service.listStudent(exam.getId(), params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.anlaysis.service.ILoadExamData#loadTestPaperCj()
	 */
	@Override
	public List<TempTotalScore> loadTestPaperCj(Exam exam, Param... params) {
		ICjService service = SpringContext.getBean("ICjService");
		return service.loadCj(exam.getId(), params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.anlaysis.service.ILoadExamData#loadItemCj()
	 */
	@Override
	public List<TempItemScore> loadItemCj(Exam exam, Param... params) {
		ICjService service = SpringContext.getBean("ICjService");
		return service.loadItemCj(exam.getId(), params);
	}

	@Override
	public StatisticSetting loadDkStatParam(Exam exam) {
		IStatisticSettingService s = SpringContext
				.getBean("IStatisticSettingService");
		return s.getCheck(1);
	}

	@Override
	public StatisticSetting loadZfStatParam(Exam exam) {
		IStatisticSettingService s = SpringContext
				.getBean("IStatisticSettingService");
		return s.getCheck(2);
	}

}
