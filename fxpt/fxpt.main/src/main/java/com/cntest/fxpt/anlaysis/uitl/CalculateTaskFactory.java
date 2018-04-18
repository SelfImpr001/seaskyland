/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.CalculateTaskFactory.java	1.0 2014年11月25日:下午3:31:02
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.lmax.disruptor.EventFactory;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午3:31:02
 * @version 1.0
 */
public class CalculateTaskFactory implements EventFactory<CalculateTask> {

	@Override
	public CalculateTask newInstance() {
		return new CalculateTask();
	}

}
