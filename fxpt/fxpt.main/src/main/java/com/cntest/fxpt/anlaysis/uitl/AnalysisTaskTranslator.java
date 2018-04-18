/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.CalculateTaskTranslator.java	1.0 2014年11月25日:下午3:34:56
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import com.cntest.fxpt.anlaysis.customize.bean.AnalysisTask;
import com.lmax.disruptor.EventTranslator;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午3:34:56
 * @version 1.0
 */
public class AnalysisTaskTranslator implements EventTranslator<AnalysisTask> {
	private AnalysisTask task;

	public AnalysisTaskTranslator(AnalysisTask task) {
		this.task = task;
	}

	@Override
	public void translateTo(AnalysisTask event, long arg1) {
		event.copy(task);
	}

}
