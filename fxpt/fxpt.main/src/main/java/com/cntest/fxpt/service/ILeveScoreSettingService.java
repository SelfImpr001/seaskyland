/*
 * @(#)com.cntest.fxpt.service.ILeveScoreSettingService.java	1.0 2015年4月13日:上午9:40:26
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;
import java.util.Map;

import com.cntest.fxpt.bean.WebLevelScore;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.LeveScoreSetting;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月13日 上午9:40:26
 * @version 1.0
 */
public interface ILeveScoreSettingService {

	public void adds(AnalysisTestpaper atp,int wl,
			List<LeveScoreSetting> leveScoreSettings);

	public List<LeveScoreSetting> find(Long atpId, int wl);

	public Map<String, List<WebLevelScore>> find(Exam exam);
}
