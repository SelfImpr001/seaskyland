/*
 * @(#)com.cntest.fxpt.etl.util.KeyContainer.java	1.0 2014年6月9日:下午2:19:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月9日 下午2:19:13
 * @version 1.0
 */
public class KeyContainer {
	private HashMap<String, List<Object>> dataset = new HashMap<String, List<Object>>();

	public boolean contains(String key, Object value) {
		List<Object> values = dataset.get(key);
		if (values == null) {
			values = new ArrayList<Object>();
			dataset.put(key, values);
		}

		return values.contains(value);
	}

	public void add(String key, Object value) {
		List<Object> values = dataset.get(key);
		if (values == null) {
			values = new ArrayList<Object>();
			dataset.put(key, values);
		}
		values.add(value);
	}
}
