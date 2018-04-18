/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.ItemGroupScoreGroupBuild.java	1.0 2014年12月3日:上午10:30:51
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cntest.fxpt.anlaysis.bean.GroupScore;
import com.cntest.fxpt.anlaysis.bean.ItemGroup;
import com.cntest.fxpt.anlaysis.bean.Score;
import com.cntest.fxpt.anlaysis.bean.ScoreInfo;
import com.cntest.fxpt.anlaysis.bean.StudentSubjectScore;
import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月3日 上午10:30:51
 * @version 1.0
 */
public class ItemGroupScoreGroupBuild {

	public Container<String, GroupScore> build(List<ItemGroup> itemGroups,
			SubjectScoreContainer ssc) {
		return calcluateItemGroupScoreGroup(itemGroups, ssc);
	}

	private Container<String, GroupScore> calcluateItemGroupScoreGroup(
			List<ItemGroup> itemGroups, SubjectScoreContainer ssc) {
		HashMap<String, HashMap<Double, ScoreInfo>> itemGroupScoreInfoMap = new HashMap<>();
		for (ItemGroup itemGroup : itemGroups) {
			for (StudentSubjectScore sss : ssc.toList()) {
				Score score = sss.getItemGroupScore(itemGroup);
				addScoreToMap(itemGroupScoreInfoMap, itemGroup, score);
			}
		}

		Container<String, GroupScore> itemGroupGroupScoreMap = new Container<>();
		for (ItemGroup itemGroup : itemGroups) {
			GroupScore groupScore = new GroupScore(itemGroup.getFullScore(),
					new ArrayList<>(itemGroupScoreInfoMap.get(
							itemGroup.getName()).values()));
			itemGroupGroupScoreMap.put(itemGroup.getName(), groupScore);
		}

		return itemGroupGroupScoreMap;
	}

	private void addScoreToMap(
			HashMap<String, HashMap<Double, ScoreInfo>> itemGroupScoreInfoMap,
			ItemGroup itemGroup, Score score) {
		HashMap<Double, ScoreInfo> scoreInfoMap = itemGroupScoreInfoMap
				.get(itemGroup.getName());
		if (scoreInfoMap == null) {
			scoreInfoMap = new HashMap<>();
			itemGroupScoreInfoMap.put(itemGroup.getName(), scoreInfoMap);
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
