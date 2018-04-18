/*
 * @(#)com.cntest.fxpt.service.AnalysisThemeService.java	1.0 2015年6月11日:上午11:43:35
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.AnalysisTheme;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 上午11:43:35
 * @version 1.0
 */
public interface AnalysisThemeService {
	public List<AnalysisTheme> list(int analysisDataLevel);
}
