/*
 * @(#)com.cntest.fxpt.service.INanShanDataService.java	1.0 2014年6月3日:上午8:50:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.remote.service;

import java.util.Map;

import org.hibernate.Query;

import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.User;
import com.cntest.remote.domain.NanShanData;

/**
 * @author 吕萌 2016年12月6日
 * @version 1.0
 */
public interface INanShanDataService {
	
	public void add(NanShanData nanShanData) throws BusinessException ;

	public void update(NanShanData nanShanData) throws BusinessException;
	
	public void updateState(NanShanData nanShanData) throws BusinessException;

	public void delete(NanShanData nanShanData);

	public NanShanData get(Long nanShanDataId);

	public NanShanData findByUid(String uid);
	
	public boolean isExistData(NanShanData nanShanData);
	
	/**
	 * 查询南山单点登录学生的考试信息的Query
	 * @param user  loginId
	 * @return
	 */
	public Query getQueryByUser(String loginId,User user,Map<String,String> parameters);

}
