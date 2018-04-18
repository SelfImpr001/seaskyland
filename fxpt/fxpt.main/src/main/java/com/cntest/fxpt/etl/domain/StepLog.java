/*
 * @(#)com.cntest.fxpt.etl.domain.Message.java	1.0 2014年5月10日:下午12:06:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * <Pre>
 * 记录处理过程信息
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 下午12:06:04
 * @version 1.0
 */
public class StepLog {
	private boolean isHasError = false;
	private boolean isHasWarn = false;
	private boolean isHasIgnore = false;
	private int errorCount;
	private int warnCount;
	private int ignoreCount;
	private List<String> logs = new ArrayList<String>();

	public boolean isHasError() {
		return isHasError;
	}

	public boolean isHasWarn() {
		return isHasWarn;
	}

	public boolean isHasIgnore() {
		return isHasIgnore;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public int getWarnCount() {
		return warnCount;
	}

	public int getIgnoreCount() {
		return ignoreCount;
	}

	public List<String> getLogs() {
		return logs;
	}

	public void setHasError(boolean isHasError) {
		this.isHasError = isHasError;
	}

	public void setHasWarn(boolean isHasWarn) {
		this.isHasWarn = isHasWarn;
	}

	public void setHasIgnore(boolean isHasIgnore) {
		this.isHasIgnore = isHasIgnore;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public void setWarnCount(int warnCount) {
		this.warnCount = warnCount;
	}

	public void setIgnoreCount(int ignoreCount) {
		this.ignoreCount = ignoreCount;
	}

	public void setLogs(List<String> logs) {
		this.logs = logs;
	}

	public void addLog(String log) {
		logs.add(log);
	}

	public void addLog(List<String> logs) {
		this.logs.addAll(logs);
	}
}
