/*
 * @(#)com.cntest.fxpt.anlaysis.bean.FilterDescription.java	1.0 2015年6月12日:下午3:41:23
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月12日 下午3:41:23
 * @version 1.0
 */
public class FilterDes {
	private String value;
	private String attrName;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	@Override
	public String toString() {
		return attrName + "->" + value;
	}

}
