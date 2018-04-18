/*
 * @(#)com.cntest.fxpt.anlaysis.event.produce.SaveFileTaskProduce.java	1.0 2014年12月11日:上午10:16:03
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.produce;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.cntest.fxpt.anlaysis.bean.SaveFileTask;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.service.impl.WritCalcualteResultToFile;
import com.cntest.fxpt.anlaysis.uitl.SaveFileTaskTranslator;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月11日 上午10:16:03
 * @version 1.0
 */
public class SaveFileTaskProduce implements Runnable {
	private IExamContext ec;
	private Disruptor<SaveFileTask> disruptor;
	private CountDownLatch countDownLatch;
	private List<File> files;

	public SaveFileTaskProduce(IExamContext ec) {
		this.ec = ec;
		init();
	}

	private void init() {
		files = ((WritCalcualteResultToFile) ec.getSaveCalcluateResultToDBService())
				.getFiles();
		int totalTaskNum = files.size();
		ec.setTaskTotalNum(totalTaskNum);
		ec.clearCompleteTask();
		countDownLatch = new CountDownLatch(totalTaskNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		for (File f : files) {
			SaveFileTask sf = new SaveFileTask();
			sf.setFile(f);
			sf.setContext(ec);
			disruptor.publishEvent(new SaveFileTaskTranslator(sf));
		}
	}

	public SaveFileTaskProduce setDisruptor(Disruptor<SaveFileTask> disruptor) {
		this.disruptor = disruptor;
		return this;
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

}
