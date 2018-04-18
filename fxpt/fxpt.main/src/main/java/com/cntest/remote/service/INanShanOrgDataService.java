/*
 * @(#)com.cntest.fxpt.service.INanShanDataService.java	1.0 2014年6月3日:上午8:50:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.remote.service;

import com.cntest.exception.BusinessException;
import com.cntest.remote.domain.NanShanData;
import com.cntest.remote.domain.NanShanOrgData;

/**
 * @author cheny 2016年12月6日
 * @version 1.0
 */
public interface INanShanOrgDataService {
	
	public void add(NanShanOrgData nanShanOrgData) throws BusinessException ;
	
	public void update(NanShanOrgData nanShanOrgData) throws Exception;
	
	public void updateState(String nanShanOrgData) throws BusinessException;

	public void delete(NanShanOrgData nanShanOrgData);
	/**
	 * 根据orgCode查询组织
	 * @param orgCode 组织代码（唯一）
	 * @return
	 */
	public NanShanOrgData findByCode(String orgCode);
	/**
	 * 判断组织是否存在
	 * @param nanShanOrgData
	 * @return
	 */
	public boolean isExistData(NanShanOrgData nanShanOrgData);

}
