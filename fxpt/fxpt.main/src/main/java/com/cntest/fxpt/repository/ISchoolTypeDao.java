/*
 * @(#)com.cntest.fxpt.repository.ISubjectDao.java	1.0 2014年5月22日:下午1:42:35
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.SchoolType;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午1:42:35
 * @version 1.0
 */
public interface ISchoolTypeDao {
	public void add(SchoolType schoolType);

	public void delete(SchoolType schoolType);

	public void update(SchoolType schoolType);

	public SchoolType get(Long id);

	public List<SchoolType> list(Page<SchoolType> page);
	
	public List<SchoolType> getAllSchoolType();

	public List<SchoolType> findSchoolTypeList(String name,Page<SchoolType> page);
	
	public SchoolType findSchoolTypeWith(String name);
}
