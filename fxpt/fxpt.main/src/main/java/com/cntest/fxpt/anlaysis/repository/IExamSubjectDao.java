/*
 * @(#)com.cntest.fxpt.anlaysis.repository.IExamSubjectDao.java	1.0 2014年6月26日:下午4:49:59
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.repository;

import java.util.List;

import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月26日 下午4:49:59
 * @version 1.0
 */
public interface IExamSubjectDao {
	public List<Subject> getSubjectByExamId(Long examId);
}
