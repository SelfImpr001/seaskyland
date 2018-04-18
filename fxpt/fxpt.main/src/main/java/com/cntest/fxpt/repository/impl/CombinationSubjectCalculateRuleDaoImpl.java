/*
 * @(#)com.cntest.fxpt.repository.impl.CombinationSubjectCalculateRuleDaoImpl.java	1.0 2015年5月22日:下午4:49:18
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.CombinationSubjectCalculateRule;
import com.cntest.fxpt.repository.CombinationSubjectCalculateRuleDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年5月22日 下午4:49:18
 * @version 1.0
 */
@Repository("CombinationSubjectCalculateRuleDao")
public class CombinationSubjectCalculateRuleDaoImpl extends
		AbstractHibernateDao<CombinationSubjectCalculateRule, Long> implements
		CombinationSubjectCalculateRuleDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.CombinationSubjectCalculateRuleDao#delete(
	 * java.lang.Long)
	 */
	@Override
	public void delete(Long combinationSubjectId) {
		String hql = "delete from CombinationSubjectCalculateRule where combinationSubject.id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, combinationSubjectId);
		query.executeUpdate();
	}

	@Override
	protected Class<CombinationSubjectCalculateRule> getEntityClass() {
		// TODO Auto-generated method stub
		return CombinationSubjectCalculateRule.class;
	}

}
