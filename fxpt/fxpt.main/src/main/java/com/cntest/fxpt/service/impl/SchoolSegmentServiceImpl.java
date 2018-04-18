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

import com.cntest.fxpt.domain.SchoolSegment;
import com.cntest.fxpt.repository.ISchoolSegmentDao;
import com.cntest.fxpt.repository.ISchoolTypeDao;
import com.cntest.fxpt.service.ISchoolSegmentService;
import com.cntest.fxpt.service.ISchoolTypeService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午1:58:47
 * @version 1.0
 */
@Service("ISchoolSegmentService")
public class SchoolSegmentServiceImpl implements ISchoolSegmentService {
	@Autowired(required = false)
	@Qualifier("ISchoolSegmentDao")
	private ISchoolSegmentDao schoolSegmentDao;

	
	//	@Service("ISchoolTypeService")
//	public class SchoolTypeServiceImpl implements ISchoolTypeService {
//		@Autowired(required = false)
//		@Qualifier("ISchoolTypeDao")
//		private ISchoolTypeDao schoolTypeDao;
//	
	
	@Override
	public List<SchoolSegment> getAllSchoolSegment() {
		return schoolSegmentDao.getAllSchoolSegment();
	}
}
