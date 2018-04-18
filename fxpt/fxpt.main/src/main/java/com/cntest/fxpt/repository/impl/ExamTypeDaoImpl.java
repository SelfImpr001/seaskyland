/*
 * @(#)com.cntest.fxpt.repository.impl.ExamTypeDaoImpl.java	1.0 2014年5月17日:下午2:37:11
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.repository.IExamTypeDao;
import com.cntest.common.repository.AbstractHibernateDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 下午2:37:11
 * @version 1.0
 */
@Repository("IExamTypeDao")
public class ExamTypeDaoImpl extends AbstractHibernateDao<ExamType, Long>
		implements IExamTypeDao {

	@Override
	public List<ExamType> list() {
		String hql = "from ExamType et order by et.id";
		return findByHql(hql);
	}

	@Override
	protected Class<ExamType> getEntityClass() {
		return ExamType.class;
	}

	@Override
	public void add(ExamType examType) {
		this.save(examType);
	}

	@Override
	public ExamType get(Long examTypeId) {
		return super.get(examTypeId);
	}

	@Override
	public List<ExamType> list(boolean isValid) {
		String hql = "from ExamType where valid=?";
		return findByHql(hql, isValid);
	}

	@Override
	public int getExamNum(ExamType examType) {
		String sql = "select count(1) from kn_exam where examTypeId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, examType.getId());
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
	public List<ExamType> findExamTypeList(String examTypeName) {
		List<ExamType> listExamTypes = new ArrayList<ExamType>();
		String sql = "select * from kn_examtype where name like '%"+examTypeName+"%'";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				ExamType examType = new ExamType();
				examType.setId(Long.parseLong(obj[0].toString()));
				examType.setName(obj[1].toString());
				examType.setValid(Boolean.parseBoolean(obj[2].toString()));
				listExamTypes.add(examType);
			}
		}
		
		return listExamTypes;
	}

	public boolean hasName(ExamType examType) {
		String sql = "SELECT COUNT(1) FROM kn_examtype WHERE NAME='"+examType.getName()+"'";
		if(examType!=null && examType.getId()!=null && examType.getId()>0){
			sql = "SELECT COUNT(1) FROM kn_examtype WHERE NAME='"+examType.getName()+"' AND id<>"+examType.getId();
		}
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && Integer.parseInt(list.get(0).toString())>0){
			return false;
		}
		return true;
	}

}
