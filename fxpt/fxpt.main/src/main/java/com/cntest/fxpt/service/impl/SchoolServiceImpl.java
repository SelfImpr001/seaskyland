/*
 * @(#)com.cntest.fxpt.service.impl.SchoolServiceImpl.java	1.0 2014年6月3日:上午8:51:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.repository.ISchoolDao;
import com.cntest.fxpt.service.ISchoolService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月3日 上午8:51:49
 * @version 1.0
 */
@Service("ISchoolService")
public class SchoolServiceImpl implements ISchoolService {

	@Autowired(required = false)
	@Qualifier("IExamDao")
	private IExamDao examDao;

	@Autowired(required = false)
	@Qualifier("ISchoolDao")
	private ISchoolDao schoolDao;

	@Override
	public List<School> list(Long examId) {
		Exam exam = examDao.findById(examId);
		return schoolDao.list(exam.getLevelCode(), exam.getOwnerCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ISchoolService#add(com.cntest.fxpt.domain.School)
	 */
	@Override
	public void add(School school) {
		schoolDao.add(school);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ISchoolService#update(com.cntest.fxpt.domain.
	 * School)
	 */
	@Override
	public void update(School school) {
		schoolDao.update(school);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ISchoolService#delete(com.cntest.fxpt.domain.
	 * School)
	 */
	@Override
	public void delete(School school) {
		schoolDao.delete(school);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.ISchoolService#get(int)
	 */
	@Override
	public School get(Long schoolId) {
		return schoolDao.get(schoolId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.ISchoolService#list()
	 */
	@Override
	public List<School> list() {
		return schoolDao.list();
	}

	@Override
	public boolean isExistSchool(School school) {
		School tmpSchool = schoolDao.findWithCode(school.getCode());
		return tmpSchool == null ? false : true;
	}

	@Override
	public List<School> listWithEducationCode(String educationCode) {
		return schoolDao.listWithEducationCode(educationCode);
	}

	@Override
	public boolean hasExamStudent(School school) {
		int examStudentNum = schoolDao.getExamStudentNum(school);
		return examStudentNum > 0 ? true : false;
	}

	@Override
	public List<School> list(Page<School> page) {
		return schoolDao.list(page);
	}

	@Override
	public List<School> examSchoolList(Long examId) {
		return schoolDao.examSchoolList(examId);
	}
	
	@Override
	public School findCodeForSchool(String code) {
		return schoolDao.findWithCode(code);
	};

}
