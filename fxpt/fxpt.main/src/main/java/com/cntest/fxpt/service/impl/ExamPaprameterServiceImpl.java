/*
 * @(#)com.cntest.fxpt.service.impl.ExamPaprameterServiceImpl.java	1.0 2014年6月12日:下午5:27:22
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.ParamSetting;
import com.cntest.fxpt.domain.ExamPaprameter;
import com.cntest.fxpt.repository.IExamPaprameterDao;
import com.cntest.fxpt.service.ICombinationSubjectService;
import com.cntest.fxpt.service.IExamPaprameterService;
import com.cntest.fxpt.service.IUplineScoreService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午5:27:22
 * @version 1.0
 */
@Service("IExamPaprameterService")
public class ExamPaprameterServiceImpl implements IExamPaprameterService {
	private static final Logger log = LoggerFactory
			.getLogger(ExamPaprameterServiceImpl.class);

	@Autowired(required = false)
	@Qualifier("IExamPaprameterDao")
	private IExamPaprameterDao examPaprameterDao;

	@Autowired(required = false)
	@Qualifier("IUplineScoreService")
	private IUplineScoreService uplineScoreService;

	@Autowired(required = false)
	@Qualifier("ICombinationSubjectService")
	private ICombinationSubjectService combinationSubjectService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.IExamPaprameterService#listByExamId(int)
	 */
	@Override
	public List<ExamPaprameter> listByExamId(Long examId) {
		return examPaprameterDao.listByExamId(examId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.IExamPaprameterService#saves(java.util.List)
	 */
	@Override
	public void saves(List<ExamPaprameter> examPaprameters) {
		if (examPaprameters != null && !examPaprameters.isEmpty()) {
			Long examId = examPaprameters.get(0).getExam().getId();
			examPaprameterDao.deleteParameterByExamId(examId);
			examPaprameterDao.saves(examPaprameters);
		} else {
			log.debug("examParameters is null or is empty!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IExamPaprameterService#updates(java.util.List)
	 */
	@Override
	public void updates(List<ExamPaprameter> examPaprameters) {
		examPaprameterDao.updates(examPaprameters);
	}

}
