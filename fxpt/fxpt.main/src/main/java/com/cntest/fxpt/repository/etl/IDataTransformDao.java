/*
 * @(#)com.cntest.fxpt.repository.etl.IDataTransformDao.java	1.0 2014年5月14日:上午9:14:36
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.etl;

import java.util.List;

import com.cntest.fxpt.bean.DataTransform;


/**
 * <Pre>
 * 处理数据转换的dao接口
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 上午9:14:36
 * @version 1.0
 */
public interface IDataTransformDao {

	public void add(DataTransform dataTransform);

	public void update(DataTransform dataTransform);

	public void delete(DataTransform dataTransform);

	public DataTransform findById(Long dataTransformId);

	public List<DataTransform> findByDataCategoryIdAndType(Long dataCategoryId,
			int type);

	public List<DataTransform> findByDataCategoryIdAndType(Long dataCategoryId,
			int type, boolean isValid);

	public List<DataTransform> findByTableNameAndType(int schemeType,
			int type, boolean isValid);

	public List<DataTransform> findByDataCategoryIdAndType(Long dataCategoryId);
}
