/*
 * @(#)com.cntest.fxpt.service.anlaysis.IAnlaysisExamService.java	1.0 2014年6月24日:下午1:31:01
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service;

import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.exception.BusinessException;
import com.cntest.fxpt.domain.Exam;
import com.cntest.security.UserDetails;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月24日 下午1:31:01
 * @version 1.0
 */
public interface IReportExamService {
	public List<Exam> countyRecentlyList(String countyCode);

	public List<Exam> schoolRecentlyList(String schoolCode);

	public List<Exam> studentRecentlyList(String studentCode);
	
	public List<Exam> countyList(String countyCode,Page<Exam> page);

	public List<Exam> schoolList(String schoolCode,Page<Exam> page);

	public List<Exam> studentList(String studentCode,Page<Exam> page);
	
	public Exam getExam(Long examId);

	public List<Exam> getSameTermExams(UserDetails user,Exam exam) throws BusinessException;
	
	public List<Exam> getSamTermReports(UserDetails user,Query<Exam> query) throws BusinessException;
}
