/*
 * @(#)com.hyt.cntest.output.template.model.CalculateSegment.java	1.0 2014年8月19日:上午10:54:24
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.util.ArrayList;
import java.util.List;

import com.cntest.fxpt.anlaysis.bean.GroupScore;
import com.cntest.fxpt.anlaysis.bean.ScoreInfo;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年8月19日 上午10:54:24
 * @version 1.0
 */
public class StatSegment {
	private GroupScore scoreGroup;

	private double begin;
	private double end;
	private double step;
	private int numSun;

	public StatSegment(double begin, double end, double step) {
		this.begin = begin;
		this.end = end;
		this.step = step;
	}

	public void setScoreGroup(GroupScore scoreGroup) {
		this.scoreGroup = scoreGroup;
	}

	public int getSegmentCount() {
		int count = (int) Math.ceil((end - begin) / step);
		if (begin != 0d) {
			count++;
		}

		if (end != getFullScore()) {
			count++;
		}
		return count;
	}

	public int getScorePosition(double score) {

		if (score < begin) {
			return 0;
		} else if (score >= end) {
			return getSegmentCount() - 1;
		} else {
			int tmp = (int) Math.floor((score - begin) / step);
			if (begin != 0d) {
				tmp++;
			}
			return tmp;
		}
	}

	public List<ScoreSegment> getScoreSegment() {
		ArrayList<ScoreSegment> result = new ArrayList<ScoreSegment>();
		int[] nums = calculate();
		int count = getSegmentCount();
		double low = 0;
		double uper = 0;
		for (int i = 0; i < count; i++) {
			if (i == 0 && begin != 0) {
				uper = begin;
			} else if (i == count - 1) {
				uper = getFullScore();
			} else if (i == count - 2 && end != getFullScore()) {
				uper = end;
			} else {
				uper = low + step;

			}

			result.add(new ScoreSegment().setLow(low).setUper(uper)
					.setNum(nums[i]).setSumNum(numSun));
			low = uper;
		}

		return result;
	}

	private int[] calculate() {
		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfos();
		int count = getSegmentCount();
		int[] nums = new int[count];
		for (ScoreInfo gsi : scoreInfos) {
			double score = gsi.getScore().getValue();
			int num = gsi.getNum();
			numSun += num;
			int scorePosition = getScorePosition(score);
			nums[scorePosition] = nums[scorePosition] + num;
		}

		return nums;
	}

	public void print() {
		int count = getSegmentCount();
		double low = 0;
		double uper = 0;
		for (int i = 0; i < count; i++) {
			if (i == 0 && begin != 0) {
				uper = begin;
			} else if (i == count - 1) {
				uper = getFullScore();
			} else if (i == count - 2 && end != getFullScore()) {
				uper = end;
			} else {
				uper = low + step;

			}

			System.out.println(i + "===>" + low + "-" + uper);
			low = uper;
		}

	}

	private double getFullScore() {
		double fullScore = 150;
		if (scoreGroup != null) {
			fullScore = scoreGroup.getFullScore();
		}
		return fullScore;
	}

}
