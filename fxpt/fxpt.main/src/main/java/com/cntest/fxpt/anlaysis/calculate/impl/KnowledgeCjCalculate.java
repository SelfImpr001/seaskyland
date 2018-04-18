/*
 * @(#)com.cntest.fxpt.anlaysis.calculate.impl.ItemCjCalculate.java	1.0 2014年12月2日:下午3:58:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate.impl;

import java.util.List;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.GroupScore;
import com.cntest.fxpt.anlaysis.bean.ItemGroup;
import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;
import com.cntest.fxpt.anlaysis.bean.TargetResult;
import com.cntest.fxpt.anlaysis.bean.TargetResultContainer;
import com.cntest.fxpt.anlaysis.calculate.ICalcluator;
import com.cntest.fxpt.anlaysis.uitl.CalculateHelper;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.anlaysis.uitl.ItemGroupScoreGroupBuild;
import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月2日 下午3:58:52
 * @version 1.0
 */
public class KnowledgeCjCalculate implements ICalcluator {

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

		List<ItemGroup> itemGroups = ItemGroup.createItemGroupsWithItemAttr(
				"knowledge", atp.getItems(),event.getContext().isHasChoice());

		Container<String, GroupScore> itemGroupScoreMap = new ItemGroupScoreGroupBuild()
				.build(itemGroups, ssc);
		calculateItemGroup(cr, itemGroups, itemGroupScoreMap);

		SubjectScoreContainer[] hlssc = ssc.getSubjectScoreContainer();
		itemGroupScoreMap = new ItemGroupScoreGroupBuild().build(itemGroups,
				hlssc[0]);
		calculateItemGroupHL(cr, itemGroups, itemGroupScoreMap, "h");

		itemGroupScoreMap = new ItemGroupScoreGroupBuild().build(itemGroups,
				hlssc[1]);
		calculateItemGroupHL(cr, itemGroups, itemGroupScoreMap, "l");

	}

	private TargetResultContainer getTargetResultContainer(CalculateResult cr,
			ItemGroup itemGroup) {
		return cr.getKnowledgeTargetResult(itemGroup);
	}

	private void calculateItemGroup(CalculateResult cr,
			List<ItemGroup> itemGroups,
			Container<String, GroupScore> itemGroupGroupScoreMap) {
		for (ItemGroup itemGroup : itemGroups) {
			TargetResultContainer targetResults = getTargetResultContainer(cr,
					itemGroup);

			GroupScore groupScore = itemGroupGroupScoreMap.get(itemGroup
					.getName());
			CalculateHelper ch = new CalculateHelper(groupScore);

			targetResults
					.put("skrs", new TargetResult("skrs", ch.getTaskNum()));
			targetResults.put("avgs", new TargetResult("avgs", ch.getAvg()));
			targetResults.put("maxs", new TargetResult("maxs", ch.getMax()));
			targetResults.put("mins", new TargetResult("mins", ch.getMin()));
			targetResults.put("stds", new TargetResult("stds", ch.getStd()));

			targetResults.put("name",
					new TargetResult("name", itemGroup.getName()));
			targetResults.put("fullScore", new TargetResult("fullScore",
					itemGroup.getFullScore()));
			targetResults.put("titleNos", new TargetResult("titleNos",
					itemGroup.getItemNoToString()));
			targetResults.put("itemNum",
					new TargetResult("itemNum", itemGroup.getItemNum()));
		}
	}

	private void calculateItemGroupHL(CalculateResult cr,
			List<ItemGroup> itemGroups,
			Container<String, GroupScore> itemGroupGroupScoreMap, String hlType) {
		for (ItemGroup itemGroup : itemGroups) {
			TargetResultContainer targetResults = getTargetResultContainer(cr,
					itemGroup);
			GroupScore groupScore = itemGroupGroupScoreMap.get(itemGroup
					.getName());
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

}
