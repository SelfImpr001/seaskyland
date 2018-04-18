/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.Exector.java	1.0 2014年11月25日:下午3:20:08
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午3:20:08
 * @version 1.0
 */
public class Exector {
	private static Lock lock = new ReentrantLock();
	private static Exector exector;
	private ExecutorService executorService;

	private Exector() {
		// executorService =
		// Executors.newCachedThreadPool(DaemonThreadFactory.INSTANCE);
		executorService = Executors
				.newCachedThreadPool(new CntesthreadFactory());
		// .newCachedThreadPool();

	}

	public static Exector newInstance() {
		lock.lock();
		try {
			if (exector == null) {
				exector = new Exector();
			}
		} finally {
			lock.unlock();
		}
		return exector;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	static class CntesthreadFactory implements ThreadFactory {
		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		CntesthreadFactory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
					.getThreadGroup();
			namePrefix = "cntest-calcluate-" + poolNumber.getAndIncrement()
					+ "-thread-";
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix
					+ threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
}
