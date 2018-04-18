/*
 * @(#)com.cntest.fxpt.anlaysis.calculate.impl.ItemCjCalculate.java	1.0 2014年12月2日:下午3:58:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.GroupScore;
import com.cntest.fxpt.anlaysis.bean.ItemScore;
import com.cntest.fxpt.anlaysis.bean.Score;
import com.cntest.fxpt.anlaysis.bean.ScoreInfo;
import com.cntest.fxpt.anlaysis.bean.StudentSubjectScore;
import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;
import com.cntest.fxpt.anlaysis.bean.TargetResult;
import com.cntest.fxpt.anlaysis.bean.TargetResultContainer;
import com.cntest.fxpt.anlaysis.calculate.ICalcluator;
import com.cntest.fxpt.anlaysis.uitl.CalculateHelper;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Item;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月2日 下午3:58:52
 * @version 1.0
 */
public class ItemCjCalculate implements ICalcluator {

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
		AnalysisTestpaper atp = event.getAnalysisTestpaper();

		List<Item> items = atp.getItems();

		HashMap<Long, GroupScore> itemGroupScoreMap = calcluateItemScoreGroup(
				items, ssc);
		calculateItem(cr, items, itemGroupScoreMap);
		statSelOption(cr, items, "", ssc);

		SubjectScoreContainer[] hlssc = ssc.getSubjectScoreContainer();
		itemGroupScoreMap = calcluateItemScoreGroup(items, hlssc[0]);
		calculateItemHL(cr, items, itemGroupScoreMap, "h");
		statSelOption(cr, items, "h", hlssc[0]);

		itemGroupScoreMap = calcluateItemScoreGroup(items, hlssc[1]);
		calculateItemHL(cr, items, itemGroupScoreMap, "l");
		statSelOption(cr, items, "l", hlssc[1]);
	}

	private void calculateItem(CalculateResult cr, List<Item> items,
			HashMap<Long, GroupScore> itemGroupScoreMap) {
		for (Item item : items) {
			TargetResultContainer targetResults = cr.getItemTargetResult(item);

			GroupScore groupScore = itemGroupScoreMap.get(item.getId());
			CalculateHelper ch = new CalculateHelper(groupScore);

			targetResults
					.put("skrs", new TargetResult("skrs", ch.getTaskNum()));
			targetResults.put("avgs", new TargetResult("avgs", ch.getAvg()));
			targetResults.put("maxs", new TargetResult("maxs", ch.getMax()));
			targetResults.put("mins", new TargetResult("mins", ch.getMin()));
			targetResults.put("stds", new TargetResult("stds", ch.getStd()));

			targetResults.put("name",
					new TargetResult("name", item.getItemNo()));
			targetResults.put("fullScore",
					new TargetResult("fullScore", item.getFullScore()));
		}
	}

	private void statSelOption(CalculateResult cr, List<Item> items,
			String hlType, SubjectScoreContainer ssc) {
		for (Item item : items) {
			if (item.getOptionType() == 1) {
				calculateSingleOption(item, ssc, cr.getItemTargetResult(item),
						hlType);
			} else if (item.getOptionType() == 2) {
				calcualteMultiOption(item, ssc, cr.getItemTargetResult(item),
						hlType);
			}
		}
	}

	private void calculateSingleOption(Item item, SubjectScoreContainer ssc,
			TargetResultContainer targetResults, String hlType) {
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		int o = 0;
		for (StudentSubjectScore sss : ssc.toList()) {
			ItemScore itemScore = sss.getItemScore(item);
			String selOption = itemScore.getSelOption();
			if (selOption.contains("A")) {
				a++;
			} else if (selOption.contains("B")) {
				b++;
			} else if (selOption.contains("C")) {
				c++;
			} else if (selOption.contains("D")) {
				d++;
			} else {
				o++;
			}
		}

		targetResults
				.put(hlType + "ANum", new TargetResult(hlType + "ANum", a));
		targetResults
				.put(hlType + "BNum", new TargetResult(hlType + "BNum", b));
		targetResults
				.put(hlType + "CNum", new TargetResult(hlType + "CNum", c));
		targetResults
				.put(hlType + "DNum", new TargetResult(hlType + "DNum", d));
		targetResults.put(hlType + "OtherNum", new TargetResult(hlType
				+ "OtherNum", o));
	}

	private void calcualteMultiOption(Item item, SubjectScoreContainer ssc,
			TargetResultContainer targetResults, String hlType) {
		int erro = 0;
		int halfRight = 0;
		int allRight = 0;

		for (StudentSubjectScore sss : ssc.toList()) {
			ItemScore itemScore = sss.getItemScore(item);
			Score score = itemScore.getScore();
			if (score.getValue() == 0) {
				erro++;
			} else if (score.getValue() == item.getFullScore()) {
				allRight++;
			} else {
				halfRight++;
			}
		}

		targetResults.put(hlType + "erro", new TargetResult(hlType + "erro",
				erro));
		targetResults.put(hlType + "halfRight", new TargetResult(hlType
				+ "halfRight", halfRight));
		targetResults.put(hlType + "allRight", new TargetResult(hlType
				+ "allRight", allRight));
	}

	private void calculateItemHL(CalculateResult cr, List<Item> items,
			HashMap<Long, GroupScore> itemGroupScoreMap, String hlType) {
		for (Item item : items) {
			TargetResultContainer targetResults = cr.getItemTargetResult(item);
			GroupScore groupScore = itemGroupScoreMap.get(item.getId());
			CalculateHelper ch = new CalculateHelper(groupScore);
			targetResults.put(hlType + "skrs", new TargetResult(
					hlType + "skrs", ch.getTaskNum()));
			targetResults.put(hlType + "avgs", new TargetResult(
					hlType + "avgs", ch.getAvg()));
			// targetResults.put(hlType + "maxs", new TargetResult(
			// hlType + "maxs", ch.getMax()));
			// targetResults.put(hlType + "mins", new TargetResult(
			// hlType + "mins", ch.getMin()));
			// targetResults.put(hlType + "stds", new TargetResult(
			// hlType + "stds", ch.getStd()));
		}
	}

	private HashMap<Long, GroupScore> calcluateItemScoreGroup(List<Item> items,
			SubjectScoreContainer ssc) {
		HashMap<Long, HashMap<Double, ScoreInfo>> itemScoreInfoMap = new HashMap<>();
		for (Item item : items) {
			for (StudentSubjectScore sss : ssc.toList()) {
				ItemScore itemScore = sss.getItemScore(item);
				Score score = itemScore.getScore();
				addScoreToMap(itemScoreInfoMap, item, score);
			}
		}

		HashMap<Long, GroupScore> itemGroupScoreMap = new HashMap<>();
		for (Item item : items) {
			GroupScore groupScore = new GroupScore(
					item.getFullScore(),
					new ArrayList<>(itemScoreInfoMap.get(item.getId()).values()));
			itemGroupScoreMap.put(item.getId(), groupScore);
		}

		return itemGroupScoreMap;
	}

	private void addScoreToMap(
			HashMap<Long, HashMap<Double, ScoreInfo>> itemScoreInfoMap,
			Item item, Score score) {
		HashMap<Double, ScoreInfo> scoreInfoMap = itemScoreInfoMap.get(item
				.getId());
		if (scoreInfoMap == null) {
			scoreInfoMap = new HashMap<>();
			itemScoreInfoMap.put(item.getId(), scoreInfoMap);
		}

		ScoreInfo si = scoreInfoMap.get(score.getValue());
		if (si == null) {
			si = new ScoreInfo();
			si.setScore(score);
			scoreInfoMap.put(score.getValue(), si);
		}
		si.setNum(si.getNum() + 1);

	}
}
