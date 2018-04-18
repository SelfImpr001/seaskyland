package com.cntest.birt.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.birt.domain.ReportScript;
import com.cntest.birt.repository.ReportScriptRepository;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.User;
import com.cntest.foura.repository.UserRepository;
import com.cntest.foura.repository.impl.UserRepositoryHibernateImpl;

@Repository
public class ReportScriptRepositoryHibernateImpl extends AbstractHibernateDao<ReportScript, Long>
		implements ReportScriptRepository {
	private static Logger logger = LoggerFactory.getLogger(ReportScriptRepositoryHibernateImpl.class);

	@Override
	protected Class<ReportScript> getEntityClass() {
		// TODO Auto-generated method stub
		return ReportScript.class;
	}

	@Override
	public void findReport(com.cntest.common.query.Query<ReportScript> query, List ids) {
		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
		if (parameters != null) {

			if (parameters.get("q") != null) {
				String q = parameters.get("q")[0];
				criteria.add(Restrictions.or(Restrictions.like("name", "%" + q + "%"),
						Restrictions.or(Restrictions.like("source", "%" + q + "%"),
								Restrictions.or(Restrictions.like("remark", "%" + q + "%"),
										Restrictions.like("directory", "%" + q + "%")))));
			}
		}
		if(ids.size()>0){
			criteria.add(Restrictions.in("orgBirt.pk", ids));
		}
		ProjectionList ps = Projections.projectionList();
		// ps.add(Projections.distinct(Projections.property("name")));
		ps.add(Projections.rowCount());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		// Long rowCount = (Long)((Object[]) o)[1];
		Long rowCount = (Long) o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		criteria.addOrder(Order.desc("pk"));
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		query.setResults(criteria.list());
	}
	@Override
	public void findReportmerge(com.cntest.common.query.Query<ReportScript> query) {
		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
		if (parameters != null) {

			if (parameters.get("q") != null) {
				String q = parameters.get("q")[0];
				criteria.add(Restrictions.or(Restrictions.like("name", "%" + q + "%"),
						Restrictions.or(Restrictions.like("source", "%" + q + "%"),
								Restrictions.or(Restrictions.like("remark", "%" + q + "%"),
										Restrictions.like("directory", "%" + q + "%")))));
			}
		}
//		if(ids.size()>0){
//			criteria.add(Restrictions.in("orgBirt.pk", ids));
//		}
		criteria.add(Restrictions.isNull("orgBirt.pk"));
		ProjectionList ps = Projections.projectionList();
		// ps.add(Projections.distinct(Projections.property("name")));
		ps.add(Projections.rowCount());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		// Long rowCount = (Long)((Object[]) o)[1];
		Long rowCount = (Long) o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		criteria.addOrder(Order.desc("pk"));
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		query.setResults(criteria.list());
	}
	@Override
	public List<ReportScript> findByIn(String directory) {
		String hql = "from ReportScript where id in (?)";

		return findByHql(hql, directory);
	}

	@Override
	public List<ReportScript> list() {
		return findByHql("from ReportScript  order by pk desc");
	}

	@Override
	public void cleatSession() {

		this.getSession().clear();
	}
	@Override
	public List<ReportScript> getPrens(Long org_id) {
		String sql = "";
		if(org_id!=-1){
			sql="from ReportScript  where org_id="+org_id+" order by pk desc";
		}else{
			sql ="from ReportScript  where org_id is not null order by pk desc";
		}
		return findByHql(sql);
	}
	@Override
	public void getPrens(Long org_id,com.cntest.common.query.Query<ReportScript> query) {
		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
		if (parameters != null) {

			if (parameters.get("q") != null) {
				String q = parameters.get("q")[0];
				criteria.add(Restrictions.or(Restrictions.like("name", "%" + q + "%"),
						Restrictions.or(Restrictions.like("source", "%" + q + "%"),
								Restrictions.or(Restrictions.like("remark", "%" + q + "%"),
										Restrictions.like("directory", "%" + q + "%")))));
			}
		}
		if(org_id!=null&&org_id>0){
			criteria.add(Restrictions.eq("orgBirt.pk", org_id));
		}else{
			criteria.add(Restrictions.isNotNull("orgBirt.pk"));
		}
		
		ProjectionList ps = Projections.projectionList();
		// ps.add(Projections.distinct(Projections.property("name")));
		ps.add(Projections.rowCount());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		// Long rowCount = (Long)((Object[]) o)[1];
		Long rowCount = (Long) o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		criteria.addOrder(Order.desc("pk"));
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		query.setResults(criteria.list());
	}

}
