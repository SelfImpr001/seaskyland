/*
 * @(#)com.cntest.fxpt.repository.impl.SchoolDaoImpl.java	1.0 2014年6月3日:上午8:46:37
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.remote.repository.impl;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.exception.BusinessException;
import com.cntest.remote.domain.NanShanOrgData;
import com.cntest.remote.repository.INanShanOrgDataDao;

/**
 * @author cheny 2016年12月6日
 * @version 1.0
 */
@Repository("INanShanOrgDataDao")
public class NanShanOrgDataDaoImpl extends AbstractHibernateDao<NanShanOrgData, Long> implements INanShanOrgDataDao {

	@Override
	protected Class<NanShanOrgData> getEntityClass() {
		return NanShanOrgData.class;
	}

	@Override
	public void add(NanShanOrgData nanShanOrgData) throws BusinessException {
		this.save(nanShanOrgData);
	}
	@Override
	public NanShanOrgData findByCode(String orgCode) {
		NanShanOrgData nanShanData=null;
		 String hql = " from NanShanOrgData where orgCode=?";
		 nanShanData=findEntityByHql(hql, orgCode);
		 return nanShanData;
	}
	@Override
	public void updateState(NanShanOrgData t) {
		 String hql = " update nanshan_org_data set status=0 where orgCode="+t.getOrgCode();
		SQLQuery query = getSession().createSQLQuery(hql);
		query.executeUpdate();
	
	}
}
