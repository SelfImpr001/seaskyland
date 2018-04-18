/*
 * @(#)com.cntest.fxpt.anlaysis.bean.GroupScore.java	1.0 2014年11月27日:下午3:35:21
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月27日 下午3:35:21
 * @version 1.0
 */
public class GroupScore {
	private SubjectScoreContainer subjectScoreContainer;
	private Double fullScore;
	private List<ScoreInfo> scoreInfos = new ArrayList<>();

	public GroupScore(Double fullScore, List<ScoreInfo> scoreInfos) {
		this.scoreInfos = scoreInfos;
		this.fullScore = fullScore;
	}

	public SubjectScoreContainer getSubjectScoreContainer() {
		return subjectScoreContainer;
	}

	public SubjectScoreContainer getGEminScoreSubjectScoreContainer(
			Score minScore) {
		SubjectScoreContainer ssc = new SubjectScoreContainer();
		ssc.setAnalysisTestpaper(subjectScoreContainer.getAnalysisTestpaper());
		for (StudentSubjectScore sss : subjectScoreContainer.toList()) {
			if (sss.getScore().getValue() >= minScore.getValue()) {
				ssc.add(sss);
			}
		}
		return ssc;
	}

	public SubjectScoreContainer getLEMaxScoreSubjectScoreContainer(
			Score maxScore) {
		SubjectScoreContainer ssc = new SubjectScoreContainer();
		ssc.setAnalysisTestpaper(subjectScoreContainer.getAnalysisTestpaper());
		for (StudentSubjectScore sss : subjectScoreContainer.toList()) {
			if (sss.getScore().getValue() <= maxScore.getValue()) {
				ssc.add(sss);
			}
		}
		return ssc;
	}

	public void setSubjectScoreContainer(
			SubjectScoreContainer subjectScoreContainer) {
		this.subjectScoreContainer = subjectScoreContainer;
	}

	public double getFullScore() {
		return fullScore;
	}

	public List<ScoreInfo> getScoreInfos() {
		return scoreInfos;
	}

	public void sort() {
		Collections.sort(scoreInfos, new Comparator<ScoreInfo>() {
			@Override
			public int compare(ScoreInfo o1, ScoreInfo o2) {
				Double o2Value = o2.getScore().getValue();
				Double o1Value = o1.getScore().getValue();
				return o2Value.compareTo(o1Value);
			}
		});

		int rank = 1;
		int num = 0;
		for (ScoreInfo si : scoreInfos) {
			si.setRank(rank + num);
			num += si.getNum();
			si.setAddNum(num);
		}
	}

	public int size() {
		return scoreInfos.size();
	}

	public boolean isEmpty() {
		return scoreInfos.isEmpty();
	}

	public List<ScoreInfo> getScoreInfos(int taskNum, double quartile) {
		int sumNum = 0;
		int idx = 0;
		ArrayList<ScoreInfo> result = new ArrayList<>();
		for (ScoreInfo si : scoreInfos) {
			sumNum += si.getNum();
			result.add(si.clone());
			if (sumNum * 1.0 / taskNum >= quartile) {
				break;
			}
		}
		return result;
	}

	public List<ScoreInfo> getScoreInfosForBack(int taskNum, double quartile) {
		int sumNum = 0;
		int size = size();
		ArrayList<ScoreInfo> result = new ArrayList<>();
		for (int i = size - 1; i >= 0; i--) {
			ScoreInfo si = scoreInfos.get(i);
			sumNum += si.getNum();
			result.add(si.clone());
			if (sumNum * 1.0 / taskNum >= quartile) {
				break;
			}
		}
		return result;
	}
}
