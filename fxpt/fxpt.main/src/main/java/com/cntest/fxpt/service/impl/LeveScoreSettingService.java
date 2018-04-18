/*
 * @(#)com.cntest.fxpt.service.impl.LeveScoreSettingService.java	1.0 2015年4月13日:上午9:41:59
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.WebLevelScore;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.LeveScoreSetting;
import com.cntest.fxpt.repository.ILeveScoreSettingDao;
import com.cntest.fxpt.service.IAnalysisTestPaperService;
import com.cntest.fxpt.service.ILeveScoreSettingService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月13日 上午9:41:59
 * @version 1.0
 */
@Service("LeveScoreSettingService")
public class LeveScoreSettingService implements ILeveScoreSettingService {

	@Autowired(required = false)
	@Qualifier("LeveScoreSettingDao")
	private ILeveScoreSettingDao lssDao;

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperService")
	private IAnalysisTestPaperService atpService;

	@Override
	public void adds(AnalysisTestpaper atp, int wl,
			List<LeveScoreSetting> leveScoreSettings) {
		lssDao.delete(atp);
		for (LeveScoreSetting lss : leveScoreSettings) {
			lss.setExam(atp.getExam());
			lss.setAnalysisTestpaper(atp);
			lss.setWl(wl);
			lss.setSubject(atp.getSubject());
			lssDao.add(lss);
		}
	}

	@Override
	public Map<String, List<WebLevelScore>> find(Exam exam) {
		List<AnalysisTestpaper> atpList = atpService.listAllWith(exam.getId());

		List<LeveScoreSetting> lssList = lssDao.find(exam);

		HashMap<String, List<LeveScoreSetting>> webLevelScoreDataMap = new HashMap<>();
		for (LeveScoreSetting lss : lssList) {
			String key = lss.getAnalysisTestpaper().getId() + "." + lss.getWl();
			List<LeveScoreSetting> row = webLevelScoreDataMap.get(key);
			if (row == null) {
				row = new ArrayList<>();
				webLevelScoreDataMap.put(key, row);
			}
			row.add(lss);
		}

		HashMap<String, List<WebLevelScore>> result = new HashMap<>();

		for (AnalysisTestpaper atp : atpList) {
			if (atp.getPaperType() == 0 && exam.isWlForExamStudent()) {
				String wl = 1 + "";
				String key = atp.getId() + "." + wl;

				lssList = webLevelScoreDataMap.get(key);
				if (lssList == null) {
					lssList = new ArrayList<>();
				}

				List<WebLevelScore> wlsList = result.get(wl);
				if (wlsList == null) {
					wlsList = new ArrayList<>();
					result.put(wl, wlsList);
				}
				WebLevelScore wls = new WebLevelScore();
				wls.setAnalysisTestpaper(atp).setWl(1)
						.setLeveScoreSettings(lssList);
				wlsList.add(wls);

				wl = 2 + "";
				key = atp.getId() + "." + wl;

				lssList = webLevelScoreDataMap.get(key);
				if (lssList == null) {
					lssList = new ArrayList<>();
				}

				wlsList = result.get(wl);
				if (wlsList == null) {
					wlsList = new ArrayList<>();
					result.put(wl, wlsList);
				}
				wls = new WebLevelScore();
				wls.setAnalysisTestpaper(atp).setWl(1)
						.setLeveScoreSettings(lssList);
				wlsList.add(wls);

			} else {
				String wl = atp.getPaperType() + "";
				String key = atp.getId() + "." + wl;
				lssList = webLevelScoreDataMap.get(key);
				if (lssList == null) {
					lssList = new ArrayList<>();
				}
				List<WebLevelScore> wlsList = result.get(wl);
				if (wlsList == null) {
					wlsList = new ArrayList<>();
					result.put(wl, wlsList);
				}
				WebLevelScore wls = new WebLevelScore();
				wls.setAnalysisTestpaper(atp).setWl(1)
						.setLeveScoreSettings(lssList);
				wlsList.add(wls);
			}
		}

		return result;
	}

	@Override
	public List<LeveScoreSetting> find(Long atpId, int wl) {
		AnalysisTestpaper atp = atpService.get(atpId);
		return lssDao.find(atp.getExam(), atpId, wl);
	}

}
