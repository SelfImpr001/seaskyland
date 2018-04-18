/*
 * @(#)com.cntest.fxpt.etl.business.EtlValues.java	1.0 2014年6月5日:下午3:51:40
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import java.util.HashMap;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 下午3:51:40
 * @version 1.0
 */
public class EtlValues {
	private HashMap<String, Object> datas = new HashMap<String, Object>();

	public void appendValue(String name, Object value) {
		datas.put(name, value);
	}

	public <T> T get(String name) {
		return (T) datas.get(name);
	}
}
