/*
 * @(#)com.cntest.fxpt.repository.impl.ExamClassDaoImpl.java	1.0 2014年11月25日:下午4:35:59
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.ExamClass;
import com.cntest.fxpt.repository.IExamClassDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午4:35:59
 * @version 1.0
 */
@Repository("IExamClassDao")
public class ExamClassDaoImpl extends AbstractHibernateDao<ExamClass, Long>
		implements IExamClassDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.IExamClassDao#list(java.lang.Long)
	 */
	@Override
	public List<ExamClass> list(Long examId) {
		String hql = "from ExamClass where exam.id=?";
		return findByHql(hql, examId);
	}

	@Override
	public List<ExamClass> list(Long examId, String schoolCode) {
		String hql = "from ExamClass where exam.id=? and schoolCode=?";
		return findByHql(hql, examId, schoolCode);
	}

	@Override
	public ExamClass get(String schoolCode, String code) {
		String hql = "from ExamClass where schoolCode=? and code=?";
		return findEntityByHql(hql, schoolCode, code);
	}

	@Override
	protected Class<ExamClass> getEntityClass() {
		return ExamClass.class;
	}

}
