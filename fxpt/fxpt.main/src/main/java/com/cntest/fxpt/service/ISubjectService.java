/*
 * @(#)com.cntest.fxpt.service.ISubjectService.java	1.0 2014年5月22日:下午1:57:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午1:57:49
 * @version 1.0
 */
public interface ISubjectService {
	public boolean add(Subject subject);

	public boolean delete(Subject subject);

	public boolean update(Subject subject);
	public Subject get(Long subjectId);
	
	public boolean hasTestPaper(Subject subject);
	public boolean hasItem(Subject subject);

	public List<Subject> list();
	public Subject getZF();
	
	public List<Subject> findSubjectList(String subjectName);
}
