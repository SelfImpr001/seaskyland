/*
 * @(#)com.cntest.fxpt.repository.ILeveScoreSettingDao.java	1.0 2015年4月13日:上午9:29:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.LeveScoreSetting;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月13日 上午9:29:04
 * @version 1.0
 */
public interface ILeveScoreSettingDao {

	public int delete(AnalysisTestpaper analysisTestpaper);

	public void add(LeveScoreSetting leveScoreSetting);

	public List<LeveScoreSetting> find(Exam exam,Long atpId, int wl);

	public List<LeveScoreSetting> find(Exam exam);
}
