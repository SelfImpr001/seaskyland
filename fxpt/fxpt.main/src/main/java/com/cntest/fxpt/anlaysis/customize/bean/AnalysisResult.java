/*
 * @(#)com.cntest.fxpt.anlaysis.customize.bean.AnalysisResult.java	1.0 2015年4月23日:下午4:16:01
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.bean;

import java.util.HashMap;
import java.util.Map;

import com.cntest.fxpt.anlaysis.uitl.Container;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月23日 下午4:16:01
 * @version 1.0
 */
public class AnalysisResult extends Container<String, Object> {

	public Map<String, Object> getResult() {

		HashMap<String, Object> result = new HashMap<>();
		for (String key : c.keySet()) {
			Object value = c.get(key);
			result.put(key, value);
		}
		return result;
	}

	private Map<String, Object> getMap(Map<String, Object> map) {
		HashMap<String, Object> result = new HashMap<>();
		for (String key : map.keySet()) {
			Object value = c.get(key);
			if (value instanceof Map) {
				value = getMap((Map<String, Object>) value);
			}
			result.put(key, value);
		}
		return result;
	}
}
