/*
 * @(#)com.cntest.fxpt.bean.EtlLog.java	1.0 2014年10月30日:上午8:55:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.Date;

import com.cntest.fxpt.domain.Exam;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月30日 上午8:55:39
 * @version 1.0
 */
public class EtlLog {
	private Long id;
	private Date createDate;
	private String optionUser;
	private int logType;
	private String optionContent;
	private String logContent;
	private boolean logIsFile=false;
	private String statusMessage;
	private Exam exam;

	public Long getId() {
		return id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getOptionUser() {
		return optionUser;
	}

	public int getLogType() {
		return logType;
	}

	public Exam getExam() {
		return exam;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getOptionContent() {
		return optionContent;
	}

	public String getLogContent() {
		return logContent;
	}

	public boolean isLogIsFile() {
		return logIsFile;
	}

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public void setLogIsFile(boolean logIsFile) {
		this.logIsFile = logIsFile;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setOptionUser(String optionUser) {
		this.optionUser = optionUser;
	}

	public void setLogType(int logType) {
		this.logType = logType;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}
}
