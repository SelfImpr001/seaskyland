/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.CalculateTaskTranslator.java	1.0 2014年11月25日:下午3:34:56
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.lmax.disruptor.EventTranslator;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午3:34:56
 * @version 1.0
 */
public class CalculateTaskTranslator implements
		EventTranslator<CalculateTask> {
	private CalculateTask task;

	public CalculateTaskTranslator(CalculateTask task) {
		this.task = task;
	}

	@Override
	public void translateTo(CalculateTask event, long arg1) {
		event.setCalculateTaskWith(task);
	}

}
