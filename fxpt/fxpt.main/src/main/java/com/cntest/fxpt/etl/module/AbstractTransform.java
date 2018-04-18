/*
 * @(#)com.cntest.fxpt.etl.module.AbstractTransform.java	1.0 2014年5月10日:上午11:47:11
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import com.cntest.fxpt.etl.IStepMetadata;
import com.cntest.fxpt.etl.domain.RowLog;
import com.cntest.fxpt.etl.domain.RowSet;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 上午11:47:11
 * @version 1.0
 */
public abstract class AbstractTransform {
	private RowLog log = new RowLog();
	private TransformStep transformStep;

	public AbstractTransform(TransformStep transformStep) {
		this.transformStep = transformStep;
		this.transformStep.appendTransform(this);
	}

	public void error(String message) {
		log.error(message);
	}

	public void warn(String message) {
		log.warn(message);
	}

	protected void clearnLog() {
		log.clear();
	}

	protected RowLog getLog() {
		return log;
	}

	protected IStepMetadata getStepMetadata() {
		return transformStep.getStepMetadata();
	}

	public abstract void execute(RowSet row) throws Exception;

	public abstract void compile() throws Exception;
}
