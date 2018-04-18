/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.StartAnalysis.java	1.0 2014年12月4日:下午4:06:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.calculate.impl.WuHouCombinationSubjectScoreCalculator;
import com.cntest.fxpt.anlaysis.event.handler.CalculateCombinationSubjectScoreHandler;
import com.cntest.fxpt.anlaysis.event.handler.EndHandler;
import com.cntest.fxpt.anlaysis.event.handler.LoadStudentAndCjHandler;
import com.cntest.fxpt.anlaysis.event.handler.SaveCalculateResultHandler;
import com.cntest.fxpt.anlaysis.event.handler.WuHouCalculateHandler;
import com.cntest.fxpt.anlaysis.event.handler.XinjiangFindCalcluateDataHander;
import com.cntest.fxpt.anlaysis.event.produce.LoadStudentTaskWithSchoolProduce;
import com.cntest.fxpt.anlaysis.event.produce.XinjiangCalculateTaskProduce;
import com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDBService;
import com.cntest.fxpt.anlaysis.uitl.BuildDisruptor;
import com.cntest.fxpt.anlaysis.uitl.Exector;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.service.IAanalysisService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.SpringContext;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月4日 下午4:06:52
 * @version 1.0
 */
public class StartAnalysisForWuHou implements Runnable {
	private static final Logger log = LoggerFactory
			.getLogger(StartAnalysisForWuHou.class);
	private ExamContext ec;

	private StartAnalysisForWuHou(Exam exam) {
		ec = new ExamContext(exam);
		ec.setSaveResult(new XinjiangSaveCalcluateResultToDBService());

		ExamMgr.getInstance().remove(exam.getId());
		ExamMgr.getInstance().put(exam.getId(), ec);
	}

	@Override
	public void run() {
		try {
			long b = System.currentTimeMillis();
			loadExamData();
			analysis();
			saveLastData();
			sucess();
			long e = System.currentTimeMillis();
			log.debug("分析完毕。花费时间:" + ((e - b) * 1.0 / 1000) + "秒");
			startNextAnalysis();
		} catch (Exception e) {
			log.error(ExceptionHelper.trace2String(e));
			fail();
		} finally {
			ec.setAllComplate();
			ExamMgr.getInstance().remove(ec.getExam().getId());
			System.gc();
		}
	}

	private void startNextAnalysis() {
		IExamService examService = SpringContext.getBean("IExamService");
		Exam exam = examService.tryGetAnalysisExam();
		if (exam != null) {
			IAanalysisService analysisService = SpringContext
					.getBean("IAanalysisService");
			analysisService.clarenAnalysisResult(exam.getId());
			analysisService.prepareAnalysis(exam.getId());
			analysisService.startAnalysis(exam.getId());
		}
	}

	private void saveLastData() throws Exception {
		ISaveCalcluateResultToDBService saveService = ec
				.getSaveCalcluateResultToDBService();
		saveService.clear();
	}

	private void sucess() {
		IExamService es = SpringContext.getBean("IExamService");
		es.updateStatus(ec.getExam().getId(), 1);
	}

	private void fail() {
		IExamService es = SpringContext.getBean("IExamService");
		es.updateStatus(ec.getExam().getId(), 3);
	}

	private void analysis() throws Exception {

		XinjiangCalculateTaskProduce ctp = new XinjiangCalculateTaskProduce(ec);
		CountDownLatch countDownLatch = ctp.getCountDownLatch();

		Disruptor<CalculateTask> disruptor = BuildDisruptor
				.getForCalculateTask(1024);
		disruptor
				.handleEventsWithWorkerPool(
						new XinjiangFindCalcluateDataHander(),
						new XinjiangFindCalcluateDataHander())
				.thenHandleEventsWithWorkerPool(new WuHouCalculateHandler(),
						new WuHouCalculateHandler())
				.thenHandleEventsWithWorkerPool(
						new SaveCalculateResultHandler(),
						new SaveCalculateResultHandler(),
						new SaveCalculateResultHandler())
				.then(new EndHandler(countDownLatch));

		disruptor.start();
		Exector.newInstance().getExecutorService()
				.submit(ctp.setDisruptor(disruptor));
		countDownLatch.await();
		disruptor.shutdown();
	}

	private void loadExamData() throws Exception {
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
						new WuHouCombinationSubjectScoreCalculator()))
				.then(new EndHandler(countDownLatch));

		disruptor.start();
		Exector.newInstance().getExecutorService()
				.submit(loadPro.setDisruptor(disruptor));
		countDownLatch.await();
		disruptor.shutdown();
	}

	public static void start(Exam exam) {
		StartAnalysisForWuHou sa = new StartAnalysisForWuHou(exam);
		Exector.newInstance().getExecutorService().submit(sa);
	}
}
