/*
 * @(#)com.cntest.fxpt.repository.IClazzDao.java	1.0 2014年10月10日:下午3:02:41
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.Clazz;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月10日 下午3:02:41
 * @version 1.0
 */
public interface IClazzDao {
	public void add(Clazz clazz);

	public void delete(Clazz clazz);

	public void update(Clazz clazz);

	public Clazz get(Integer clazzId);

	public List<Clazz> list();
	
	public List<Clazz> findByName(String clazzName);
	
	public boolean getHasNum(Clazz clazz);
	
	public Clazz findWithCode(String code);
	
	public boolean hasName(Clazz clazz);
	
	public boolean hasCode(Clazz clazz);

	public List<Clazz> getchildren(String orgid,String examid);
}
