/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.report;

import java.util.Map;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年12月19日
 * @version 1.0
 **/
public class SchoolReportQuery extends AbstractReportQuery {

	private static String queryName = "com.cntest.fxpt.report.SchoolReportQuery.reportFor";
	
	private static String countQueryName = "com.cntest.fxpt.report.SchoolReportQuery.reportFor.count";
	
	@Override
	protected org.hibernate.Query createReportQuery(Map root) {
		return this.dynamicHibernateTemplate.createSQLQuery(queryName, root);
	}

	@Override
	protected org.hibernate.Query createReportCountyQuery(Map root) {
		return this.dynamicHibernateTemplate.createSQLQuery(countQueryName, root);
	}
}

