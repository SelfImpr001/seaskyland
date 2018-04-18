/*
 * @(#)com.cntest.fxpt.repository.impl.StatisticSettingDaoImpl.java	1.0 2014年10月27日:下午3:10:11
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.StatisticSetting;
import com.cntest.fxpt.repository.IStatisticSettingDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:10:11
 * @version 1.0
 */
@Repository("IStatisticSettingDao")
public class StatisticSettingDaoImpl extends
		AbstractHibernateDao<StatisticSetting, Integer> implements
		IStatisticSettingDao {

	public List<StatisticSetting> list() {
		String hql = "from StatisticSetting";
		return findByHql(hql);
	}

	protected Class<StatisticSetting> getEntityClass() {
		return StatisticSetting.class;
	}

	public void update(int single, int multi) {
		String sql = "UPDATE kn_statisticsetting SET checked=0,status=1";
		String sql1 = "UPDATE kn_statisticsetting SET checked=1 WHERE id="
				+ single;
		String sql2 = "UPDATE kn_statisticsetting SET checked=1 WHERE id="
				+ multi;
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.executeUpdate();
		sqlQuery = createSQLQuery(sql1);
		sqlQuery.executeUpdate();
		sqlQuery = createSQLQuery(sql2);
		sqlQuery.executeUpdate();
	}

	public int getSubjectStatisticSettingValue(int type) {
		String sql = "SELECT svalue FROM kn_statisticsetting WHERE stype="
				+ type + " AND checked=1";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0).toString());
		} else {
			return 0;
		}

	}

	@Override
	public StatisticSetting getCheck(int stype) {
		String hql = "from StatisticSetting where stype=? and checked=?";
		return findEntityByHql(hql, stype, 1);
	}

}
