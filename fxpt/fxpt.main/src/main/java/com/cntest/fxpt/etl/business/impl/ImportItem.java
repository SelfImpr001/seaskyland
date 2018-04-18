/*
 * @(#)com.cntest.fxpt.etl.business.impl.ImportItem.java	1.0 2014年10月9日:下午5:13:48
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.EtlLog;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.etl.EtlExecutor;
import com.cntest.fxpt.etl.business.IEtlExecuteor;
import com.cntest.fxpt.etl.business.util.ImportUtil;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.etl.util.IRetrieveValue;
import com.cntest.fxpt.etl.util.KeyContainer;
import com.cntest.fxpt.service.IAnalysisTestPaperService;
import com.cntest.fxpt.service.IItemService;
import com.cntest.fxpt.service.ISubjectService;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.util.IReadXls;
import com.cntest.util.XlsCreator;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月9日 下午5:13:48
 * @version 1.0
 */
@Service("Item.IEtlExecuteor")
public class ImportItem extends BaseEtlExecutor implements IEtlExecuteor {

	@Autowired(required = false)
	@Qualifier("ITestPaperService")
	private ITestPaperService testPaperService;

	@Autowired(required = false)
	@Qualifier("ISubjectService")
	private ISubjectService subjectService;

	@Autowired(required = false)
	@Qualifier("IItemService")
	private IItemService itemService;

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperService")
	private IAnalysisTestPaperService analysisTestPaperService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.business.IEtlExecuteor#execute(com.cntest.fxpt.
	 * systemsetting.etl.domain.WebRetrieveResult)
	 */
	@Override
	public EtlProcessResult execute(WebRetrieveResult wrr) throws Exception {
		TestPaper testPaper = createTestPaper(wrr);
		if (testPaper == null) {
			throw new RuntimeException("创建试卷失败!");
		}

		EtlExecutor executor = null;
		Exception exception = null;
		try {
			DefaultEtlProcessBuild build = initBuilder(wrr);
			setValidateParames(build);
			setFinalData(build, testPaper);

			executor = exec(build);

		} catch (Exception e) {
			exception = e;
		} finally {
			deleteFile(wrr);
		}

		EtlProcessResult result = createMessage(executor, exception);
		EtlLog etlLog = createAndSaveEtlLog(wrr.getUserName(), wrr.getExamId(),
				"导入" + testPaper.getName() + "细目表", wrr.getSchemeType(), result);

		// 导入完毕进行最后一步验证
		if (result.isHasError()) {
			itemService.importFail(testPaper.getId());
		} else {
			itemService.importSuccess(wrr.getExamId(), testPaper);
		}

		return result;
	}

	private void setFinalData(DefaultEtlProcessBuild build,
			final TestPaper testPaper) {
		HashMap<String, IRetrieveValue> finalDatas = new HashMap<String, IRetrieveValue>();
		finalDatas.put("testPaperId", new IRetrieveValue<Long>() {
			@Override
			public Long value() {
				return testPaper.getId();
			}

		});
		finalDatas.put("examId", new IRetrieveValue<Long>() {
			@Override
			public Long value() {
				return testPaper.getExam().getId();
			}

		});
		finalDatas.put("sortNum", new IRetrieveValue<Integer>() {
			private int sortNum = 1;

			@Override
			public Integer value() {
				return sortNum++;
			}

		});
		build.setFinalDatas(finalDatas);
	}

	private void setValidateParames(DefaultEtlProcessBuild build) {
		build.setValidateParame("subjects", subjectList());
		build.setValidateParame("repeatUtil", new KeyContainer());
	}

	private Map<String, Subject> subjectList() {
		List<Subject> subjects = subjectService.list();
		HashMap<String, Subject> result = new HashMap<String, Subject>();
		for (Subject subject : subjects) {
			result.put(subject.getName(), subject);
		}
		return result;
	}

	private TestPaper createTestPaper(WebRetrieveResult webRetrieveResult) {
		TestPaper testPaper = parseTestPaper(
				webRetrieveResult.getSuffix(),
				webRetrieveResult.getFileDir()
						+ webRetrieveResult.getFileName(),
				webRetrieveResult.getSheetName());

		if (testPaper != null) {
			Exam exam = new Exam();
			exam.setId(webRetrieveResult.getExamId());
			testPaper.setExam(exam);
			testPaperService.add(testPaper);
		}

		return testPaper;
	}

	@Override
	public boolean executeClean(WebRetrieveResult webRetrieveResult) {
		TestPaper testPaper = parseTestPaper(
				webRetrieveResult.getSuffix(),
				webRetrieveResult.getFileDir()
						+ webRetrieveResult.getFileName(),
				webRetrieveResult.getSheetName());

		testPaper = testPaperService.get(webRetrieveResult.getExamId(),
				testPaper.getName());
		if (testPaper != null && webRetrieveResult.isOverlayImport()) {
			testPaperService.delete(testPaper);
			return true;
		} else if (testPaper == null) {
			return true;
		} else {
			return false;
		}

	}

	private TestPaper parseTestPaper(String suffix, String path,
			String sheetName) {
		IReadXls xls = XlsCreator.create(suffix, path);
		xls.open();
		xls.setSheet(sheetName);
		List<String> testPaperInfo = xls.getRow(1);
		xls.close();
		return ImportUtil.createTestPaper(testPaperInfo);
	}

}
