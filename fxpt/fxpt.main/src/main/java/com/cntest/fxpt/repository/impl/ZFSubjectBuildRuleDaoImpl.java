/*
 * @(#)com.cntest.fxpt.repository.impl.ZFSubjectBuildRuleDaoImpl.java	1.0 2015年6月11日:下午5:29:57
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.ZFSubjectBuildRule;
import com.cntest.fxpt.repository.ZFSubjectBuildRuleDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 下午5:29:57
 * @version 1.0
 */
@Repository("ZFSubjectBuildRuleDao")
public class ZFSubjectBuildRuleDaoImpl extends
		AbstractHibernateDao<ZFSubjectBuildRule, Long> implements
		ZFSubjectBuildRuleDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.ZFSubjectBuildRuleDao#list()
	 */
	@Override
	public List<ZFSubjectBuildRule> list() {
		String hql = "from ZFSubjectBuildRule where available=true";
		return findByHql(hql);
	}

	@Override
	protected Class<ZFSubjectBuildRule> getEntityClass() {
		return ZFSubjectBuildRule.class;
	}

	@Override
	public void updateZF(String classfield) {
		String sql = "";
		if("wl".equalsIgnoreCase(classfield)){
			sql = "UPDATE kn_zfsubjectbuildrule SET available=1 WHERE classifyFiled='wl'";
			SQLQuery sqlQuery = createSQLQuery(sql);
			sqlQuery.executeUpdate();
			sql = "UPDATE kn_zfsubjectbuildrule SET available=0 WHERE classifyFiled='languageType'";
			sqlQuery = createSQLQuery(sql);
			sqlQuery.executeUpdate();
		}else if("languageType".equalsIgnoreCase(classfield)){
			sql = "UPDATE kn_zfsubjectbuildrule SET available=0 WHERE classifyFiled='wl'";
			SQLQuery sqlQuery = createSQLQuery(sql);
			sqlQuery.executeUpdate();
			sql = "UPDATE kn_zfsubjectbuildrule SET available=1 WHERE classifyFiled='languageType'";
			sqlQuery = createSQLQuery(sql);
			sqlQuery.executeUpdate();
		}
	}

}
