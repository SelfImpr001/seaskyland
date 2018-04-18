/*
 * @(#)com.cntest.fxpt.service.impl.AnalysisThemeServiceImpl.java	1.0 2015年6月11日:上午11:44:02
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.AnalysisTheme;
import com.cntest.fxpt.repository.AnalysisThemeDao;
import com.cntest.fxpt.service.AnalysisThemeService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 上午11:44:02
 * @version 1.0
 */
@Service("AnalysisThemeService")
public class AnalysisThemeServiceImpl implements AnalysisThemeService {

	@Autowired(required = false)
	@Qualifier("AnalysisThemeDao")
	private AnalysisThemeDao analysisThemeDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.AnalysisThemeService#list()
	 */
	@Override
	public List<AnalysisTheme> list(int analysisDataLevel) {
		return analysisThemeDao.list(analysisDataLevel);
	}

}
