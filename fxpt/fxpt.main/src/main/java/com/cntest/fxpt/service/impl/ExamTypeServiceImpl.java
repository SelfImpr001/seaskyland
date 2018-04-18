/*
 * @(#)com.cntest.fxpt.service.impl.ExamTypeServiceImpl.java	1.0 2014年5月17日:下午2:36:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.repository.IExamTypeDao;
import com.cntest.fxpt.service.IExamTypeService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 下午2:36:13
 * @version 1.0
 */
@Service("IExamTypeService")
public class ExamTypeServiceImpl implements IExamTypeService {

	@Autowired(required = false)
	@Qualifier("IExamTypeDao")
	private IExamTypeDao examTypeDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.IExamTypeService#list()
	 */
	@Override
	public List<ExamType> list() {
		return examTypeDao.list();
	}

	@Override
	public boolean add(ExamType examType) {
		if(examTypeDao.hasName(examType)){
			examTypeDao.add(examType);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void delete(ExamType examType) {
		examTypeDao.delete(examType);
	}

	@Override
	public boolean update(ExamType examType) {
		if(examTypeDao.hasName(examType)){
			examTypeDao.update(examType);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public ExamType get(Long examTypeId) {
		return examTypeDao.get(examTypeId);
	}

	@Override
	public List<ExamType> list(boolean isValid) {
		return examTypeDao.list(isValid);
	}

	@Override
	public boolean hasExam(ExamType examType) {
		int examNum = examTypeDao.getExamNum(examType);
		return examNum > 0 ? true : false;
	}

	@Override
	public List<ExamType> findExamTypeList(String examTypeName) {
		return examTypeDao.findExamTypeList(examTypeName);
	}

}
