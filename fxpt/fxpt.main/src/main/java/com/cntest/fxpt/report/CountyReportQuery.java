/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.report;

import java.util.Map;

/** 
 * <pre>
 * 区县报告查询
 * </pre>
 *  
 * @author 李贵庆2014年12月19日
 * @version 1.0
 **/
public class CountyReportQuery extends AbstractReportQuery {

	private static String queryName = "com.cntest.fxpt.report.CountyReport.reportFor";
	
	private static String countQueryName = "com.cntest.fxpt.report.CountyReport.reportFor.count";

	@Override
	protected org.hibernate.Query createReportQuery(Map root) {
		return this.dynamicHibernateTemplate.createSQLQuery(queryName, root);
	}

	@Override
	protected org.hibernate.Query createReportCountyQuery(Map root) {
		return this.dynamicHibernateTemplate.createSQLQuery(countQueryName, root);
	}
	
//	@Override
//	public List<Exam> queryFor(UserDetails user, Query<Exam> query) {
//		List<UserOrg> orgs = this.userDetailsService.findUserOrgs(user);
//		if(orgs == null || orgs.size() == 0)
//			return null;
//		Map root = createOrgsRoot(orgs,query.getParameters());
//		
//		org.hibernate.Query sqlQuery = this.dynamicHibernateTemplate.createSQLQuery(queryName, root);
//		setParameters(sqlQuery,(Map)root.get(PARAMS_KEY));
//		
//		org.hibernate.Query countSqlQuery = this.dynamicHibernateTemplate.createSQLQuery(countQueryName, root);
//		setParameters(countSqlQuery,(Map)root.get(PARAMS_KEY));
//		
//		
//		BigInteger countNum = (BigInteger) countSqlQuery.uniqueResult();
//		query.setTotalRows(countNum.intValue());
//		
//		sqlQuery.setFirstResult(query.getStartRow());
//		sqlQuery.setMaxResults(query.getPagesize());
//		
//		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//		List<Map<String, Object>> list = sqlQuery.list();
//
//		ArrayList<Exam> exams = new ArrayList<Exam>();
//		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
//		for (Map<String, Object> row : list) {
//			exams.add(createExam(row, gradeMap));
//		}
//		query.setResults(exams);
//		return exams;
//
//	}

}

