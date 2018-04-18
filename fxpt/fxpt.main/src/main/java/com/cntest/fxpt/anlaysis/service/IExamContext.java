/*
 * @(#)com.cntest.fxpt.anlaysis.service.IExamContext.java	1.0 2014年11月24日:下午1:56:00
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service;

import com.cntest.fxpt.anlaysis.bean.AnalysisTestpaperContainer;
import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.bean.StudentCjContainer;
import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.domain.*;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:56:00
 * @version 1.0
 */
public interface IExamContext {
	public boolean isAllComplate();

	public void setAllComplate();
	
	public boolean isHasChoice();

	public void setHasChoice(boolean hasChoice);


	public void setTaskTotalNum(int taskTotalNum);

	public void addTaskNumToTaskTotalNum(int taskTotalNum);

	public int setCompleteTaskNum(int completeTaskNum);

	public void clearCompleteTask();

	public int completeTask();

	public int getTaskTotalNum();

	public int getCompleteTaskNum();

	public Exam getExam();

	public StudentCjContainer getStudentCjContainer();

	public void loadStudent(Param... params);

	public void loadCj(Param... params);

	public List<ExamClass> getClasses();

	public List<School> getSchools();

	public List<UplineScore> getUplineScores();

	public AnalysisTestpaperContainer getAnalysisTestpaperContainer();

	public StudentCjContainer getStudentCjContainer(
			AbstractStudentCjFilter filter);

	public AbstractStudentCjFilter getStatRankFilter(AnalysisTestpaper atp);

	/**************************************/
	public ISaveCalcluateResultToDBService getSaveCalcluateResultToDBService();

}
