/*
 * @(#)com.cntest.fxpt.repository.impl.ExamPaprameterDaoImpl.java	1.0 2014年6月12日:下午5:28:40
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.domain.ExamPaprameter;
import com.cntest.fxpt.repository.IExamPaprameterDao;
import com.cntest.common.repository.AbstractHibernateDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午5:28:40
 * @version 1.0
 */
@Repository("IExamPaprameterDao")
public class ExamPaprameterDaoImpl extends
		AbstractHibernateDao<ExamPaprameter, Long> implements
		IExamPaprameterDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.IIExamPaprameterDao#listByExamId(int)
	 */
	@Override
	public List<ExamPaprameter> listByExamId(Long examId) {
		String hql = "from ExamPaprameter where exam.id=?";
		return findByHql(hql, examId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.IIExamPaprameterDao#saves(java.util.List)
	 */
	@Override
	public void saves(List<ExamPaprameter> examPaprameters) {
		for (ExamPaprameter ep : examPaprameters) {
			save(ep);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.IIExamPaprameterDao#updates(java.util.List)
	 */
	@Override
	public void updates(List<ExamPaprameter> examPaprameters) {
		for (ExamPaprameter ep : examPaprameters) {
			super.update(ep);
		}
	}

	@Override
	protected Class<ExamPaprameter> getEntityClass() {
		return ExamPaprameter.class;
	}

	@Override
	public void deleteParameterByExamId(Long examId) {
		String hql = "delete from ExamPaprameter where exam.id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, examId);
		query.executeUpdate();
	}

}
