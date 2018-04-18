/*
 * @(#)com.cntest.fxpt.repository.impl.GradeDaoImpl.java	1.0 2014年5月17日:上午10:30:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.repository.IGradeDao;
import com.cntest.common.repository.AbstractHibernateDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:30:04
 * @version 1.0
 */
@Repository("IGradeDao")
public class GradeDaoImpl extends AbstractHibernateDao<Grade, Long>
		implements IGradeDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.IGradeDao#list()
	 */
	@Override
	public List<Grade> list() {
		String hql = "from Grade as g order by g.id";
		return findByHql(hql);
	}

	@Override
	protected Class<Grade> getEntityClass() {
		return Grade.class;
	}

	@Override
	public void add(Grade grade) {
		this.save(grade);
	}

	@Override
	public Grade get(Long gradeId) {
		return super.get(gradeId);
	}

	@Override
	public int getExamNum(Grade grade) {
		String sql = "select count(1) from kn_exam where gradeId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, grade.getId());
		Object obj = sqlQuery.uniqueResult();
		int result = -1;
		if (obj instanceof Integer) {
			result = (Integer) obj;
		} else if (obj instanceof BigInteger) {
			BigInteger tmp = (BigInteger) obj;
			result = tmp.intValue();
		}
		return result;
	}

	@Override
	public List<Grade> findGradeList(String gradeName) {
		List<Grade> listGrades = new ArrayList<Grade>();
		String sql = "select * from kn_grade where name like '%"+gradeName+"%'";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				Grade grade = new Grade();
				grade.setId(Long.parseLong(obj[0].toString()));
				grade.setName(obj[1].toString());
				listGrades.add(grade);
			}
		}
		
		return listGrades;
	}

	public boolean hasName(Grade grade) {
		String sql = "SELECT COUNT(1) FROM kn_grade WHERE NAME='"+grade.getName()+"'";
		if(grade!=null && grade.getId()!=null && grade.getId()>0){
			sql = "SELECT COUNT(1) FROM kn_grade WHERE NAME='"+grade.getName()+"' AND id<>"+grade.getId();
		}
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && Integer.parseInt(list.get(0).toString())>0){
			return false;
		}
		return true;
	}

	
	
}
