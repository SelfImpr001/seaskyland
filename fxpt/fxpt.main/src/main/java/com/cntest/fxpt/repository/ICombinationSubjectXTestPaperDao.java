/*
 * @(#)com.cntest.fxpt.repository.CombinationSubjectXTestPaperDao.java	1.0 2014年6月12日:下午5:05:58
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.CombinationSubjectXTestPaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午5:05:58
 * @version 1.0
 */
public interface ICombinationSubjectXTestPaperDao {
	public void deleteByCombinationSubjectId(Long combinationSubjectId);

	public void delete(Long examId);

	public void saves(
			List<CombinationSubjectXTestPaper> combinationSubjectXTestPapers);
}
