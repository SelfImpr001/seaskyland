/*
 * @(#)com.cntest.fxpt.service.impl.SubjectServiceImpl.java	1.0 2014年5月22日:下午1:58:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.SchoolType;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.repository.ISchoolTypeDao;
import com.cntest.fxpt.repository.ISubjectDao;
import com.cntest.fxpt.service.ISchoolTypeService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午1:58:47
 * @version 1.0
 */
@Service("ISchoolTypeService")
public class SchoolTypeServiceImpl implements ISchoolTypeService {
	@Autowired(required = false)
	@Qualifier("ISchoolTypeDao")
	private ISchoolTypeDao schoolTypeDao;
	
	@Override
	public void add(SchoolType schoolType) {
		schoolTypeDao.add(schoolType);
	}

	@Override
	public void delete(SchoolType schoolType) {
		schoolTypeDao.delete(schoolType);
	}

	@Override
	public void update(SchoolType schoolType) {
		schoolTypeDao.update(schoolType);
	}

	@Override
	public SchoolType get(Long id) {
		return schoolTypeDao.get(id);
	}

	@Override
	public List<SchoolType> findSchoolTypeList(String name,Page<SchoolType> page) {
		return schoolTypeDao.findSchoolTypeList(name,page);
	}

	@Override
	public SchoolType findSchoolTypeWith(String name) {
		return schoolTypeDao.findSchoolTypeWith(name);
	}

	@Override
	public List<SchoolType> list(Page<SchoolType> page) {
		return schoolTypeDao.list(page);
	}

	@Override
	public List<SchoolType> getAllSchoolType() {
		return schoolTypeDao.getAllSchoolType();
	}
}
