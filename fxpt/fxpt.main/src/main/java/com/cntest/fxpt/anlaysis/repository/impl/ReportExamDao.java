/*
 * @(#)com.cntest.fxpt.repository.anlaysis.impl.AnlaysisExamDao.java	1.0 2014年6月24日:下午2:01:08
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cntest.common.page.Page;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.anlaysis.repository.IReportExamDao;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月24日 下午2:01:08
 * @version 1.0
 */
@Repository("IReportExamDao")
public class ReportExamDao extends AbstractHibernateDao<Exam, Integer> implements IReportExamDao {

	@Autowired(required = false)
	@Qualifier("houseJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	private Exam createExam(Map<String, Object> row, Map<Long, Grade> gradeMap,HashMap<Long, ExamType> examtypeMap) {
		Exam exam = new Exam();
		exam.setId(Long.parseLong(row.get("id").toString()));
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
		if(row.get("typeName") != null) {
			ExamType type = new ExamType();
			type.setName((String) row.get("typeName"));
			exam.setExamType(type);	
		}

		if(row.get("gradeId") !=null) {
			Long gradeId =Long.parseLong(row.get("gradeId").toString());
			Grade grade = gradeMap.get(gradeId);
			if (grade == null) {
				grade = new Grade();
				grade.setId(gradeId);
				grade.setName((String) row.get("gradeName"));
				gradeMap.put(gradeId, grade);
			}
			exam.setGrade(grade);
		}
		if(row.get("examtypeId") !=null) {
			Long examtypeId =Long.parseLong(row.get("examtypeId").toString());
			ExamType examType = examtypeMap.get(examtypeId);
			if (examType == null) {
				examType = new ExamType();
				examType.setId(examtypeId);
				examType.setName((String) row.get("examtypeName"));
				examtypeMap.put(examtypeId, examType);
			}
			exam.setExamType(examType);
		}
		return exam;
	}

	@Override
	public List<Exam> countyRecentlyList(String countyCode) {

		SQLQuery sqlQuery = getSession().createSQLQuery(countySQL(null));
		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(10);
		sqlQuery.setParameter(0, countyCode);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();

		ArrayList<Exam> exams = new ArrayList<Exam>();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		HashMap<Long, ExamType> examtypeMap = new HashMap<Long, ExamType>();
		for (Map<String, Object> row : list) {
			exams.add(createExam(row, gradeMap,examtypeMap));
		}

		return exams;
	}

	@Override
	public List<Exam> schoolRecentlyList(String schoolCode) {

		SQLQuery sqlQuery = getSession().createSQLQuery(schoolSQL(null));
		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(10);
		sqlQuery.setParameter(0, schoolCode);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();

		ArrayList<Exam> exams = new ArrayList<Exam>();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		HashMap<Long, ExamType> examtypeMap = new HashMap<Long, ExamType>();
		for (Map<String, Object> row : list) {
			exams.add(createExam(row, gradeMap,examtypeMap));
		}

		return exams;
	}

	@Override
	public List<Exam> studentRecentlyList(String studentCode) {
		SQLQuery sqlQuery = getSession().createSQLQuery(studentSQL(null));
		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(10);
		sqlQuery.setParameter(0, studentCode);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();

		ArrayList<Exam> exams = new ArrayList<Exam>();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		HashMap<Long, ExamType> examtypeMap = new HashMap<Long, ExamType>();
		for (Map<String, Object> row : list) {
			exams.add(createExam(row, gradeMap,examtypeMap));
		}
		return exams;
	}

	@Override
	public List<Exam> countyList(String countyCode, Page<Exam> page) {

//		SQLQuery sqlQuery = getSession().createSQLQuery(
//				countyCountSQL(page.getParameter()));
		String sql = countyCountSQL(page.getParameter());
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, countyCode);
		BigInteger countNum = (BigInteger) sqlQuery.uniqueResult();
		page.setTotalRows(countNum.intValue());

		sqlQuery = getSession().createSQLQuery(countySQL(page.getParameter()));
		sqlQuery.setFirstResult((page.getCurpage() - 1) * page.getPagesize());
		sqlQuery.setMaxResults(page.getPagesize());
		sqlQuery.setParameter(0, countyCode);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();

		ArrayList<Exam> exams = new ArrayList<Exam>();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		HashMap<Long, ExamType> examtypeMap = new HashMap<Long, ExamType>();
		for (Map<String, Object> row : list) {
			exams.add(createExam(row, gradeMap,examtypeMap));
		}
		page.setList(exams);
		return exams;
	}

	private String countyCountSQL(Map<String, String> parameter) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("count(1) as num ");
		sql.append("FROM kn_exam exam  ");
		sql.append("INNER JOIN kn_grade grade ON exam.gradeId=grade.id  ");
		sql.append("INNER JOIN ( ");
		sql.append("SELECT examId FROM dw_examstudent_fact xs ");
		sql.append("INNER JOIN dw_dim_county  c ON xs.countyId=c.id  ");
		sql.append("WHERE c.code=? ");
		sql.append("GROUP BY examId ");
		sql.append(") tmp  ON exam.id = tmp.examId  ");
		sql.append("WHERE exam.levelCode<=3  ");
		sql.append(createWhere(parameter));
		sql.append("ORDER BY exam.examDate DESC,exam.id DESC ");
		return sql.toString();
	}

	private String countySQL(Map<String, String> parameter) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("exam.id, ");
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
		sql.append("exam.levelName, et.name as typeName,");
		sql.append("grade.id AS gradeId, ");
		sql.append("grade.NAME AS gradeName ");
		sql.append("FROM kn_exam exam  ");
		sql.append("INNER JOIN kn_grade grade ON exam.gradeId=grade.id  ");
		sql.append("INNER JOIN kn_examtype et ON exam.examTypeId=et.id ");
		sql.append("INNER JOIN ( ");
		sql.append("SELECT examId FROM dw_examstudent_fact xs ");
		sql.append("INNER JOIN dw_dim_county c ON xs.countyId=c.id  ");
		sql.append("WHERE c.code=? ");
		sql.append("GROUP BY examId ");
		sql.append(") tmp  ON exam.id = tmp.examId  ");
		sql.append("WHERE exam.levelCode<=3  ");
		sql.append(createWhere(parameter));
		sql.append("GROUP BY exam.examTypeId,exam.schoolYear,exam.schoolTerm,exam.examDate ");
		sql.append("ORDER BY exam.examDate DESC,exam.id DESC ");
		return sql.toString();
	}

	private String schoolCountSQL(Map<String, String> parameter) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("count(1) as num ");
		sql.append("FROM kn_exam exam  ");
		sql.append("INNER JOIN kn_grade grade ON exam.gradeId=grade.id  ");
		sql.append("INNER JOIN ( ");
		sql.append("SELECT examId FROM dw_examstudent_fact xs ");
		sql.append("INNER JOIN dw_dim_school xx ON xs.schoolId=xx.id  ");
		sql.append("WHERE xx.code=? ");
		sql.append("GROUP BY examId ");
		sql.append(") tmp  ON exam.id = tmp.examId  ");
		sql.append("WHERE exam.levelCode<=4  ");
		sql.append(createWhere(parameter));
		sql.append("ORDER BY exam.examDate DESC,exam.id DESC ");
		return sql.toString();
	}

	private String schoolSQL(Map<String, String> parameter) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("exam.id, ");
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
		sql.append("FROM kn_exam exam  ");
		sql.append("INNER JOIN kn_grade grade ON exam.gradeId=grade.id  ");
		sql.append("INNER JOIN ( ");
		sql.append("SELECT examId FROM dw_examstudent_fact xs ");
		sql.append("INNER JOIN dw_dim_school xx ON xs.schoolId=xx.id  ");
		sql.append("WHERE xx.code=? ");
		sql.append("GROUP BY examId ");
		sql.append(") tmp  ON exam.id = tmp.examId  ");
		sql.append("WHERE exam.levelCode<=4  ");
		sql.append(createWhere(parameter));
		sql.append("ORDER BY exam.examDate DESC,exam.id DESC ");
		return sql.toString();
	}

	private String studentCountSQL(Map<String, String> parameter) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("count(1) as num ");
		sql.append("FROM kn_exam exam  ");
		sql.append("INNER JOIN kn_grade grade ON exam.gradeId=grade.id  ");
		sql.append("INNER JOIN ( ");
		sql.append("SELECT examId FROM dw_examstudent_fact xs ");
		sql.append("WHERE xs.studentId=? ");
		sql.append("GROUP BY examId ");
		sql.append(") tmp  ON exam.id = tmp.examId  ");
		sql.append("WHERE 1=1  ");
		sql.append(createWhere(parameter));
		sql.append("ORDER BY exam.examDate DESC,exam.id DESC ");
		return sql.toString();
	}

	private String studentSQL(Map<String, String> parameter) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("exam.id, ");
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
		sql.append("FROM kn_exam exam  ");
		sql.append("INNER JOIN kn_grade grade ON exam.gradeId=grade.id  ");
		sql.append("INNER JOIN ( ");
		sql.append("SELECT examId FROM dw_examstudent_fact xs ");
		sql.append("WHERE xs.studentId=? ");
		sql.append("GROUP BY examId ");
		sql.append(") tmp  ON exam.id = tmp.examId  ");
		sql.append("WHERE 1=1  ");
		sql.append(createWhere(parameter));
		sql.append("ORDER BY exam.examDate DESC,exam.id DESC ");
		return sql.toString();
	}

	private String createWhere(Map<String, String> parameter) {
		if (parameter == null) {
			return "";
		}

		StringBuffer sqlWhere = new StringBuffer();

		String examName = parameter.get("examName");
		if (examName != null) {
			sqlWhere.append("   AND exam.name LIKE '%" + examName + "%'");
		}

		return sqlWhere.toString();
	}

	@Override
	public List<Exam> schoolList(String schoolCode, Page<Exam> page) {
		SQLQuery sqlQuery = getSession().createSQLQuery(
				schoolCountSQL(page.getParameter()));
		sqlQuery.setParameter(0, schoolCode);
		BigInteger countNum = (BigInteger) sqlQuery.uniqueResult();
		page.setTotalRows(countNum.intValue());

		sqlQuery = getSession().createSQLQuery(schoolSQL(page.getParameter()));
		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(10);
		sqlQuery.setParameter(0, schoolCode);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();

		ArrayList<Exam> exams = new ArrayList<Exam>();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		HashMap<Long, ExamType> examtypeMap = new HashMap<Long, ExamType>();
		for (Map<String, Object> row : list) {
			exams.add(createExam(row, gradeMap,examtypeMap));
		}
		page.setList(exams);
		return exams;
	}

	@Override
	public List<Exam> studentList(String studentCode, Page<Exam> page) {
		SQLQuery sqlQuery = getSession().createSQLQuery(
				studentCountSQL(page.getParameter()));
		sqlQuery.setParameter(0, studentCode);
		BigInteger countNum = (BigInteger) sqlQuery.uniqueResult();
		page.setTotalRows(countNum.intValue());

		sqlQuery = getSession().createSQLQuery(studentSQL(page.getParameter()));
		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(10);
		sqlQuery.setParameter(0, studentCode);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();

		ArrayList<Exam> exams = new ArrayList<Exam>();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		HashMap<Long, ExamType> examtypeMap = new HashMap<Long, ExamType>();
		for (Map<String, Object> row : list) {
			exams.add(createExam(row, gradeMap,examtypeMap));
		}
		page.setList(exams);
		return exams;
	}

	@Override
	public Exam getExam(Long examId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("exam.id, ");
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
		sql.append("grade.NAME AS gradeName, ");
		sql.append("examtype.id AS examtypeId, ");
		sql.append("examtype.NAME AS examtypeName ");
		sql.append("FROM kn_exam exam  ");
		sql.append("INNER JOIN kn_grade grade ON exam.gradeId=grade.id  ");
		sql.append("INNER JOIN kn_examtype examtype ON exam.examtypeId=examtype.id  ");
		sql.append("WHERE exam.id=?  ");

		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter(0, examId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Object obj = sqlQuery.uniqueResult();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		HashMap<Long, ExamType> examtypeMap = new HashMap<Long, ExamType>();
		return createExam((Map<String, Object>) obj, gradeMap,examtypeMap);
	}

	@Override
	protected Class<Exam> getEntityClass() {
		return Exam.class;
	}

	@Override
	public List<Exam> selectSameTermExams(Long examId) {
		Query sqlQuery = dynamicHibernateTemplate.createSQLQuery("com.cntest.fxpt.anlaysis.repository.impl.ReportExamDao.selectSameTermExams", null);
		sqlQuery.setParameter(0, examId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();
		ArrayList<Exam> exams = new ArrayList<Exam>();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		HashMap<Long, ExamType> examtypeMap = new HashMap<Long, ExamType>();
		for (Map<String, Object> row : list) {
			exams.add(createExam(row, gradeMap,examtypeMap));
		}
		return exams;
	}

}
