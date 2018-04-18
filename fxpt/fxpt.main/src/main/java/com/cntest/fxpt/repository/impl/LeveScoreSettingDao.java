/*
 * @(#)com.cntest.fxpt.repository.impl.LeveScoreSettingDao.java	1.0 2015年4月13日:上午9:32:07
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.LeveScoreSetting;
import com.cntest.fxpt.repository.ILeveScoreSettingDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月13日 上午9:32:07
 * @version 1.0
 */
@Repository("LeveScoreSettingDao")
public class LeveScoreSettingDao extends
		AbstractHibernateDao<LeveScoreSetting, Long> implements
		ILeveScoreSettingDao {

	@Override
	public int delete(AnalysisTestpaper analysisTestpaper) {
		String hql = "delete from LeveScoreSetting where analysisTestpaper.id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, analysisTestpaper.getId());
		return query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ILeveScoreSettingDao#add(com.cntest.fxpt.bean
	 * .LeveScoreSetting)
	 */
	@Override
	public void add(LeveScoreSetting leveScoreSetting) {
		this.save(leveScoreSetting);
	}

	@Override
	public List<LeveScoreSetting> find(Exam exam, Long atpId, int wl) {

		String tableName = "";
		switch (exam.getLevelCode()) {
		case 1:
			tableName = "dw_agg_province_scoreinfo";
			break;
		case 2:
			tableName = "dw_agg_city_scoreinfo";
			break;
		case 3:
			tableName = "dw_agg_county_scoreinfo";
			break;
		case 4:
			tableName = "dw_agg_school_scoreinfo";
			break;
		default:
			tableName = "dw_agg_province_scoreinfo";
		}

		String sql = "SELECT p.id,p.levelName,p.levelScore,p.beginScore,p.endScore,ifnull(t.skrs,0) skrs,ifnull(SUM(t.num),0) num "
				+ "FROM kn_levelscoresetting p "
				+ "LEFT JOIN "+tableName+" t ON p.analysisTestPaperId=t.testpaperId AND p.wl=t.wl AND p.beginScore<=t.score AND p.endScore>t.score "
				+ "WHERE p.analysisTestPaperId=? AND p.wl=? "
				+ "GROUP BY p.beginScore ORDER BY p.beginScore DESC";

		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.addScalar("id", StandardBasicTypes.LONG);
		sqlQuery.addScalar("levelName", StandardBasicTypes.STRING);
		sqlQuery.addScalar("levelScore", StandardBasicTypes.DOUBLE);
		sqlQuery.addScalar("beginScore", StandardBasicTypes.DOUBLE);
		sqlQuery.addScalar("endScore", StandardBasicTypes.DOUBLE);
		sqlQuery.addScalar("skrs", StandardBasicTypes.INTEGER);
		sqlQuery.addScalar("num", StandardBasicTypes.INTEGER);

		sqlQuery.setLong(0, atpId);
		sqlQuery.setLong(1, wl);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List<Map<String, Object>> resultList = sqlQuery.list();
		ArrayList<LeveScoreSetting> result = new ArrayList<>();
		int skrs = 0;
		for (Map<String, Object> row : resultList) {
			skrs = (Integer) row.get("skrs");
			if (skrs > 0) {
				break;
			}
		}

		for (Map<String, Object> row : resultList) {
			LeveScoreSetting lss = new LeveScoreSetting();
			lss.setId((Long) row.get("id"));
			lss.setLevelName((String) row.get("levelName"));
			lss.setLevelScore((Double) row.get("levelScore"));
			lss.setBeginScore((Double) row.get("beginScore"));
			lss.setEndScore((Double) row.get("endScore"));
			int num = (Integer) row.get("num");
			lss.setSkrs(skrs);
			lss.setNum(num);
			result.add(lss);
		}
		return result;
	}

	@Override
	public List<LeveScoreSetting> find(Exam exam) {
		String hql = "from LeveScoreSetting as lss where lss.exam.id=? order by lss.beginScore desc ";
		Query query = getSession().createQuery(hql);
		query.setLong(0, exam.getId());
		return query.list();
	}

	@Override
	protected Class<LeveScoreSetting> getEntityClass() {
		// TODO Auto-generated method stub
		return LeveScoreSetting.class;
	}

}
