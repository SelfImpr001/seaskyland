/*
 * @(#)com.cntest.fxpt.service.IExamClassService.java	1.0 2014年11月28日:上午10:08:50
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.ExamClass;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 上午10:08:50
 * @version 1.0
 */
public interface IExamClassService {
	public List<ExamClass> list(Long examId);

	public List<ExamClass> list(Long examId, String schoolCode);

	public ExamClass get(Long Id);

	public ExamClass get(String schoolCode, String code);
}
