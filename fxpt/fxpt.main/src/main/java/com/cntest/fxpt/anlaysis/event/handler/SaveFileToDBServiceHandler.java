/*
 * @(#)com.cntest.fxpt.anlaysis.event.handler.SaveFileToDBServiceHandler.java	1.0 2014年12月10日:下午5:59:57
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.SaveFileTask;
import com.cntest.fxpt.anlaysis.service.impl.SaveFileToDBServiceImpl;
import com.cntest.util.ExceptionHelper;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月10日 下午5:59:57
 * @version 1.0
 */
public class SaveFileToDBServiceHandler implements EventHandler<SaveFileTask>,
		WorkHandler<SaveFileTask> {
	private static final Logger log = LoggerFactory
			.getLogger(SaveFileToDBServiceHandler.class);

	@Override
	public void onEvent(SaveFileTask event) throws Exception {
		try {
			SaveFileToDBServiceImpl sf = new SaveFileToDBServiceImpl();
			sf.save(event);
		} catch (Exception e) {
			log.error(ExceptionHelper.trace2String(e));
		}
	}

	@Override
	public void onEvent(SaveFileTask event, long sequence, boolean endOfBatch)
			throws Exception {
		this.onEvent(event);
	}

}
