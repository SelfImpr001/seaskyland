/*
 * @(#)com.cntest.fxpt.service.IEtlLogService.java	1.0 2014年10月30日:上午11:32:16
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.fxpt.bean.EtlLog;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月30日 上午11:32:16
 * @version 1.0
 */
public interface IEtlLogService {
	public void save(EtlLog etlLog);

	public List<EtlLog> list(Long examId, int logType, Page<EtlLog> page);
}
