/*
 * @(#)com.cntest.fxpt.service.IStudentBaseService.java	1.0 2014年10月9日:下午3:44:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import com.cntest.common.query.Query;
import com.cntest.fxpt.domain.StudentBase;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月9日 下午3:44:49
 * @version 1.0
 */
public interface IStudentBaseService {
	public Query<StudentBase> list(Query<StudentBase> query);
	
	public StudentBase get(Integer studentBaseId);
	
	public void update(StudentBase studentBase);
	
	public int getMaxId();
	
	public void importExamStudents(Integer[] examids);
	
	public boolean haveData(String xh,String name,String schoolCode,String grade);
	
	public boolean isSchoolCodeExists(String schoolCode);
	
	public boolean isClazzCodeExists(String clazzCode);
	
	StudentBase findByExample(StudentBase example);
}
