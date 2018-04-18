/*
 * @(#)com.cntest.fxpt.anlaysis.customize.LoadExamData.java	1.0 2015年4月23日:下午4:06:33
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize;

import java.util.concurrent.CountDownLatch;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.calculate.impl.DefaultCalculateCombinationSubjectScoreImpl;
import com.cntest.fxpt.anlaysis.event.handler.CalculateCombinationSubjectScoreHandler;
import com.cntest.fxpt.anlaysis.event.handler.EndHandler;
import com.cntest.fxpt.anlaysis.event.handler.LoadStudentAndCjHandler;
import com.cntest.fxpt.anlaysis.event.produce.LoadStudentTaskWithSchoolProduce;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.BuildDisruptor;
import com.cntest.fxpt.anlaysis.uitl.Exector;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月23日 下午4:06:33
 * @version 1.0
 */
public class LoadExamData {
	public void loadExamData(IExamContext ec) throws Exception {
		LoadStudentTaskWithSchoolProduce loadPro = new LoadStudentTaskWithSchoolProduce(
				ec);
		CountDownLatch countDownLatch = loadPro.getCountDownLatch();

		Disruptor<CalculateTask> disruptor = BuildDisruptor
				.getForCalculateTask(1024);
		disruptor
				.handleEventsWithWorkerPool(new LoadStudentAndCjHandler(),
						new LoadStudentAndCjHandler(),
						new LoadStudentAndCjHandler())
				.then(new CalculateCombinationSubjectScoreHandler(
						new DefaultCalculateCombinationSubjectScoreImpl()))
				.then(new EndHandler(countDownLatch));

		disruptor.start();
		Exector.newInstance().getExecutorService()
				.submit(loadPro.setDisruptor(disruptor));
		countDownLatch.await();
		disruptor.shutdown();
	}
}
