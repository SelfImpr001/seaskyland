/*
 * @(#)com.cntest.fxpt.repository.impl.SaveCalcluateResultDaoImpl.java	1.0 2014年11月28日:下午2:45:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.anlaysis.bean.ScoreInfo;
import com.cntest.fxpt.anlaysis.bean.TargetResult;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.repository.ICalcluateResultDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 下午2:45:10
 * @version 1.0
 */
@Repository("ICalcluateResultDao")
public class CalcluateResultDaoImpl extends AbstractHibernateDao<Exam, Long>
		implements ICalcluateResultDao {

	@Override
	public void deleteResult(Long examId, Long objId, Long testPaperId,
			String tableName, int wl) {
		String sql = "DELETE FROM " + tableName
				+ " WHERE objId=? AND testpaperId=? and wl=?";
		SQLQuery query = getSession().createSQLQuery(sql);
		int idx = 0;
		query.setParameter(idx++, objId);
		query.setParameter(idx++, testPaperId);
		query.setParameter(idx++, wl);
		query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ISaveCalcluateResultDao#saveClassResult(java
	 * .lang.Long, java.lang.Long, java.util.List)
	 */
	@Override
	public void saveTotalScoreResult(Long examId, Long objId, Long subjectId,
			Long testPaperId, List<TargetResult> targetResults,
			String tableName, int wl) {
		StringBuffer sql1 = new StringBuffer("insert into " + tableName
				+ "(examId,objId,subjectId,testpaperId,wl");
		StringBuffer sql2 = new StringBuffer("VALUES(?,?,?,?,?");

		for (TargetResult tr : targetResults) {
			sql1.append("," + tr.getName());
			sql2.append(",?");
		}
		sql1.append(")  ");
		sql2.append(")  ");

		SQLQuery query = getSession().createSQLQuery(
				sql1.toString() + sql2.toString());
		int idx = 0;
		query.setParameter(idx++, examId);
		query.setParameter(idx++, objId);
		query.setParameter(idx++, subjectId);
		query.setParameter(idx++, testPaperId);
		query.setParameter(idx++, wl);
		for (TargetResult tr : targetResults) {
			query.setParameter(idx++, tr.getValue());
		}
		query.executeUpdate();
	}

	@Override
	public void saveScoreInfo(Long examId, Long objId, Long subjectId,
			Long testPaperId, int skrs, ScoreInfo scoreInfo, String tableName,
			int wl) {
		String sql = "INSERT INTO "
				+ tableName
				+ "(examId,objId,subjectId,testpaperId,skrs,score,rank,num,wl) VALUES(?,?,?,?,?,?,?,?,?)";
		SQLQuery query = getSession().createSQLQuery(sql);
		int idx = 0;
		query.setParameter(idx++, examId);
		query.setParameter(idx++, objId);
		query.setParameter(idx++, subjectId);
		query.setParameter(idx++, testPaperId);
		query.setParameter(idx++, skrs);
		query.setParameter(idx++, scoreInfo.getScore().getValue());
		query.setParameter(idx++, scoreInfo.getRank());
		query.setParameter(idx++, scoreInfo.getNum());
		query.setParameter(idx++, wl);
		query.executeUpdate();
	}

	@Override
	public void saveItem(Long examId, Long objId, Long subjectId,
			Long testPaperId, Long itemId, List<TargetResult> targetResults,
			String tableName, int wl) {
		StringBuffer sql1 = new StringBuffer("insert into " + tableName
				+ "(examId,objId,subjectId,testpaperId,itemId,wl");
		StringBuffer sql2 = new StringBuffer("VALUES(?,?,?,?,?,?");

		for (TargetResult tr : targetResults) {
			sql1.append("," + tr.getName());
			sql2.append(",?");
		}
		sql1.append(")  ");
		sql2.append(")  ");

		SQLQuery query = getSession().createSQLQuery(
				sql1.toString() + sql2.toString());
		int idx = 0;
		query.setParameter(idx++, examId);
		query.setParameter(idx++, objId);
		query.setParameter(idx++, subjectId);
		query.setParameter(idx++, testPaperId);
		query.setParameter(idx++, itemId);
		query.setParameter(idx++, wl);
		for (TargetResult tr : targetResults) {
			query.setParameter(idx++, tr.getValue());
		}
		query.executeUpdate();
	}

	@Override
	public void saveItemGroup(Long examId, Long objId, Long subjectId,
			Long testPaperId, List<TargetResult> targetResults,
			String tableName, int wl) {
		StringBuffer sql1 = new StringBuffer("insert into " + tableName
				+ "(examId,objId,subjectId,testpaperId,wl");
		StringBuffer sql2 = new StringBuffer("VALUES(?,?,?,?,?");

		for (TargetResult tr : targetResults) {
			sql1.append("," + tr.getName());
			sql2.append(",?");
		}
		sql1.append(")  ");
		sql2.append(")  ");

		SQLQuery query = getSession().createSQLQuery(
				sql1.toString() + sql2.toString());
		int idx = 0;
		query.setParameter(idx++, examId);
		query.setParameter(idx++, objId);
		query.setParameter(idx++, subjectId);
		query.setParameter(idx++, testPaperId);
		query.setParameter(idx++, wl);
		for (TargetResult tr : targetResults) {
			query.setParameter(idx++, tr.getValue());
		}
		query.executeUpdate();
	}

	@Override
	protected Class<Exam> getEntityClass() {
		return Exam.class;
	}

	@Override
	public void clearAnalysisResult(Long examId, Long analysisTestpaperId) {
		String sql = "{Call P_delete_analysisTestpaper_result(?,?)}";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.setLong(1, analysisTestpaperId);
		sqlQuery.executeUpdate();
	}

	@Override
	public void clearAnalysisResult(Long examId) {
		String sql = "{Call P_clearExamAnalysisResult(?)}";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
	}

	@Override
	public void clearAnalysisResultForXinjiang(Long examId) {
		String sql = "{Call P_clearExamAnalysisResultForXinjiang(?)}";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
	}

	@Override
	public List<Map> getZfCreateRule(Long examId) {
		String sql = "SELECT DISTINCT xs.learLanguage, xs.languagePattern,xs.studentType,sjcj.testPaperId "
				+ "FROM dw_examstudent_fact xs INNER JOIN dw_testpapercj_fact sjcj ON  xs.id=sjcj.studentId "
				+ "INNER JOIN dw_dim_analysis_testpaper sj ON sjcj.analysisTestpaperId=sj.id "
				+ "WHERE xs.examId=? AND sj.isComposite=0 "
				+ "ORDER BY xs.learLanguage,sjcj.analysisTestpaperId";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);

		sqlQuery.addScalar("learLanguage", StringType.INSTANCE)
				.addScalar("languagePattern", StringType.INSTANCE)
				.addScalar("studentType", StringType.INSTANCE)
				.addScalar("testPaperId", LongType.INSTANCE);
		sqlQuery.setLong(0, examId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}

	@Override
	public List<Map> getZfCreateRule(String sql) {
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}

}
