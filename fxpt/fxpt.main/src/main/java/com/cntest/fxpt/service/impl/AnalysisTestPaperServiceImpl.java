/*
 * @(#)com.cntest.fxpt.service.impl.AnalysisTestPaperServiceImpl.java	1.0 2014年10月17日:上午10:42:14
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.repository.IAnalysisTestPaperDao;
import com.cntest.fxpt.repository.IITemDao;
import com.cntest.fxpt.repository.ITestPaperDao;
import com.cntest.fxpt.service.IAnalysisTestPaperService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月17日 上午10:42:14
 * @version 1.0
 */
@Service("IAnalysisTestPaperService")
public class AnalysisTestPaperServiceImpl implements IAnalysisTestPaperService {

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperDao")
	private IAnalysisTestPaperDao analysisTestPaperDao;

	@Autowired(required = false)
	@Qualifier("ITestPaperDao")
	private ITestPaperDao testPaperDao;

	@Autowired(required = false)
	@Qualifier("IITemDao")
	private IITemDao itemDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IAnalysisTestPaperService#list(java.lang.Long)
	 */
	@Override
	public List<AnalysisTestpaper> list(Long testPaperId) {
		return analysisTestPaperDao.list(testPaperId);
	}

	@Override
	public List<AnalysisTestpaper> listAllWith(Long examId) {
		return analysisTestPaperDao.listAllWith(examId);
	}

	@Override
	public AnalysisTestpaper get(Long atpId) {
		return analysisTestPaperDao.get(atpId);
	}

}
