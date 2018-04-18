/*
 * @(#)com.cntest.fxpt.repository.IExamClassDao.java	1.0 2014年11月25日:下午4:34:56
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.ExamClass;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午4:34:56
 * @version 1.0
 */
public interface IExamClassDao {
	public List<ExamClass> list(Long examId);

	public List<ExamClass> list(Long examId, String schoolCode);

	public ExamClass get(Long Id);

	public ExamClass get(String schoolCode, String code);
}
