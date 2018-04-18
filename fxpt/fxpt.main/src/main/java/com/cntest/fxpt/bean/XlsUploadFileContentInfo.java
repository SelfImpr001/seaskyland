/*
 * @(#)com.cntest.fxpt.bean.XlsUploadFileContentInfo.java	1.0 2014年10月15日:上午11:26:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午11:26:39
 * @version 1.0
 */
public class XlsUploadFileContentInfo extends UploadFileContentInfo {
	private List<String> sheetNames;
	private String curSheet;

	public List<String> getSheetNames() {
		return sheetNames;
	}

	public String getCurSheet() {
		return curSheet;
	}

	public void setSheetNames(List<String> sheetNames) {
		this.sheetNames = sheetNames;
	}

	public void setCurSheet(String curSheet) {
		this.curSheet = curSheet;
	}

}
