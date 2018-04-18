/*
 * @(#)com.cntest.fxpt.etl.business.impl.CjUploadFileContentInfoBuild.java	1.0 2014年10月16日:上午10:50:46
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.bean.CjUploadFileContentInfo;
import com.cntest.fxpt.bean.ItemXlsUploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileInfo;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.IReadXls;
import com.cntest.util.ReadDBF;
import com.cntest.util.XlsCreator;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月16日 上午10:50:46
 * @version 1.0
 */
public class CjUploadFileContentInfoBuild extends UploadFileContentInfoBuild {
	private static final Logger log = LoggerFactory
			.getLogger(CjUploadFileContentInfoBuild.class);

	@Override
	public UploadFileContentInfo build(UploadFileInfo fileInfo, String sheetName) {
		if (".dbf".equalsIgnoreCase(fileInfo.getSuffix())) {
			return dbf(fileInfo);
		} else if (".xls".equalsIgnoreCase(fileInfo.getSuffix())
				|| ".xlsx".equalsIgnoreCase(fileInfo.getSuffix())) {
			return xls(fileInfo, sheetName);
		}

		return null;
	}

	private CjUploadFileContentInfo xls(UploadFileInfo fileInfo,
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

		CjUploadFileContentInfo contentInfo = new CjUploadFileContentInfo();
		contentInfo.setHead(head);
		contentInfo.setSheetNames(sheetNames);
		contentInfo.setTestPaperName(fileInfo.getNameNoSuffix());
		return contentInfo;
	}

	private CjUploadFileContentInfo dbf(UploadFileInfo fileInfo) {

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

		CjUploadFileContentInfo contentInfo = new CjUploadFileContentInfo();
		contentInfo.setHead(head);
		contentInfo.setTestPaperName(fileInfo.getNameNoSuffix());
		return contentInfo;
	}

}
