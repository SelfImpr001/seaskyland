/*
 * @(#)com.cntest.fxpt.anlaysis.event.handler.AnalysisHandler.java	1.0 2015年4月22日:下午5:27:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.handler;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.customize.anlaysis.Calcluator;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisTask;
import com.cntest.util.ExceptionHelper;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 下午5:27:04
 * @version 1.0
 */
public class AnalysisHandler implements EventHandler<AnalysisTask>,
		WorkHandler<AnalysisTask> {
	private Logger log = LoggerFactory.getLogger(AnalysisHandler.class);
	private CountDownLatch countDownLatch;

	public AnalysisHandler(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void onEvent(AnalysisTask event) throws Exception {
		try {
			Calcluator c = new Calcluator(event);
			c.analysis();
		} catch (Exception e) {
			log.error(ExceptionHelper.trace2String(e));
		} finally {
			if (countDownLatch != null) {
				countDownLatch.countDown();
			}
		}

	}

	@Override
	public void onEvent(AnalysisTask event, long sequence, boolean endOfBatch)
			throws Exception {
		onEvent(event);
	}

}
