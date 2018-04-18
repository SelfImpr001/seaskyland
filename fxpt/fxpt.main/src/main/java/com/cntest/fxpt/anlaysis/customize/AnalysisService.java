/*
 * @(#)com.cntest.fxpt.anlaysis.customize.AnalysisService.java	1.0 2015年4月24日:上午8:20:03
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize;

import java.util.concurrent.CountDownLatch;

import com.cntest.fxpt.anlaysis.customize.bean.AnalysisResource;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisResult;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisTask;
import com.cntest.fxpt.anlaysis.event.handler.AnalysisHandler;
import com.cntest.fxpt.anlaysis.event.produce.AnalysisTaskProduce;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.BuildDisruptor;
import com.cntest.fxpt.anlaysis.uitl.Exector;
import com.cntest.fxpt.domain.Exam;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月24日 上午8:20:03
 * @version 1.0
 */
public class AnalysisService {
	public AnalysisResult analysis(Exam exam, AnalysisResource analysisResource)
			throws Exception {
		IExamContext ec = AnalysisExamMgr.getInstance().get(exam);
		AnalysisTaskProduce analysisTaskProduce = new AnalysisTaskProduce(ec,
				analysisResource);

		CountDownLatch countDownLatch = analysisTaskProduce.getCountDownLatch();

		Disruptor<AnalysisTask> disruptor = BuildDisruptor
				.getForAnalysisTask(1024);
		disruptor.handleEventsWithWorkerPool(
				new AnalysisHandler(countDownLatch), new AnalysisHandler(
						countDownLatch), new AnalysisHandler(countDownLatch));

		disruptor.start();
		Exector.newInstance().getExecutorService()
				.submit(analysisTaskProduce.setDisruptor(disruptor));
		countDownLatch.await();
		disruptor.shutdown();
		return analysisTaskProduce.getAnalysisResult();
	}
}
