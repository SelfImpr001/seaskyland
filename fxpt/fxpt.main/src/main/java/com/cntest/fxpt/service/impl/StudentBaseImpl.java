/*
 * @(#)com.cntest.fxpt.service.impl.StudentBase.java	1.0 2014年10月9日:下午3:45:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.common.query.Query;
import com.cntest.fxpt.domain.StudentBase;
import com.cntest.fxpt.repository.IStudentBaseDao;
import com.cntest.fxpt.service.IStudentBaseService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月9日 下午3:45:45
 * @version 1.0
 */
@Service("IStudentBaseService")
public class StudentBaseImpl implements IStudentBaseService {
	@Autowired(required = false)
	@Qualifier("IStudentBaseDao")
	private IStudentBaseDao studentBaseDao;

	public Query<StudentBase> list(Query<StudentBase> query) {
		return studentBaseDao.list(query);
	}

	public StudentBase get(Integer studentBaseId) {
		return studentBaseDao.get(studentBaseId);
	}

	public void update(StudentBase studentBase) {
		studentBaseDao.update(studentBase);
	}

	public int getMaxId() {
		return studentBaseDao.getMaxId();
	}

	public void importExamStudents(Integer[] examids) {
		studentBaseDao.importExamStudents(examids);
	}

	public boolean haveData(String xh,String name, String schoolCode,
			String grade) {
		return studentBaseDao.haveData(xh,name, schoolCode, grade);
	}

	public boolean isSchoolCodeExists(String schoolCode) {
		return studentBaseDao.isSchoolCodeExists(schoolCode);
	}

	public boolean isClazzCodeExists(String clazzCode) {
		return studentBaseDao.isClazzCodeExists(clazzCode);
	}

	@Override
	public StudentBase findByExample(StudentBase example) {
		return studentBaseDao.findByExample(example);
	}

}
