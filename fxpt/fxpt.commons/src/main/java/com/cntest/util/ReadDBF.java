/*
 * @(#)com.cntest.util.ReadDBF.java	1.0 2014年5月12日:下午1:13:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.exception.ReadFileException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

/**
 * <Pre>
 * 对DBF文件的数据
 * </Pre>
 * 
 * @author 刘海林 2014年5月12日 下午1:13:10
 * @version 1.0
 */
public class ReadDBF {
	private static final Logger log = LoggerFactory.getLogger(ReadDBF.class);
	private String filePath;
	private DBFReader dbfReader = null;
	private int currentRowNum = 1;
	private int rowNumber = 0;
	private ArrayList<String> columns;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 
	 */
	public ReadDBF() {
		// TODO Auto-generated constructor stub
	}

	public void openFile() throws ReadFileException {
		log.debug("打开dbf文件");
		try {
			File file = new File(filePath);
			dbfReader = new DBFReader(new FileInputStream(file));
			dbfReader.setCharactersetName("GBK");
		} catch (Exception e) {
			throw new ReadFileException("打开DBF文件出错！原因:" + e.getMessage());
		}
		rowNumber = dbfReader.getRecordCount();
		log.debug("打开dbf文件成功");
	}

	public void closeFile() {

	}

	public List<String> header() throws Exception {
		log.debug("读取dbf文件表头信息");
		int columnNumber = dbfReader.getFieldCount();
		columns = new ArrayList<String>();
		for (int i = 0; i < columnNumber; i++) {
			DBFField field = dbfReader.getField(i);
			String fieldName = field.getName().trim();
			columns.add(fieldName);
		}
		log.debug("读dbf文件表头信息成功");
		return columns;
	}

	public boolean hasNext() {
		return currentRowNum <= rowNumber;
	}

	public List<String> nextRow() throws Exception {
		currentRowNum++;
		log.debug("处理" + currentRowNum + "行数据");
		ArrayList<String> result = new ArrayList<String>();
		Object[] obj = this.dbfReader.nextRecord();
		int columnNumber = columns.size();
		for (int i = 0; i < columnNumber; i++) {
			String data = String.valueOf(obj[i]).trim();
			result.add(data);
		}
		log.debug("处理" + currentRowNum + "行数据成功");
		return result;
	}

	public int getRows() {
		return rowNumber;
	}
}
