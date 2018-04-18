/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.CalculateHandler.java	1.0 2014年11月28日:上午9:16:02
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.calculate.impl.DefaultCalcluator;
import com.cntest.util.ExceptionHelper;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 上午9:16:02
 * @version 1.0
 */
public class DefaultCalculateHandler implements EventHandler<CalculateTask>,
		WorkHandler<CalculateTask> {
	private static final Logger log = LoggerFactory
			.getLogger(DefaultCalculateHandler.class);

	@Override
	public void onEvent(CalculateTask event) throws Exception {
		try {
			DefaultCalcluator calculator = new DefaultCalcluator();
			CalculateResult cr = event.getCalculateResult(0);
			calculator.calculate(event, cr, event.getSubjectScoreContainer());
 		} catch (Exception e) {
			log.error(event.toString() + "==>>"
					+ ExceptionHelper.trace2String(e));
			e.printStackTrace();
		}

		log.debug("计算完毕" + event.toString());
	}

	@Override
	public void onEvent(CalculateTask event, long arg1, boolean arg2)
			throws Exception {
		this.onEvent(event);
	}

}
