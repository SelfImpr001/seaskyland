/*
 * @(#)com.cntest.fxpt.etl.business.impl.ItemUploadFileContentInfoBuild.java	1.0 2014年10月15日:上午11:35:03
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.List;

import com.cntest.fxpt.bean.ItemXlsUploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileInfo;
import com.cntest.util.IReadXls;
import com.cntest.util.XlsCreator;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午11:35:03
 * @version 1.0
 */
public class ItemUploadFileContentInfoBuild extends UploadFileContentInfoBuild {

	@Override
	public UploadFileContentInfo build(UploadFileInfo fileInfo, String sheetName) {

		if (!".xls".equalsIgnoreCase(fileInfo.getSuffix())
				&& !".xlsx".equalsIgnoreCase(fileInfo.getSuffix())) {
			throw new RuntimeException("双向细目表只能为excel文件");
		}

		IReadXls xls = XlsCreator.create(fileInfo.getSuffix(),
				fileInfo.getPath());
		xls.open();
		List<String> sheetNames = xls.getSheetNames();
		if (sheetName == null) {
			sheetName = sheetNames.get(0);
		}
		xls.setSheet(sheetName);
		ItemXlsUploadFileContentInfo info = new ItemXlsUploadFileContentInfo();
		info.setTestPaperInfo(xls.getRow(1));
		info.setCurSheet(sheetName);
		info.setHead(xls.getRow(2));
		info.setSheetNames(sheetNames);
		xls.close();
		return info;
	}

}
