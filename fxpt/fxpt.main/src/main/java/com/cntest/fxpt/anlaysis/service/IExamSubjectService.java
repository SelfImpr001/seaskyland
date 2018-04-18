/*
 * @(#)com.cntest.fxpt.anlaysis.service.IExamSubjectService.java	1.0 2014年6月26日:下午4:48:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service;

import java.util.List;

import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月26日 下午4:48:49
 * @version 1.0
 */
public interface IExamSubjectService {
	public List<Subject> getSubjectByExamId(Long examId);
}
