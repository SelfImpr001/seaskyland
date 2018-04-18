/*
 * @(#)com.cntest.fxpt.etl.business.impl.UploadFileContentInfoBuild.java	1.0 2014年10月15日:上午11:33:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.bean.CjUploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileInfo;
import com.cntest.fxpt.bean.XlsUploadFileContentInfo;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.IReadXls;
import com.cntest.util.ReadDBF;
import com.cntest.util.XlsCreator;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午11:33:28
 * @version 1.0
 */
public class UploadFileContentInfoBuild {
	private static final Logger log = LoggerFactory
			.getLogger(UploadFileContentInfoBuild.class);

	public UploadFileContentInfo build(UploadFileInfo fileInfo, String sheetName) {
		if (fileInfo.isExcel()) {
			return xls(fileInfo, sheetName);
		} else {
			return dbf(fileInfo);
		}
	}

	private UploadFileContentInfo dbf(UploadFileInfo fileInfo) {
		List<String> head = new ArrayList<String>();
		ReadDBF dbf = new ReadDBF();
		dbf.setFilePath(fileInfo.getPath());
		dbf.openFile();
		try {
			head = dbf.header();
		} catch (Exception e) {
			log.debug(ExceptionHelper.trace2String(e));
			e.printStackTrace();
		} finally {
			dbf.closeFile();
		}

		UploadFileContentInfo contentInfo = new UploadFileContentInfo();
		contentInfo.setHead(head);
		return contentInfo;
	}

	private XlsUploadFileContentInfo xls(UploadFileInfo fileInfo,
			String sheetName) {
		IReadXls xls = XlsCreator.create(fileInfo.getSuffix(),
				fileInfo.getPath());
		xls.open();
		List<String> sheetNames = xls.getSheetNames();
		if (sheetName == null) {
			sheetName = sheetNames.get(0);
		}
		xls.setSheet(sheetName);
		List<String> head = xls.getRow(1);
		xls.close();

		XlsUploadFileContentInfo contentInfo = new XlsUploadFileContentInfo();
		contentInfo.setHead(head);
		contentInfo.setSheetNames(sheetNames);
		return contentInfo;
	}
}
