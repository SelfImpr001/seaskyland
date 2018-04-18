/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.disruptor;

import org.apache.commons.beanutils.MethodUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;




/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月6日
 * @version 1.0
 **/
public class PersistenceEventHandler implements EventHandlerWrap<DisruptorEevent> {
	private static final Logger logger = LoggerFactory.getLogger(PersistenceEventHandler.class);
	
	SessionFactory sessionFactory;

	//private PersistanceEvent event;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	@Override
	public void onEvent(DisruptorEevent event, long sequence, boolean endOfBatch) throws Exception {
		
		event.getEventSource().setSessionFactory(sessionFactory);
		try {
		    MethodUtils.invokeMethod(event.getEventSource(), event.fire(),null);
		}catch(Exception e) {
			logger.error(ExceptionHelper.trace2String(e));
		};
	}

//	@Override
//	public Class handlerOf() {
//		return event.getClass();
//	}

}

