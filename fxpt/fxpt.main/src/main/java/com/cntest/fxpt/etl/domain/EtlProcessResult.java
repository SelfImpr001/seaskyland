/*
 * @(#)com.cntest.fxpt.etl.domain.EtlProcessResult.java	1.0 2014年10月29日:上午10:21:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.domain;

import java.util.ArrayList;
import java.util.List;

import com.cntest.fxpt.bean.EtlLog;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月29日 上午10:21:28
 * @version 1.0
 */
public class EtlProcessResult {
	private boolean isValidateError;
	private boolean isHasError;
	private boolean isHasWarn;
	private boolean isHasIgnore;
	private boolean isHasLog;
	private int extractNum;
	private int loadNum;
	private String message;
	private EtlLog etlLog;
	private List<String> logs = new ArrayList<String>();

	
	public boolean isValidateError() {
		return isValidateError;
	}

	public void setValidateError(boolean isValidateError) {
		this.isValidateError = isValidateError;
	}

	public boolean isHasError() {
		return isHasError;
	}

	public boolean isHasWarn() {
		return isHasWarn;
	}

	public boolean isHasIgnore() {
		return isHasIgnore;
	}

	public int getExtractNum() {
		return extractNum;
	}

	public int getLoadNum() {
		return loadNum;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getLogs() {
		return logs;
	}

	public EtlLog getEtlLog() {
		return etlLog;
	}

	public boolean isHasLog() {
		return isHasLog;
	}

	public void setHasLog(boolean isHasLog) {
		this.isHasLog = isHasLog;
	}

	public void setEtlLog(EtlLog etlLog) {
		this.etlLog = etlLog;
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

	public void setExtractNum(int extractNum) {
		this.extractNum = extractNum;
	}

	public void setLoadNum(int loadNum) {
		this.loadNum = loadNum;
	}

	public void setMessage(String message) {
		this.message = message;
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
