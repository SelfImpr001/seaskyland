/*
 * @(#)com.cntest.fxpt.repository.impl.SubjectDaoImpl.java	1.0 2014年5月22日:下午1:44:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.repository.ISubjectDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午1:44:13
 * @version 1.0
 */
@Repository("ISubjectDao")
public class SubjectDaoImpl extends AbstractHibernateDao<Subject, Long>
		implements ISubjectDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ISubjectDao#add(com.cntest.fxpt.domain.Subject
	 * )
	 */
	@Override
	public void add(Subject subject) {
		this.save(subject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ISubjectDao#delete(com.cntest.fxpt.domain.
	 * Subject)
	 */
	@Override
	public void delete(Subject subject) {
		super.delete(subject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ISubjectDao#update(com.cntest.fxpt.domain.
	 * Subject)
	 */
	@Override
	public void update(Subject subject) {
		super.update(subject);
	}

	@Override
	public Subject get(Long subjectId) {
		return super.get(subjectId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.ISubjectDao#update()
	 */
	@Override
	public List<Subject> list() {
		//String hql = "from Subject where ZF=false order by ordernum";
		String hql = "from Subject order by ordernum";
		return findByHql(hql);
	}

	@Override
	protected Class<Subject> getEntityClass() {
		return Subject.class;
	}

	@Override
	public int getTestPaperNum(Subject subject) {
		String sql = "select count(1) from dw_dim_analysis_testpaper where subjectId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, subject.getId());
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
	public int getItemNum(Subject subject) {
		String sql = "select count(1) from kn_item where subjectId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, subject.getId());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ISubjectDao#findSubjectList(java.lang.String)
	 */
	@Override
	public List<Subject> findSubjectList(String subjectName) {
		List<Subject> listsubjects = new ArrayList<Subject>();
		String sql = "select * from kn_subject where name like '%"
				+ subjectName + "%' order by ordernum";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				Subject subject = new Subject();
				subject.setId(Long.parseLong(obj[0].toString()));
				subject.setName(obj[1].toString());
				subject.setOrdernum(Integer.parseInt(obj[4].toString()));
				listsubjects.add(subject);
			}
		}

		return listsubjects;
	}

	@Override
	public Subject findSubjectListWith(String subjectName) {
		String hql = "from Subject where name=? order by id desc";
		Subject subject = findEntityByHql(hql, subjectName);
		return subject;
	}

	public boolean hasName(Subject subject) {
		String sql = "SELECT COUNT(1) FROM kn_subject WHERE NAME='"
				+ subject.getName() + "'";
		if (subject != null && subject.getId() != null && subject.getId() > 0) {
			sql = "SELECT COUNT(1) FROM kn_subject WHERE NAME='"
					+ subject.getName() + "' AND id<>" + subject.getId();
		}
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0
				&& Integer.parseInt(list.get(0).toString()) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Subject getZF() {
		String hql = "from Subject where ZF=true";
		return findEntityByHql(hql);
	}

}
