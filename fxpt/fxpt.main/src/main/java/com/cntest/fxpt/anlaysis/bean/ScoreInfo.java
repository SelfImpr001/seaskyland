/*
 * @(#)com.cntest.fxpt.anlaysis.bean.ScoreInfo.java	1.0 2014年11月27日:下午3:33:53
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月27日 下午3:33:53
 * @version 1.0
 */
public class ScoreInfo {
	private Score score;
	private int num;
	private int addNum;
	private int rank;

	public Score getScore() {
		return score;
	}

	public int getNum() {
		return num;
	}

	public int getAddNum() {
		return addNum;
	}

	public int getRank() {
		return rank;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setAddNum(int addNum) {
		this.addNum = addNum;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public ScoreInfo clone() {
		ScoreInfo tmp = new ScoreInfo();
		tmp.score = this.score;
		tmp.num = this.num;
		tmp.addNum = this.addNum;
		tmp.rank = this.rank;
		return tmp;
	}

	@Override
	public String toString() {
		return score.toString() + "--" + num + "--" + rank;
	}
}
