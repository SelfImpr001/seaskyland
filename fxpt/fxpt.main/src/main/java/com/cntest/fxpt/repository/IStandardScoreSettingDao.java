/*
 * @(#)com.cntest.fxpt.repository.IStandardScoreSettingDao.java	1.0 2015年4月20日:下午5:05:48
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2015年4月20日 下午5:05:48
 * @version 1.0
 */
public interface IStandardScoreSettingDao {
	public List<Object[]> findList(Long examid);
	public List<String> getWl(Long examid);
	public void save(Long examid,int subjectid,String zvalue,int wl);
	public String getZvalue(Long examid,int wl);
}
