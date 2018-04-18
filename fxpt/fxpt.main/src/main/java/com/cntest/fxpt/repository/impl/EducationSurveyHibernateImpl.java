/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.query.Query;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.EducationSurvey;
import com.cntest.fxpt.domain.FSDataNnalysis;
import com.cntest.fxpt.repository.EducationSurveyRepository;
import com.cntest.fxpt.service.impl.EducationSurveyServiceImpl;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
@Repository
@SuppressWarnings("unchecked")
public class EducationSurveyHibernateImpl extends AbstractHibernateDao<EducationSurvey,Long> implements EducationSurveyRepository {
	private static Logger logger = LoggerFactory.getLogger(EducationSurveyServiceImpl.class);
	@Override
	protected Class<EducationSurvey> getEntityClass() {
		return EducationSurvey.class;
	}

	
	@Override
	public List<EducationSurvey> listByParentIsNull(String name) {
		logger.info("list<EducationSurvey>  parent isNull");
		
		Criteria c = getSession().createCriteria(getEntityClass())
				.add(Restrictions.isNull("parent"));

		    if(name != null && name.length()>0) {
		    	c.add(Restrictions.like("name", "%"+name+"%"));
		   }
		
		return c.list();
	}

	@Override
	public List<EducationSurvey> listByParentFor(Long pk,String name) {
		logger.info("list<Organization>  parent.pk = {}",pk);
		Criteria c = getSession().createCriteria(getEntityClass());
		if(pk != null) 
			c.add(Restrictions.eq("parent.pk", pk));
		else
			c.add(Restrictions.isNull("parent.pk"));
		if(name != null && name.length() >0)
			c.add(Restrictions.like("name", "%"+name+"%"));
		return c.list();
	}


	@Override
	public EducationSurvey getEduByPk(Long pk) {
		return null;
	}


	@Override
	public void savaEdu(EducationSurvey edu) {
		
	}


	@Override
	public int findOrgList() {
		int num=0;
		String sql ="select * from as_target";
		SQLQuery query = getSession().createSQLQuery(sql);
		List orgList = query.list();
		if(orgList!=null) {
			num=orgList.size();
		}
		return num;
	}
	@Override
	public void listByParentFor(Long pk,Query<EducationSurvey> query) {
		logger.info("list<Organization>  parent.pk = {},name={}",query);

		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
		
		if(pk != null) 
			criteria.add(Restrictions.eq("parent.pk", pk));
		if(parameters != null) {
		    String[] name = parameters.get("qname");
		    if(name != null && name[0].length()>0) {
		    	criteria.add(Restrictions.like("name", "%"+name[0]+"%"));
		    }
		}
		ProjectionList ps = Projections.projectionList();

		ps.add(Projections.rowCount());
		
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		Long rowCount = (Long)o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		query.setResults(criteria.list());

	}


	@Override
	public List<EducationSurvey> selectChildrenNotLeafFor(Long parentPk) {
		logger.info("selectChildrenNotLeafFor  parent.pk = {}",parentPk);
		Criteria c = getSession().createCriteria(getEntityClass());
		if(parentPk != null) 
			c.add(Restrictions.eq("parent.pk", parentPk));
		else
			c.add(Restrictions.isNull("parent.pk"));
		c.add(Restrictions.le("type", 3));
		return c.list();
	}


	@Override
	public FSDataNnalysis getFSDataNnalysisById(Long id) {
		String sql = "SELECT * FROM fs_data_analysis WHERE id=?";
		
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, id);
		
		sqlQuery.addEntity(FSDataNnalysis.class);
		FSDataNnalysis fsdn = (FSDataNnalysis) sqlQuery.uniqueResult();
		return fsdn;
	}
	
}

