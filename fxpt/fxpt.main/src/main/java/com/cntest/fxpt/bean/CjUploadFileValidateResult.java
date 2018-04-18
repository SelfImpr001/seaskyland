/*
 * @(#)com.cntest.fxpt.bean.CjUploadFileVerification.java	1.0 2014年10月15日:上午10:35:59
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import com.cntest.fxpt.domain.TestPaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午10:35:59
 * @version 1.0
 */
public class CjUploadFileValidateResult extends UploadFileValidateResult {
	private boolean isExistTestPaper = false;

	public boolean isExistTestPaper() {
		return isExistTestPaper;
	}

	public void setExistTestPaper(boolean isExistTestPaper) {
		this.isExistTestPaper = isExistTestPaper;
	}

}
