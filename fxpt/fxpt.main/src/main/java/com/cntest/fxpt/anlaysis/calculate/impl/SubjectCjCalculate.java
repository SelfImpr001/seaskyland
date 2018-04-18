/*
 * @(#)com.cntest.fxpt.anlaysis.calculate.impl.SubjectCjCalculate.java	1.0 2014年11月28日:下午2:18:48
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate.impl;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.GroupScore;
import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;
import com.cntest.fxpt.anlaysis.bean.TargetResult;
import com.cntest.fxpt.anlaysis.bean.TargetResultContainer;
import com.cntest.fxpt.anlaysis.calculate.ICalcluator;
import com.cntest.fxpt.anlaysis.uitl.CalculateHelper;
import com.cntest.fxpt.anlaysis.uitl.Container;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 下午2:18:48
 * @version 1.0
 */
public class SubjectCjCalculate implements ICalcluator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.calculate.ICalcluator#calculate(com.cntest.fxpt
	 * .anlaysis.bean.CalculateTask)
	 */
	@Override
	public void calculate(CalculateTask event, CalculateResult cr,
			SubjectScoreContainer ssc) {

		calculateAll(ssc, cr);

		SubjectScoreContainer[] hlssc = ssc.getSubjectScoreContainer();
		calculateHL(hlssc[0], cr, "h");
		calculateHL(hlssc[1], cr, "l");

		TargetResultContainer targetResults = cr.getTotalScoreTargetResults();
		targetResults.put("bkrs", new TargetResult("bkrs", event
				.getSubjectScoreContainer().size()));
		calculateReliability(cr);
	}

	private void calculateReliability(CalculateResult cr) {
		Container<Long, TargetResultContainer> itemTargetResult = cr
				.getItemTargetResult();
		int titleNum = itemTargetResult.size();
		if (titleNum > 0) {
			double varianceSum = 0;
			for (TargetResultContainer trc : itemTargetResult.toList()) {
				TargetResult tr = trc.get("stds");
				double std = tr.getValue();
				varianceSum += std * std;
			}

			double std = cr.getTotalScoreTargetResults().get("stds").getValue();
			double variance = std * std;
			double re = titleNum * (1 - varianceSum / variance)
					/ (titleNum - 1);
			re = Double.isNaN(re) ? 0 : re;
			re = Double.isInfinite(re) ? 0 : re;
			cr.getTotalScoreTargetResults().put("reliability",
					new TargetResult("reliability", re));
		}
	}

	private void calculateAll(SubjectScoreContainer ssc, CalculateResult cr) {

		TargetResultContainer targetResults = cr.getTotalScoreTargetResults();
		GroupScore groupScore = ssc.getGroupScore();
		CalculateHelper ch = new CalculateHelper(groupScore);
		targetResults.put("skrs", new TargetResult("skrs", ch.getTaskNum()));
		targetResults.put("avgs", new TargetResult("avgs", ch.getAvg()));
		targetResults.put("maxs", new TargetResult("maxs", ch.getMax()));
		targetResults.put("mins", new TargetResult("mins", ch.getMin()));
		targetResults.put("stds", new TargetResult("stds", ch.getStd()));
		targetResults.put("medians",
				new TargetResult("medians", ch.getMedians()));
		targetResults.put("modes", new TargetResult("modes", ch.getModes()));
		targetResults.put("skewness",
				new TargetResult("skewness", ch.getSkewness()));
		targetResults.put("kurtosis",
				new TargetResult("kurtosis", ch.getKurtosis()));

		cr.setTotalScoreScoreInfo(groupScore.getScoreInfos());
	}

	private void calculateHL(SubjectScoreContainer ssc, CalculateResult cr,
			String hlType) {
		TargetResultContainer targetResults = cr.getTotalScoreTargetResults();
		GroupScore groupScore = ssc.getGroupScore();
		CalculateHelper ch = new CalculateHelper(groupScore);
		targetResults.put(hlType + "skrs",
				new TargetResult(hlType + "skrs", ch.getTaskNum()));
		targetResults.put(hlType + "avgs",
				new TargetResult(hlType + "avgs", ch.getAvg()));
		// targetResults.put(hlType + "maxs",
		// new TargetResult(hlType + "maxs", ch.getMax()));
		// targetResults.put(hlType + "mins",
		// new TargetResult(hlType + "mins", ch.getMin()));
		// targetResults.put(hlType + "stds",
		// new TargetResult(hlType + "stds", ch.getStd()));
		// targetResults.put(hlType + "medians", new TargetResult(hlType
		// + "medians", ch.getMedians()));
		// targetResults.put(hlType + "modes", new TargetResult(hlType +
		// "modes",
		// ch.getModes()));
		// targetResults.put(hlType + "skewness", new TargetResult(hlType
		// + "skewness", ch.getSkewness()));
		// targetResults.put(hlType + "kurtosis", new TargetResult(hlType
		// + "kurtosis", ch.getKurtosis()));
	}

}
