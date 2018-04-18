/*
 * @(#)com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDB.java	1.0 2014年12月12日:上午10:32:15
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月12日 上午10:32:15
 * @version 1.0
 */
public interface ISaveCalcluateResultToDBService {
	public void save(CalculateTask event) throws Exception;

	public void clear() throws Exception;
}
