/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.ExamSubjectServiceImpl.java	1.0 2014年6月26日:下午4:49:37
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.anlaysis.repository.IExamSubjectDao;
import com.cntest.fxpt.anlaysis.service.IExamSubjectService;
import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月26日 下午4:49:37
 * @version 1.0
 */
@Service("IExamSubjectService")
public class ExamSubjectServiceImpl implements IExamSubjectService {

	@Autowired(required = false)
	@Qualifier("IExamSubjectDao")
	private IExamSubjectDao examSubjectDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.service.IExamSubjectService#getSubjectByExamId
	 * (int)
	 */
	@Override
	public List<Subject> getSubjectByExamId(Long examId) {
		// TODO Auto-generated method stub
		return examSubjectDao.getSubjectByExamId(examId);
	}

}
