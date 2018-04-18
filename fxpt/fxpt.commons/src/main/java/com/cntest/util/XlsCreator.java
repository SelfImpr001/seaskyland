/*
 * @(#)com.cntest.util.XlsCreator.java	1.0 2014年10月9日:下午5:25:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.util;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月9日 下午5:25:47
 * @version 1.0
 */
public class XlsCreator {
	public static IReadXls create(String suffix, String filePath) {
		IReadXls xls = null;
		if (".xls".equalsIgnoreCase(suffix)) {
			xls = new ReadXLS(filePath);
		} else if (".xlsx".equalsIgnoreCase(suffix)) {
			xls = new ReadXLSX(filePath);
		}
		return xls;
	}
}
