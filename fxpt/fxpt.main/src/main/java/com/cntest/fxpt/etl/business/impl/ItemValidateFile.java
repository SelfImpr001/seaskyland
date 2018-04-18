/*
 * @(#)com.cntest.fxpt.etl.business.impl.ItemValidateFile.java	1.0 2014年10月15日:上午11:43:57
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.List;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.bean.ItemXlsUploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileValidateResult;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.etl.business.IValidateFile;
import com.cntest.fxpt.etl.business.util.ImportUtil;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * 验证上传文件是否满足双向细目表的要求
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午11:43:57
 * @version 1.0
 */
public class ItemValidateFile extends AbstractValidate implements IValidateFile {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.etl.business.IValidateFile#validate(com.cntest.fxpt.bean
	 * .UploadFileContentInfo)
	 */
	@Override
	public UploadFileValidateResult validate(UploadFileContentInfo fileContent,
			boolean isValidataHead, Long examId, int schemeType) {

		UploadFileValidateResult vr = new UploadFileValidateResult();

		ItemXlsUploadFileContentInfo itemFileContent = (ItemXlsUploadFileContentInfo) fileContent;
		boolean isTemplateFile = isTemplate(vr,
				itemFileContent.getTestPaperInfo());
		if (isTemplateFile && isValidataHead) {
			isTemplateFile = validataHeade(vr, itemFileContent.getHead());
		}

		TestPaper testPaper = getTestPaper(examId,
				itemFileContent.getTestPaperInfo());
		boolean isExistContent = testPaper != null;

		vr.setTemplateFile(isTemplateFile);
		vr.setExistContent(isExistContent);
		vr.setTestPaper(testPaper);
		vr.setHasError(!vr.getMessages().isEmpty());
		return vr;
	}

	private TestPaper getTestPaper(Long examId, List<String> row) {
		TestPaper testPaper = ImportUtil.createTestPaper(row);
		if (testPaper != null) {
			ITestPaperService testPaperService = SpringContext
					.getBean("ITestPaperService");
			testPaper = testPaperService.get(examId, testPaper.getName());
		}
		return testPaper;
	}

	private boolean isTemplate(UploadFileValidateResult vr, List<String> row) {
		TestPaper testPaper = ImportUtil.createTestPaper(row);
		boolean result = testPaper != null;
		if (!result) {
			vr.appendMessage("获取试卷信息出错！请检查文件是否符合模板文件再导入。");
		}

		return result;
	}

	private boolean validataHeade(UploadFileValidateResult vr, List<String> head) {
		boolean result = true;
		IDataFieldService dataFieldService = SpringContext
				.getBean("etl.IDataFieldService");
		List<DataField> dataFields = dataFieldService.list(2, 0L);
		
		
		for (DataField d : dataFields) {
			if (!isValidateFiled(d, head)) {
				result = false;
				vr.appendMessage(d.getAsName() + "-->没有对应数据!该文件不能进行导入。");
				break;
			}
		}
		return result;
	}

}
