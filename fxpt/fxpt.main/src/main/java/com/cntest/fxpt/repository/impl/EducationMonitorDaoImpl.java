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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.query.Query;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.EducationMonitor;
import com.cntest.fxpt.domain.FSDataNnalysis;
import com.cntest.fxpt.repository.EducationMonitorDao;
import com.cntest.fxpt.service.impl.EducationSurveyServiceImpl;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
@Repository("EducationMonitorDao")
public class EducationMonitorDaoImpl extends AbstractHibernateDao<EducationMonitor,Long> implements EducationMonitorDao {
	private static Logger logger = LoggerFactory.getLogger(EducationSurveyServiceImpl.class);

	@Override
	protected Class<EducationMonitor> getEntityClass() {
			return EducationMonitor.class;
	}
	@Override
	public void findMonitor(Query<EducationMonitor> query) {
		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
		if(parameters != null) {
			if(parameters.get("q")!= null) {
				String q = parameters.get("q")[0];
				criteria.createAlias("userInfo", "a")
					.add(Restrictions.or(Restrictions.like("a.realName", "%" + q + "%"), 
							Restrictions.or(Restrictions.like("a.cellphone", "%" + q + "%"),
									Restrictions.or(Restrictions.like("a.nickName", "%" + q + "%"),
											Restrictions.or(Restrictions.like("a.email", "%" + q + "%"),
													Restrictions.or(Restrictions.like("a.telphone", "%" + q + "%"),Restrictions.like("name", "%" + q + "%")))))));
				    //.add(Restrictions.or(Restrictions.like("a.nickName", "%" + q + "%"), Restrictions.like("a.cellphone", "%" + q + "%")))
				    //.add(Restrictions.or(Restrictions.like("a.email", "%" + q + "%"), Restrictions.like("a.telphone", "%" + q + "%")));
			}
		}
		
		ProjectionList ps = Projections.projectionList();
		//ps.add(Projections.distinct(Projections.property("name")));
		ps.add(Projections.rowCount());

		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		//Long rowCount = (Long)((Object[]) o)[1];
		Long rowCount = (Long)o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		criteria.addOrder(Order.desc("pk"));
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		query.setResults(criteria.list());
	}
	public void deletem(Long pk) {
		String sql ="delete from  fs_monitor where id="+pk;
		getSession().createSQLQuery(sql).executeUpdate();

	}
	public EducationMonitor get(Long pk) {
		List<EducationMonitor >  l = getSession().createQuery("from EducationMonitor where pk=?" ).setLong(0, pk).list();
		if(l.size()>0) {
			return l.get(0);
		}
		return new EducationMonitor();
	}
	public static Date getDate(String date) {
		SimpleDateFormat datefor = new SimpleDateFormat("YYYY-MM-dd");
		try {
			return datefor.parse(date);
		} catch (ParseException e) {
		}
		return new Date();
	}
	public void updatem(EducationMonitor m) {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE fs_monitor ");
		sql.append(" SET ");
		sql.append("monitorDate = ? , ");
		sql.append("semester = ? , ");
		sql.append("academicYear = ? , ");
		sql.append("analysisType = ? ,  ");
		sql.append("questionName = ? , ");
		sql.append("institutions = ? , ");
		sql.append("CreateUser = ? ,  ");
		sql.append("grade = ?   ");
		sql.append("where id = ?   ");
		SQLQuery query =  getSession().createSQLQuery(sql.toString());
		query.setDate(0, getDate(set(m.getMonitorDate())));
		query.setString(1, set(m.getSemester()));
		query.setString(2, set(m.getAcademicYear()));
		query.setString(3, set(m.getAnalysisType()));
		query.setString(4, set(m.getQuestionName())); 
		query.setString(5, set(m.getInstitutions()));
		query.setString(6, set(m.getCreateUser()));
		query.setString(7, set(m.getGrade()));
		query.setString(8, set(m.getPk()));
		query.executeUpdate();
	}
	
	public void savem(EducationMonitor m ) {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO fs_monitor( ");
		sql.append("monitorDate, ");
		sql.append("semester, ");
		sql.append("academicYear, ");
		sql.append("analysisType, ");
		sql.append("questionName, ");
		sql.append("institutions, ");
		sql.append("CreateUser, ");
		sql.append("grade, ");
		sql.append("CreateTime");
		sql.append(")");
		sql.append(" VALUES( ?,?,?,?,?,?,?,?,now() )");
		SQLQuery query =  getSession().createSQLQuery(sql.toString());
		query.setDate(0, getDate(set(m.getMonitorDate())));
		query.setString(1, set(m.getSemester()));
		query.setString(2, set(m.getAcademicYear()));
		query.setString(3, set(m.getAnalysisType()));
		query.setString(4, set(m.getQuestionName())); 
		query.setString(5, set(m.getInstitutions()));
		query.setString(6, set(m.getCreateUser()));
		query.setString(7, set(m.getGrade()));
		query.executeUpdate();
	}
	private String set(Object o ) {
		if(null == o) {
			return "";
		}
		return o+"";
	}
	//考试数据上传--结果导出增加验证
	@Override
	public void saveReportData(FSDataNnalysis fsdn) {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO fs_data_analysis( ");
		sql.append("create_time, ");
		sql.append("name, ");
		sql.append("status, ");
		sql.append("data_collection, ");
		sql.append("student, ");
		sql.append("spec_item, ");
		sql.append("score, ");
		sql.append("monitor_name, ");
		sql.append("monitor_school_year,");
		sql.append("monitor_semester, ");
		sql.append("monitor_type,");
		sql.append("monitor_date, ");
		sql.append("monitor_href");
		sql.append(")");
		sql.append(" VALUES( now(),?,?,?,?,?,?,?,?,?,?,?,? )");
		SQLQuery query =  getSession().createSQLQuery(sql.toString());
		query.setString(0,set(fsdn.getName()) );
		query.setString(1, set(fsdn.getStatus()));
		query.setString(2, set(fsdn.getDataCollection()));
		query.setString(3, set(fsdn.getStudent()));
		query.setString(4, set(fsdn.getSpecTtem())); 
		query.setString(5, set(fsdn.getScore()));
		query.setString(6, set(fsdn.getMonitorName()));
		query.setString(7, set(fsdn.getMonitorSchoolYear()));
		query.setString(8, set(fsdn.getMonitorSemester()));
		query.setString(9, set(fsdn.getMonitorType()));
		query.setDate(10, getDate(set(fsdn.getMonitorDate()))); 
		query.setString(11, set(fsdn.getMonitorHref()));
		query.executeUpdate();
	}
	
}

