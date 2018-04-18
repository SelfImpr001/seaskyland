/*
 * @(#)com.cntest.fxpt.repository.impl.AnalysisThemeDaoImpl.java	1.0 2015年6月11日:上午11:41:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.AnalysisTheme;
import com.cntest.fxpt.repository.AnalysisThemeDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 上午11:41:39
 * @version 1.0
 */
@Repository("AnalysisThemeDao")
public class AnalysisThemeDaoImpl extends
		AbstractHibernateDao<AnalysisTheme, Long> implements AnalysisThemeDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.AnalysisThemeDao#list()
	 */
	@Override
	public List<AnalysisTheme> list(int analysisDataLevel) {
		String hql = "from AnalysisTheme where available=true and analysisThemeLevel>=?";
		return findByHql(hql, analysisDataLevel);
	}

	@Override
	protected Class<AnalysisTheme> getEntityClass() {
		return AnalysisTheme.class;
	}

	@Override
	public void setSubjectAndLanguagetype(long examId) {
		//插入考试科目的语言类型
		String sql = "INSERT INTO dw_dim_exam_subjectandlanguagetype"
				+ " SELECT cj.examid,cj.subjectid subjectid,xs.languageType languageType FROM dw_examstudent_fact xs INNER JOIN dw_testpapercj_fact cj ON xs.id=cj.studentid "
				+ " WHERE cj.examid=?"
				+ " GROUP BY xs.languageType,cj.subjectid";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
		//插入省
		sql = "INSERT INTO dw_dim_exam_org "
				+ "SELECT  f.examId,o.org_id,o.p_id,o.org_type "
				+ "FROM dw_examstudent_fact f  INNER JOIN 4a_org o ON o.org_id=f.provinceId "
				+ "WHERE f.examId = ?"
				+ " GROUP BY f.provinceid";
		sqlQuery = createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
		//插入市
		sql = "INSERT INTO dw_dim_exam_org "
				+ "SELECT  f.examId,o.org_id,o.p_id,o.org_type "
				+ "FROM dw_examstudent_fact f  INNER JOIN 4a_org o ON o.org_id=f.cityId "
				+ "WHERE f.examId = ?"
				+ " GROUP BY f.cityId";
		sqlQuery = createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
		
		//插入区
		sql = "INSERT INTO dw_dim_exam_org "
				+ "SELECT  f.examId,o.org_id,o.p_id,o.org_type "
				+ "FROM dw_examstudent_fact f  INNER JOIN 4a_org o ON o.org_id=f.countyId "
				+ "WHERE f.examId = ?"
				+ " GROUP BY f.countyId";
		sqlQuery = createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
		
		
		//插入学校
		sql = "INSERT INTO dw_dim_exam_org "
				+ "SELECT  f.examId,o.org_id,o.p_id,o.org_type "
				+ "FROM dw_examstudent_fact f  INNER JOIN 4a_org o ON o.org_id=f.schoolId "
				+ "WHERE f.examId = ?"
				+ " GROUP BY f.schoolId";
		sqlQuery = createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
	}

}
