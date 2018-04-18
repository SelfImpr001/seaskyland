/*
 * @(#)com.cntest.fxpt.service.ICombinationSubject.java	1.0 2014年6月12日:下午3:42:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.CombinationSubject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午3:42:13
 * @version 1.0
 */
public interface ICombinationSubjectService {
	public void add(CombinationSubject combinationSubject);

	public void add(Long examId, List<CombinationSubject> combinationSubjects);

	public void delete(CombinationSubject combinationSubject);

	public void delete(Long examId);

	public void update(CombinationSubject combinationSubject);

	public CombinationSubject get(Long combinationSubjectId);

	public List<CombinationSubject> listByExamId(Long examId);

}
