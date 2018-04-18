/*
 * @(#)com.cntest.fxpt.repository.IIExamPaprameterDao.java	1.0 2014年6月12日:下午5:27:48
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.ExamPaprameter;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午5:27:48
 * @version 1.0
 */
public interface IExamPaprameterDao {
	public List<ExamPaprameter> listByExamId(Long examId);

	public void saves(List<ExamPaprameter> examPaprameters);

	public void updates(List<ExamPaprameter> examPaprameters);
	
	public void deleteParameterByExamId(Long examId);
}
