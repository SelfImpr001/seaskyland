/*
 * @(#)com.cntest.fxpt.service.ISchoolService.java	1.0 2014年6月3日:上午8:50:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.School;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月3日 上午8:50:10
 * @version 1.0
 */
public interface ISchoolService {
	public void add(School school);

	public void update(School school);

	public void delete(School school);

	public School get(Long schoolId);

	public List<School> list();

	public List<School> list(Long examId);

	public List<School> examSchoolList(Long examId);

	public List<School> list(Page<School> page);

	public List<School> listWithEducationCode(String educationCode);

	public boolean isExistSchool(School school);

	public boolean hasExamStudent(School school);
	
	public School  findCodeForSchool(String code);
	
}
