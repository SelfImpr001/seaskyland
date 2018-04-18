/*
 * @(#)com.cntest.util.ReadXLS.java	1.0 2014年5月9日:下午4:49:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.io.excel;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import com.cntest.exception.ExceptionHelper;
import com.cntest.io.FileReadException;
import com.cntest.io.excel.xlsx.IRowOpt;
import com.cntest.io.excel.xlsx.ReaderXlsxUtil;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月9日 下午4:49:13
 * @version 1.0
 */
public class XLSXFileReader implements XlsReader {
	private static final Logger log = LoggerFactory.getLogger(XLSXFileReader.class);
	private ReaderXlsxUtil xlsx = new ReaderXlsxUtil();
	private String filePath;
	private String curSheetName;
	private List<String> headerRow;

	public String getFilePath() {
		return filePath;
	}

	public XLSXFileReader(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void open() throws FileReadException {
		log.debug("打开xlsx文件");
		try {
			xlsx.open(filePath);
		} catch (Exception e) {
			throw new FileReadException("打开Excle文件出错！原因:" + e.getMessage(), e);
		}
		log.debug("打开xlsx文件完成");
	}

	@Override
	public void setSheet(String sheetName) {
		curSheetName = sheetName;
	}

	@Override
	public List<String> getSheetNames() {
		return xlsx.getSheetNames();
	}

	@Override
	public void close() {
		try {
			xlsx.close();
		} catch (Exception e) {
			throw new FileReadException(e.getMessage(), e);
		}
		log.debug("关闭xls文件完毕");
	}

	@Override
	public List<String> getRow(final int rowIdx) {
		final int row = rowIdx + 1;
		headerRow = new ArrayList<String>();
		xlsx.setRowOption(new IRowOpt() {
			public void row(int rowIdx, List<String> rowData) {
				if (rowIdx == row) {
					headerRow = rowData;
					xlsx.setInterrupted(true);
				}
			}
		});
		try {
			xlsx.processSheet(curSheetName);
		} catch (Exception e) {
			log.debug(ExceptionHelper.trace2String(e));
			throw new FileReadException(e.getMessage(), e);
		}
		log.debug("获取xlsx文件的表头信息完毕");
		return headerRow;
	}
	
	@Override
	public int rowCount() {
		return this.xlsx.getAllRows();
	}

	public List<String> header() {
		return header(0);
	}

	public List<String> header(int headRow) {
		log.debug("获取xlsx文件的表头信息");
		headerRow = getRow(headRow);
		log.debug("获取xlsx文件的表头信息完毕");
		return headerRow;
	}

	public void dealWithRowData(IRowOpt rowOption) {
		log.debug("处理文件数据");
		xlsx.setRowOption(rowOption);
		try {
			xlsx.processSheet(curSheetName);
		} catch (Exception e) {
			log.debug(ExceptionHelper.trace2String(e));
			throw new FileReadException(e.getMessage(), e);
		}
		log.debug("处理文件数据完毕");
	}

}
