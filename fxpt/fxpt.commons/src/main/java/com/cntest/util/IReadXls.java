/*
 * @(#)com.cntest.util.IReadXls.java	1.0 2014年10月9日:下午5:18:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.util;

import java.util.List;

import com.cntest.exception.ReadFileException;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月9日 下午5:18:47
 * @version 1.0
 */
public interface IReadXls {

	public void open() throws ReadFileException;

	public void close();

	public void setSheet(String sheetName);

	public int getRows();

	public List<String> getSheetNames();

	public List<String> getRow(int row);

	public int rowCount();

}
