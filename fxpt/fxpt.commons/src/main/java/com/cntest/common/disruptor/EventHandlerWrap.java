/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.disruptor;

import com.lmax.disruptor.EventHandler;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月6日
 * @version 1.0
 **/
public interface EventHandlerWrap<E> extends EventHandler<E> {

	//Class<E> handlerOf();
}

