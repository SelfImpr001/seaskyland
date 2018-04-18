/**
 * <p><b>© 1997-2016 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.validate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

/** 
 * <pre>
 * 数组型HandlerGroup，非线程安全
 * </pre>
 *  
 * @author 李贵庆 @2016年1月24日
 * @version 1.0
 **/
public class ArrayHandlerGrop implements HandlerGroup {

	private ArrayList<ValidateHandler> handlers;
	
	public ArrayHandlerGrop(ValidateHandler...handlers ) {
		this.handlers = Lists.newArrayList(handlers);
	}
	
	@Override
	public List<ValidateHandler> handlers() {
		return this.handlers;
	}

	@Override
	public HandlerGroup add(ValidateHandler handler) {
		this.handlers.add(handler);
		return this;
	}

	@Override
	public void remove(ValidateHandler handler) {
		Iterator<ValidateHandler> it = this.handlers.iterator();
		while(it.hasNext()) {
			ValidateHandler sample = it.next();
			if(sample.equals(handler)) {
				it.remove();
			}
		}
	}

}
