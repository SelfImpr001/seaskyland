/*
 * @(#)com.cntest.fxpt.service.impl.EtlLogServiceImpl.java	1.0 2014年10月30日:上午11:33:41
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.common.page.Page;
import com.cntest.fxpt.bean.EtlLog;
import com.cntest.fxpt.repository.IEtlLogDao;
import com.cntest.fxpt.service.IEtlLogService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月30日 上午11:33:41
 * @version 1.0
 */
@Service("IEtlLogService")
public class EtlLogServiceImpl implements IEtlLogService {

	@Autowired(required = false)
	@Qualifier("IEtlLogDao")
	private IEtlLogDao etlLogDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IEtlLogService#save(com.cntest.fxpt.bean.EtlLog)
	 */
	@Override
	public void save(EtlLog etlLog) {
		etlLogDao.save(etlLog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.IEtlLogService#list(java.lang.Long, int,
	 * com.cntest.common.page.Page)
	 */
	@Override
	public List<EtlLog> list(Long examId, int logType, Page<EtlLog> page) {
		return etlLogDao.list(page, examId, logType);
	}

}
