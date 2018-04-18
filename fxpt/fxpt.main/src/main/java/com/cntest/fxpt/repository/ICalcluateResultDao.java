/*
 * @(#)com.cntest.fxpt.repository.ISaveCalcluateResultDao.java	1.0 2014年11月28日:下午2:42:58
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;
import java.util.Map;

import com.cntest.fxpt.anlaysis.bean.ScoreInfo;
import com.cntest.fxpt.anlaysis.bean.TargetResult;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 下午2:42:58
 * @version 1.0
 */
public interface ICalcluateResultDao {

	public void deleteResult(Long examId, Long objId, Long testPaperId,
			String tableName, int wl);

	public void saveTotalScoreResult(Long examId, Long objId, Long subjectId,
			Long testPaperId, List<TargetResult> targetResults,
			String tableName, int wl);

	public void saveScoreInfo(Long examId, Long objId, Long subjectId,
			Long testPaperId, int skrs, ScoreInfo scoreInfo, String tableName,
			int wl);

	public void saveItem(Long examId, Long objId, Long subjectId,
			Long testPaperId, Long itemId, List<TargetResult> targetResults,
			String tableName, int wl);

	public void saveItemGroup(Long examId, Long objId, Long subjectId,
			Long testPaperId, List<TargetResult> targetResults,
			String tableName, int wl);

	public void clearAnalysisResult(Long examId, Long analysisTestpaperId);

	public void clearAnalysisResult(Long examId);

	public void clearAnalysisResultForXinjiang(Long examId);

	public List<Map> getZfCreateRule(Long examId);

	public List<Map> getZfCreateRule(String sql);

}
