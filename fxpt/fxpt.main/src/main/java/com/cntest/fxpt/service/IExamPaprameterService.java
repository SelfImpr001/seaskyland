/*
 * @(#)com.cntest.fxpt.service.IExamPaprameterService.java	1.0 2014年6月12日:下午5:25:15
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.bean.ParamSetting;
import com.cntest.fxpt.domain.ExamPaprameter;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午5:25:15
 * @version 1.0
 */
public interface IExamPaprameterService {
	public List<ExamPaprameter> listByExamId(Long examId);

	public void saves(List<ExamPaprameter> examPaprameters);

	public void updates(List<ExamPaprameter> examPaprameters);
}
