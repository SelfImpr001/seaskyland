package com.cntest.birt.repository.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.birt.domain.OrgBirt;
import com.cntest.birt.domain.ReportScript;
import com.cntest.birt.repository.orgBirtRepository;
import com.cntest.common.query.Query;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.foura.domain.Organization;

@Repository
public class orgBirtRepositoryHibernateImpl extends AbstractHibernateDao<OrgBirt, Long>
		implements orgBirtRepository {
	private static Logger logger = LoggerFactory.getLogger(orgBirtRepositoryHibernateImpl.class);

	@Override
	protected Class<OrgBirt> getEntityClass() {
		// TODO Auto-generated method stub
		return OrgBirt.class;
	}
	@Override
	public List<OrgBirt> nextOrgCountOfParent(List<OrgBirt> list) {
		int num=0;	
		for(int i=0;i<list.size();i++){
			String sql ="select count(1) from kn_birt_org where p_id="+list.get(i).getPk();
			SQLQuery query = getSession().createSQLQuery(sql);
			num=Integer.valueOf(query.uniqueResult().toString());
		}
		return list;
	}
	@Override
	public List<OrgBirt> listByParentIsNull(String name) {
		logger.info("list<OrgBirt>  parent isNull");
		  List<OrgBirt> orgList=new ArrayList<OrgBirt>();
		Criteria c  = createCriteria()
				.add(Restrictions.isNull("parent"));

		    if(name != null && name.length()>0) {
		    	c.add(Restrictions.like("name", "%"+name+"%"));
		   }
		
		    orgList=c.list();
		
		return orgList;
	}
	
	
	@Override
	public List<OrgBirt> selectChildrenNotLeafFor(Long parentPk) {
		logger.info("selectChildrenNotLeafFor  parent.pk = {}",parentPk);
		
		Criteria c = getSession().createCriteria(getEntityClass());
		if(parentPk != null) 
			c.add(Restrictions.eq("parent.pk", parentPk));
		else
			c.add(Restrictions.isNull("parent.pk"));
//		c.add(Restrictions.le("type", 3));
		return c.list();
	}
	
	@Override
	public void findReport(com.cntest.common.query.Query<OrgBirt> query) {
		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
		if (parameters != null) {

			if (parameters.get("q") != null) {
				String q = parameters.get("q")[0];
				criteria.add(Restrictions.like("name", "%" + q + "%"));
			}
			if(parameters.get("pk")!=null&&!parameters.get("pk").equals("-1")){
				criteria.add(Restrictions.eq("parent.pk", parameters.get("pk")));
			}else{
				criteria.add(Restrictions.isNull("parent"));
			}
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
	public List<OrgBirt> findByIn(String directory) {
		String hql = "from OrgBirt where id in (?)";

		return findByHql(hql, directory);
	}

	@Override
	public List<OrgBirt> list() {
		return findByHql("from ReportScript  order by pk desc");
	}

	@Override
	public void cleatSession() {

		this.getSession().clear();
	}
	@Override
	public List<OrgBirt> getNextOrgList(Long pk) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<OrgBirt> listByParentFor(Long pk,String name) {
		logger.info("list<OrgBirt>  parent.pk = {}",pk);
		Criteria c = getSession().createCriteria(getEntityClass());
		if(pk != null&&!pk.equals("-1")) {
			c.add(Restrictions.eq("parent.pk", pk));
		}			
		else{
			c.add(Restrictions.isNull("parent.pk"));
		}
		if(name != null && name.length() >0 && !"-1".equals(name))
			c.add(Restrictions.like("name", "%"+name+"%"));
		return c.list();
	}
	@Override
	public void listByParentFor(Long pk,Query<OrgBirt> query) {
		logger.info("list<Organization>  parent.pk = {},name={}",query);

		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
		
		if(pk != null&&!pk.equals("-1")) 
			criteria.add(Restrictions.eq("parent.pk", pk));
		else {
			criteria.add(Restrictions.isNull("parent"));
		}
		if(parameters != null) {
		    String[] name = parameters.get("qname");
		    if(name != null && name[0].length()>0&&!name[0].equals("undefined")) {
		    	//criteria.add(Restrictions.like("name", "%"+name[0]+"%"));
		    	criteria.add(Restrictions.like("name", "%" + name[0] + "%"));
		    }
		}
		ProjectionList ps = Projections.projectionList();

		ps.add(Projections.rowCount());
		
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		Long rowCount = (Long)o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		criteria.addOrder(Order.desc("pk"));
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		query.setResults(criteria.list()) ;
	}
	
	@Override
	public List<OrgBirt> listByParentFors(Long pk,String name) {
		logger.info("list<OrgBirt>  parent.pk = {}",pk);
		Criteria c = getSession().createCriteria(getEntityClass());
		if(pk != null) {
			c.createAlias("ReportScript", "a");
			c.add(Restrictions.eq("a.org_id", pk));
		}			
		else{
			c.add(Restrictions.isNull("parent.pk"));
		}
	
		return c.list();
	}
	@Override
	public List<Organization> getOrgAllByPk(Long pk, Long type) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Organization getOrgByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean findOrgByCode(String code, String orgId) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void delete(Long pk) {
		// TODO Auto-generated method stub
		String sql ="delete from  kn_birt_org where p_id="+pk;
		SQLQuery query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

}
