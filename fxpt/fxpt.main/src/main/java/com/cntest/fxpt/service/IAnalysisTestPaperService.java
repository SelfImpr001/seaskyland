/*
 * @(#)com.cntest.fxpt.service.IAnalysisTestPaperService.java	1.0 2014年10月17日:上午10:41:40
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月17日 上午10:41:40
 * @version 1.0
 */
public interface IAnalysisTestPaperService {
	public List<AnalysisTestpaper> list(Long testPaperId);

	public List<AnalysisTestpaper> listAllWith(Long examId);

	public AnalysisTestpaper get(Long atpId);

}
