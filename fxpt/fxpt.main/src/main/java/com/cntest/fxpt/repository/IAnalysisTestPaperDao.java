/*
 * @(#)com.cntest.fxpt.repository.ITestPaperDao.java	1.0 2014年5月22日:上午9:39:12
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 上午9:39:12
 * @version 1.0
 */
public interface IAnalysisTestPaperDao {
	public void add(AnalysisTestpaper analysisTestpaper);

	public void delete(AnalysisTestpaper analysisTestpaper);

	public void deleteWithCombinationSubjectId(Long combinationSubjectId);

	public List<AnalysisTestpaper> getAndDeleteHasCombinationSubject(Long exam);

	public void deleteByTestPaperId(Long testPaperId);

	public List<AnalysisTestpaper> list(Long testPaperId);

	public List<AnalysisTestpaper> listAllWith(Long examId);

	public void updatePaperType(Long testPaperId, int paperType);

	public void updateExamAllAnalysisTestPaperPaperType(Long examId,
			int paperType);

	public AnalysisTestpaper get(Long atpId);

	public AnalysisTestpaper getWithCombinationSubjectId(
			Long combinationSubjectId);

	public List<AnalysisTestpaper> listAndCombinationSubjectIsNotNull(
			Long examId);
	
	public double findCombinationSubjectZFFromDwdimitem(Long examId,Long analysistestpaperid,String paper,String optionType);
}
