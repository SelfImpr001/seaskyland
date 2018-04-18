/*
 * @(#)com.cntest.fxpt.repository.IExamTypeDao.java	1.0 2014年5月17日:下午2:36:30
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.ExamType;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 下午2:36:30
 * @version 1.0
 */
public interface IExamTypeDao {
	public void add(ExamType examType);

	public void delete(ExamType examType);

	public void update(ExamType examType);

	public ExamType get(Long examTypeId);

	public List<ExamType> list();
	public List<ExamType> list(boolean isValid);
	
	public int getExamNum(ExamType examType);
	
	public List<ExamType> findExamTypeList(String examTypeName);
	
	public boolean hasName(ExamType examType);
}
