/*
 * @(#)com.cntest.util.ReadXLS.java	1.0 2014年5月9日:下午4:49:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.io.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.io.FileReadException;



/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月9日 下午4:49:13
 * @version 1.0
 */
public class XLSFileReader implements XlsReader {
	private static final Logger logger = LoggerFactory.getLogger(XLSFileReader.class);
	private String filePath;
	private Workbook wb = null;
	private Sheet sheet = null;
	private int currentRowNum = 1;
	private int rowNumber = 0;
	private int columnNumber = 0;
	private File file;

	public String getFilePath() {
		return filePath;
	}

	/**
	 * 
	 */
	public XLSFileReader(String filePath) {
		this.filePath = filePath;
		this.file = new File(filePath);
		
	}
	
	public XLSFileReader(File file) {
		this.file = file;
	}

	@Override
	public void open() {
		logger.debug("打开xls文件");
		File file = this.file;
		try {
			wb = Workbook.getWorkbook(file);
		} catch (Exception e) {
			throw new FileReadException("打开Excle文件出错！原因:" + e.getMessage());
		}
		logger.debug("打开xls文件完毕");
	}

	@Override
	public void setSheet(String sheetName) {
		sheet = wb.getSheet(sheetName);
		rowNumber = sheet.getRows();
		columnNumber = sheet.getColumns();
		if (rowNumber < 1) {
			throw new FileReadException("excle文件没有数据！");
		}
	}

	@Override
	public List<String> getSheetNames() {
		ArrayList<String> result = new ArrayList<String>();
		Sheet[] sheets = wb.getSheets();
		for (Sheet sheet : sheets) {
			result.add(sheet.getName());
		}
		return result;
	}

	@Override
	public void close() {
		if (null != wb) {
			wb.close();
			wb = null;
		}
		logger.debug("关闭xls文件完毕");
	}

	@Override
	public List<String> getRow(int row) {
		Cell[] cells = sheet.getRow(row);
		List<String> result = getValues(cells);
		return result;
	}
	
	@Override
	public int rowCount() {
		return this.rowNumber;
	}

	public List<String> header() {
		return header(0);
	}

	public List<String> header(int headRow) {
		logger.debug("读xls文件表头信息");
		List<String> result = getRow(headRow);
		currentRowNum = headRow + 1;
		logger.debug("读xls文件表头信息完毕");
		return result;
	}

	public boolean hasNext() {
		return currentRowNum < rowNumber;
	}

	public List<String> nextRow() {
		logger.debug("读取第{}行数据",+ (currentRowNum + 1));
		Cell[] cells = sheet.getRow(currentRowNum++);
		List<String> result = getValues(cells);
		logger.debug("读取第{}行数据完毕", (currentRowNum));
		return result;
	}

	private List<String> getValues(Cell[] cells) {
		ArrayList<String> result = new ArrayList<String>();
		int cellsNum = cells.length;
		for (int i = 0; i < columnNumber; i++) {
			if (i >= cellsNum) {
				result.add("");
			} else {
				Cell cell = cells[i];
				String cellValue = cell.getContents().trim();
				result.add(cellValue);
			}
		}

		return result;
	}

}
