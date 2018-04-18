/*
 * @(#)com.cntest.fxpt.service.impl.GradeServiceImpl.java	1.0 2014年5月17日:下午3:50:27
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.repository.IGradeDao;
import com.cntest.fxpt.service.IGradeService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 下午3:50:27
 * @version 1.0
 */
@Service("IGradeService")
public class GradeServiceImpl implements IGradeService {
	@Autowired(required = false)
	@Qualifier("IGradeDao")
	private IGradeDao gradeDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.IGradeService#list()
	 */
	@Override
	public List<Grade> list() {
		return gradeDao.list();
	}

	@Override
	public boolean add(Grade grade) {
		if(gradeDao.hasName(grade)){
			gradeDao.add(grade);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void delete(Grade grade) {
		gradeDao.delete(grade);
	}

	@Override
	public boolean update(Grade grade) {
		if(gradeDao.hasName(grade)){
			gradeDao.update(grade);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Grade get(Long gradeId) {
		return gradeDao.get(gradeId);
	}

	@Override
	public boolean hasExam(Grade grade) {
		int examNum = gradeDao.getExamNum(grade);
		return examNum > 0 ? true : false;
	}

	@Override
	public List<Grade> findGradeList(String gradeName) {
		return gradeDao.findGradeList(gradeName);
	}
}
