/*
 * @(#)com.cntest.fxpt.repository.IGradeDao.java	1.0 2014年5月17日:上午10:28:46
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.Grade;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:28:46
 * @version 1.0
 */
public interface IGradeDao {
	public List<Grade> list();

	public void add(Grade grade);

	public void delete(Grade grade);

	public void update(Grade grade);

	public Grade get(Long gradeId);

	public int getExamNum(Grade grade);
	
	public List<Grade> findGradeList(String gradeName);
	
	public boolean hasName(Grade grade);
}
