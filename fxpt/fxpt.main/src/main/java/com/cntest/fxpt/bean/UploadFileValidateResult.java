/*
 * @(#)com.cntest.fxpt.bean.UploadFileVerification.java	1.0 2014年10月15日:上午10:32:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.ArrayList;
import java.util.List;

import com.cntest.fxpt.domain.TestPaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午10:32:47
 * @version 1.0
 */
public class UploadFileValidateResult {
	private TestPaper testPaper;
	private boolean isTemplateFile = true;
	private boolean isExistContent = false;
	private boolean isHasError = false;
	private List<String> messages = new ArrayList<String>();

	public boolean isTemplateFile() {
		return isTemplateFile;
	}

	public boolean isExistContent() {
		return isExistContent;
	}

	public TestPaper getTestPaper() {
		return testPaper;
	}

	public boolean isHasError() {
		return isHasError;
	}

	public void setHasError(boolean isHasError) {
		this.isHasError = isHasError;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public void setTestPaper(TestPaper testPaper) {
		this.testPaper = testPaper;
	}

	public void setTemplateFile(boolean isTemplateFile) {
		this.isTemplateFile = isTemplateFile;
	}

	public void setExistContent(boolean isExistContent) {
		this.isExistContent = isExistContent;
	}

	public void appendMessage(String message) {
		messages.add(message);
	}

}
