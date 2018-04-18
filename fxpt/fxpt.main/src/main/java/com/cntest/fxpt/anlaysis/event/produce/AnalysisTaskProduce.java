/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.ClassTaskProduce.java	1.0 2014年11月25日:下午4:17:17
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.produce;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.cntest.fxpt.anlaysis.customize.bean.AnalysisResource;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisResult;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisTask;
import com.cntest.fxpt.anlaysis.customize.parse.AnalysisFilterWhereResolver;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.AnalysisTaskTranslator;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午4:17:17
 * @version 1.0
 */
public class AnalysisTaskProduce implements Runnable {
	private IExamContext ec;
	private AnalysisResource analysisResource;
	private AnalysisResult analysisResult = new AnalysisResult();
	private Disruptor<AnalysisTask> disruptor;
	private CountDownLatch countDownLatch;
	private List<AnalysisTask> analysisTaskList;

	public AnalysisTaskProduce(IExamContext ec,
			AnalysisResource analysisResource) {
		this.ec = ec;
		this.analysisResource = analysisResource;
		init();
	}

	private void init() {
		AnalysisFilterWhereResolver resolver = new AnalysisFilterWhereResolver();
		resolver.parse(analysisResource.getAnalysisFilterWhere());
		analysisTaskList = resolver.getAnalysisTasks();
		countDownLatch = new CountDownLatch(analysisTaskList.size());
	}

	public AnalysisTaskProduce setDisruptor(Disruptor<AnalysisTask> disruptor) {
		this.disruptor = disruptor;
		return this;
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	public AnalysisResult getAnalysisResult() {
		return analysisResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		for (AnalysisTask analysisTask : analysisTaskList) {
			analysisTask.setContext(ec);
			analysisTask.setAnalysisResult(analysisResult);
			analysisTask.setFormulas(analysisResource.getFormulas());
			analysisTask.setAnalysisTestpapers(analysisResource
					.getAnalysisTestpapers());
			disruptor.publishEvent(new AnalysisTaskTranslator(analysisTask));
		}
	}

}
