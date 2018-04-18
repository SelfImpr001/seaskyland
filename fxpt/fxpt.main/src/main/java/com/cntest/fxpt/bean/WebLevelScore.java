/*
 * @(#)com.cntest.fxpt.bean.WebLevelScore.java	1.0 2015年4月15日:下午1:17:34
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.List;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.LeveScoreSetting;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月15日 下午1:17:34
 * @version 1.0
 */
public class WebLevelScore {
	private AnalysisTestpaper analysisTestpaper;
	private int wl;

	private List<LeveScoreSetting> leveScoreSettings;

	public int getWl() {
		return wl;
	}

	public List<LeveScoreSetting> getLeveScoreSettings() {
		return leveScoreSettings;
	}

	public AnalysisTestpaper getAnalysisTestpaper() {
		return analysisTestpaper;
	}

	public WebLevelScore setAnalysisTestpaper(AnalysisTestpaper analysisTestpaper) {
		this.analysisTestpaper = analysisTestpaper;
		return this;
	}

	public WebLevelScore setLeveScoreSettings(
			List<LeveScoreSetting> leveScoreSettings) {
		this.leveScoreSettings = leveScoreSettings;
		return this;
	}

	public WebLevelScore setWl(int wl) {
		this.wl = wl;
		return this;
	}

}
