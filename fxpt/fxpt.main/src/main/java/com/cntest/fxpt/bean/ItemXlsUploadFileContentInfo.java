/*
 * @(#)com.cntest.fxpt.bean.ItemXlsUploadFileContentInfo.java	1.0 2014年10月15日:上午11:30:01
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午11:30:01
 * @version 1.0
 */
public class ItemXlsUploadFileContentInfo extends XlsUploadFileContentInfo{
	private List<String> testPaperInfo;

	public List<String> getTestPaperInfo() {
		return testPaperInfo;
	}

	public void setTestPaperInfo(List<String> testPaperInfo) {
		this.testPaperInfo = testPaperInfo;
	}

}
