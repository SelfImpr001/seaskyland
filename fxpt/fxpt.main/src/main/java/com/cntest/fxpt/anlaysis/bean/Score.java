/*
 * @(#)com.cntest.fxpt.anlaysis.bean.Score.java	1.0 2014年11月24日:下午1:23:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:23:45
 * @version 1.0
 */
public class Score implements PrimaryKey<Double> {
	private Double value = 0d;

	@Override
	public Double getPk() {
		return value;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Score add(Score score) {
		value += score.value;
		return this;
	}

	@Override
	public String toString() {
		return value + "";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Score) {
			Score tmp = (Score) obj;
			return value.compareTo(tmp.getValue()) == 0;
		}
		return false;
	}

}
