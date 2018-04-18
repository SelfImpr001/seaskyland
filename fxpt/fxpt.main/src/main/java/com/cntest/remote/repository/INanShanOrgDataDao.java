/*
 * @(#)com.cntest.fxpt.repository.INanShanDataDao.java	1.0 2014年6月3日:上午8:44:41
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.remote.repository;

import com.cntest.exception.BusinessException;
import com.cntest.remote.domain.NanShanOrgData;

/**
 * @author cheny 2016年12月13日
 * @version 1.0
 */
public interface INanShanOrgDataDao {
	
	public void add(NanShanOrgData nanShanOrgData) throws BusinessException ;

	public void update(NanShanOrgData NanShanOrgData);
	
	public void updateState(NanShanOrgData NanShanOrgData);

	public void delete(NanShanOrgData NanShanOrgData);
	/**
	 * 根据orgCode查询组织
	 * @param orgCode 组织代码（唯一）
	 * @return
	 */
	public NanShanOrgData findByCode(String orgCode);
}
