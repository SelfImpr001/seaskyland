/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.ClassTaskProduce.java	1.0 2014年11月25日:下午4:17:17
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.produce;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.event.handler.EndHandler;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.CalculateTaskTranslator;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.service.ISchoolService;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.SpringContext;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午4:17:17
 * @version 1.0
 */
public class LoadStudentTaskWithSchoolProduce implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(EndHandler.class);
	private IExamContext ec;
	private Disruptor<CalculateTask> disruptor;
	private CountDownLatch countDownLatch;
	private List<School> schools;

	public LoadStudentTaskWithSchoolProduce(IExamContext ec) {
		this.ec = ec;
		init();
	}

	public LoadStudentTaskWithSchoolProduce setDisruptor(
			Disruptor<CalculateTask> disruptor) {
		this.disruptor = disruptor;
		return this;
	}

	private void init() {
		ISchoolService service = SpringContext.getBean("ISchoolService");
		schools = service.examSchoolList(ec.getExam().getId());
		int size = schools.size();
		//有多少个学校就有多少个任务
		ec.setTaskTotalNum(size);
		ec.clearCompleteTask();
		countDownLatch = new CountDownLatch(size);
	}


	public CountDownLatch getCountDownLatch() {
		
		return countDownLatch;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			for (School school : schools) {
				CalculateTask task = new CalculateTask();
				task.setObj(school);
				task.setContext(ec);
				disruptor.publishEvent(new CalculateTaskTranslator(task));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("LoadStudentTaskWithSchoolProduce.class" + ExceptionHelper.trace2String(e));
		}

	}

}
