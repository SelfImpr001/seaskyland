/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.ClassTaskProduce.java	1.0 2014年11月25日:下午4:17:17
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.produce;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.cntest.fxpt.anlaysis.bean.AnalysisTestpaperContainer;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.CalculateTaskDes;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.BuildTask;
import com.cntest.fxpt.anlaysis.uitl.CalculateTaskTranslator;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午4:17:17
 * @version 1.0
 */
public class DefaultCalculateTaskProduce implements Runnable {
	private IExamContext ec;
	private Disruptor<CalculateTask> disruptor;
	private CountDownLatch countDownLatch;
	private List<CalculateTaskDes> tasksList;

	public DefaultCalculateTaskProduce(IExamContext ec) {
		this.ec = ec;
		init();
	}

	public List<CalculateTaskDes> getTasksList() {
		return tasksList;
	}

	private void init() {
		BuildTask buildTask = new BuildTask();
		tasksList = buildTask.build(ec);
		int taskTotalNum = calcuateTaskNum();
		ec.setTaskTotalNum(taskTotalNum);
		ec.clearCompleteTask();
		countDownLatch = new CountDownLatch(taskTotalNum);
	}

	private int calcuateTaskNum() {
		int taskCount = getTaskCount();
		int testPaperCount = getTestpaperCount();
		int wlTestpaperCount = getWLTestpaperCount();
		return taskCount * testPaperCount + taskCount * 2 * wlTestpaperCount;
	}

	private int getTaskCount() {
		return tasksList.size();
	}

	private int getTestpaperCount() {
		return ec.getAnalysisTestpaperContainer().size();
	}

	private int getWLTestpaperCount() {
		int testpaperCount = 0;
		if (ec.getExam().isWlForExamStudent()) {
			AnalysisTestpaperContainer atpContainer = ec
					.getAnalysisTestpaperContainer();
			for (AnalysisTestpaper at : atpContainer.toList()) {
				if (at.getPaperType() == 0) {
					testpaperCount++;
				}
			}
		}
		return testpaperCount;
	}

	public DefaultCalculateTaskProduce setDisruptor(
			Disruptor<CalculateTask> disruptor) {
		this.disruptor = disruptor;
		return this;
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
		List<AnalysisTestpaper> testpapers = ec.getAnalysisTestpaperContainer()
				.toList();
		for (CalculateTaskDes taskDes : tasksList) {
			for (AnalysisTestpaper at : testpapers) {
				publishWL(taskDes, at);
				publishCurrentTestPaper(taskDes, at);
			}
		}
	}

	private void publishWL(CalculateTaskDes taskDes, AnalysisTestpaper testpaper) {
		if (isPublishWLTestpaper(testpaper)) {
			CalculateTask task = new CalculateTaskFactory().build(ec, taskDes,
					testpaper, 1);
			publish(task);
			CalculateTask task2 = new CalculateTaskFactory().build(ec, taskDes,
					testpaper, 2);
			publish(task2);
		}
	}

	private boolean isPublishWLTestpaper(AnalysisTestpaper testpaper) {
		return ec.getExam().isWlForExamStudent()
				&& testpaper.getPaperType() == 0;
	}

	private void publishCurrentTestPaper(CalculateTaskDes taskDes,
			AnalysisTestpaper testpaper) {
		CalculateTask task = new CalculateTaskFactory().build(ec, taskDes,
				testpaper, testpaper.getPaperType());
		publish(task);
	}

	private void publish(CalculateTask task) {
		disruptor.publishEvent(new CalculateTaskTranslator(task));
	}

	public void printlnTask() {
		List<AnalysisTestpaper> testpapers = ec.getAnalysisTestpaperContainer()
				.toList();

		System.out
				.println("***************************************************");
		System.out.println("任务总数:" + calcuateTaskNum());
		System.out
				.println("***************************************************");
		int num = 1;
		for (CalculateTaskDes taskDes : tasksList) {
			for (AnalysisTestpaper testpaper : testpapers) {
				if (isPublishWLTestpaper(testpaper)) {
					CalculateTask task = new CalculateTaskFactory().build(ec,
							taskDes, testpaper, 1);
					System.out.println((num++) + "==" + task.toString());
					CalculateTask task2 = new CalculateTaskFactory().build(ec,
							taskDes, testpaper, 2);
					System.out.println((num++) + "==" + task2.toString());

				}
				CalculateTask task = new CalculateTaskFactory().build(ec,
						taskDes, testpaper, testpaper.getPaperType());
				System.out.println((num++) + "==" + task.toString());
			}
		}
	}
}
