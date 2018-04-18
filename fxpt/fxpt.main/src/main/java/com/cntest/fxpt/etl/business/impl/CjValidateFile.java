/*
 * @(#)com.cntest.fxpt.etl.business.impl.CjValidateFile.java	1.0 2014年10月16日:上午11:21:12
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.List;

import com.cntest.fxpt.bean.CjUploadFileContentInfo;
import com.cntest.fxpt.bean.CjUploadFileValidateResult;
import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.bean.OmrStrAndScoreStr;
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
 * </Pre>
 * 
 * @author 刘海林 2014年10月16日 上午11:21:12
 * @version 1.0
 */
public class CjValidateFile extends AbstractValidate implements IValidateFile {

	@Override
	public UploadFileValidateResult validate(UploadFileContentInfo fileContent,
			boolean isValidataHead, Long examId, int schemeType) {
		CjUploadFileValidateResult vr = new CjUploadFileValidateResult();

		CjUploadFileContentInfo ci = (CjUploadFileContentInfo) fileContent;

		TestPaper testPaper = getTestPaper(examId, ci.getTestPaperName());
		if (testPaper == null) {
			vr.appendMessage(ci.getTestPaperName() + "试卷和细目表不存在，请先在细目表中导入"
					+ ci.getTestPaperName() + "试卷和细目表再导入。");
		}

		boolean isTemplateFile = isValidataHead && testPaper != null
				&& validataHeade(vr, ci, testPaper);

		vr.setTemplateFile(isTemplateFile);
		vr.setExistTestPaper(testPaper != null);
		vr.setExistContent(testPaper != null && testPaper.isHasCj());
		vr.setTestPaper(testPaper);
		vr.setHasError(!vr.getMessages().isEmpty());
		return vr;
	}

	private TestPaper getTestPaper(Long examId, String testPaperName) {
		ITestPaperService testPaperService = SpringContext
				.getBean("ITestPaperService");
		return testPaperService.get(examId, testPaperName);
	}

	private boolean validataHeade(CjUploadFileValidateResult vr,
			CjUploadFileContentInfo contentInfo, TestPaper testPaper) {

		List<String> head = contentInfo.getHead();

		IDataFieldService dataFieldService = SpringContext
				.getBean("etl.IDataFieldService");

		OmrStrAndScoreStr omrStrAndScoreStr = ImportUtil
				.getOrmStrAndScoreStr(head);
		List<DataField> dataFields = dataFieldService
				.list(3, testPaper.getId());
		boolean result = true;
		for (DataField d : dataFields) {
			if (d.isSelItem()) {
				if (!validateSelItem(d, head, omrStrAndScoreStr)) {
					result = false;
					vr.appendMessage(d.getAsName()
							+ "-->没有对应的字段。请检查文件中是否有客观题选择串和得分串。该文件不能进行导入。");
					break;
				}
			} else {
				if (!isValidateFiled(d, head)) {
					result = false;
					vr.appendMessage(d.getAsName() + "-->没有对应的字段。该文件不能进行导入。");
					break;
				}
			}
		}

		return result;
	}

	private boolean validateSelItem(DataField d, List<String> head,
			OmrStrAndScoreStr omrStrAndScoreStr) {
		String[] defNames = d.getDefaultName().split("\\|");
		boolean result = true;
		if (defNames[0].equals("")) {
			if (d.isSelOption()) {
				return omrStrAndScoreStr.isHasOmrStr();
			} else {
				return omrStrAndScoreStr.isHasScoreStr();
			}
		} else {
			result = isValidateFiled(d, head);
		}
		return result;
	}
}
