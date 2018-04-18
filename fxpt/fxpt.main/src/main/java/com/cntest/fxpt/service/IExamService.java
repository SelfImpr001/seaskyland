/*
 * @(#)com.cntest.fxpt.service.IExamService.java	1.0 2014年5月17日:上午10:55:33
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.Date;
import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamCheckin;
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:55:33
 * @version 1.0
 */
public interface IExamService {
	public void createExams(Date examDate, int schoolYear, int schoolTerm,
			int levelCode, String levelName, String ownerCode,
			String ownerName, String createUserName, ExamType examType,
			List<Grade> grades) throws Exception;

	public void add(Exam exam);

	public void update(Exam exam);

	public void updateStatus(Long examId, int status);

	public void delete(Exam exam);

	public Exam findById(Long examId);

	public List<Exam> list(Page<Exam> page);

	public boolean hasExamStudent(Exam exam);

	public boolean hasTestPaper(Exam exam);

	public ExamCheckin createCheckin(Exam exam, String... specNames);

	public Exam tryGetAnalysisExam();

	Education getExamRootEducation(Exam exam);

	String[] getExamNations(Exam exam);
	
	public boolean getHasChoice(Long examid) ;
	
	public boolean hasStudentsAndSubjcetsAndCj(Long examid);
	/**
	 * 
	 * @return 获取所有考试列表
	 */
	public List<Exam> getExamAllList();
	/**
	 * 考试列表权限过滤
	 * @param username
	 * @param page
	 * @return
	 */
	public Page<Exam> getPowerList(String username,Page<Exam> page);
	/**
	 * 根据用户Id查询用户被授权的考试列表
	 * @param page
	 * @param userId
	 * @return
	 */
	public void examlist(com.cntest.common.query.Query<Exam> query, Long userId);
	
	public List<Exam> getExamByorgCodes(String codes);
	
	
	public List<Exam> listBybach(Page<Exam> page,Integer ... status);
	
	/**
	 * 获取所有的学年类型
	 * @return
	 */
	public List<Exam> getExamAllSchoolYears();


}
