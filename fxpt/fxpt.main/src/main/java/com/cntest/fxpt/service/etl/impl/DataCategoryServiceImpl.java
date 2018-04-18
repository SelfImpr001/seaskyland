/*
 * @(#)com.cntest.fxpt.service.etl.impl.DataCategoryServiceImpl.java	1.0 2014年5月15日:上午11:19:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.etl.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.fxpt.bean.DataCategory;
import com.cntest.fxpt.repository.etl.IDataCategoryDao;
import com.cntest.fxpt.service.etl.IDataCategoryService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月15日 上午11:19:28
 * @version 1.0
 */
@Service("etl.IDataCategoryService")
public class DataCategoryServiceImpl implements IDataCategoryService {

	@Autowired(required = false)
	@Qualifier("etl.IDataCategoryDao")
	private IDataCategoryDao dataCategoryDao;

	@Override
	public void add(DataCategory dataCategory) {
		int num = dataCategoryDao.maxSchemeType();
		++num;
		dataCategory.setSchemeType(num);
		dataCategoryDao.add(dataCategory);
	}

	@Override
	public void delete(DataCategory dataCategory) {
		dataCategoryDao.delete(dataCategory);
	}

	@Override
	public void update(DataCategory dataCategory) {
		dataCategoryDao.update(dataCategory);
	}

	@Override
	public List<DataCategory> list() {
		return dataCategoryDao.list();
	}

	@Override
	public DataCategory findById(Long dataCategoryId) {
		return dataCategoryDao.findDataCategoryById(dataCategoryId);
	}

	@Override
	public boolean hasDataField(DataCategory dataCategory) {
		int count = dataCategoryDao.getDataFieldCount(dataCategory);
		return count > 0 ? true : false;
	}

	@Override
	public boolean hasTransform(DataCategory dataCategory) {
		int count = dataCategoryDao.getTransformCount(dataCategory);
		return count > 0 ? true : false;
	}

	public boolean hasTableName(DataCategory dataCategory) {
		return dataCategoryDao.hasTableName(dataCategory);
	}

}
