/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventFactory;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月6日
 * @version 1.0
 **/
public class DisruptorEventFactory<E extends PersistanceEvent> implements EventFactory<DisruptorEevent<E>>{
	private static Logger logger = LoggerFactory.getLogger(DisruptorEventFactory.class);

	@Override
	public DisruptorEevent<E> newInstance() {
		return new DisruptorEevent<E>() {
			private E source;
			
			private String fire;
			@Override
			public DisruptorEevent<E> eventSource(E source) {
				this.source = source;
				return this;
			}
 
			@Override
			public E getEventSource() {
				return this.source;
			}

			@Override
			public void on(String method) {
				this.fire = method;
			}

			@Override
			public String fire() {
				return this.fire;
			}

		};
			
	}

//	public static <E> EventFactory<E> lookup(EventHandlerWrap<E>... handlers) {
//		for(EventHandlerWrap<E> handler:handlers) {
//			try {
//				final Class<E> clazz = handler.handlerOf();
//				return new EventFactory<E>() {
//					@Override
//					public E newInstance() {
//						try {
//							return clazz.newInstance();
//						} catch (InstantiationException e) {
//							logger.error(ExceptionHelper.trace2String(e));
//						} catch (IllegalAccessException e) {
//							logger.error(ExceptionHelper.trace2String(e));
//						}
//						return null;
//					}
//				};
//		    }catch(Exception e) {
//		    	logger.error(ExceptionHelper.trace2String(e));
//		    }
//		}		
//		return null;	
//	}
}
