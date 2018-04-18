/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.specification;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月4日
 * @version 1.0
 **/
public class AndSpecificationBuilder {
	private static final Logger loggr = LoggerFactory.getLogger(AndSpecificationBuilder.class);
	
	public static <T> Specification<T> build(String...specs) {
		Specification<T> spec = null;
		try {
			for(String specname:specs) {
				Specification<T> spec1 = (Specification<T>)Class.forName(specname).getConstructors()[0].newInstance();
				if(spec == null)
					spec = spec1;
				else
					spec.and(spec1);
			}			
		} catch (IllegalArgumentException e) {
			loggr.error(ExceptionHelper.trace2String(e));
		} catch (SecurityException e) {
			loggr.error(ExceptionHelper.trace2String(e));
		} catch (InstantiationException e) {
			loggr.error(ExceptionHelper.trace2String(e));
		} catch (IllegalAccessException e) {
			loggr.error(ExceptionHelper.trace2String(e));
		} catch (InvocationTargetException e) {
			loggr.error(ExceptionHelper.trace2String(e));
		} catch (ClassNotFoundException e) {
			loggr.error(ExceptionHelper.trace2String(e));
		}
		return spec;
	}
}

