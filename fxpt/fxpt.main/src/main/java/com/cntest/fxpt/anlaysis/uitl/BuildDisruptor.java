/*
 * @(#)com.cntest.analysis.event.TestUtil.java	1.0 2014年12月2日:下午3:03:42
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.SaveFileTask;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisTask;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月2日 下午3:03:42
 * @version 1.0
 */
public class BuildDisruptor<T> {
	private BuildDisruptor() {

	}

	public Disruptor<T> getDisruptor(EventFactory<T> factory) {
		return getDisruptor(8, factory);
	}

	public Disruptor<T> getDisruptor(int num, EventFactory<T> factory) {
		int taskBuffer = Util.ceilingNextPowerOfTwo(num);
		Disruptor<T> disruptor = new Disruptor<>(factory, taskBuffer, Exector
				.newInstance().getExecutorService(), ProducerType.SINGLE,
				new YieldingWaitStrategy());
		return disruptor;
	}

	public static Disruptor<CalculateTask> getForCalculateTask(int num) {
		BuildDisruptor<CalculateTask> build = new BuildDisruptor<>();
		return build.getDisruptor(num, new CalculateTaskFactory());
	}

	public static Disruptor<AnalysisTask> getForAnalysisTask(int num) {
		BuildDisruptor<AnalysisTask> build = new BuildDisruptor<>();
		return build.getDisruptor(num, new AnalysisTaskFactory());
	}

	public static Disruptor<SaveFileTask> getForSaveFileTask(int num) {
		BuildDisruptor<SaveFileTask> build = new BuildDisruptor<>();
		return build.getDisruptor(num, new SaveFileTaskFactory());
	}
}
