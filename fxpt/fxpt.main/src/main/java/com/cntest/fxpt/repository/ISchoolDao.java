/*
 * @(#)com.cntest.fxpt.repository.ISchoolDao.java	1.0 2014年6月3日:上午8:44:41
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.School;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月3日 上午8:44:41
 * @version 1.0
 */
public interface ISchoolDao {
	public void add(School school);

	public void delete(School school);

	public void update(School school);

	public School get(Long schoolId);

	public School findWithCode(String code);

	public List<School> list();

	public List<School> examSchoolList(Long examId);

	public List<School> list(int type, String code);

	public List<School> list(Page<School> page);

	public List<School> listWithEducationCode(String educationCode);

	public int getExamStudentNum(School school);

}
