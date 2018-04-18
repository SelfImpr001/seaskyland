/*
 * @(#)com.cntest.fxpt.service.IAanalysisService.java	1.0 2014年12月4日:下午3:07:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月4日 下午3:07:28
 * @version 1.0
 */
public interface IAanalysisService {
	public void startAnalysis(Long examId);

	public void prepareAnalysis(Long examId);
	public void setSubjectAndLanguagetype(long examId);
	
	public void clarenAnalysisResult(Long examId);
}
