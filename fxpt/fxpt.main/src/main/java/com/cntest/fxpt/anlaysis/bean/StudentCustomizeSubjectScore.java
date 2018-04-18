/*
 * @(#)com.cntest.fxpt.anlaysis.bean.StudentSubjectScore.java	1.0 2014年11月24日:下午1:25:42
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

public class StudentCustomizeSubjectScore extends StudentSubjectScore {
	private int qkNum = 0;
	private int zeroNum = 0;


	public int getQkNum() {
		return qkNum;
	}

	public int getZeroNum() {
		return zeroNum;
	}

	public void setQkNum(int qkNum) {
		this.qkNum = qkNum;
	}

	public void setZeroNum(int zeroNum) {
		this.zeroNum = zeroNum;
	}


	@Override
	public String toString() {
		return super.toString();
	}
}
