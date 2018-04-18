/*
 * @(#)com.cntest.fxpt.repository.ISubjectDao.java	1.0 2014年5月22日:下午1:42:35
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午1:42:35
 * @version 1.0
 */
public interface ISubjectDao {
	public void add(Subject subject);

	public Subject getZF();

	public void delete(Subject subject);

	public void update(Subject subject);

	public Subject get(Long subjectId);

	public List<Subject> list();

	public List<Subject> findSubjectList(String subjectName);
	public Subject findSubjectListWith(String subjectName);

	public int getTestPaperNum(Subject subject);

	public int getItemNum(Subject subject);

	public boolean hasName(Subject subject);
}
