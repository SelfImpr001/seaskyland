/*
 * @(#)com.cntest.fxpt.anlaysis.service.ISvaeFileToDBService.java	1.0 2014年12月10日:下午5:42:51
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service;

import com.cntest.fxpt.anlaysis.bean.SaveFileTask;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月10日 下午5:42:51
 * @version 1.0
 */
public interface ISaveFileToDBService {
	public void save(SaveFileTask event) throws Exception;
}
