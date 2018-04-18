/*
 * @(#)com.cntest.fxpt.repository.impl.CombinationSubjectDaoImpl.java	1.0 2014年6月12日:下午3:45:36
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.CombinationSubject;
import com.cntest.fxpt.repository.ICombinationSubjectDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午3:45:36
 * @version 1.0
 */
@Repository("ICombinationSubjectDao")
public class CombinationSubjectDaoImpl extends
		AbstractHibernateDao<CombinationSubject, Long> implements
		ICombinationSubjectDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ICombinationSubjectDao#add(com.cntest.fxpt
	 * .domain.CombinationSubject)
	 */
	@Override
	public void add(CombinationSubject combinationSubject) {
		// this.save(combinationSubject);
		this.saveOrUpdate(combinationSubject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ICombinationSubjectDao#delete(com.cntest.fxpt
	 * .domain.CombinationSubject)
	 */
	@Override
	public void delete(CombinationSubject combinationSubject) {
		super.delete(combinationSubject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ICombinationSubjectDao#update(com.cntest.fxpt
	 * .domain.CombinationSubject)
	 */
	@Override
	public void update(CombinationSubject combinationSubject) {
		this.saveOrUpdate(combinationSubject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.ICombinationSubjectDao#get(int)
	 */
	@Override
	public CombinationSubject get(Long combinationSubjectId) {
		return super.get(combinationSubjectId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.ICombinationSubjectDao#listByExamId(int)
	 */
	@Override
	public List<CombinationSubject> listByExamId(Long examId) {
		String hql = "from CombinationSubject where exam.id=? and isSysCreate=false";
		return findByHql(hql, examId);
	}

	@Override
	public List<CombinationSubject> list(Long examId) {
		String hql = "from CombinationSubject where exam.id=?";
		return findByHql(hql, examId);
	}

	@Override
	protected Class<CombinationSubject> getEntityClass() {
		return CombinationSubject.class;
	}

	@Override
	public void delete(Long examId) {
		String hql = "delete from CombinationSubject where exam.id=? and isSysCreate=false";
		Query query = getSession().createQuery(hql);
		query.setLong(0, examId);
		query.executeUpdate();
	}

	@Override
	public List<CombinationSubject> list(Long examId, boolean isSysCreate) {
		String hql = "from CombinationSubject where exam.id=? and isSysCreate=?";
		return findByHql(hql, examId, isSysCreate);
	}

	@Override
	public void flushSession() {
		getSession().flush();
		getSession().clear();
	}

}
