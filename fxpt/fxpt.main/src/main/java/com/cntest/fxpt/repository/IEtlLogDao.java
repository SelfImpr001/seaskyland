/*
 * @(#)com.cntest.fxpt.repository.IEtlLogDao.java	1.0 2014年10月30日:上午10:44:21
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.fxpt.bean.EtlLog;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月30日 上午10:44:21
 * @version 1.0
 */
public interface IEtlLogDao {
	public void save(EtlLog etlLog);

	public List<EtlLog> list(Page<EtlLog> page, Long examId, int logType);
}
