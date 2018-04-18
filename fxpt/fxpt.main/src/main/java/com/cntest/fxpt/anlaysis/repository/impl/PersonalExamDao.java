/*
 * @(#)com.cntest.fxpt.anlaysis.repository.impl.PersonalExamDao.java	1.0 2014年7月18日:下午5:03:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



















import org.apache.poi.ss.formula.functions.T;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.anlaysis.repository.IPersonalReportExamDao;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.util.SystemConfig;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年7月18日 下午5:03:47
 * @version 1.0
 */
@Repository("IPersonalExamDao")
public class PersonalExamDao extends AbstractDao implements
		IPersonalReportExamDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static Map<String, List<String>> abilitymap = new HashMap<String, List<String>>();
	private static Map<String, List<String>> contentmap = new HashMap<String, List<String>>();
	private static Map<String, List<String>> titiletypeymap = new HashMap<String, List<String>>();
	
	@Override
	public Exam getExam(Long examId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("exam.examId, ");
		sql.append("exam.name, ");
		sql.append("exam.sortName, ");
		sql.append("exam.examDate, ");
		sql.append("exam.isWlForExamStudent, ");
		sql.append("exam.examStudentJiebie, ");
		sql.append("exam.schoolYear, ");
		sql.append("exam.schoolTerm, ");
		sql.append("exam.ownerCode, ");
		sql.append("exam.ownerName, ");
		sql.append("exam.levelCode, ");
		sql.append("exam.levelName, ");
		sql.append("grade.id AS gradeId, ");
		sql.append("grade.NAME AS gradeName ");
		sql.append("FROM tb_dim_exam exam  ");
		sql.append("INNER JOIN tb_dim_grade grade ON exam.gradeId=grade.id  ");
		sql.append("WHERE exam.examId=?  ");

		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter(0, examId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Object obj = sqlQuery.uniqueResult();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		return createExam((Map<String, Object>) obj, gradeMap);
	}

	private Exam createExam(Map<String, Object> row, Map<Long, Grade> gradeMap) {
		Exam exam = new Exam();
		exam.setId((Long) row.get("examId"));
		exam.setName((String) row.get("name"));
		exam.setSortName((String) row.get("sortName"));
		exam.setExamDate((Date) row.get("examDate"));
		exam.setWlForExamStudent((Boolean) row.get("isWlForExamStudent"));
		exam.setExamStudentJiebie((Integer) row.get("examStudentJiebie"));
		exam.setSchoolYear((Integer) row.get("schoolYear"));
		exam.setSchoolTerm((Integer) row.get("schoolTerm"));
		exam.setOwnerCode((String) row.get("ownerCode"));
		exam.setOwnerName((String) row.get("ownerName"));
		exam.setLevelCode((Integer) row.get("levelCode"));
		exam.setLevelName((String) row.get("levelName"));

		Long gradeId = (Long) row.get("gradeId");
		Grade grade = gradeMap.get(gradeId);
		if (grade == null) {
			grade = new Grade();
			grade.setId(gradeId);
			grade.setName((String) row.get("gradeName"));
			gradeMap.put(gradeId, grade);
		}
		exam.setGrade(grade);

		return exam;
	}

	public List<Object> findALLStudentByExamId(Long examId) {
		String sql = "SELECT s.name,c.schoolName,l.className,s.studentId FROM dw_examstudent_fact s,dw_dim_school c,tb_dim_examclass l WHERE s.examId="
				+ examId + " AND c.schoolId=s.schoolId AND l.cId=s.classId";
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		return list;
	}

	public Object[] findStuentByExamIdAndStudentId(Long examid, String studentId) {
		String sql = "SELECT xs.name,xx.name schoolName,bj.name className,xs.studentId,xs.wl,xx.id,xs.classid,xs.schoolid,xs.countyId FROM dw_examstudent_fact xs "
				+ "LEFT JOIN dw_dim_class bj ON xs.classId = bj.id "
				+ "LEFT JOIN dw_dim_school xx ON xs.schoolId = xx.id "
				+ "WHERE xs.examid="
				+ examid
				+ " AND xs.studentId='"
				+ studentId + "'";
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (Object[]) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Object> getSubjectList(Long examId) {
		String sql = "SELECT subjectId,NAME FROM tb_dim_testpaper WHERE examid="
				+ examId;
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		return list;
	}

	@Override
	public List<Object[]> getDetailList(Long examId,int wl, Long subjectId,String stuid) {
//		String procName = "{call SP_student_MyDetailList(?,?,?,?)}";
//		SQLQuery query = getSession().createSQLQuery(procName);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT i.itemno,i.fullscore,IF(c.score=-888,0,c.score),i.knowledgecontent,i.ability,i.titletype,f.studentId,c.subjectid");
		sql.append(" FROM dw_itemcj_fact c ");
		sql.append(" INNER JOIN dw_dim_item i ON i.id=c.itemid ");
		sql.append(" INNER JOIN dw_examstudent_fact f ON f.id=c.studentId");
		sql.append(" WHERE c.examid=? AND c.subjectid=? AND c.wl=? and f.studentid=? GROUP BY f.studentId,i.itemNo ORDER BY c.itemId;");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter(0, examId);
		query.setParameter(1, subjectId);
		query.setParameter(2, wl);
		query.setParameter(3, stuid);
		List list = query.list();
		return list;
	}
	
	
	public void clearTempTable(){
		String delTmp_contentratio = "delete from tmp_contentratio";
		jdbcTemplate.execute(delTmp_contentratio);
		String delTmp_subjectsumscore = "delete from tmp_subjectsumscore";
		jdbcTemplate.execute(delTmp_subjectsumscore);
		String delTmp_zf = "delete from tmp_zf";
		jdbcTemplate.execute(delTmp_zf);
	}
	
	public void cacluteAllSubjectRank(Long examId,int wl,Long subjectid){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("CREATE TABLE IF NOT EXISTS tmp_subjectSumScore(examid INT,rank INT,score DOUBLE,studentid VARCHAR(32),subjectid VARCHAR(32),subjectname VARCHAR(32),stuname VARCHAR(32),wl INT,analysisTestpaperId int) ");
		SQLQuery query = getSession().createSQLQuery(stringBuffer.toString());
		query.executeUpdate();
		stringBuffer = new StringBuffer();
		stringBuffer.append(" select * from tmp_subjectSumScore where examid="+examId+" and wl="+wl+" and subjectid="+subjectid+" limit 1");
		query = getSession().createSQLQuery(stringBuffer.toString());
		List list = query.list();
		if(list==null || list.size()<1){
			stringBuffer = new StringBuffer();
			stringBuffer.append("INSERT INTO tmp_subjectSumScore");
			stringBuffer.append(" SELECT c.examid,c.rank,c.score,c.studentid,c.subjectid,c.subjectname,c.stuname,c.wl,c.analysisTestpaperId FROM(");
			stringBuffer.append(" SELECT @rownum:=@rownum+1 AS rownum,IF(@total=totalScore,@rank,@rank:=@rownum) AS rank,@total:=totalScore score,a.*");
			stringBuffer.append(" FROM (SELECT s.studentid,c.subjectid,s.examId,j.NAME subjectname,s.NAME stuName,c.totalScore,s.wl,c.analysisTestpaperId");
			stringBuffer.append(" FROM dw_examstudent_fact s INNER JOIN dw_testpapercj_fact c ON s.id = c.studentId");
			stringBuffer.append(" INNER JOIN kn_subject j ON j.id = c.subjectId");
			stringBuffer.append(" INNER JOIN dw_dim_analysis_testpaper t ON t.id=c.analysisTestpaperId");
			stringBuffer.append(" WHERE c.examId ="+examId+" AND c.subjectid ="+subjectid+" AND t.isComposite=0 AND c.wl="+wl+" GROUP BY c.studentid ORDER BY c.totalScore desc) a,(SELECT @rank:=0, @rownum:=0, @total:=NULL)b ) c; ");
			jdbcTemplate.execute(stringBuffer.toString());
		}
	}
	
	
	public List<Map<String, Object>> cacluteAllSubjectRankS(Long examId,int wl,Long subjectid){
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(" SELECT c.subjectname,floor(c.rank) rank,c.score,c.subjectid,c.fullscore,c.examid,c.studentid,c.wl FROM(");
			stringBuffer.append(" SELECT @rownum:=@rownum+1 AS rownum,IF(@total=totalScore,@rank,@rank:=@rownum) AS rank,@total:=totalScore score,a.*");
			stringBuffer.append(" FROM (SELECT s.studentid,c.subjectid,s.examId,j.NAME subjectname,s.NAME stuName,c.totalScore,s.wl,t.fullscore");
			stringBuffer.append(" FROM dw_examstudent_fact s INNER JOIN dw_testpapercj_fact c ON s.id = c.studentId");
			stringBuffer.append(" INNER JOIN kn_subject j ON j.id = c.subjectId");
			stringBuffer.append(" INNER JOIN dw_dim_analysis_testpaper t ON t.id=c.analysisTestpaperId");
			stringBuffer.append(" WHERE c.examId ="+examId+" AND c.subjectid ="+subjectid+" AND t.isComposite=0 AND c.wl="+wl+" GROUP BY c.studentid ORDER BY c.totalScore desc) a,(SELECT @rank:=0, @rownum:=0, @total:=NULL)b ) c; ");
			jdbcTemplate.execute(stringBuffer.toString());
			List<Map<String, Object>> l = jdbcTemplate.queryForList(stringBuffer.toString());
//			List<Object[]> obj = new ArrayList<>();
//			for(Map<String, Object> map : l){
//				Object[] o = new Object[8];
//				o[0]=map.get("subjectname");
//				o[1]=map.get("rank");
//				o[2]=Integer.parseInt(map.get("score").toString());
//				o[3]=map.get("subjectid");
//				o[4]=map.get("fullscore");
//				o[5]=map.get("examid");
//				o[6]=map.get("studentid");
//				o[7]=map.get("wl");
//				obj.add(o);
//			}
			return l;
	}
	
	
	public List<Object> getAllSubjectRank(Long examid,int wl){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT s.subjectname, s.rank, s.score, s.subjectid, COUNT(s.studentid) samenum,t.fullScore,s.examid");
		sql.append(" FROM tmp_subjectsumscore s, dw_dim_analysis_testpaper t");
		sql.append(" WHERE s.wl = "+wl+" AND s.examid = "+examid+" AND s.analysisTestpaperId = t.id GROUP BY s.rank,s.subjectid;");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		return query.list();
	}
	
	
	public List<Object> getStudentsSubjectRank(Long examid,int wl){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT s.subjectname,s.rank,s.score,s.subjectid,t.fullScore,s.examid,s.studentid,s.wl");
		sql.append(" FROM tmp_subjectsumscore s,dw_dim_analysis_testpaper t");
		sql.append(" WHERE s.wl ="+wl+" AND s.examid = "+examid+" AND s.analysisTestpaperId =t.id AND s.examid=t.examId;");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		return query.list();
	}
	
	
	@Override
	public List<Object> getAllsubjectSumScoreProc(Long examId, int wl,String studentId,Long subjectid) {
//		String procName = "{call SP_student_percentage(?,?,?)}";
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("CREATE TABLE IF NOT EXISTS tmp_subjectSumScore(examid INT,rank INT,score DOUBLE,studentid VARCHAR(32),subjectid VARCHAR(32),subjectname VARCHAR(32),stuname VARCHAR(32),wl INT,analysisTestpaperId int) ");
		SQLQuery query = getSession().createSQLQuery(stringBuffer.toString());
		query.executeUpdate();
		stringBuffer = new StringBuffer();
		stringBuffer.append("SELECT rank INTO @rank FROM tmp_subjectSumScore WHERE examid="+examId+" AND subjectid="+subjectid+" AND wl="+wl+" AND studentid='"+studentId+"';");
		jdbcTemplate.execute(stringBuffer.toString());
		stringBuffer = new StringBuffer();
		stringBuffer.append("SELECT s.subjectname, s.rank, s.score, s.subjectid, COUNT(s.studentid) samenum,t.fullScore");
		stringBuffer.append(" FROM tmp_subjectsumscore s,dw_dim_analysis_testpaper t ");
		stringBuffer.append(" WHERE s.subjectid ="+subjectid+" AND s.wl ="+wl+" AND s.examid ="+examId+" AND s.analysisTestpaperId = t.id AND s.rank=@rank;");
		query = getSession().createSQLQuery(stringBuffer.toString());
		List<Object> list = query.list();
		if(list!=null && list.size()>0 && ((Object[])list.get(0))[0]!=null){
			return list;
		}else{
			stringBuffer = new StringBuffer();
			stringBuffer.append("INSERT INTO tmp_subjectSumScore");
			stringBuffer.append(" SELECT c.examid,c.rank,c.score,c.studentid,c.subjectid,c.subjectname,c.stuname,c.wl,c.analysisTestpaperId FROM(");
			stringBuffer.append(" SELECT @rownum:=@rownum+1 AS rownum,IF(@total=totalScore,@rank,@rank:=@rownum) AS rank,@total:=totalScore score,a.*");
			stringBuffer.append(" FROM (SELECT s.studentid,c.subjectid,s.examId,j.NAME subjectname,s.NAME stuName,c.totalScore,s.wl,c.analysisTestpaperId");
			stringBuffer.append(" FROM dw_examstudent_fact s INNER JOIN dw_testpapercj_fact c ON s.id = c.studentId");
			stringBuffer.append(" INNER JOIN kn_subject j ON j.id = c.subjectId");
			stringBuffer.append(" INNER JOIN dw_dim_analysis_testpaper t ON t.id=c.analysisTestpaperId");
			stringBuffer.append(" WHERE c.examId ="+examId+" AND c.subjectid ="+subjectid+" AND t.isComposite=0 AND c.wl="+wl+" GROUP BY c.studentid ORDER BY c.totalScore desc) a,(SELECT @rank:=0, @rownum:=0, @total:=NULL)b ) c; ");
			jdbcTemplate.execute(stringBuffer.toString());
			stringBuffer = new StringBuffer();
			stringBuffer.append("SELECT rank INTO @rank FROM tmp_subjectSumScore WHERE examid="+examId+" AND subjectid="+subjectid+" AND wl="+wl+" AND studentid='"+studentId+"';");
			jdbcTemplate.execute(stringBuffer.toString());
			stringBuffer = new StringBuffer();
			stringBuffer.append("SELECT s.subjectname, s.rank, s.score, s.subjectid, COUNT(s.studentid) samenum,t.fullScore");
			stringBuffer.append(" FROM tmp_subjectsumscore s,dw_dim_analysis_testpaper t ");
			stringBuffer.append(" WHERE s.subjectid ="+subjectid+" AND s.wl ="+wl+" AND s.examid ="+examId+" AND s.analysisTestpaperId = t.id AND s.rank=@rank;");
			query = getSession().createSQLQuery(stringBuffer.toString());
			list = query.list();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.repository.IReportExamDao#getAllHissubjectSumScore
	 * (int, java.lang.String)
	 */
	@Override
	public List<Object> getAllHissubjectSumScoreProc(Long examId, int wl,
			String studentId) {
		String procName = "{call SP_student_history(?,?,?)}";
		SQLQuery query = getSession().createSQLQuery(procName);
		query.setLong(0, examId);
		query.setInteger(1, wl);
		query.setString(2, studentId);
		List list = query.list();
		if (list.size() < 1) {
			return null;
		}
		return list;
	}
	
	
	
	public Long getHisExamidByCurrExamid(Long examid){
		String sql = "SELECT ke1.id examid,ke1.sortName FROM kn_exam ke INNER JOIN kn_exam ke1 ON ke.examStudentJiebie = ke1.examStudentJiebie AND ke.examDate >= ke1.examDate AND ke.STATUS=ke1.STATUS WHERE ke.id ="+examid+" AND ke.STATUS=5 ORDER BY ke1.id";
		SQLQuery query = getSession().createSQLQuery(sql);
		List<Object[]> list = query.list();
		if(list!=null && list.size()>1){
			Object[] o = (Object[]) list.get(0);
			return Long.parseLong(o[0].toString());
		}else{
			return null;
		}
	}
	
	
	//计算标准分
	public List<Object[]> cacluteStudentAllSubjectsStd(Long examId,String objid){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cj.subjectid,b.subjectname,f.studentid,cj.totalScore,(cj.totalScore-b.avgs)/b.stds stds,cj.examid,f.wl FROM dw_testpapercj_fact cj ");
		sql.append(" RIGHT JOIN(");
		sql.append(" SELECT c.*,s.NAME subjectname FROM(");
		sql.append(" SELECT t.examid,t.wl,t.subjectid,t.avgs,t.stds FROM dw_agg_xinjiang_totalscore t INNER JOIN 4a_org o ON o.org_id=t.objId WHERE t.examid IN (");
		sql.append(" SELECT a.examid FROM(");
		sql.append(" SELECT ke1.id examid,ke1.sortName FROM kn_exam ke INNER JOIN kn_exam ke1 ON ke.examStudentJiebie = ke1.examStudentJiebie AND ke.examDate >= ke1.examDate");
		sql.append(" WHERE ke.id ="+examId+" ORDER BY ke1.id DESC) a");
		sql.append(" ) AND t.scoreType=1 AND o.org_code='"+objid+"' AND t.reliability IS NOT NULL) c LEFT JOIN kn_subject s ON s.id=c.subjectid");
		sql.append(" ) b ");
		sql.append(" ON cj.examid=b.examid AND cj.wl=b.wl AND cj.subjectid=b.subjectid");
		sql.append(" INNER JOIN dw_examstudent_fact f ON f.id=cj.studentid;");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		List<Object[]> list = query.list();
		return list;
	}
	
	
	//标准分
	public List<Object> getAllHissubjectSumScoreStdsProc(Long examId, int wl,
			String studentId,String objid) {
//		String procName = "{call SP_student_historyStds(?,?,?,?)}";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT s.id,s.NAME subjectname,f.name stuname,cj.totalScore,(cj.totalScore-t.avgs)/t.stds stds,cj.examid FROM dw_testpapercj_fact cj ");
		sql.append(" INNER JOIN dw_agg_xinjiang_totalscore t ON t.subjectId=cj.subjectId");
		sql.append(" INNER JOIN kn_subject s ON s.id=t.subjectId");
		sql.append(" INNER JOIN dw_examstudent_fact f ON f.id=cj.studentid");
		sql.append(" INNER JOIN (SELECT ks.id examid, xs.studentid FROM dw_examstudent_fact xs");
		sql.append(" INNER JOIN kn_exam ks ON xs.examId = ks.id AND xs.examid<=?");
		sql.append(" WHERE xs.studentId = ? AND ks.status=5 ORDER BY ks.id DESC LIMIT 2");
		sql.append(" ) ks ON ks.examid=cj.examid AND ks.studentid=f.studentid");
		sql.append(" INNER JOIN 4a_org o ON o.org_id=t.objid");
		sql.append(" WHERE cj.wl=? AND o.org_Code=? AND t.scoreType=1 GROUP BY cj.subjectid,cj.examid");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter(0, examId);
		query.setParameter(1, studentId);
		query.setParameter(2, wl);
		query.setParameter(3, objid);
		List list = query.list();
		if (list.size() < 1) {
			return null;
		}
		return list;
	}
	

	public List<Map<String, Object>> getAllContentRatioProcS(Long examId,int wl,String objid) {
			StringBuffer calcuateData = new StringBuffer();
			calcuateData.append(" SELECT k.name,k.lavgs/k.fullScore lratio, k.havgs/k.fullScore hratio,k.examId,k.subjectId,k.wl ");
			calcuateData.append(" FROM dw_agg_xinjiang_itemgroup k ");
			calcuateData.append(" INNER JOIN 4a_org o ON o.org_id=k.objid");
			calcuateData.append(" WHERE k.examId="+examId+" AND k.wl="+wl+"  AND k.itemGroupType  LIKE  '%knowledgeContent%'  AND k.avgs<>0 AND o.org_code="+objid+";");
			List<Map<String, Object>> l = jdbcTemplate.queryForList(calcuateData.toString());
			return l;
	}
	
	

	public List<Object[]> getAllContentRatioProc(Long examId,int wl,String objid) {
		String createTmpTable = "CREATE TABLE IF NOT EXISTS tmp_contentRatio(conname VARCHAR(1024),lratio DOUBLE,hratio DOUBLE,examid INT,subjectid INT,wl INT);";
		jdbcTemplate.execute(createTmpTable);
		String sql = "SELECT * FROM tmp_contentRatio c WHERE c.examid=? AND c.wl=?";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, examId);
		query.setParameter(1, wl);
		List list = query.list();
		if(!(list!=null && list.size()>0)){
			StringBuffer calcuateData = new StringBuffer();
			calcuateData.append("INSERT INTO tmp_contentRatio");
			calcuateData.append(" SELECT k.name,k.lavgs/k.fullScore lratio, k.havgs/k.fullScore hratio,k.examId,k.subjectId,k.wl ");
			calcuateData.append(" FROM dw_agg_xinjiang_itemgroup k ");
			calcuateData.append(" INNER JOIN 4a_org o ON o.org_id=k.objid");
			calcuateData.append(" WHERE k.examId="+examId+" AND k.wl="+wl+"  AND k.itemGroupType  LIKE  '%knowledgeContent%'  AND k.avgs<>0 AND o.org_code="+objid+";");
			jdbcTemplate.execute(calcuateData.toString());
			list = query.list();
		}
		return list;
	}

	public List<Object> getMyContentRatio(Long examId, Long subjectId,
			String studentId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT i.knowledgeContent content, IF(c.score=-888,0,SUM(c.score) / SUM(i.fullScore)) myratio");
		sql.append(" FROM dw_itemcj_fact c");
		sql.append(" LEFT JOIN dw_dim_item i ON i.id = c.itemId");
		sql.append(" INNER JOIN dw_examstudent_fact f ON f.id=c.studentid");
		sql.append(" WHERE c.examId =?");
		sql.append(" AND c.subjectId =?");
		sql.append(" AND f.studentId = ?");
		sql.append(" GROUP BY i.knowledgeContent;");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setLong(0, examId);
		query.setLong(1, subjectId);
		query.setString(2, studentId);
		List list = query.list();
		return list;
	}
	
	
	public List<Object[]> getAllSubjectKnowledgeContentMap(Long examid,int wl){
		String sql = "SELECT * FROM dw_agg_exam_knowledgecontent_scoreinfo s WHERE s.examId="+examid+" AND wl="+wl+" GROUP BY s.subjectId,s.NAME,s.score,s.rank;";
		SQLQuery query = getSession().createSQLQuery(sql);
		return query.list();
	}
	
	public List<Object[]> getAllSubjectAbilityMapMap(Long examid,int wl){
		String sql = "SELECT * FROM dw_agg_exam_ability_scoreinfo s WHERE s.examId="+examid+" AND wl="+wl+" GROUP BY s.subjectId,s.NAME,s.score,s.rank;";
		SQLQuery query = getSession().createSQLQuery(sql);
		return query.list();
	}
	
	public List<Object[]> getAllSubjectTitleTypeMap(Long examid,int wl){
		String sql = "SELECT * FROM dw_agg_exam_titletype_scoreinfo s WHERE s.examId="+examid+" AND wl="+wl+" GROUP BY s.subjectId,s.NAME,s.score,s.rank;";
		SQLQuery query = getSession().createSQLQuery(sql);
		return query.list();
	}
	
	

	@Override
	public List<Object[]> getMyContentRatioProc(Long examId,int wl, Long subjectId,
			String studentId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT xtcj.examId,xtcj.subjectId,xt.knowledgeContent,SUM(xt.fullScore) fullscore,SUM(xtcj.score) score,f.studentid,xtcj.wl");
		sb.append("  FROM dw_itemcj_fact xtcj INNER JOIN dw_dim_item xt ON xtcj.itemId = xt.id");
		sb.append(" INNER JOIN dw_examstudent_fact f ON f.id=xtcj.studentId");
		sb.append(" WHERE xtcj.subjectId = "+subjectId+" AND xtcj.examId="+examId+" AND xtcj.score>=0 AND xtcj.wl="+wl+"");
//		sb.append(" AND  f.studentId='"+studentId+"'");
		sb.append(" GROUP BY f.studentId,xt.knowledgeContent;");
		SQLQuery query = getSession().createSQLQuery(sb.toString());
		return query.list();
	}

	public List<Object[]> getMyAbilityRatioProc(Long examId,int wl, Long subjectId,
			String studentId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT xtcj.examId,xtcj.subjectId,xt.ability,SUM(xt.fullScore) fullscore,SUM(xtcj.score) score,f.studentid,xtcj.wl");
		sb.append("  FROM dw_itemcj_fact xtcj INNER JOIN dw_dim_item xt ON xtcj.itemId = xt.id");
		sb.append(" INNER JOIN dw_examstudent_fact f ON f.id=xtcj.studentId");
		sb.append(" WHERE xtcj.subjectId = "+subjectId+" AND xtcj.examId="+examId+" AND xtcj.score>=0 AND xtcj.wl="+wl+"");
//		sb.append(" AND  f.studentId='"+studentId+"'");
		sb.append(" GROUP BY f.studentId,xt.ability;");
		SQLQuery query = getSession().createSQLQuery(sb.toString());
		return query.list();
	}

	public List<Object[]> getMyTitleTypeRatioProc(Long examId, int wl,Long subjectId,
			String studentId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT xtcj.examId,xtcj.subjectId,xt.titletype,SUM(xt.fullScore) fullscore,SUM(xtcj.score) score,f.studentid,xtcj.wl");
		sb.append("  FROM dw_itemcj_fact xtcj INNER JOIN dw_dim_item xt ON xtcj.itemId = xt.id");
		sb.append(" INNER JOIN dw_examstudent_fact f ON f.id=xtcj.studentId");
		sb.append(" WHERE xtcj.subjectId = "+subjectId+" AND xtcj.examId="+examId+" AND xtcj.score>=0 AND xtcj.wl="+wl+"");
//		sb.append(" AND  f.studentId='"+studentId+"'");
		sb.append(" GROUP BY f.studentId,xt.titletype;");
		SQLQuery query = getSession().createSQLQuery(sb.toString());
		return query.list();
	}
	
	
	public List<Object[]> getStduentSLevel(Long examid){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT l.examId,l.SubjectId,s.NAME,l.levelName,f.studentid FROM kn_levelscoresetting l");
		sql.append("  INNER JOIN kn_subject s");
		sql.append(" ON l.SubjectId=s.id");
		sql.append(" INNER JOIN dw_testpapercj_fact f");
		sql.append(" ON f.examId=l.examId AND f.wl=l.wl AND f.subjectId=s.id");
		sql.append("  WHERE l.examId IN (");
		sql.append("  SELECT a.examid FROM(");
		sql.append("  SELECT ke1.id examid,ke1.sortName FROM kn_exam ke");
		sql.append(" INNER JOIN kn_exam ke1");
		sql.append("  ON ke.examStudentJiebie = ke1.examStudentJiebie AND ke.examDate >= ke1.examDate");
		sql.append("  WHERE ke.id = "+examid+" ORDER BY ke1.id DESC) a   ");
		sql.append(" )");
		sql.append(" AND f.totalScore>=l.beginScore AND f.totalScore<l.endScore GROUP BY s.id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		List<Object[]> list = query.list();
		return list;
	}
	
	
	public List<Object> getStudentSubjectLevelProc(Long examId, int wl,
			String studentId) {
		String sql = "{call SP_student_level(?,?,?)}";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setLong(0, examId);
		query.setInteger(1,wl);
		query.setString(2, studentId);
		List list = new ArrayList<Object>();
		try {
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Object[]> getTopRatioProc(Long examId,int wl,String orgcode) {
		int orgId = getTopOrgCode(orgcode);;
		String sql = "SELECT i.name,i.avgs/i.fullScore ratio,i.subjectId FROM dw_agg_xinjiang_item i WHERE i.examId=? AND i.wl=? AND i.objId=?   GROUP BY i.subjectId,i.itemId order by i.subjectId,i.itemId;";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, examId);
		query.setParameter(1, wl);
		query.setParameter(2, orgId);
		List list = query.list();
		return list;
	}

	@Override
	public Map<String, List<Object[]>> getAllStudentContentPreProc(Long examId,
			Long subjectId) {
		String sql = " SELECT knowledgeContent,score,COUNT(1) nums FROM ("
				+ " SELECT cj.studentId,xt.knowledgeContent,SUM(cj.score) score FROM tb_itemcj_fact cj"
				+ " INNER JOIN tb_dim_item xt ON cj.itemId=xt.id"
				+ " WHERE cj.examId="
				+ examId
				+ " AND cj.subjectId="
				+ subjectId
				+ " GROUP BY cj.studentId,xt.knowledgeContent"
				+ " ) b GROUP BY knowledgeContent,score ORDER BY knowledgeContent,score DESC";
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			String key = obj[0].toString();
			List<Object[]> listo = map.get(key);
			if (listo == null) {
				listo = new ArrayList<Object[]>();
				map.put(key, listo);
			}
			listo.add(obj);
		}
		return map;
	}

	public Map<String, List<Object[]>> getAllStudentAbilityPreProc(Long examId,
			Long subjectId) {
		String sql = " SELECT ability,score,COUNT(1) nums FROM ("
				+ " SELECT cj.studentId,xt.ability,SUM(cj.score) score FROM tb_itemcj_fact cj"
				+ " INNER JOIN tb_dim_item xt ON cj.itemId=xt.id"
				+ " WHERE cj.examId=" + examId + " AND cj.subjectId="
				+ subjectId + " GROUP BY cj.studentId,xt.ability"
				+ " ) b GROUP BY ability,score ORDER BY ability,score DESC";
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			String key = obj[0].toString();
			List<Object[]> listo = map.get(key);
			if (listo == null) {
				listo = new ArrayList<Object[]>();
				map.put(key, listo);
			}
			listo.add(obj);
		}
		return map;
	}

	public Map<String, List<Object[]>> getAllStudentTitleTypePreProc(
			Long examId, Long subjectId) {
		String sql = " SELECT titleType,score,COUNT(1) nums FROM ("
				+ " SELECT cj.studentId,xt.titleType,SUM(cj.score) score FROM tb_itemcj_fact cj"
				+ " INNER JOIN tb_dim_item xt ON cj.itemId=xt.id"
				+ " WHERE cj.examId=" + examId + " AND cj.subjectId="
				+ subjectId + " GROUP BY cj.studentId,xt.titleType"
				+ " ) b GROUP BY titleType,score ORDER BY titleType,score DESC";
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			String key = obj[0].toString();
			List<Object[]> listo = map.get(key);
			if (listo == null) {
				listo = new ArrayList<Object[]>();
				map.put(key, listo);
			}
			listo.add(obj);
		}
		return map;
	}

	@Override
	public int getAllExamStudentNumProc(Long examid, Long subjectId) {
		String sql = "SELECT COUNT(*)  FROM tb_examstudentsubjectrank WHERE examid="
				+ examid + " AND subjectId=" + subjectId;
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		int num = Integer.parseInt(list.get(0).toString());
		return num;
	}
	public List<Object[]> getAllSubjectSkrs(Long examid, int wl,String objid){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.examId,t.wl,t.subjectId,t.skrs FROM dw_agg_xinjiang_totalscore t ");
		sql.append(" INNER JOIN 4a_org o ON o.org_id=t.objId ");
		sql.append(" INNER JOIN dw_dim_analysis_testpaper p ON p.id=t.testpaperId");
		sql.append(" WHERE t.examid = ? AND t.wl = ? AND t.scoretype = 1 AND o.org_code = ? AND p.isComposite=0");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter(0, examid);
		query.setParameter(1, wl);
		query.setParameter(2, objid);
		List list = query.list();
		return list;
	}
	
	
	@Override
	public int getExamstudentNum(Long examid, Long subjectid, int wl,String objid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.skrs FROM dw_agg_xinjiang_totalscore t ");
		sql.append(" INNER JOIN 4a_org o ON o.org_id=t.objId ");
		sql.append(" INNER JOIN dw_dim_analysis_testpaper p ON p.id=t.testpaperId");
		sql.append(" WHERE t.examid = ? AND t.wl = ? AND t.subjectid = ? AND t.scoretype = 1 AND o.org_code = ? AND p.isComposite=0");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter(0, examid);
		query.setParameter(1, wl);
		query.setParameter(2, subjectid);
		query.setParameter(3, objid);
		List list = query.list();
		int num = 0;
		if(list!=null && list.size()>0){
		  num = Integer.parseInt(list.get(0).toString());
		}
		return num;
	}

	public int getTopOrgCode(String orgcode) {
		String sql = "SELECT org_id FROM 4a_org WHERE org_code='"+orgcode+"'";
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		int id = Integer.parseInt(list.get(0).toString());
		return id;
	}

	public List<Object[]> getAllStudentList(Long examid) {
	    String sql = "SELECT s.examid,o.provincecode,o.provinceName,o.cityCode,o.cityname,o.countycode,o.countyname,o.code,o.name,s.studentid,s.name FROM dw_examstudent_fact s"
	    		+ " LEFT JOIN dw_dim_school o"
	    		+ " ON s.schoolid=o.id"
	    		+ " LEFT JOIN dw_dim_class c"
	    		+ " ON s.classid=c.id"
	    		+ " WHERE s.examid="+examid;
	    SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		return list;
	}

	
	
	public List<Map<String, Object>> cacluteZFScoreS(Long examId, int wl){
			StringBuffer calculateZfData = new StringBuffer();
			calculateZfData.append(" SELECT c.examid,c.wl,c.rank,c.score, c.totalNum,c.studentid,c.fullScore FROM(");
			calculateZfData.append(" SELECT @rownum:=@rownum+1 AS rownum,IF(@total=a.totalScore,@rank,@rank:=@rownum) AS rank,@total:=a.totalScore score,FOUND_ROWS() AS totalNum,a.*");
			calculateZfData.append(" FROM (SELECT s.studentId,SUM(f.totalScore) totalScore,f.examid,f.wl,SUM(t.fullScore) fullScore FROM dw_testpapercj_fact f ");
			calculateZfData.append(" INNER JOIN dw_dim_analysis_testpaper t  ON f.analysisTestpaperId=t.id");
			calculateZfData.append(" INNER JOIN dw_examstudent_fact s ON s.id=f.studentId");
			calculateZfData.append(" WHERE f.examId="+examId+" AND f.wl="+wl+" AND t.isComposite=0 GROUP BY s.id");
			calculateZfData.append("  )a ,(SELECT @rank:=0, @rownum:=0, @total:=NULL) b ORDER BY a.totalScore DESC) c;");
			List<Map<String, Object>> l = jdbcTemplate.queryForList(calculateZfData.toString());
			return l;
	}
	
	
	public void cacluteZFScore(Long examId, int wl){
		String createTmpTable = "CREATE TABLE IF NOT EXISTS tmp_zf(examid INT,wl INT,rank INT,score DOUBLE,totalNum INT,studentid VARCHAR(128),fullScore double);";
		SQLQuery query = getSession().createSQLQuery(createTmpTable);
		query.executeUpdate();
		String sql = "SELECT * FROM tmp_zf z WHERE z.examid="+examId+" AND z.wl="+wl+" limit 1";
		query = getSession().createSQLQuery(sql);
		List list = query.list();
		if(!(list!=null && list.size()>0)){
			StringBuffer calculateZfData = new StringBuffer();
			calculateZfData.append("INSERT INTO tmp_zf");
			calculateZfData.append(" SELECT c.examid,c.wl,c.rank,c.score, c.totalNum,c.studentid,c.fullScore FROM(");
			calculateZfData.append(" SELECT @rownum:=@rownum+1 AS rownum,IF(@total=a.totalScore,@rank,@rank:=@rownum) AS rank,@total:=a.totalScore score,FOUND_ROWS() AS totalNum,a.*");
			calculateZfData.append(" FROM (SELECT s.studentId,SUM(f.totalScore) totalScore,f.examid,f.wl,SUM(t.fullScore) fullScore FROM dw_testpapercj_fact f ");
			calculateZfData.append(" INNER JOIN dw_dim_analysis_testpaper t  ON f.analysisTestpaperId=t.id");
			calculateZfData.append(" INNER JOIN dw_examstudent_fact s ON s.id=f.studentId");
			calculateZfData.append(" WHERE f.examId="+examId+" AND f.wl="+wl+" AND t.isComposite=0 GROUP BY s.id");
			calculateZfData.append("  )a ,(SELECT @rank:=0, @rownum:=0, @total:=NULL) b ORDER BY a.totalScore DESC) c;");
			jdbcTemplate.execute(calculateZfData.toString());
			list = query.list();
		}
	}
	
	public List<Object[]> getallZFScore(Long examId, int wl){
		String sql = "SELECT * FROM tmp_zf z WHERE z.examid="+examId+" AND z.wl="+wl+"";
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		return list;
	}
	
	
	public Object getzfSumScore(Long examId, int wl, String studentId) {
		String createTmpTable = "CREATE TABLE IF NOT EXISTS tmp_zf(examid INT,wl INT,rank INT,score DOUBLE,totalNum INT,studentid VARCHAR(128),fullScore double);";
		SQLQuery query = getSession().createSQLQuery(createTmpTable);
		query.executeUpdate();
		String sql = "SELECT * FROM tmp_zf z WHERE z.examid="+examId+" AND z.wl="+wl+" AND z.studentid='"+studentId+"'";
		query = getSession().createSQLQuery(sql);
		List list = query.list();
		if(!(list!=null && list.size()>0)){
			StringBuffer calculateZfData = new StringBuffer();
			calculateZfData.append("INSERT INTO tmp_zf");
			calculateZfData.append(" SELECT c.examid,c.wl,c.rank,c.score, c.totalNum,c.studentid,c.fullScore FROM(");
			calculateZfData.append(" SELECT @rownum:=@rownum+1 AS rownum,IF(@total=a.totalScore,@rank,@rank:=@rownum) AS rank,@total:=a.totalScore score,FOUND_ROWS() AS totalNum,a.*");
			calculateZfData.append(" FROM (SELECT s.studentId,SUM(f.totalScore) totalScore,f.examid,f.wl,SUM(t.fullScore) fullScore FROM dw_testpapercj_fact f ");
			calculateZfData.append(" INNER JOIN dw_dim_analysis_testpaper t  ON f.analysisTestpaperId=t.id");
			calculateZfData.append(" INNER JOIN dw_examstudent_fact s ON s.id=f.studentId");
			calculateZfData.append(" WHERE f.examId="+examId+" AND f.wl="+wl+" AND t.isComposite=0 GROUP BY s.id");
			calculateZfData.append("  )a ,(SELECT @rank:=0, @rownum:=0, @total:=NULL) b ORDER BY a.totalScore DESC) c;");
			jdbcTemplate.execute(calculateZfData.toString());
			list = query.list();
		}
		return list.get(0);
	}

	@Override
	public int getSameRank(Long examid, int wl, int rank) {
		String sql = "SELECT COUNT(*) FROM tmp_zf z WHERE z.examid=? AND z.wl=? AND z.rank=?";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, examid);
		query.setParameter(1, wl);
		query.setParameter(2, rank);
		List list = query.list();
		if(list!=null && list.size()>0){
			return Integer.parseInt(list.get(0).toString());
		}else{
		return 0;
		}
	}

	@Override
	public Map<Long, Double> getStudentAllSubjectScore(Long examid, String studentid,
			int wl) {
		Map<Long, Double> res = null;
		String sql = "SELECT subjectid,totalScore FROM dw_testpapercj_fact f INNER JOIN dw_examstudent_fact s ON s.examId=f.examId AND s.id=f.studentId AND s.wl=f.wl WHERE s.studentid=? AND f.examid=? AND f.wl=?;";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, studentid);
		query.setParameter(1, examid);
		query.setParameter(2, wl);
		List list = query.list();
		if(list!=null && list.size()>0){
			res = new HashMap<Long, Double>();
			for(Object o : list){
				Object[] obj = (Object[]) o;
				res.put(Long.parseLong(obj[0].toString()), Double.parseDouble(obj[1].toString()));
			}
		}
		return res;
	}
	@Override
	public Map<Long, Double> getSocreByObjID(Long examid, int objid, int wl) {
		Map<Long, Double> res = null;
		String sql = "SELECT subjectid,avgs FROM dw_agg_xinjiang_totalscore t WHERE t.examId=? AND t.objId=? AND wl=? AND t.scoreType=1 AND reliability IS NOT NULL;";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, examid);
		query.setParameter(1, objid);
		query.setParameter(2, wl);
		List list = query.list();
		if(list!=null && list.size()>0){
			res = new HashMap<Long, Double>();
			for(Object o : list){
				Object[] obj = (Object[]) o;
				res.put(Long.parseLong(obj[0].toString()), Double.parseDouble(obj[1].toString()));
			}
		}
		return res;
	}

}
