package com.cntest.fxpt.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.RootEntityResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.cntest.common.page.Page;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.repository.IDownLoadReportDao;

@Repository("IDownLoadReportDao")
public class DownLoadReportDaoImpl extends AbstractHibernateDao<Exam, Long> implements
		IDownLoadReportDao {

	@Override
	protected Class<Exam> getEntityClass() {
		return Exam.class;
	}

	@Override
	public Exam findById(Long examId) {
		return this.get(examId);
	}

	@Override
	public List<Exam> list(Page<Exam> page) {

		Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());

		if (!page.getParameter().isEmpty()) {
			if (page.hasParam("examData")) {
				criteria.add(Restrictions.sqlRestriction("examDate='"
						+ page.getString("examData") + "'"));
			}

			if (page.hasParam("examName")) {
				criteria.add(Restrictions.like("name",
						"%" + page.getString("examName") + "%"));
			}
			if (page.hasParam("examSortName")) {
				criteria.add(Restrictions.like("sortName",
						"%" + page.getString("examSortName") + "%"));
			}
			if (page.hasParam("schoolYear")) {
				criteria.add(Restrictions.like("schoolYearName",
						"%" + page.getString("schoolYear") + "%"));
			}
			if (page.hasParam("schoolTerm")) {
				criteria.add(Restrictions.eq("schoolTerm",
						page.getInteger("schoolTerm")));
			}
			if (page.hasParam("grade")) {
				criteria.add(Restrictions.eq("grade.id", page.getLong("grade")));
			}
			if (page.hasParam("examType")) {
				criteria.add(Restrictions.eq("examType.id",
						page.getLong("examType")));
			}
			if (page.hasParam("examStatus")) {
				criteria.add(Restrictions.eq("status",
						page.getInteger("examStatus")));
			}

			if (page.hasParam("hasExamStudent")) {
				criteria.add(Restrictions.eq("hasExamStudent",
						page.getBoolean("hasExamStudent")));
			}

			if (page.hasParam("hasItem")) {
				Boolean tmp = page.getBoolean("hasItem");
				if (tmp) {
					criteria.add(Restrictions.gt("impItemCount", 0));
				} else {
					criteria.add(Restrictions.eq("impItemCount", 0));
				}
			}

			if (page.hasParam("hasCj")) {
				Boolean tmp = page.getBoolean("hasCj");
				if (tmp) {
					criteria.add(Restrictions.gt("impCjCount", 0));
				} else {
					criteria.add(Restrictions.eq("impCjCount", 0));
				}
			}

			if (page.hasParam("isCjList")) {
				Boolean tmp = page.getBoolean("isCjList");
				if (tmp) {
					criteria.add(Restrictions.eq("hasExamStudent", true));
					criteria.add(Restrictions.gt("impItemCount", 0));
				}
			}
			if (page.hasParam("isParamList")) {
				Boolean tmp = page.getBoolean("isParamList");
				if (tmp) {
					criteria.add(Restrictions.eq("hasExamStudent", true));
					criteria.add(Restrictions.eqProperty("impItemCount",
							"impCjCount"));
					criteria.add(Restrictions.gt("impItemCount", 0));
				}
			}
			if (page.hasParam("studentBaseStatus")) {
				criteria.add(Restrictions.eq("studentBaseStatus",
						page.getInteger("studentBaseStatus")));
			}
		}

		Long rowCount = (Long) criteria.setProjection(
				Projections.countDistinct("id")).uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());

		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());
		criteria.addOrder(Order.desc("examDate"));
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);

		List<Exam> list = criteria.list();

		page.setList(list);
		return list;
	}

	@Override
	public String[] findExamNations(Exam exam) {
		String sql = "SELECT DISTINCT xs.nation FROM dw_examstudent_fact xs INNER JOIN dw_dim_school xx ON xs.schoolId=xx.id WHERE xs.examId=?";
		SQLQuery query = createSQLQuery(sql);
		query.setParameter(0, exam.getId());
		//query.setResultTransformer(Transformers.TO_LIST);
		List list = query.list();
		String[] nations = {};
		if(list!= null && list.size() > 0) {
			nations = new String[list.size()];
			for(int i=0;i<list.size();i++) {
				nations[i] = list.get(i) +"";
			}
		}
		return nations;
	}
	
	@Override
	public int getSingleMaxScore(Long examId){
		String sql = "SELECT MAX(fullscore) FROM dw_dim_analysis_testpaper WHERE examid = ? and subjectid!=98";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, examId);
		//Object obj = sqlQuery.uniqueResult();
		Object obj = sqlQuery.uniqueResult();
		int result = 150;
		if(obj!=null){
			result = (new Double(obj.toString())).intValue();
		}
		return result;
	}
	
	@Override
	public Map<String, List<?>> produceHoleData(Long examId) {
		//获取总分分数段
		Map<String, List<Map<String, Integer>>> scoreSegments = gettotalscoreSegment(examId);
		
		Map<String, List<?>> holeSheetDataMap = new HashMap<String, List<?>>();
		if(examId!=null){
			holeSheetDataMap.put("provicedata", getHoleProvinceData(examId));
			holeSheetDataMap.put("citydata", getHoleCityData(examId));
			holeSheetDataMap.put("areadata", getHoleAreaData(examId));
			holeSheetDataMap.put("scoredata", getTotalScoreData(examId,"no","no",scoreSegments.get("unaccumulation")));//总分不累计
			holeSheetDataMap.put("totalScoredata", getTotalScoreData(examId,"yes","no",scoreSegments.get("accumulation")));//总分累计
			holeSheetDataMap.put("singleScoredata", getTotalScoreData(examId,"no","yes",scoreSegments.get("unaccumulation")));//单科不累计
			holeSheetDataMap.put("singleTotalScoredata", getTotalScoreData(examId,"yes","yes",scoreSegments.get("accumulation")));//单科累计
			holeSheetDataMap.put("knowledgeData", getKnowledgeData(examId));
			holeSheetDataMap.put("subjectiveObjectiveData", getSubjectiveObjectiveData(examId));
		}
		return holeSheetDataMap;
	}
	
	@Override
	public Map<String, List<Map<String, Integer>>> gettotalscoreSegment(Long examId){
		Map<String, List<Map<String, Integer>>> scoreSegments = new HashMap<String, List<Map<String, Integer>>>();
		try {
			String sql = "SELECT MAX(fullscore) FROM dw_dim_analysis_testpaper WHERE examid = ?";
			SQLQuery sqlQuery = createSQLQuery(sql);
			sqlQuery.setParameter(0, examId);
			//Object obj = sqlQuery.uniqueResult();
			Object obj = sqlQuery.uniqueResult();
			int result = 750;
			if(obj!=null){
				result = (new Double(obj.toString())).intValue();
			}
			List<Map<String, Integer>> segmentMapList = new ArrayList<Map<String, Integer>>();//不累计
			List<Map<String, Integer>> totalSegmentMapList = new ArrayList<Map<String, Integer>>();//累计
			Map<String, Integer> segmentMap = null;
			Map<String, Integer> totalSegmentMap = null;
			int tmpscore = result;
			int totaltmpscore = result;
			for (int i = 0; i < result/10; i++) {
				segmentMap = new HashMap<String, Integer>();
				segmentMap.put("Startscore", tmpscore);
				segmentMap.put("Endscore", tmpscore = tmpscore-10);
				segmentMapList.add(segmentMap);
				totalSegmentMap = new HashMap<String, Integer>();
				totalSegmentMap.put("Startscore", result);
				totalSegmentMap.put("Endscore", totaltmpscore = totaltmpscore-10);
				totalSegmentMapList.add(totalSegmentMap);
			}
			scoreSegments.put("accumulation", totalSegmentMapList);
			scoreSegments.put("unaccumulation", segmentMapList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scoreSegments;
	}
	
	public List getHoleProvinceData(Long examId){
		StringBuilder sql = new StringBuilder("SELECT a.provinceId,a.testpaperId,a.languageType,");
		sql.append("a.provinceName,a.testpaperName,skrs,FORMAT (avgs,2) avgs,FORMAT (stds,2) stds,");
		sql.append("FORMAT (cyxs,2) cyxs,maxs,mins,qj,medians,modes,FORMAT (kurtosis,2) kurtosis,");
		sql.append("FORMAT (skewness,2) skewness,quartile25,quartile50,quartile75,");
		sql.append("IFNULL(yxrs,0) yxrs,FORMAT ((b.yxrs/a.skrs)*100,2) yxl,");
		sql.append("IFNULL(lhrs,0) lhrs,FORMAT ((b.lhrs/a.skrs)*100,2) lhl,");
		sql.append("IFNULL(jgrs,0) jgrs,FORMAT ((b.jgrs/a.skrs)*100,2) jgl,");
		sql.append("IFNULL(jbhgrs,0) jbhgrs,FORMAT ((b.jbhgrs/a.skrs)*100,2) jbhgl,");
		sql.append("IFNULL(dfrs,0) dfrs,FORMAT ((b.dfrs/a.skrs)*100,2) dfl");
		
		sql.append(" FROM ( ");
		
		sql.append("SELECT province.org_id provinceId,");
		sql.append("tj.testpaperId testpaperId,");
		sql.append("tj.languageType languageType,");
		sql.append("province.org_name provinceName,");
		sql.append("sj.NAME testpaperName,");
		sql.append("tj.skrs skrs,");
		sql.append("tj.avgs avgs,");
		sql.append("tj.stds stds,");
		sql.append("tj.stds/tj.avgs cyxs,");
		sql.append("tj.maxs maxs,");
		sql.append("tj.mins mins,");
		sql.append("tj.maxs-tj.mins qj,");
		sql.append("tj.medians medians,");
		sql.append("tj.modes modes,");
		sql.append("tj.kurtosis kurtosis,");
		sql.append("tj.skewness skewness,");
		sql.append("tj.quartile25 quartile25,");
		sql.append("tj.quartile50 quartile50,");
		sql.append("tj.quartile75 quartile75 ");
		sql.append("FROM dw_agg_xinjiang_totalscore tj ");
		sql.append("INNER JOIN dw_dim_analysis_testpaper sj ON tj.testpaperId=sj.id ");
		sql.append("INNER JOIN 4a_org province ON tj.objId=province.org_id ");
		sql.append("WHERE tj.examId= :examid AND tj.scoreType=1 AND (tj.objType=1 OR tj.objType=2) ");
		sql.append("ORDER BY tj.languageType,sj.`NAME` DESC,provinceId ) a ");
		
		sql.append(" LEFT JOIN (");
		
		sql.append("SELECT province.org_id provinceId,");
		sql.append("tj.testpaperId testpaperId,");
		sql.append("tj.languageType languageType,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'yxl' AND kp.examId= :examid) n),tj.num,0)) yxrs,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'lhl' AND kp.examId= :examid) n),tj.num,0)) lhrs,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'jgl' AND kp.examId= :examid) n),tj.num,0)) jgrs,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'jbhgl' AND kp.examId= :examid) n),tj.num,0)) jbhgrs,");
		sql.append("SUM(IF(tj.score<sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'dfl' AND kp.examId= :examid) n),tj.num,0)) dfrs");
		sql.append(" FROM dw_agg_xinjiang_segment tj ");
		sql.append("INNER JOIN dw_dim_analysis_testpaper sj ON tj.testpaperId=sj.id ");
		sql.append("INNER JOIN 4a_org province ON tj.objId=province.org_id ");
		sql.append("WHERE tj.examId= :examid AND (tj.objType=1 OR tj.objType=2) ");
		sql.append("GROUP BY province.org_id,tj.languageType,tj.testpaperId ");
		
		sql.append(") b ON a.provinceId=b.provinceId AND a.testpaperId=b.testpaperId AND a.languageType=b.languageType");
		
		SQLQuery query = createSQLQuery(sql.toString());
		query.setParameter("examid", examId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> list = query.list();
		return list;
	}

	public List getHoleCityData(Long examId){
		StringBuilder sql = new StringBuilder("SELECT a.provinceId,a.testpaperId,a.languageType,a.dz,a.org_type,");
		sql.append("a.provinceName,a.testpaperName,skrs,FORMAT (avgs,2) avgs,FORMAT (stds,2) stds,");
		sql.append("FORMAT (cyxs,2) cyxs,maxs,mins,qj,medians,modes,FORMAT (kurtosis,2) kurtosis,");
		sql.append("FORMAT (skewness,2) skewness,quartile25,quartile50,quartile75,");
		sql.append("IFNULL(yxrs,0) yxrs,FORMAT ((b.yxrs/a.skrs)*100,2) yxl,");
		sql.append("IFNULL(lhrs,0) lhrs,FORMAT ((b.lhrs/a.skrs)*100,2) lhl,");
		sql.append("IFNULL(jgrs,0) jgrs,FORMAT ((b.jgrs/a.skrs)*100,2) jgl,");
		sql.append("IFNULL(jbhgrs,0) jbhgrs,FORMAT ((b.jbhgrs/a.skrs)*100,2) jbhgl,");
		sql.append("IFNULL(dfrs,0) dfrs,FORMAT ((b.dfrs/a.skrs)*100,2) dfl");
		
		sql.append(" FROM ( ");
		
		sql.append("SELECT province.org_id provinceId,");
		sql.append("tj.testpaperId testpaperId,");
		sql.append("tj.languageType languageType,");
		sql.append("(IF((SELECT org.org_type FROM 4a_org org WHERE org.org_id=province.p_id)=2,(SELECT org.org_name FROM 4a_org org WHERE org.org_id=province.p_id),province.org_name)) dz,");
		sql.append("(IF((SELECT org.org_type FROM 4a_org org WHERE org.org_id=province.p_id)=2,province.org_name,'_全地州')) provinceName,");
		sql.append("sj.NAME testpaperName,");
		sql.append("province.org_type org_type,");
		sql.append("tj.skrs skrs,");
		sql.append("tj.avgs avgs,");
		sql.append("tj.stds stds,");
		sql.append("tj.stds/tj.avgs cyxs,");
		sql.append("tj.maxs maxs,");
		sql.append("tj.mins mins,");
		sql.append("tj.maxs-tj.mins qj,");
		sql.append("tj.medians medians,");
		sql.append("tj.modes modes,");
		sql.append("tj.kurtosis kurtosis,");
		sql.append("tj.skewness skewness,");
		sql.append("tj.quartile25 quartile25,");
		sql.append("tj.quartile50 quartile50,");
		sql.append("tj.quartile75 quartile75 ");
		sql.append("FROM dw_agg_xinjiang_totalscore tj ");
		sql.append("INNER JOIN dw_dim_analysis_testpaper sj ON tj.testpaperId=sj.id ");
		sql.append("INNER JOIN 4a_org province ON tj.objId=province.org_id ");
		sql.append("WHERE tj.examId= :examid AND tj.scoreType=1 AND (tj.objType=3 OR tj.objType=2) ");
		sql.append("ORDER BY tj.languageType,sj.`NAME` DESC,dz,org_type ) a ");
		
		sql.append(" LEFT JOIN (");
		
		sql.append("SELECT province.org_id provinceId,");
		sql.append("tj.testpaperId testpaperId,");
		sql.append("tj.languageType languageType,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'yxl' AND kp.examId= :examid) n),tj.num,0)) yxrs,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'lhl' AND kp.examId= :examid) n),tj.num,0)) lhrs,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'jgl' AND kp.examId= :examid) n),tj.num,0)) jgrs,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'jbhgl' AND kp.examId= :examid) n),tj.num,0)) jbhgrs,");
		sql.append("SUM(IF(tj.score<sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'dfl' AND kp.examId= :examid) n),tj.num,0)) dfrs");
		sql.append(" FROM dw_agg_xinjiang_segment tj ");
		sql.append("INNER JOIN dw_dim_analysis_testpaper sj ON tj.testpaperId=sj.id ");
		sql.append("INNER JOIN 4a_org province ON tj.objId=province.org_id ");
		sql.append("WHERE tj.examId= :examid AND (tj.objType=3 OR tj.objType=2) ");
		sql.append("GROUP BY province.org_id,tj.languageType,tj.testpaperId ");
		
		sql.append(") b ON a.provinceId=b.provinceId AND a.testpaperId=b.testpaperId AND a.languageType=b.languageType");
		
		SQLQuery query = createSQLQuery(sql.toString());
		query.setParameter("examid", examId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> list = query.list();
		return list;
	}
	
	public List getHoleAreaData(Long examId){
		StringBuilder sql = new StringBuilder("SELECT a.schoolId,a.testpaperId,a.languageType,a.dz,a.org_type,");
		sql.append("a.provinceName,a.testpaperName,skrs,FORMAT (avgs,2) avgs,FORMAT (stds,2) stds,");
		sql.append("FORMAT (cyxs,2) cyxs,maxs,mins,qj,medians,modes,FORMAT (kurtosis,2) kurtosis,");
		sql.append("FORMAT (skewness,2) skewness,quartile25,quartile50,quartile75,");
		sql.append("IFNULL(yxrs,0) yxrs,FORMAT ((b.yxrs/a.skrs)*100,2) yxl,");
		sql.append("IFNULL(lhrs,0) lhrs,FORMAT ((b.lhrs/a.skrs)*100,2) lhl,");
		sql.append("IFNULL(jgrs,0) jgrs,FORMAT ((b.jgrs/a.skrs)*100,2) jgl,");
		sql.append("IFNULL(jbhgrs,0) jbhgrs,FORMAT ((b.jbhgrs/a.skrs)*100,2) jbhgl,");
		sql.append("IFNULL(dfrs,0) dfrs,FORMAT ((b.dfrs/a.skrs)*100,2) dfl");
		
		sql.append(" FROM ( ");
		
		sql.append("SELECT school.org_id schoolId,");
		sql.append("tj.testpaperId testpaperId,");
		sql.append("tj.languageType languageType,");
		sql.append("(IF((SELECT org.org_type FROM 4a_org org WHERE org.org_id=school.p_id)=3,(SELECT org.org_name FROM 4a_org org WHERE org.org_id=school.p_id),school.org_name)) dz,");
		sql.append("(IF((SELECT org.org_type FROM 4a_org org WHERE org.org_id=school.p_id)=3,school.org_name,'_全县')) provinceName,");
		sql.append("sj.NAME testpaperName,");
		sql.append("school.org_type org_type,");
		sql.append("tj.skrs skrs,");
		sql.append("tj.avgs avgs,");
		sql.append("tj.stds stds,");
		sql.append("tj.stds/tj.avgs cyxs,");
		sql.append("tj.maxs maxs,");
		sql.append("tj.mins mins,");
		sql.append("tj.maxs-tj.mins qj,");
		sql.append("tj.medians medians,");
		sql.append("tj.modes modes,");
		sql.append("tj.kurtosis kurtosis,");
		sql.append("tj.skewness skewness,");
		sql.append("tj.quartile25 quartile25,");
		sql.append("tj.quartile50 quartile50,");
		sql.append("tj.quartile75 quartile75 ");
		sql.append("FROM dw_agg_xinjiang_totalscore tj ");
		sql.append("INNER JOIN dw_dim_analysis_testpaper sj ON tj.testpaperId=sj.id ");
		sql.append("INNER JOIN 4a_org school ON tj.objId=school.org_id ");
		sql.append("WHERE tj.examId= :examid AND tj.scoreType=1 AND (tj.objType=3 OR tj.objType=4) ");
		sql.append("ORDER BY tj.languageType,sj.`NAME` DESC,dz,org_type ) a ");
		
		sql.append(" LEFT JOIN (");
		
		sql.append("SELECT school.org_id provinceId,");
		sql.append("tj.testpaperId testpaperId,");
		sql.append("tj.languageType languageType,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'yxl' AND kp.examId= :examid) n),tj.num,0)) yxrs,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'lhl' AND kp.examId= :examid) n),tj.num,0)) lhrs,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'jgl' AND kp.examId= :examid) n),tj.num,0)) jgrs,");
		sql.append("SUM(IF(tj.score>=sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'jbhgl' AND kp.examId= :examid) n),tj.num,0)) jbhgrs,");
		sql.append("SUM(IF(tj.score<sj.fullScore*(SELECT n.paramValue/100 FROM (SELECT kp.paramValue FROM kn_parameters kp WHERE kp.paramName = 'dfl' AND kp.examId= :examid) n),tj.num,0)) dfrs");
		sql.append(" FROM dw_agg_xinjiang_segment tj ");
		sql.append("INNER JOIN dw_dim_analysis_testpaper sj ON tj.testpaperId=sj.id ");
		sql.append("INNER JOIN 4a_org school ON tj.objId=school.org_id ");
		sql.append("WHERE tj.examId= :examid AND (tj.objType=3 OR tj.objType=4) ");
		sql.append("GROUP BY school.org_id,tj.languageType,tj.testpaperId ");
		
		sql.append(") b ON a.schoolId=b.provinceId AND a.testpaperId=b.testpaperId AND a.languageType=b.languageType");
		
		SQLQuery query = createSQLQuery(sql.toString());
		query.setParameter("examid", examId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> list = query.list();
		return list;
	}
	
	public List getTotalScoreData(Long examId,String ifTotalOrNot,String ifSingleOrNot,List<Map<String, Integer>> scoreSegment){
		StringBuilder sql = new StringBuilder("SELECT * FROM (");
		sql.append("SELECT city.org_id cityId,");
		sql.append("county.`org_id` countyId,");
		sql.append("tj.`testpaperId` testpaperId,");
		sql.append("tj.`languageType` languageType,");
		sql.append("city.`org_name` cityName,");
		sql.append("(IF((SELECT org.org_type FROM 4a_org org WHERE org.org_id=county.p_id)=2,county.org_name,'_全地州')) countyName,");
		sql.append("sj.`NAME` testpaperName,");
		sql.append("sj.`fullScore` fullscore,");
		sql.append("tj.`skrs` skrs,");
		sql.append("yes".equals(ifTotalOrNot)?getTotalScoreParagraphSql(scoreSegment):getScoreParagraphSql(scoreSegment));
		sql.append(" FROM `dw_agg_xinjiang_segment` tj");
		sql.append(" INNER JOIN `dw_dim_analysis_testpaper` sj ON tj.`testpaperId`=sj.`id`");
		sql.append(" INNER JOIN `4a_org` county ON tj.`objId`=county.`org_id`");
		sql.append(" INNER JOIN `4a_org` city ON county.`p_id`=city.`org_id`");
		if("yes".equals(ifSingleOrNot)){
			sql.append(" WHERE tj.`examId`= :examid AND tj.`objType`=3 AND sj.subjectId!=98");
		}else{
			sql.append(" WHERE tj.`examId`= :examid AND tj.`objType`=3 AND sj.subjectId=98");
		}
		sql.append(" GROUP BY county.org_id,tj.`languageType`,tj.testpaperId");
		sql.append(" UNION ALL SELECT city.org_id cityId,");
		sql.append("1 countyId,tj.`testpaperId` testpaperId,tj.`languageType` languageType,");
		sql.append("city.`org_name` cityName,'_全地州' countyName,");
		sql.append("sj.`NAME` testpaperName,sj.`fullScore`,tj.`skrs` skrs,");
		sql.append("yes".equals(ifTotalOrNot)?getTotalScoreParagraphSql(scoreSegment):getScoreParagraphSql(scoreSegment));
		sql.append("FROM `dw_agg_xinjiang_segment` tj");
		sql.append(" INNER JOIN `dw_dim_analysis_testpaper` sj ON tj.`testpaperId`=sj.`id`");
		sql.append(" INNER JOIN `4a_org` city ON tj.`objId`=city.`org_id`");
		if("yes".equals(ifSingleOrNot)){
			sql.append(" WHERE tj.`examId`= :examid AND tj.`objType`=2 AND sj.subjectId!=98");
		}else{
			sql.append(" WHERE tj.`examId`= :examid AND tj.`objType`=2 AND sj.subjectId=98");
		}
		sql.append(" GROUP BY city.org_id,tj.`languageType`,tj.testpaperId");
		sql.append(" ) a ORDER BY `languageType`,testpaperId,cityId,countyId");
		SQLQuery query = createSQLQuery(sql.toString());
		query.setParameter("examid", examId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		System.out.println(sql);
		List<?> list = query.list();
		return list;
	}
	
	public String getScoreParagraphSql(List<Map<String, Integer>> scoreSegment){
		StringBuilder sql = new StringBuilder();
 		for (int i = 0; i < scoreSegment.size(); i++) {
			int Startscore = scoreSegment.get(i).get("Startscore");
			int Endscore = scoreSegment.get(i).get("Endscore");
			if(i == 0){
				sql.append("SUM(IF(tj.`score`>="+Endscore+" AND tj.`score`<="+Startscore+",tj.`num`,0)) `"+Endscore+"-"+Startscore+"`,");
			}else if(i == scoreSegment.size()-1){
				sql.append("SUM(IF(tj.`score`>="+Endscore+" AND tj.`score`<"+Startscore+",tj.`num`,0)) `"+Endscore+"-"+Startscore+"` ");
			}else{
				sql.append("SUM(IF(tj.`score`>="+Endscore+" AND tj.`score`<"+Startscore+",tj.`num`,0)) `"+Endscore+"-"+Startscore+"`,");
			}
			
		}
		return sql.toString();
	}
	
	
	public String getTotalScoreParagraphSql(List<Map<String, Integer>> scoreSegment){
		StringBuilder sql = new StringBuilder();
 		for (int i = 0; i < scoreSegment.size(); i++) {
			int Startscore = scoreSegment.get(i).get("Startscore");
			int Endscore = scoreSegment.get(i).get("Endscore");
			if(i == 0){
				sql.append("SUM(IF(tj.`score`>="+Endscore+" AND tj.`score`<="+Startscore+",tj.`num`,0)) `"+Endscore+"-"+Startscore+"`,");
			}else if(i == scoreSegment.size()-1){
				sql.append("SUM(IF(tj.`score`>="+Endscore+" AND tj.`score`<"+Startscore+",tj.`num`,0)) `"+Endscore+"-"+Startscore+"` ");
			}else{
				sql.append("SUM(IF(tj.`score`>="+Endscore+" AND tj.`score`<"+Startscore+",tj.`num`,0)) `"+Endscore+"-"+Startscore+"`,");
			}
			
		}
		return sql.toString();
	}
	
	public List getKnowledgeData(Long examId){
		StringBuilder sql = new StringBuilder("SELECT daxt.languagetype languagetype,sj.NAME testpapername,org.org_name provinceName, ");
		sql.append("ddt.knowledge NAME,ddt.itemno itemno,ddt.titletype titletype,ddt.fullscore fullscore, ");
		sql.append("ROUND(daxt.avgs,2) avgs,ROUND(daxt.stds,2) stds,");
		sql.append("ROUND((daxt.havgs-daxt.lavgs)/ddt.fullScore,2) dd, ");
		sql.append("ddt.forecastdifficulty forecastDifficulty, ");
		sql.append("ROUND(daxt.avgs/ddt.fullScore,2) difficulty ");
		sql.append("FROM dw_agg_xinjiang_item daxt ");
		
		sql.append("INNER JOIN dw_dim_item ddt ON daxt.itemId = ddt.id ");
		sql.append("INNER JOIN dw_dim_analysis_testpaper sj ON sj.id = daxt.testpaperId ");
		sql.append("INNER JOIN 4a_org org ON daxt.objId = org.org_id ");
		sql.append("WHERE daxt.examid = :examid AND daxt.objType <> 5");
		
		
		SQLQuery query = createSQLQuery(sql.toString());
		query.setParameter("examid", examId);
		query.setResultTransformer(Transformers.TO_LIST);
		System.out.println(sql);
		List<?> list = query.list();
		return list;
	}
	
	public List getSubjectiveObjectiveData(Long examId){
		StringBuilder sql = new StringBuilder("SELECT a.languagetype,a.testpapername,a.provinceName,a.name titletype,a.fullscore, ");
		sql.append("ROUND(a.avgs,2) avgs,ROUND(a.stds,2) stds,ROUND(a.dfl,2) dfl,ROUND(a.dd,2) dd,a.rd ");
		sql.append("FROM (");
		sql.append("SELECT daxt.languageType languageType,sj.NAME testpaperName,org.org_name provinceName,");
		sql.append("daxt.NAME,daxt.fullscore,daxt.avgs,daxt.stds, ");
		sql.append("daxt.avgs/daxt.fullScore dfl, ");
		sql.append("(daxt.havgs-daxt.lavgs)/daxt.fullScore dd,'-' AS rd ");
		sql.append("FROM dw_agg_xinjiang_itemgroup daxt ");
		sql.append("INNER JOIN dw_dim_analysis_testpaper sj ON sj.id = daxt.testpaperId ");
		sql.append("INNER JOIN 4a_org org ON daxt.objId = org.org_id ");
		sql.append("WHERE daxt.examid = :examid AND daxt.itemGroupType = 'titletype' AND daxt.objType <> 5 ");
		
		sql.append("UNION ALL ");
		sql.append("SELECT daxt.languageType languageType,sj.NAME testpaperName,org.org_name provinceName, ");
		sql.append("'全卷' AS NAME,sj.fullscore,daxt.avgs,daxt.stds, ");
		sql.append("daxt.avgs/sj.fullScore dfl, ");
		sql.append("(daxt.havgs-daxt.lavgs)/sj.fullScore dd, ");
		sql.append("ROUND(daxt.reliability,2) AS rd ");
		sql.append("FROM dw_agg_xinjiang_totalscore daxt ");
		sql.append("INNER JOIN dw_dim_analysis_testpaper sj ON sj.id = daxt.testpaperId ");
		sql.append("INNER JOIN 4a_org org ON daxt.objId = org.org_id ");
		sql.append("WHERE daxt.examid = :examid AND daxt.scoreType=1 AND daxt.subjectid<>98 AND daxt.objType <> 5");
		sql.append(") a ORDER BY a.languagetype,a.testpapername,a.provinceName");
		
		SQLQuery query = createSQLQuery(sql.toString());
		query.setParameter("examid", examId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		System.out.println(sql);
		List<?> list = query.list();
		return list;
	}
}
