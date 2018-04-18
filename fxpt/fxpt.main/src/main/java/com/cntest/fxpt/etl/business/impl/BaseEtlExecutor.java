/*
 * @(#)com.cntest.fxpt.etl.business.impl.BaseEtlExecutor.java	1.0 2014年10月10日:上午8:39:59
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.DataTransform;
import com.cntest.fxpt.bean.EtlLog;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.etl.EtlExecutor;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.IStep;
import com.cntest.fxpt.etl.IStepMetadata;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.etl.domain.StepLog;
import com.cntest.fxpt.etl.util.DateUtils;
import com.cntest.fxpt.service.IEtlLogService;
import com.cntest.fxpt.service.etl.IDataTransformService;
import com.cntest.fxpt.util.SaveEtlProcessResultToFile;
import com.cntest.util.ExceptionHelper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月10日 上午8:39:59
 * @version 1.0
 */
@Service
public abstract class BaseEtlExecutor {
	@Autowired(required = false)
	@Qualifier("etl.IDataTransformService")
	private IDataTransformService dataTransformService;

	@Autowired(required = false)
	@Qualifier("IEtlLogService")
	private IEtlLogService etlLogService;

	protected DefaultEtlProcessBuild initBuilder(
			WebRetrieveResult webRetrieveResult) {

		DefaultEtlProcessBuild build = null;
		if (webRetrieveResult.getSchemeType() == 3) {
			build = new CjEtlProcessBuild();
		} else {
			build = new DefaultEtlProcessBuild();
		}

		build.setWebRetrieveResult(webRetrieveResult);

		List<DataTransform> transforms = dataTransformService
				.findByTableNameAndType(webRetrieveResult.getSchemeType(), 2,
						true);
		build.setTransforms(transforms);

		List<DataTransform> validates = dataTransformService
				.findByTableNameAndType(webRetrieveResult.getSchemeType(), 1,
						true);
		build.setValidates(validates);
		return build;
	}

	protected EtlExecutor exec(DefaultEtlProcessBuild build) throws Exception {
		IStep step = build.build();
		EtlExecutor executor = new EtlExecutor(step, build.getContext());
		executor.execute();
		return executor;
	}

	protected EtlProcessResult createMessage(EtlExecutor executor, Exception e) {

		EtlProcessResult result = new EtlProcessResult();
		boolean hasError = false;
		boolean hasWarn = false;
		boolean hasIgnore = false;

		if (executor != null) {
			IEtlContext context = executor.getContext();
			for (IStep tempStep : context.getSteps()) {
				IStepMetadata stepMetadata = tempStep.getStepMetadata();
				if (stepMetadata.getPk() != null
						&& stepMetadata.getPk().equalsIgnoreCase("E")) {
					result.setExtractNum(stepMetadata.getSuccessRowNumber());
				} else if (stepMetadata.getPk() != null
						&& stepMetadata.getPk().equalsIgnoreCase("L")) {
					result.setLoadNum(stepMetadata.getSuccessRowNumber());
				}
				StepLog stepLog = stepMetadata.getStepLog();
				hasError = hasError || stepLog.isHasError();
				hasWarn = hasWarn || stepLog.isHasWarn();
				hasIgnore = hasIgnore || stepLog.isHasIgnore();
				if (!stepLog.getLogs().isEmpty()) {
					result.addLog(stepLog.getLogs());
				}
			}
		}
		result.setHasError(hasError);
		result.setHasWarn(hasWarn);
		result.setHasIgnore(hasIgnore);
		if (result.getLoadNum() == 0) {
			result.setHasError(true);
		}

		if (e != null) {
			result.setHasError(true);
			result.addLog(ExceptionHelper.trace2String(e));
		}
		result.setHasLog(!result.getLogs().isEmpty());

		return result;
	}

	protected EtlLog createAndSaveEtlLog(String userName, Long examId,
			String optionContent, int schemeType,
			EtlProcessResult etlProcessResult) throws Exception {
		boolean logIsFile = false;
		String logContent = "";
		String statusMessage = "导入成功"; 
		if (etlProcessResult.isHasError()) {
			statusMessage = "导入失败";
		}

		if (etlProcessResult.isHasLog()) {
			String fileName = DateUtils.getMillisecodeToString() + ".csv";
			SaveEtlProcessResultToFile.saveToFile(etlProcessResult.getLogs(),
					fileName);
			/**陈勇注：2017-4-19**///
			etlProcessResult.setLogs(null);
			logIsFile = true;
			logContent = fileName;
		}

		EtlLog etlLog = new EtlLog();
		etlLog.setCreateDate(new Date());
		Exam exam = new Exam();
		exam.setId(examId);
		etlLog.setExam(exam);
		etlLog.setLogType(schemeType);
		etlLog.setOptionUser(userName);
		etlLog.setOptionContent(optionContent);
		etlLog.setLogContent(logContent);
		etlLog.setLogIsFile(logIsFile);
		etlLog.setStatusMessage(statusMessage);
		etlProcessResult.setEtlLog(etlLog);
		etlLogService.save(etlLog);
		return etlLog;
	}

	protected void deleteFile(WebRetrieveResult webRetrieveResult) {

		File file = new File(webRetrieveResult.getFileDir()
				+ webRetrieveResult.getFileName());
		if (file.exists()) {
			file.delete();
		}
	}
}
