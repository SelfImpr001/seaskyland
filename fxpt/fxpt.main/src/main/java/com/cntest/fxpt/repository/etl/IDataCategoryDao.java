/*
 * @(#)com.cntest.fxpt.repository.etl.IDataCategoryDao.java	1.0 2014年5月13日:下午3:44:32
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.etl;

import java.util.List;

import com.cntest.fxpt.bean.DataCategory;


/**
 * <Pre>
 * etl操作的数据类型，比如学生信息，成绩，细目表等等
 * </Pre>
 * 
 * @author 刘海林 2014年5月13日 下午3:44:32
 * @version 1.0
 */
public interface IDataCategoryDao {
	public void add(DataCategory dataCategory);
	public void delete(DataCategory dataCategory);
	public void update(DataCategory dataCategory);
	public DataCategory findDataCategoryById(Long dataCategoryId);
	public List<DataCategory> list();
	public int getDataFieldCount(DataCategory dataCategory);
	public int getTransformCount(DataCategory dataCategory);
	public int maxSchemeType();
	public boolean hasTableName(DataCategory dataCategory);
}
