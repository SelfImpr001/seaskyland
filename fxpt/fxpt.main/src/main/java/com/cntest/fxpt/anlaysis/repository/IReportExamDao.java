/*
 * @(#)com.cntest.fxpt.repository.anlaysis.IAnlaysisExamDao.java	1.0 2014年6月24日:下午2:00:40
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.repository;

import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.Exam;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月24日 下午2:00:40
 * @version 1.0
 */
public interface IReportExamDao {
	public List<Exam> countyRecentlyList(String countyCode);

	public List<Exam> schoolRecentlyList(String schoolCode);

	public List<Exam> studentRecentlyList(String studentCode);

	public List<Exam> countyList(String countyCode,Page<Exam> page);

	public List<Exam> schoolList(String schoolCode,Page<Exam> page);

	public List<Exam> studentList(String studentCode,Page<Exam> page);
	
	public Exam getExam(Long examId);

	public List<Exam> selectSameTermExams(Long examId);
}
