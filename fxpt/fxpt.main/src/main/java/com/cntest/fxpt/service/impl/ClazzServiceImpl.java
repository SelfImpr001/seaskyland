/*
 * @(#)com.cntest.fxpt.service.impl.ClazzServiceImpl.java	1.0 2014年10月10日:下午3:01:51
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.Clazz;
import com.cntest.fxpt.repository.IClazzDao;
import com.cntest.fxpt.service.IClazzService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月10日 下午3:01:51
 * @version 1.0
 */
@Service("IClazzService")
public class ClazzServiceImpl implements IClazzService {
	@Autowired(required = false)
	@Qualifier("IClazzDao")
	private IClazzDao clazzDao;

	public boolean add(Clazz clazz) {
		if (clazzDao.hasName(clazz)) {
			clazzDao.add(clazz);
			return true;
		} else {
			return false;
		}
	}

	public boolean delete(Clazz clazz) {
		clazz = clazzDao.get(clazz.getId());
		boolean b = hasBind(clazz);
		if (!b) {
			clazzDao.delete(clazz);
			return true;
		} else {
			return false;
		}

	}

	public boolean hasBind(Clazz clazz) {
		return clazzDao.getHasNum(clazz);
	}

	public boolean update(Clazz clazz) {
		if (clazzDao.hasName(clazz)) {
			clazzDao.update(clazz);
			return true;
		} else {
			return false;
		}
	}

	public Clazz get(Integer clazzId) {
		return clazzDao.get(clazzId);
	}

	public List<Clazz> list() {
		return clazzDao.list();
	}

	public List<Clazz> findByName(String clazzName) {
		return clazzDao.findByName(clazzName);
	}
	
	public boolean hasCode(Clazz clazz){
		return clazzDao.hasCode(clazz);
	}
	public List<Clazz> getchildren(String orgid,String examid) {
		return clazzDao.getchildren(orgid,examid);
	} 

}
