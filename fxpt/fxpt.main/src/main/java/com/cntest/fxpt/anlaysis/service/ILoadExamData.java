/*
 * @(#)com.cntest.fxpt.anlaysis.service.ILoadExamData.java	1.0 2014年11月24日:下午1:52:06
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service;

import java.util.List;

import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.bean.StudentCjContainer;
import com.cntest.fxpt.anlaysis.bean.TempItemScore;
import com.cntest.fxpt.anlaysis.bean.TempTotalScore;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.StatisticSetting;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:52:06
 * @version 1.0
 */
public interface ILoadExamData {
	public Exam loadExam(Long examId);

	public void loadAnlaysisTestPaper(
			Container<Long, AnalysisTestpaper> analysisTestpaperContainer,
			Exam exam);

	public StudentCjContainer loadStudent(Exam exam, Param... params);

	public List<ExamStudent> loadStudentList(Exam exam, Param... params);

	public List<TempTotalScore> loadTestPaperCj(Exam exam, Param... params);

	public List<TempItemScore> loadItemCj(Exam exam, Param... params);

	public StatisticSetting loadDkStatParam(Exam exam);

	public StatisticSetting loadZfStatParam(Exam exam);

}
