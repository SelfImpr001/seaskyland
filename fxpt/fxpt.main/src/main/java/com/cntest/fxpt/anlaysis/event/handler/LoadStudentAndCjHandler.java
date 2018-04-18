/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.LoadStudentHandler.java	1.0 2014年12月1日:上午9:08:22
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.domain.School;
import com.cntest.util.ExceptionHelper;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月1日 上午9:08:22
 * @version 1.0
 */
public class LoadStudentAndCjHandler implements EventHandler<CalculateTask>,
		WorkHandler<CalculateTask> {
	private static final Logger log = LoggerFactory
			.getLogger(LoadStudentAndCjHandler.class);

	@Override
	public void onEvent(CalculateTask event) throws Exception {

		IExamContext context = event.getContext();
		try {
			School school = (School) event.getObj();
			Param param = new Param("school.id", school.getId());
			context.loadStudent(param);
			context.loadCj(new Param("schoolId", school.getId()));
		} catch (Exception e) {
			log.error(event.toString() + "==>>" + ExceptionHelper.trace2String(e));
			e.printStackTrace();
		}

		log.debug("加载学生信息和成绩完毕---->" + event.toString());
	}

	@Override
	public void onEvent(CalculateTask event, long sequence, boolean endOfBatch)
			throws Exception {
		this.onEvent(event);
	}

}
