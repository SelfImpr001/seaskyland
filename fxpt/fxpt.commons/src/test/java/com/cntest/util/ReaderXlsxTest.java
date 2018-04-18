/*
 * @(#)com.hyt.cntest.util.ReaderXlsxTest.java	1.0 2014年5月30日:上午9:30:07
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.util;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.excle2007.IRowOpt;
import com.cntest.util.excle2007.ReaderXlsxUtil;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月30日 上午9:30:07
 * @version 1.0
 */
public class ReaderXlsxTest {
	private static final Logger log = LoggerFactory
			.getLogger(ReaderXlsxTest.class);

	private String filePath = "D:/test/kk/2007.xlsx";

	@Test
	public void test() throws Exception {

		final ReaderXlsxUtil r = new ReaderXlsxUtil();
		r.open(filePath);

		r.setRowOption(new IRowOpt() {
			public void row(int rowIndex, List<String> row) {
				System.out.println(rowIndex);
				r.setInterrupted(true);
			}
		});

		r.processSheet("Sheet3");
		r.processSheet("Sheet3");

	}

	@Test
	public void testReadXLSX() {
//		ReadXLSX xlsx = new ReadXLSX();
//		xlsx.setFilePath(filePath);
//		xlsx.openFile();
//		xlsx.setSheet("Sheet3");
//		List<String> header = xlsx.header();
//		log.debug(listToString(header));
//
//		xlsx.dealWithRowData(new IRowOpt() {
//
//			@Override
//			public void row(int rowIndex, List<String> row) {
//				if (rowIndex == 1) {
//					return;
//				}
//				log.debug(listToString(row));
//			}
//		});
//		xlsx.closeFile();

	}

	private String listToString(List<String> row) {
		StringBuffer sb = new StringBuffer();
		for (String cell : row) {
			sb.append(cell + "\t");
		}
		return sb.toString();
	}
}
