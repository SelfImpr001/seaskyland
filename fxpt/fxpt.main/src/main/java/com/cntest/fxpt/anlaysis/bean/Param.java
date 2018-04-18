/*
 * @(#)com.cntest.fxpt.anlaysis.bean.Param.java	1.0 2014年11月25日:上午11:10:58
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 上午11:10:58
 * @version 1.0
 */
public class Param {
	private String key;
	private Object value;

	public Param(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
