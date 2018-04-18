/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/** 
 * <pre>
 * 多线程启动执行器
 * </pre>
 *  
 * @author 李贵庆2015年7月22日
 * @version 1.0
 **/
public class Executor {

	private ExecutorService executorService ;
	
	private Executor() {
		this.executorService =  Executors.newCachedThreadPool(new MyThreadFactory());
	}
	
	private final static class SingletonHolder{
		private final static Executor instance = new Executor();
	}
	
	public static Executor getInstance() {
		return SingletonHolder.instance;
	}
	
	public ExecutorService getExecutorService() {
		return this.executorService;
	}
	
	public void submit(Runnable task) {
		this.executorService.submit(task);
	}
	
	static class MyThreadFactory implements ThreadFactory{
		private static final AtomicInteger poolNumber = new AtomicInteger();
		@Override
		public Thread newThread(Runnable r) {
			SecurityManager manager = System.getSecurityManager();
			ThreadGroup group  = manager != null? manager.getThreadGroup():Thread.currentThread().getThreadGroup();
			Thread thread  = new Thread(group,r,"cntest-executor-"+poolNumber.getAndIncrement()+"-thread-");
			if(thread.isDaemon()) {
				thread.setDaemon(false);
			}
			if(thread.getPriority() != Thread.NORM_PRIORITY) {
				thread.setPriority(Thread.NORM_PRIORITY);
			}
			return thread;
		}
		
	}
}

