package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.RootEntityResultTransformer;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.ApplySet;
import com.cntest.fxpt.repository.IApplySetDao;


@Repository("IApplySetDao")
public class ApplySetDaoImpl extends AbstractHibernateDao<ApplySet, Long>
		implements IApplySetDao {

	@Override
	protected Class<ApplySet> getEntityClass() {
		return ApplySet.class;
	}

	@Override
	public List<ApplySet> findAllApply() {
		String hql="from ApplySet where status<=1 order by ordernum";
		return findByHql(hql);
	}
	
	@Override
	public ApplySet get(Long id) {
		return super.get(id);
	}

	@Override
	public List<ApplySet> findApplyByStatus(String status) {
		String hql="from ApplySet where 1=1 and status=? order by ordernum";
		return findByHql(hql,status);
	}

	@Override
	public List<ApplySet> findApplyByName(String systemName) {
		Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());
		criteria.add(Restrictions.like("systemName", systemName, MatchMode.ANYWHERE));
		criteria.addOrder(Order.desc("order"));
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);
		return criteria.list();
	}

	@Override
	public void addApply(ApplySet applySet) {
		String sql="INSERT INTO kn_apply_set(login_icon,handle_icon,STATUS) VALUES(?,?,3)";
		SQLQuery query =getSession().createSQLQuery(sql);
		query.setParameter(0, applySet.getLoginIcon());
		query.setParameter(1, applySet.getHandleIcon());
		query.executeUpdate();
	}

	@Override
	public void deleteByStatus(String status) {
		String sql="delete from kn_apply_set where status=3";
		SQLQuery query =getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public void updateByApply(ApplySet applySet) {
		String sql="update  kn_apply_set set login_icon=?,handle_icon=? where id=?";
		SQLQuery query =getSession().createSQLQuery(sql);
		query.setParameter(0, applySet.getLoginIcon());
		query.setParameter(1, applySet.getHandleIcon());
		query.setParameter(2, applySet.getId());
		query.executeUpdate();
	}
}
