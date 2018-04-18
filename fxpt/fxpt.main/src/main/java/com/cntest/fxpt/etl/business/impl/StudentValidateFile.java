/*
 * @(#)com.cntest.fxpt.etl.business.impl.StudentValidateFile.java	1.0 2014年10月20日:下午4:25:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileValidateResult;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.service.IExamService;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月20日 下午4:25:45
 * @version 1.0
 */
public class StudentValidateFile extends DefaultValidateFile {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.etl.business.impl.DefaultValidateFile#validate(com.cntest
	 * .fxpt.bean.UploadFileContentInfo, boolean, java.lang.Long, int)
	 */
	@Override
	public UploadFileValidateResult validate(UploadFileContentInfo fileContent,
			boolean isValidataHead, Long examId, int schemeType) {
		UploadFileValidateResult result = super.validate(fileContent,
				isValidataHead, examId, 1);
		result.setExistContent(isExistContent(examId));
		return result;
	}

	private boolean isExistContent(Long examId) {
		IExamService examService = SpringContext.getBean("IExamService");
		Exam exam = examService.findById(examId);
		boolean result = exam.isHasExamStudent();
		return result;
	}

}
