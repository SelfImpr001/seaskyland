/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.FindDataHander.java	1.0 2014年11月25日:上午10:49:36
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.StudentCjContainer;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.util.ExceptionHelper;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 上午10:49:36
 * @version 1.0
 */
public class DefaultFindCalcluateDataHander implements
		EventHandler<CalculateTask>, WorkHandler<CalculateTask> {
	private static final Logger log = LoggerFactory
			.getLogger(DefaultFindCalcluateDataHander.class);

	@Override
	public void onEvent(CalculateTask event) throws Exception {
		try {
			IExamContext context = event.getContext();
			StudentCjContainer scjc = context.getStudentCjContainer(event
					.getFindStudentFilter());
			event.setBkrs(scjc.size());
			event.setSubjectScoreContainer(scjc.getSubjectScoreContainer(
					event.getAnalysisTestpaper(), null));
		} catch (Exception e) {
			log.error(event.toString() + "==>>"
					+ ExceptionHelper.trace2String(e));
			e.printStackTrace();
		}

		log.debug("构建CalculateTask加载完毕--->" + event.toString());
	}

	@Override
	public void onEvent(CalculateTask event, long arg1, boolean arg2)
			throws Exception {
		this.onEvent(event);
	}

}
