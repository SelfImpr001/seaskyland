/*
 * @(#)com.cntest.fxpt.service.IClazzService.java	1.0 2014年10月10日:下午3:00:26
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.Clazz;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月10日 下午3:00:26
 * @version 1.0
 */
public interface IClazzService {
	public boolean add(Clazz clazz);

	public boolean delete(Clazz clazz);

	public boolean update(Clazz clazz);

	public Clazz get(Integer clazzId);

	public List<Clazz> list();
	
	public List<Clazz> findByName(String clazzName);
	
	public boolean hasCode(Clazz clazz);
	
	public List<Clazz> getchildren(String orgid,String examid);

}
