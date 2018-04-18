/*
 * @(#)com.cntest.fxpt.anlaysis.bean.AnalysisTestpaperContainer.java	1.0 2014年12月2日:上午11:32:07
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import java.util.ArrayList;
import java.util.List;

import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月2日 上午11:32:07
 * @version 1.0
 */
public class AnalysisTestpaperContainer extends
		Container<Long, AnalysisTestpaper> {

	public List<AnalysisTestpaper> getAnalysisTestpaperForCombinationSubject() {
		ArrayList<AnalysisTestpaper> result = new ArrayList<>();
		for (AnalysisTestpaper atp : c.values()) {
			if (atp.getCombinationSubject() != null) {
				result.add(atp);
			}
		}
		return result;
	}

	public List<AnalysisTestpaper> getAnalysisTestpaper() {
		ArrayList<AnalysisTestpaper> result = new ArrayList<>();
		for (AnalysisTestpaper atp : c.values()) {
			if (atp.getTestPaper() != null) {
				result.add(atp);
			}
		}
		return result;
	}
}
