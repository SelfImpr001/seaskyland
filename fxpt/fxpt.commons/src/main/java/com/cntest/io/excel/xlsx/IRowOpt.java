/*
 * @(#)com.excle2007.IRowOpt.java	1.0 2014年5月29日:上午11:26:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.io.excel.xlsx;

import java.util.List;

/**
 * <Pre>
 * 处理一行数据
 * </Pre>
 * 
 * @author 刘海林 2014年5月29日 上午11:26:10
 * @version 1.0
 */
public interface IRowOpt {
	public void row(int rowIndex, List<String> row);
}
