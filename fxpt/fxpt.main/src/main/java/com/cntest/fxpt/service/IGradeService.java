/*
 * @(#)com.cntest.fxpt.service.IGradeService.java	1.0 2014年5月17日:下午3:48:56
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 下午3:48:56
 * @version 1.0
 */
public interface IGradeService {
	public boolean add(Grade grade);

	public void delete(Grade grade);

	public boolean update(Grade grade);

	public Grade get(Long gradeId);

	public List<Grade> list();

	public boolean hasExam(Grade grade);
	public List<Grade> findGradeList(String subjectName);
}
