/*
 * @(#)com.cntest.fxpt.etl.business.impl.DefaultValidate.java	1.0 2014年10月20日:下午4:17:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.List;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileValidateResult;
import com.cntest.fxpt.etl.business.IValidateFile;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月20日 下午4:17:52
 * @version 1.0
 */
public class DefaultValidateFile extends AbstractValidate implements
		IValidateFile {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.etl.business.IValidateFile#validate(com.cntest.fxpt.bean
	 * .UploadFileContentInfo, boolean, java.lang.Long)
	 */
	@Override
	public UploadFileValidateResult validate(UploadFileContentInfo fileContent,
			boolean isValidataHead, Long examId, int schemeType) {

		UploadFileValidateResult result = new UploadFileValidateResult();
		if (isValidataHead) {
			result.setTemplateFile(validataHeade(result, fileContent,
					schemeType));
		}
		result.setHasError(!result.getMessages().isEmpty());
		return result;
	}

	private boolean validataHeade(UploadFileValidateResult vr,
			UploadFileContentInfo fileContent, int schemeType) {
		List<String> head = fileContent.getHead();
		boolean result = true;
		IDataFieldService dataFieldService = SpringContext
				.getBean("etl.IDataFieldService");
		List<DataField> dataFields = dataFieldService.list(schemeType, 0L);
		for (DataField d : dataFields) {
			if (!isValidateFiled(d, head)) {
				result = false;
				vr.appendMessage(d.getAsName() + "-->对应的字段不存在。不能进行导入。");
				break;
			}
		}
		return result;
	}

}
