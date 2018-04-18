/*
 * @(#)com.cntest.fxpt.repository.impl.EtlLogDaoImpl.java	1.0 2014年10月30日:上午10:46:35
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cntest.common.page.Page;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.bean.EtlLog;
import com.cntest.fxpt.repository.IEtlLogDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月30日 上午10:46:35
 * @version 1.0
 */
@Repository("IEtlLogDao")
public class EtlLogDaoImpl extends AbstractHibernateDao<EtlLog, Long> implements
		IEtlLogDao {

	@Override
	protected Class<EtlLog> getEntityClass() {
		return EtlLog.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.IEtlLogDao#save(com.cntest.fxpt.bean.EtlLog)
	 */
	@Override
	public void save(EtlLog etlLog) {
		super.save(etlLog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.IEtlLogDao#list(com.cntest.common.page.Page,
	 * java.lang.Long, int)
	 */
	@Override
	public List<EtlLog> list(Page<EtlLog> page, Long examId, int logType) {
		Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());
		criteria.add(Restrictions.eq("logType", logType));
		criteria.add(Restrictions.eq("exam.id", examId));

		Object o = criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(Integer.parseInt(o.toString()));
		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());
		criteria.addOrder(Order.desc("createDate"));
		List<EtlLog> list = criteria.list();
		page.setList(list);
		return list;
	}

}
