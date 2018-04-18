/*
 * @(#)com.cntest.fxpt.repository.INanShanDataDao.java	1.0 2014年6月3日:上午8:44:41
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.remote.repository;

import java.util.Map;

import org.hibernate.Query;

import com.cntest.foura.domain.User;
import com.cntest.remote.domain.NanShanData;

/**
 * @author 吕萌 2016年12月6日
 * @version 1.0
 */
public interface INanShanDataDao {
	
	public void add(NanShanData nanShanData);
	
	public void save(NanShanData nanShanData);

	public void delete(NanShanData nanShanData);

	public void update(NanShanData nanShanData);
	
	public NanShanData get(Long nanShanDataId);

	public NanShanData findByUid(String uid);
	
	/**
	 * 查询南山单点登录学生的考试信息的Query
	 * @param user
	 * @return
	 */
	public Query getQueryByUser(String loginId ,User user,Map<String,String> parameters);
	
}
