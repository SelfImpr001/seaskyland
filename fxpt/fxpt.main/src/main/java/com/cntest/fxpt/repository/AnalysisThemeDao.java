/*
 * @(#)com.cntest.fxpt.repository.AnalysisThemeDao.java	1.0 2015年6月11日:上午11:40:42
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.AnalysisTheme;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 上午11:40:42
 * @version 1.0
 */
public interface AnalysisThemeDao {
	public List<AnalysisTheme> list(int analysisDataLevel);
	
	public void setSubjectAndLanguagetype(long examId);
}
