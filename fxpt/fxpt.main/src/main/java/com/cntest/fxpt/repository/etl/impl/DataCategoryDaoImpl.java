/*
 * @(#)com.cntest.fxpt.repository.etl.impl.DataCategoryDaoImpl.java	1.0 2014年5月13日:下午3:48:15
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.etl.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.bean.DataCategory;
import com.cntest.fxpt.repository.etl.IDataCategoryDao;
import com.cntest.common.repository.AbstractHibernateDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月13日 下午3:48:15
 * @version 1.0
 */
@Repository("etl.IDataCategoryDao")
public class DataCategoryDaoImpl extends
		AbstractHibernateDao<DataCategory, Long> implements IDataCategoryDao {

	/**
	 * 
	 */
	public DataCategoryDaoImpl() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.etl.IDataCategoryDao#add(com.cntest.fxpt.
	 * systemsetting.etl.domain.DataCategory)
	 */
	@Override
	public void add(DataCategory dataCategory) {
		this.save(dataCategory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.etl.IDataCategoryDao#delete(com.cntest.fxpt
	 * .systemsetting.etl.domain.DataCategory)
	 */
	@Override
	public void delete(DataCategory dataCategory) {
		super.delete(dataCategory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.etl.IDataCategoryDao#update(com.cntest.fxpt
	 * .systemsetting.etl.domain.DataCategory)
	 */
	@Override
	public void update(DataCategory dataCategory) {
		super.update(dataCategory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.etl.IDataCategoryDao#findDataCategoryById(int)
	 */
	@Override
	public DataCategory findDataCategoryById(Long dataCategoryId) {
		DataCategory result = this.get(dataCategoryId);
		return result;

	}

	@Override
	public List<DataCategory> list() {
		String hql = "from DataCategory as dc";
		return findByHql(hql);
	}

	@Override
	protected Class<DataCategory> getEntityClass() {
		return DataCategory.class;
	}

	@Override
	public int getDataFieldCount(DataCategory dataCategory) {
		String sql = "select count(1) from kn_etl_datafield where dataCategoryId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, dataCategory.getId());
		Object obj = sqlQuery.uniqueResult();
		int result = -1;
		if (obj instanceof Integer) {
			result = (Integer) obj;
		} else if (obj instanceof BigInteger) {
			BigInteger tmp = (BigInteger) obj;
			result = tmp.intValue();
		}
		return result;
	}

	@Override
	public int getTransformCount(DataCategory dataCategory) {
		String sql = "select count(1) from kn_etl_datatransform where dataCategoryId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, dataCategory.getId());
		Object obj = sqlQuery.uniqueResult();
		int result = -1;
		if (obj instanceof Integer) {
			result = (Integer) obj;
		} else if (obj instanceof BigInteger) {
			BigInteger tmp = (BigInteger) obj;
			result = tmp.intValue();
		}
		return result;
	}

	
	public int maxSchemeType(){
		String sql = "SELECT MAX(schemeType) FROM kn_etl_datacategory";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0){
			return Integer.parseInt(list.get(0).toString());
		}else{
			return 1;
		}
	}

	public boolean hasTableName(DataCategory dataCategory) {
		String sql = "SELECT COUNT(1) FROM kn_etl_datacategory WHERE tableName='"+dataCategory.getTableName()+"'";
		if(dataCategory!=null && dataCategory.getId()!=null && dataCategory.getId()>0){
			sql = "SELECT COUNT(1) FROM kn_etl_datacategory WHERE tableName='"+dataCategory.getTableName()+"' AND id<>"+dataCategory.getId();
		}
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && Integer.parseInt(list.get(0).toString())>0){
			return false;
		}
		return true;
	}
}
