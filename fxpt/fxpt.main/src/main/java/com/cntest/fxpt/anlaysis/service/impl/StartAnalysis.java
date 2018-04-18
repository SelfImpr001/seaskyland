/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.StartAnalysis.java	1.0 2014年12月4日:下午4:06:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.SaveFileTask;
import com.cntest.fxpt.anlaysis.calculate.impl.DefaultCombinationSubjectScoreCalculator;
import com.cntest.fxpt.anlaysis.calculate.impl.WuHouCombinationSubjectScoreCalculator;
import com.cntest.fxpt.anlaysis.event.handler.*;
import com.cntest.fxpt.anlaysis.event.produce.CalculateTaskProduce;
import com.cntest.fxpt.anlaysis.event.produce.DefaultCalculateTaskProduce;
import com.cntest.fxpt.anlaysis.event.produce.LoadStudentTaskWithSchoolProduce;
import com.cntest.fxpt.anlaysis.event.produce.SaveFileTaskProduce;
import com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDBService;
import com.cntest.fxpt.anlaysis.uitl.BuildDisruptor;
import com.cntest.fxpt.anlaysis.uitl.Exector;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.service.IAanalysisService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.SpringContext;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月4日 下午4:06:52
 * @version 1.0
 */
public class StartAnalysis implements Runnable {
	private static final Logger log = LoggerFactory
			.getLogger(StartAnalysis.class);
	private ExamContext ec;

	private StartAnalysis(Exam exam) {
		ec = new ExamContext(exam);
		
		IExamService es = SpringContext.getBean("IExamService");
		if(es.getHasChoice(ec.getExam().getId())){
			ec.setHasChoice(true);
		}
		
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
		try {
			ISaveCalcluateResultToDBService saveService = ec
					.getSaveCalcluateResultToDBService();
			saveService.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sucess() {
		try {
			IExamService es = SpringContext.getBean("IExamService");
			es.updateStatus(ec.getExam().getId(), 1);
			ec.updateScore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fail() {
		IExamService es = SpringContext.getBean("IExamService");
		es.updateStatus(ec.getExam().getId(), 3);
	}
	
	
	private void saveFileToDB() throws Exception {
		SaveFileTaskProduce sfp = new SaveFileTaskProduce(ec);
		CountDownLatch countDownLatch = sfp.getCountDownLatch();

		Disruptor<SaveFileTask> disruptor = BuildDisruptor
				.getForSaveFileTask(1024);
		disruptor.handleEventsWithWorkerPool(new SaveFileToDBServiceHandler(),
				new SaveFileToDBServiceHandler(),
				new SaveFileToDBServiceHandler()).then(
				new EndHandlerForSaveFileTask(countDownLatch));
		disruptor.start();
		Exector.newInstance().getExecutorService()
				.submit(sfp.setDisruptor(disruptor));
		countDownLatch.await();
		disruptor.shutdown();
	}

	private void analysis() throws Exception {
		try {
			DefaultCalculateTaskProduce produce = new DefaultCalculateTaskProduce(
					ec);
			CountDownLatch countDownLatch = produce.getCountDownLatch();
			Disruptor<CalculateTask> disruptor = BuildDisruptor
					.getForCalculateTask(1024);
			disruptor
			.handleEventsWithWorkerPool(
					new DefaultFindCalcluateDataHander(),
					new DefaultFindCalcluateDataHander())
					.thenHandleEventsWithWorkerPool(new DefaultCalculateHandler(),
							new DefaultCalculateHandler())
							.thenHandleEventsWithWorkerPool(
									new SaveCalculateResultHandler(),
									new SaveCalculateResultHandler(),
									new SaveCalculateResultHandler())
									.then(new EndHandler(countDownLatch));
			disruptor.start();
			Exector.newInstance().getExecutorService()
			.submit(produce.setDisruptor(disruptor));
			countDownLatch.await();
			disruptor.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	private void analysis2() throws Exception {
		CalculateTaskProduce ctp = new CalculateTaskProduce(ec);
		CountDownLatch countDownLatch = ctp.getCountDownLatch();

		Disruptor<CalculateTask> disruptor = BuildDisruptor
				.getForCalculateTask(1024);
		disruptor
				.handleEventsWithWorkerPool(new FindCalcluateDataHander())
				.thenHandleEventsWithWorkerPool(new CalculateHandler(),
						new CalculateHandler())
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
		try {
			LoadStudentTaskWithSchoolProduce loadPro = new LoadStudentTaskWithSchoolProduce(
					ec);
			CountDownLatch countDownLatch = loadPro.getCountDownLatch();
			
			Disruptor<CalculateTask> disruptor = BuildDisruptor
					.getForCalculateTask(1024);
			
			if("wuhou".equals(SystemConfig.newInstance().getValue(
					"area.org.code"))){
				disruptor
				.handleEventsWithWorkerPool(new LoadStudentAndCjHandler(),
						new LoadStudentAndCjHandler(),
						new LoadStudentAndCjHandler())
						.then(new CalculateCombinationSubjectScoreHandler(
								new WuHouCombinationSubjectScoreCalculator()))
								.then(new EndHandler(countDownLatch));
			}else{
				disruptor
				.handleEventsWithWorkerPool(new LoadStudentAndCjHandler(),
						new LoadStudentAndCjHandler(),
						new LoadStudentAndCjHandler())
						.then(new CalculateCombinationSubjectScoreHandler(
								new DefaultCombinationSubjectScoreCalculator()))
								.then(new EndHandler(countDownLatch));
			}
			
			disruptor.start();
			Exector.newInstance().getExecutorService()
			.submit(loadPro.setDisruptor(disruptor));
			countDownLatch.await();
			disruptor.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void start(Exam exam) {
		StartAnalysis sa = new StartAnalysis(exam);
		Exector.newInstance().getExecutorService().submit(sa);
	}
}
