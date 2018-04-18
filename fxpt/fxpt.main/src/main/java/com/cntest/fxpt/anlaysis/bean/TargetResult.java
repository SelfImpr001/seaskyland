/*
 * @(#)com.cntest.fxpt.anlaysis.bean.Target.java	1.0 2014年11月28日:上午9:23:06
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 上午9:23:06
 * @version 1.0
 */
public class TargetResult {
	private String name;
	private Object value;

	public TargetResult(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public <T> T getValue() {
		return (T) value;
	}

	public TargetResult setName(String name) {
		this.name = name;
		return this;
	}

	public TargetResult setValue(Object value) {
		this.value = value;
		return this;
	}

	@Override
	public String toString() {
		return name + "<--->" + value;
	}

}
