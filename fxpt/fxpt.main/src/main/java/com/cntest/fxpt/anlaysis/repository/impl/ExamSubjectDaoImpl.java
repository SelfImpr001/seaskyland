/*
 * @(#)com.cntest.fxpt.anlaysis.repository.impl.ExamSubjectDaoImpl.java	1.0 2014年6月26日:下午4:50:48
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.anlaysis.repository.IExamSubjectDao;
import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月26日 下午4:50:48
 * @version 1.0
 */
@Repository("IExamSubjectDao")
public class ExamSubjectDaoImpl extends AbstractDao implements IExamSubjectDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.repository.IExamSubjectDao#getSubjectByExamId
	 * (int)
	 */
	@Override
	public List<Subject> getSubjectByExamId(Long examId) {
		String sql = "SELECT km.id subjectId,km.name subjectName  FROM tb_dim_testpaper sj "
				+ "INNER JOIN tb_dim_subject km ON sj.subjectId=km.id "
				+ "WHERE examId=? ORDER BY km.id ASC";

		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter(0, examId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();

		ArrayList<Subject> subjects = new ArrayList<Subject>();
		for (Map<String, Object> row : list) {
			subjects.add(createSubject(row));
		}
		return subjects;
	}

	private Subject createSubject(Map<String, Object> row) {
		Subject subject = new Subject();
		subject.setId((Long) row.get("subjectId"));
		subject.setName((String) row.get("subjectName"));
		return subject;
	}

}
