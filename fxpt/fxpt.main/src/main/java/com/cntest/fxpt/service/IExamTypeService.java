/*
 * @(#)com.cntest.fxpt.service.IExamTypeService.java	1.0 2014年5月17日:下午2:34:57
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.ExamType;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 下午2:34:57
 * @version 1.0
 */
public interface IExamTypeService {
	public boolean add(ExamType examType);

	public void delete(ExamType examType);

	public boolean update(ExamType examType);

	public ExamType get(Long examTypeId);

	public List<ExamType> list();
	
	public List<ExamType> list(boolean isValid);
	
	public boolean hasExam(ExamType examType);
	
	public List<ExamType> findExamTypeList(String examTypeName);
}
