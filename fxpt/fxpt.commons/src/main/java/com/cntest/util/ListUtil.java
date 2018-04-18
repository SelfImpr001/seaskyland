/*
 * @(#)com.cntest.util.ListUtil.java	1.0 2014年10月14日:下午5:28:22
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.util;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月14日 下午5:28:22
 * @version 1.0
 */
public class ListUtil {
	public static boolean hasValue(List<String> values, String value) {
		boolean result = getValue(values, value) != null;
		return result;
	}

	public static String getValue(List<String> values, String value) {
		String result = null;
		for (String tmp : values) {
			if (tmp.equalsIgnoreCase(value)) {
				result = tmp;
				break;
			}
		}

		return result;
	}
}
