/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.RootEntityResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.EducationSurvey;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.FSDataNnalysis;
import com.cntest.fxpt.repository.EducationSurveyRepository;
import com.cntest.fxpt.repository.IAnalysisDao;
import com.cntest.fxpt.service.impl.EducationSurveyServiceImpl;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
@Repository("IAnalysisDao")
public class AnalysisDaoImpl extends AbstractHibernateDao<FSDataNnalysis,Long> implements IAnalysisDao {
	private static Logger logger = LoggerFactory.getLogger(EducationSurveyServiceImpl.class);
	@Override
	protected Class<FSDataNnalysis> getEntityClass() {
		return FSDataNnalysis.class;
	}
	@Override
	public void list(Long pk,Query<FSDataNnalysis> query) {
		logger.info("list<Organization>  parent.pk = {},name={}",query);

		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
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
	public List<FSDataNnalysis> reportgeneratelist(Page<FSDataNnalysis> page) {
		Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());

		if (!page.getParameter().isEmpty()) {
			if (page.hasParam("reportdata")) {
				Date data=new Date();
				try {
					data = new SimpleDateFormat("yyyy-MM-dd").parse(page.getString("reportdata").toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				criteria.add(Restrictions.eq("monitorDate"
						,data));
			}

			if (page.hasParam("reportyear")) {
				criteria.add(Restrictions.like("monitorSchoolYear",
						"%" + page.getString("reportyear") + "%"));
			}
			if (page.hasParam("reportsemester")) {
				criteria.add(Restrictions.like("monitorSemester",
						"%" + page.getString("reportsemester") + "%"));
			}
			if (page.hasParam("reporttype")) {
				criteria.add(Restrictions.like("monitorType",
						"%" + page.getString("reporttype") + "%"));
			}
		}
		
		criteria.add(Restrictions.isNotNull("dataCollection"));
		
		
		Long rowCount = (Long) criteria.setProjection(
				Projections.countDistinct("id")).uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());

		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());
		criteria.addOrder(Order.desc("monitorDate"));
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);

		List<FSDataNnalysis> list = criteria.list();

		page.setList(list);
		return list;
	}
	
}

