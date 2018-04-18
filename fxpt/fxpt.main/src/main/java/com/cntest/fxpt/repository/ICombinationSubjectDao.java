/*
 * @(#)com.cntest.fxpt.repository.ICombinationSubjectDao.java	1.0 2014年6月12日:下午3:44:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.CombinationSubject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午3:44:28
 * @version 1.0
 */
public interface ICombinationSubjectDao {
	public void add(CombinationSubject combinationSubject);

	public void delete(CombinationSubject combinationSubject);

	public void delete(Long examId);

	public void update(CombinationSubject combinationSubject);

	public CombinationSubject get(Long combinationSubjectId);

	public List<CombinationSubject> listByExamId(Long examId);

	public List<CombinationSubject> list(Long examId);

	public List<CombinationSubject> list(Long examId, boolean isSysCreate);

	public void flushSession();
}
