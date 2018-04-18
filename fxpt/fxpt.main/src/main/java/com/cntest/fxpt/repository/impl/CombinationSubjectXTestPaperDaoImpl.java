/*
 * @(#)com.cntest.fxpt.repository.impl.CombinationSubjectXTestPaperDaoImpl.java	1.0 2014年6月12日:下午5:12:16
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.CombinationSubjectXTestPaper;
import com.cntest.fxpt.repository.ICombinationSubjectXTestPaperDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午5:12:16
 * @version 1.0
 */
@Repository("ICombinationSubjectXTestPaperDao")
public class CombinationSubjectXTestPaperDaoImpl extends
		AbstractHibernateDao<CombinationSubjectXTestPaper, Long> implements
		ICombinationSubjectXTestPaperDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.ICombinationSubjectXTestPaperDao#
	 * deleteByCombinationSubjectId(int)
	 */
	@Override
	public void deleteByCombinationSubjectId(Long combinationSubjectId) {
		String hql = "delete from CombinationSubjectXTestPaper where combinationSubject.id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, combinationSubjectId);
		query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ICombinationSubjectXTestPaperDao#saves(java
	 * .util.List)
	 */
	@Override
	public void saves(
			List<CombinationSubjectXTestPaper> combinationSubjectXTestPapers) {
		for (CombinationSubjectXTestPaper csXtp : combinationSubjectXTestPapers) {
			save(csXtp);
		}
	}

	@Override
	protected Class<CombinationSubjectXTestPaper> getEntityClass() {
		return CombinationSubjectXTestPaper.class;
	}

	@Override
	public void delete(Long examId) {
		String sql = "DELETE a "
				+ "FROM kn_combinationsubjectxtestpaper a "
				+ "INNER JOIN kn_combinationsubject b ON a.combinationSubjectId=b.id "
				+ "WHERE  b.examId=? AND b.isSysCreate=0";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, examId);
		query.executeUpdate();
	}

}
