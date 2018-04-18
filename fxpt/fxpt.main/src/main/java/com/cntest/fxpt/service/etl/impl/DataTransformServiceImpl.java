/*
 * @(#)com.cntest.fxpt.service.etl.impl.DataTransformServiceImpl.java	1.0 2014年5月16日:下午2:23:30
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.etl.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.DataTransform;
import com.cntest.fxpt.repository.etl.IDataTransformDao;
import com.cntest.fxpt.service.etl.IDataTransformService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月16日 下午2:23:30
 * @version 1.0
 */
@Service("etl.IDataTransformService")
public class DataTransformServiceImpl implements IDataTransformService {

	@Autowired(required = false)
	@Qualifier("etl.IDataTransformDao")
	private IDataTransformDao dataTransformDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.etl.IDataTransformService#add(com.cntest.fxpt
	 * .systemsetting.etl.domain.DataTransform)
	 */
	@Override
	public void add(DataTransform dataTransform) {
		dataTransformDao.add(dataTransform);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.etl.IDataTransformService#delete(com.cntest.fxpt
	 * .systemsetting.etl.domain.DataTransform)
	 */
	@Override
	public void delete(DataTransform dataTransform) {
		dataTransformDao.delete(dataTransform);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.etl.IDataTransformService#update(com.cntest.fxpt
	 * .systemsetting.etl.domain.DataTransform)
	 */
	@Override
	public void update(DataTransform dataTransform) {
		dataTransformDao.update(dataTransform);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.etl.IDataTransformService#list()
	 */
	@Override
	public List<DataTransform> list() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.etl.IDataTransformService#list(int)
	 */
	@Override
	public List<DataTransform> list(Long dataCategoryId) {
		return dataTransformDao.findByDataCategoryIdAndType(dataCategoryId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.etl.IDataTransformService#findById(int)
	 */
	@Override
	public DataTransform findById(Long dataTransformId) {
		return dataTransformDao.findById(dataTransformId);
	}

	@Override
	public List<DataTransform> findByTableNameAndType(int schemeType,
			int type, boolean isValid) {
		return dataTransformDao.findByTableNameAndType(schemeType, type, isValid);
	}

	
	
}
