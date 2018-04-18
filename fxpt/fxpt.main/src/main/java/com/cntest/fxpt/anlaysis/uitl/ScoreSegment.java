/*
 * @(#)com.hyt.cntest.n.stat.ScoreSegment.java	1.0 2014年11月13日:上午10:31:33
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月13日 上午10:31:33
 * @version 1.0
 */
public class ScoreSegment {
	private int sumNum;

	private double low;
	private double uper;
	private int num;

	public int getNum() {
		return num;
	}

	public int getSumNum() {
		return sumNum;
	}

	public double getPercent() {
		return 1.0 * num / sumNum;
	}

	public int getLowInt() {
		return (int) low;
	}

	public double getLow() {
		return low;
	}

	public int getUperInt() {
		return (int) uper;
	}

	public double getUper() {
		return uper;
	}

	public ScoreSegment setLow(double low) {
		this.low = low;
		return this;
	}

	public ScoreSegment setUper(double uper) {
		this.uper = uper;
		return this;
	}

	public ScoreSegment setSumNum(int sumNum) {
		this.sumNum = sumNum;
		return this;
	}

	public ScoreSegment setNum(int num) {
		this.num = num;
		return this;
	}

	public String toString() {
		return getLowInt() + "-" + getUperInt() + "=" + num;
	}
}
