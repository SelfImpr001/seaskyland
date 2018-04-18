/*
 * @(#)com.cntest.fxpt.repository.impl.SchoolDaoImpl.java	1.0 2014年6月3日:上午8:46:37
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.remote.repository.impl;

import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.foura.domain.User;
import com.cntest.remote.domain.NanShanData;
import com.cntest.remote.repository.INanShanDataDao;
import com.cntest.util.DateUtil;

/**
 * @author 吕萌 2016年12月6日
 * @version 1.0
 */
@Repository("INanShanDataDao")
public class NanShanDataDaoImpl extends AbstractHibernateDao<NanShanData, Long> implements INanShanDataDao {

	@Override
	protected Class<NanShanData> getEntityClass() {
		return NanShanData.class;
	}
	
	@Override
	public void add(NanShanData nanShanData) {
		this.save(nanShanData);
	}
	
	@Override
	public NanShanData findByUid(String uid) {
		String hql = " from NanShanData where uid=?";
		return findEntityByHql(hql, uid);
	}

	@Override
	public Query getQueryByUser(String loginId,User user,Map<String,String> parameters) {
		
		String userNickName=user.getUserInfo()!=null?user.getUserInfo().getNickName():"";
		String sql="SELECT "+
			  " exam.id,"+
			  " exam.name,"+
			  " exam.sortName,"+
			  " exam.examDate,"+
			  " exam.isWlForExamStudent,"+
			  " exam.examStudentJiebie,"+
			  " exam.schoolYear,"+
			 " exam.schoolTerm,"+
			  " a.studentId AS ownerCode,"+
			 " exam.ownerName,"+
			  " exam.levelCode,"+
			  " exam.levelName,"+
			 " et.name as typeName,"+
			  "grade.id                AS gradeId,"+
			 " grade.NAME              AS gradeName"+
			" FROM dw_examstudent_fact a"+
			  " INNER JOIN kn_exam exam"+
			  "  ON exam.id = a.examId AND exam.STATUS = 5"+
			 " INNER JOIN kn_examtype et"+
			   " ON et.id = exam.examTypeId"+
			 " INNER JOIN kn_grade grade"+
			   " ON exam.gradeId = grade.id"+
			  " INNER JOIN dw_dim_school b"+
			 "   ON b.id = a.schoolId"+
			    "  AND a.NAME = '"+userNickName+
			  "'   AND a.studentId = '"+loginId+
			"' WHERE 1=1   ";        
		sql=getSelectSql(sql,parameters);
		Query sqlQuery = createSQLQuery(sql);
		return sqlQuery;
	}
	
	private String getSelectSql(String sql,Map<String,String> parameters){
		if (parameters != null && parameters.size() > 0) {
			Set keys = parameters.keySet();
			int i = 0;
			for (Object o : keys) {
//				query.setParameter(i++, (parameters.get(o)[0]+""));
				switch (o+"") {
				case "schoolYear" :
					sql+=" and exam.schoolYear like '%"+Integer.valueOf(parameters.get(o))+"%' ";
					break;
				case "schoolTerm" :
					sql+=" and exam.schoolTerm like '%"+Integer.valueOf(parameters.get(o))+"%' ";
					break;
				case "examDate" :
					sql+=" and exam.examDate like '%"+Integer.valueOf(parameters.get(o))+"%' ";
					break;
				case "examTypeId" :
					sql+=" and exam.examTypeId like '%"+Integer.valueOf(parameters.get(o))+"%' ";
					break;
				case "gradeid" :
					sql+=" and exam.gradeid like '%"+Integer.valueOf(parameters.get(o))+"%' ";
					break;
				}
			}
		}
		return sql;
	}

}
