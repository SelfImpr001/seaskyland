/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.CalculateHandler.java	1.0 2014年11月28日:上午9:16:02
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.handler;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.SaveFileTask;
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
public class EndHandlerForSaveFileTask implements EventHandler<SaveFileTask>,
		WorkHandler<SaveFileTask> {
	private static final Logger log = LoggerFactory
			.getLogger(EndHandlerForSaveFileTask.class);
	private CountDownLatch countDownLatch;

	public EndHandlerForSaveFileTask(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void onEvent(SaveFileTask event) throws Exception {

		try {
			countDownLatch.countDown();
			int cur = event.getContext().completeTask();
			int total = event.getContext().getTaskTotalNum();
			log.debug("总数/完成:" + total + "/" + cur);
		} catch (Exception e) {
			log.error(event.toString() + "==>>"
					+ ExceptionHelper.trace2String(e));
			e.printStackTrace();
		}
	}

	@Override
	public void onEvent(SaveFileTask event, long arg1, boolean arg2)
			throws Exception {
		this.onEvent(event);
	}

}
