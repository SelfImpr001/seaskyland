/*
 * @(#)com.cntest.fxpt.repository.etl.impl.DataTransformDaoImpl.java	1.0 2014年5月14日:上午9:18:56
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.etl.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.bean.DataTransform;
import com.cntest.fxpt.repository.etl.IDataTransformDao;
import com.cntest.common.repository.AbstractHibernateDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 上午9:18:56
 * @version 1.0
 */
@Repository("etl.IDataTransformDao")
public class DataTransformDaoImpl extends
		AbstractHibernateDao<DataTransform, Long> implements
		IDataTransformDao {

	@Override
	public void add(DataTransform dataTransform) {
		this.save(dataTransform);
	}

	@Override
	public void update(DataTransform dataTransform) {
		super.update(dataTransform);
	}

	@Override
	public void delete(DataTransform dataTransform) {
		super.delete(dataTransform);
	}

	@Override
	public DataTransform findById(Long dataTransformId) {
		return this.get(dataTransformId);
	}

	@Override
	public List<DataTransform> findByDataCategoryIdAndType(Long dataCategoryId,
			int type) {
		String hql = "from DataTransform as dt where dt.dataCategory.id=? and dt.type=?";
		List<DataTransform> result = findByHql(hql, dataCategoryId, type);
		return result;
	}

	@Override
	public List<DataTransform> findByDataCategoryIdAndType(Long dataCategoryId,
			int type, boolean isValid) {
		String hql = "from DataTransform as dt where dt.dataCategory.id=? and dt.type=? and dt.valid=?";
		List<DataTransform> result = findByHql(hql, dataCategoryId, type,
				isValid);
		return result;
	}

	@Override
	public List<DataTransform> findByDataCategoryIdAndType(Long dataCategoryId) {
		String hql = "from DataTransform as dt where dt.dataCategory.id=? order by dt.type";
		List<DataTransform> result = findByHql(hql, dataCategoryId);
		return result;
	}

	@Override
	public List<DataTransform> findByTableNameAndType(int schemeType,
			int type, boolean isValid) {
		String hql = "from DataTransform as dt where dt.dataCategory.schemeType=? and dt.type=? and dt.valid=?";
		List<DataTransform> result = findByHql(hql, schemeType, type, isValid);
		return result;
	}

	@Override
	protected Class<DataTransform> getEntityClass() {
		return DataTransform.class;
	}

}
