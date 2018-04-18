/*
 * @(#)com.cntest.fxpt.anlaysis.service.ICalcluator.java	1.0 2014年11月28日:下午2:11:00
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 下午2:11:00
 * @version 1.0
 */
public interface ICalcluator {
	public void calculate(CalculateTask event,CalculateResult cr,SubjectScoreContainer ssc);
}
