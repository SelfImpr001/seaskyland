/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cntest.common.page.Page;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamCheckin;
import com.cntest.fxpt.repository.ExamCheckinDao;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月20日
 * @version 1.0
 **/

@Repository
public class ExamCheckinDaoImpl extends AbstractHibernateDao<ExamCheckin, Long> implements ExamCheckinDao {
	private static Logger logger = LoggerFactory.getLogger(ExamCheckinDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	protected Class<ExamCheckin> getEntityClass() {
		return ExamCheckin.class;
	}

	@Override
	public void findAll(Page page) {
		Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());
		criteria.createAlias("exam", "exam",JoinType.RIGHT_OUTER_JOIN);
		if (!page.getParameter().isEmpty()) {
			if (page.hasParam("examData")) {
				criteria.add(Restrictions.sqlRestriction("examDate='"
						+ page.getString("examData") + "'"));
			}

			if (page.hasParam("examName")) {
				criteria.add(Restrictions.like("exam.name",
						"%" + page.getString("examName") + "%"));
			}
			if (page.hasParam("examSortName")) {
				criteria.add(Restrictions.like("exam.sortName",
						"%" + page.getString("examSortName") + "%"));
			}

			if (page.hasParam("status")) {
				int status = page.getInteger("status");
				if(status >0) {
				   criteria.add(Restrictions.eq("status",status));
				}else {
					criteria.add(Restrictions.isNull("status"));
				}
			}
			if (page.hasParam("hasExamStudent")) {
				criteria.add(Restrictions.eq("exam.hasExamStudent",
						page.getBoolean("hasExamStudent")));
			}
		}

		Long rowCount = (Long) criteria.setProjection(
				Projections.countDistinct("pk")).uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());

		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());
		criteria.addOrder(Order.desc("exam.examDate"));
		//criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);
		criteria.setResultTransformer(Transformers.TO_LIST);
		List<List> list = criteria.list();
		ArrayList<ExamCheckin> chechins = new ArrayList<ExamCheckin>();
		for(List os:list) {
			ExamCheckin checkin = (ExamCheckin) os.get(1);
			Exam exam = (Exam) os.get(0);
			if(checkin == null)
				checkin = new ExamCheckin(exam);
			else {
				checkin.setExam(exam);
			}
			chechins.add(checkin);
		}
		page.setList(chechins);

	}
	

	@Override
	public ExamCheckin findByExam(Exam exam) {
		return findEntityByHql("From ExamCheckin where exam.id=?", exam.getId());
	}


	@Override
	public void findExamChecked(Long examId,Page page) {
		String sql = "SELECT b.studentId,b.name,CASE WHEN b.gender = 0 THEN '--' WHEN b.gender=1 THEN '男' ELSE '女' END gender,d.schoolname,"
				+ " d.name  AS className,f.xh ,f.name AS stName, CASE WHEN f.sex = 0 THEN '--' WHEN f.sex = 1 THEN '男' ELSE '女' END sex,g.name mySchoolName FROM kn_examinee_checkin a "
				+ " LEFT JOIN dw_examstudent_fact b ON a.exam_student_fact_id = b.id   LEFT JOIN dw_dim_class d ON d.id=b.classId "
				+ " LEFT JOIN kn_student_exam e ON e.exam_student_fact_id = a.exam_student_fact_id LEFT JOIN kn_studentbase f ON f.guid=e.studentGuid "
				+ " LEFT JOIN dw_dim_school g ON g.code=f.schoolCode WHERE  a.status=9 AND b.examId=?";

		SQLQuery cq = createSQLQuery("SELECT COUNT(1) FROM kn_student_exam WHERE examId = ?");
		cq.setLong(0, examId);
		int c = Integer.valueOf(cq.uniqueResult()+"");
		page.setTotalRows(c);
		
		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
				
		SQLQuery query = createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);		
		query.addScalar("studentId");
		query.addScalar("name");
		query.addScalar("gender");
		query.addScalar("schoolname");
		query.addScalar("className");
		query.addScalar("xh");
		query.addScalar("stName");
		query.addScalar("sex");
		query.addScalar("mySchoolName");		
		query.setLong(0, examId);
		query.setFirstResult(first);
		query.setMaxResults(page.getPagesize());
		
		List<Map<String, Object>> result =  query.list();
		page.setList(result);
	}


	@Override
	public void findExamCheckedless(Long examId, Page page) {
			//,e.examStudentJiebieName as grade left join kn_exam e on e.id=b.examId
			String sql = "SELECT b.studentId,b.name,CASE WHEN b.gender = 0 THEN '--' WHEN b.gender=1 THEN '男' ELSE '女' END gender,"
					+ "d.schoolname,d.name  AS className FROM kn_examinee_checkin a LEFT JOIN dw_examstudent_fact b "
					+ " ON b.id=a.exam_student_fact_id  LEFT JOIN dw_dim_class d ON d.id=b.classId  WHERE  a.status =8 AND b.examId=?";
			
			SQLQuery cq = createSQLQuery("SELECT COUNT(1) FROM kn_examinee_checkin a LEFT JOIN kn_exam_checkin b ON b.exam_checkin_id=a.exam_checkin_id WHERE  a.status =8  AND b.examId=?");
			cq.setLong(0, examId);
			int c = Integer.valueOf(cq.uniqueResult()+"");
			page.setTotalRows(c);
			
			int first = (page.getCurpage() - 1) * page.getPagesize();
			first = first < 0 ? 0 : first;
			
			SQLQuery query = createSQLQuery(sql);	
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);				
			query.addScalar("studentId");
			query.addScalar("name");
			query.addScalar("gender");
			query.addScalar("schoolname");
			//query.addScalar("grade");
			query.addScalar("className");				
			query.setLong(0, examId);
			query.setFirstResult(first);
			query.setMaxResults(page.getPagesize());
			
			List<Map<String, Object>> result =  query.list();
			page.setList(result);
	}
	
	@Override
	public void delete(ExamCheckin checkin) {		
		executeBySQL("delete from kn_examinee_checkin where exam_checkin_id=?", checkin.getPk());
		super.delete(checkin);				
	}

	@Override
	public void deleteBy(Exam exam) {
		String hql  = "From ExamCheckin where examId=?";
		ExamCheckin checkin = findEntityByHql(hql, exam.getId());
		if(checkin != null) 
			this.delete(checkin);
	}
}

