/*
 * @(#)com.cntest.fxpt.service.impl.ExamClassServiceImpl.java	1.0 2014年11月28日:上午10:09:33
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.ExamClass;
import com.cntest.fxpt.repository.IExamClassDao;
import com.cntest.fxpt.service.IExamClassService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 上午10:09:33
 * @version 1.0
 */
@Service("IExamClassService")
public class ExamClassServiceImpl implements IExamClassService {

	@Autowired(required = false)
	@Qualifier("IExamClassDao")
	private IExamClassDao examClassDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.IExamClassService#list(java.lang.Long)
	 */
	@Override
	public List<ExamClass> list(Long examId) {
		return examClassDao.list(examId);
	}

	@Override
	public List<ExamClass> list(Long examId, String schoolCode) {
		return examClassDao.list(examId, schoolCode);
	}

	@Override
	public ExamClass get(Long Id) {
		return examClassDao.get(Id);
	}

	@Override
	public ExamClass get(String schoolCode, String code) {
		return examClassDao.get(schoolCode, code);
	}

}
