/*
 * @(#)com.cntest.fxpt.anlaysis.customize.bean.DimesionType.java	1.0 2015年4月22日:上午11:36:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 上午11:36:28
 * @version 1.0
 */
public enum FormulaType {
	AVG(1), STD(2), MIN(3), MAX(4), DFL(5),SKRS(6);
	private int type;

	private FormulaType(int type) {
		this.type = type;
	}

	public int getValue() {
		return type;
	}
}
