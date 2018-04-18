/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月6日
 * @version 1.0
 **/
public class DisruptorProxy<E extends DisruptorEevent<T>,T extends PersistanceEvent> {

	private static final Logger logger = LoggerFactory.getLogger(DisruptorProxy.class);


	private static final int BUFFER_SIZE = 1024 * 8;

	private Disruptor<E> disruptor;
	
	private RingBuffer<E> ringBuffer;
	
	private static DisruptorProxy instance;

	public DisruptorProxy(EventHandlerWrap<E>... handlers) {
		Executor executor = Executors.newCachedThreadPool();
		disruptor = new Disruptor<E>(new DisruptorEventFactory(), BUFFER_SIZE, executor);
		disruptor.handleEventsWith(handlers);
		//disruptor.
		this.ringBuffer = disruptor.start();
		instance = this;
	}
	
	public static DisruptorProxy getInstance(){
		return instance;
	}
	
	public Disruptor<E> getDisruptor(){
		return this.disruptor;
	}
	
	public void produce(T t,String method) {
		long sequence = this.ringBuffer.next();
		E event = this.ringBuffer.get(sequence);
		event.eventSource(t).on(method);
		logger.debug("execute {} of {}",method,t);
		this.ringBuffer.publish(sequence);
	}

}
