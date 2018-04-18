/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.common.query.Query;
import com.cntest.foura.domain.User;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.security.UserDetails;

/** 
 * <pre>
 * 学生或者考生报告查询
 * </pre>
 *  
 * @author 李贵庆2014年12月19日
 * @version 1.0
 **/
public class StudentReportQuery extends AbstractReportQuery {
	private static Logger logger = LoggerFactory.getLogger(StudentReportQuery.class);

	private static final String studentKey = "student";
	
	private static final String examineeKey = "examinee";
	
	private static final String studentQueryName = "com.cntest.fxpt.anlaysis.repository.impl.ReportExamDao.selectStudentExamsFromStudentBase";
	
	private static final String countStudentQueryName = "com.cntest.fxpt.anlaysis.repository.impl.ReportExamDao.selectStudentExamsFromStudentBase.count";
	
	private static final String examineeQueryName = "com.cntest.fxpt.anlaysis.repository.impl.ReportExamDao.selectStudentExamsFromExamStudent";
	
	private static final String countExamineeQueryName = "com.cntest.fxpt.anlaysis.repository.impl.ReportExamDao.selectStudentExamsFromExamStudent.count";
	
	@Override
	protected org.hibernate.Query createReportQuery(Map root) {
		if(root.get(studentKey)!= null)
		    return this.dynamicHibernateTemplate.createSQLQuery(studentQueryName, root);
		else
			return this.dynamicHibernateTemplate.createSQLQuery(examineeQueryName, root);
	}

	@Override
	protected org.hibernate.Query createReportCountyQuery(Map root) {
		if(root.get(studentKey)!= null)
		    return this.dynamicHibernateTemplate.createSQLQuery(countStudentQueryName, root);
		else
			return this.dynamicHibernateTemplate.createSQLQuery(countExamineeQueryName, root);
	}

	@Override
	public List<Exam> querySameTermExamReport(UserDetails user, Exam exam) {
		ArrayList<Exam> exams = new ArrayList<Exam>();				
		exams.add(exam);		
		return exams;
	}
	
	@Override
	public List<Exam> queryFor(UserDetails user, Query<Exam> query) {
		logger.debug("Query Report For User {}", user.getUserName());

		String key = studentKey;
		if( user.getOrigin() instanceof ExamStudent)
			key = examineeKey;
		
		Map root = createRoot(key,user.getOrigin());
		addParamsToRoot(root,query.getParameters());
		return doQuery(root,query);
	}	
}

