/*
 * @(#)com.cntest.fxpt.anlaysis.bean.SaveFileTask.java	1.0 2014年12月10日:下午4:16:36
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import java.io.File;

import com.cntest.fxpt.anlaysis.service.IExamContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月10日 下午4:16:36
 * @version 1.0
 */
public class SaveFileTask {
	private IExamContext context;
	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public IExamContext getContext() {
		return context;
	}

	public void setContext(IExamContext context) {
		this.context = context;
	}

	public void copyFrom(SaveFileTask task) {
		this.context = task.context;
		this.file = task.file;
	}
}
