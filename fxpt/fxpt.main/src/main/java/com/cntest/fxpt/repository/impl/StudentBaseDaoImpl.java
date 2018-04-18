/*
 * @(#)com.cntest.fxpt.repository.impl.StudentBaseDaoImpl.java	1.0 2014年10月9日:下午3:50:50
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.RootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.cntest.common.query.Query;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.domain.StudentBase;
import com.cntest.fxpt.repository.IClazzDao;
import com.cntest.fxpt.repository.ISchoolDao;
import com.cntest.fxpt.repository.IStudentBaseDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月9日 下午3:50:50
 * @version 1.0
 */
@Repository("IStudentBaseDao")
public class StudentBaseDaoImpl extends AbstractHibernateDao<StudentBase, Integer> implements IStudentBaseDao{

	@Autowired(required = false)
	@Qualifier("IClazzDao")
	private IClazzDao clazzDao ;
	
	@Autowired(required = false)
	@Qualifier("ISchoolDao")
	private ISchoolDao schoolDao;
	
	public Query<StudentBase> list1(Query<StudentBase> query) {
		Criteria criteria = this.getSession().createCriteria(this.getEntityClass().getName());
		criteria.createAlias("school", "sch");
		Map<String, String[]> parameter = query.getParameters();
		if (!parameter.isEmpty()) {
			String[] xh = parameter.get("xh");
			if (xh != null && !"".equals(xh[0])) {
				criteria.add(Restrictions.like("xh", "%"
						+ xh[0] + "%"));
			}
			String[] qname = parameter.get("qname");
			if (qname != null && !"".equals(qname[0])) {
				criteria.add(Restrictions.like("Name", "%"
						+ qname[0] + "%"));
			}
			String[] qschoolName = parameter.get("qschoolName");
			if (qschoolName != null && !"".equals(qschoolName[0])) {
				criteria.add(Restrictions.like("sch.name", "%"
						+ qschoolName[0] + "%"));
			}
		}
		Long rowCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		query.setTotalRows(rowCount.intValue());
		int first = (query.getCurpage() - 1) * query.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);
		List<StudentBase> listStudentBases = criteria.list();
		query.setResults(listStudentBases);
		return query;
	}
	
	
	public Query<StudentBase> list(Query<StudentBase> query) {
		Map<String, String[]> parameters = query.getParameters();
		List<StudentBase> listStudentBases = new ArrayList<StudentBase>();
		int s = 0;
		String sql = "SELECT stu.id,stu.guid,stu.name,stu.sex,stu.grade,stu.schoolCode,stu.xh FROM kn_studentbase stu"+
				  " LEFT JOIN dw_dim_school sch ON stu.schoolCode=sch.code";
		if(!parameters.isEmpty() &&(!"".equals(parameters.get("xh")[0]) || !"".equals(parameters.get("qname")[0])||!"".equals(parameters.get("qschoolName")[0]))){
			sql += " where";
			
			if (!parameters.isEmpty()) {
				String[] xh = parameters.get("xh");
				if (xh != null && !"".equals(xh[0])) {
					sql +=" stu.xh LIKE '%"+xh[0]+"%'";
					s+=1;
				}
			}
			if (!parameters.isEmpty()) {
				String[] qname = parameters.get("qname");
				if (qname != null && !"".equals(qname[0])) {
					if(s>0){
						sql +=" and stu.Name LIKE '%"+qname[0]+"%'";
					}else{
						sql +=" stu.Name LIKE '%"+qname[0]+"%'";
						s+=1;
					}
				}
			}
			if (!parameters.isEmpty()) {
				String[] qschoolName = parameters.get("qschoolName");
				if (qschoolName != null && !"".equals(qschoolName[0])) {
					if(s>0){
						sql +=" AND sch.name LIKE '%"+qschoolName[0]+"%'";
					}else{
						sql +=" sch.name LIKE '%"+qschoolName[0]+"%'";
						s+=1;
					}
				}
			}
		}
		int first = (query.getCurpage() - 1) * query.getPagesize();
		first = first < 0 ? 0 : first;
		SQLQuery sqlQuery = createSQLQuery(sql);
		int totalRows = 0;
		if(s>0){
			totalRows = sqlQuery.list().size();
		}else{
			totalRows = countRows();
		}
		sqlQuery.setFirstResult(first);
		sqlQuery.setMaxResults(query.getPagesize());
		List list = sqlQuery.list();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				StudentBase studentBase = new StudentBase();
				studentBase.setId(Integer.parseInt(obj[0].toString()));
				studentBase.setGuid(obj[1].toString());
				studentBase.setName(obj[2].toString());
				studentBase.setSex(Integer.parseInt(obj[3].toString()));
				studentBase.setGrade(obj[4].toString());
				School school = schoolDao.findWithCode(obj[5].toString());
				studentBase.setSchool(school);
				studentBase.setXh(obj[6].toString());
				listStudentBases.add(studentBase);
			}
		}
		query.setTotalRows(totalRows);
		query.setResults(listStudentBases);
		if(totalRows<1){
			query.setTotalpage(0);
			query.setHasNext(false);
		}
		
		return query;
	}

	protected Class<StudentBase> getEntityClass() {
		return StudentBase.class;
	}
	
	
	public int countRows(){
		int count = 0 ;
		String sql = "SELECT COUNT(*) FROM kn_studentbase";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0){
			count = Integer.parseInt(list.get(0).toString());
		}
		return count;
	}
	
	
	public StudentBase get(Integer studentBaseId){
		String sql = "SELECT * FROM kn_studentbase WHERE id="+studentBaseId;
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		StudentBase studentBase = new StudentBase();
		if(list!=null && list.size()>0){
				Object[] obj = (Object[]) list.get(0);
				studentBase.setId(Integer.parseInt(obj[0].toString()));
				studentBase.setGuid(obj[1].toString());
				studentBase.setName(obj[2].toString());
				studentBase.setSex(Integer.parseInt(obj[3].toString()));
				studentBase.setGrade(obj[4].toString());
				School school = schoolDao.findWithCode(obj[5].toString());
				studentBase.setSchool(school);
				studentBase.setXh(obj[6].toString());
		}
		return studentBase;
	}
	
	public void update(StudentBase studentBase){
		super.update(studentBase);
	}

	@Override
	public int getMaxId() {
		String sql = "SELECT MAX(id) FROM kn_studentbase";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && list.get(0)!=null){
			return Integer.parseInt(list.get(0).toString());
		}else{
			return 1;
		}
	}

	public void importExamStudents(Integer[] examids) {
		int n = 0;
		boolean ok = true;
		for (int i = 0; i < examids.length; i++) {
			int examid= examids[i];
			try {
			String createTemp = "CREATE TABLE temp(guid VARCHAR(50) ,NAME VARCHAR(50),xh VARCHAR(50),sex INT,grade VARCHAR(50),schoolcode VARCHAR(16))";
			SQLQuery sqlQuery = createSQLQuery(createTemp);
			sqlQuery.executeUpdate();
			String tempIndex = "ALTER TABLE temp ADD UNIQUE t_index(NAME,xh,grade,schoolCode)";
			sqlQuery = createSQLQuery(tempIndex);
			sqlQuery.executeUpdate();
			String insertTemp = "INSERT INTO temp(guid,NAME,xh,sex,grade,schoolcode) SELECT DISTINCT UUID(),s.name,s.studentId,s.gender AS sex,e.examStudentJiebieName,o.code FROM dw_examstudent_fact s LEFT JOIN"
					+ " dw_dim_school o ON s.schoolid=o.id LEFT JOIN"
					+ " kn_exam e ON s.examid=e.id WHERE s.examid="+examid+" AND o.code IS NOT NULL";
			sqlQuery = createSQLQuery(insertTemp);
			sqlQuery.executeUpdate();
			String isHaveData = "SELECT COUNT(1) FROM temp t LEFT JOIN kn_studentbase b ON b.name=t.name AND b.grade=t.grade AND b.schoolcode=t.schoolcode and b.xh=t.xh WHERE b.name IS NULL";
			sqlQuery = createSQLQuery(isHaveData);
			List count = sqlQuery.list();
				if(count!=null && Integer.parseInt(count.get(0).toString())>=1){
					String importSql = "INSERT INTO kn_studentbase (guid, NAME, sex, grade, schoolCode,xh) SELECT t.guid,t.name,t.sex,t.grade,t.schoolcode,t.xh FROM temp t LEFT JOIN kn_studentbase b ON b.name=t.name AND b.grade=t.grade AND b.schoolcode=t.schoolcode WHERE b.name IS NULL";
					sqlQuery = createSQLQuery(importSql);
					n = sqlQuery.executeUpdate();
				}
			} catch (Exception e) {
				ok = false;
			} finally{
				String dropIndex = "DROP INDEX t_index ON temp";
				SQLQuery  sqlQuery = createSQLQuery(dropIndex);
				sqlQuery.executeUpdate();
				String deleteTemp = "drop table temp";
				sqlQuery = createSQLQuery(deleteTemp);
			    sqlQuery.executeUpdate();
			    if(ok){
					String sql = "UPDATE kn_exam SET studentBaseStatus=1 WHERE id="+examid;
					sqlQuery = createSQLQuery(sql);
					sqlQuery.executeUpdate();
				}
			}
		}
	}

	public boolean haveData(String xh,String name, String schoolCode,
			String grade) {
		String sql = "SELECT COUNT(*) FROM kn_studentbase WHERE xh='"+xh+"' and NAME='"+name+"' AND schoolCode='"+schoolCode+"' AND grade='"+grade+"'";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && Integer.parseInt(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}

	public boolean isSchoolCodeExists(String schoolCode) {
		String sql = "SELECT COUNT(1) FROM dw_dim_school WHERE CODE='"+schoolCode+"'";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && Integer.parseInt(list.get(0).toString())>0){
			return true;
		}
		return false;
	}

	public boolean isClazzCodeExists(String clazzCode) {
		String sql = "SELECT COUNT(1) FROM kn_class WHERE CODE='"+clazzCode+"'";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && Integer.parseInt(list.get(0).toString())>0){
			return true;
		}
		return false;
	}

	@Override
	public StudentBase findByExample(StudentBase example) {
		StringBuilder hql = new StringBuilder("From StudentBase where 1=1");
		ArrayList<Object> params = new ArrayList<>();
		if(example.getName() != null) {
			hql.append(" and name=? ");
			params.add(example.getName());
		}
			
		if(example.getXh() != null) {
			hql.append(" and xh=?");
			params.add(example.getXh());
		}
		
		/*if(example.getSchool() != null  ) {
			hql.append(" and school.code=?");
			params.add(example.getSchool().getCode());
		}
		return findEntityByHql(hql.toString(), params.toArray(new Object[] {}));
		*/
		List<StudentBase> list = findByHql(hql.toString(), params.toArray(new Object[] {}));
		return (list.size() > 0) ? list.get(0) : null;
	}
	
}
