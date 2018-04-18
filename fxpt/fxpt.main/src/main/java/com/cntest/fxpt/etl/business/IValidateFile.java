/*
 * @(#)com.cntest.fxpt.etl.business.IValidateFile.java	1.0 2014年10月15日:上午10:38:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileValidateResult;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午10:38:45
 * @version 1.0
 */
public interface IValidateFile {
	public UploadFileValidateResult validate(UploadFileContentInfo fileContent, boolean isValidataHead, Long examId, int schemeType);
}
