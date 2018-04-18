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
public enum DimesionType {
	PROVINCE(1), CITY(2), COUNTY(3), SCHOOL(4), CLAZZ(5), GENDER(10), NATION(11),WL(12);
	private int type;

	private DimesionType(int type) {
		this.type = type;
	}

	public int getValue() {
		return type;
	}
}
