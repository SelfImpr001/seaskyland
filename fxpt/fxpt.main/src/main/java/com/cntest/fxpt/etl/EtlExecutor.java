/*
 * @(#)com.cntest.fxpt.etl.EtlExecutor.java	1.0 2014年5月10日:下午4:35:34
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl;

import com.cntest.fxpt.etl.domain.StepLog;

/**
 * <Pre>
 * 执行etl过程
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 下午4:35:34
 * @version 1.0
 */
public class EtlExecutor {
	private IStep beginStep;
	private IEtlContext context;

	public IEtlContext getContext() {
		return context;
	}

	/**
	 * 
	 */
	public EtlExecutor(IStep beginStep, IEtlContext context) {
		this.beginStep = beginStep;
		this.context = context;
	}

	public void execute() throws Exception {
		begin();
		beginStep.excuteStep();
		end();
	}
	private void begin() throws Exception {
		for (IStep step : context.getSteps()) {
			step.begin();
		}
	}

	private void end() throws Exception {
		for (IStep step : context.getSteps()) {
			step.end();
		}
	}

	public boolean hasError() {
		boolean has = false;
		for (IStep step : context.getSteps()) {
			int errorNum = step.getStepMetadata().getFailRowNumber();
			if (errorNum > 0) {
				has = true;
				break;
			}
		}
		return has;
	}

	public void printMessage() {
		for (IStep step : context.getSteps()) {
			IStepMetadata stepMetadata = step.getStepMetadata();
			System.out.println(stepMetadata.getName() + ":成功处理:"
					+ stepMetadata.getSuccessRowNumber() + "行数据;错误:"
					+ stepMetadata.getFailRowNumber() + ";警告:"
					+ stepMetadata.getWarnRowNumber() + ";忽略"
					+ stepMetadata.getIgnoreRowNumber() + "行数;");
			StepLog stepLog = stepMetadata.getStepLog();
			int index = 0;
			for (String s : stepLog.getLogs()) {
				System.out.println((++index) + "--->" + s);
			}
		}
	}

	public String messageToString() {
		StringBuffer messageSb = new StringBuffer("\t\n");
		for (IStep step : context.getSteps()) {
			IStepMetadata stepMetadata = step.getStepMetadata();
			messageSb.append(stepMetadata.getName() + ":成功处理:"
					+ stepMetadata.getSuccessRowNumber() + "行数据;错误:"
					+ stepMetadata.getFailRowNumber() + ";警告:"
					+ stepMetadata.getWarnRowNumber() + ";忽略"
					+ stepMetadata.getIgnoreRowNumber() + "行数;" + "\t\n");

			StepLog stepLog = stepMetadata.getStepLog();
			int index = 0;
			for (String s : stepLog.getLogs()) {
				messageSb.append((++index) + "--->" + s + "\t\n");
			}
		}
		return messageSb.toString();
	}
}
