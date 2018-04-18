/*
 * @(#)com.cntest.cache.aop.CacheAspect.java	1.0 2014年7月25日:上午9:36:09
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.cache.aop;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.cache.EhCacheMgr;
import com.cntest.cache.ICache;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年7月25日 上午9:36:09
 * @version 1.0
 */

public class CacheAspect {
	private static final Logger log = LoggerFactory
			.getLogger(CacheAspect.class);
	private String cacheName;

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public Object cacheAround(ProceedingJoinPoint point) throws Throwable {

		String className = point.getTarget().getClass().getName();
		String methedName = point.getSignature().getName();
		Object[] args = point.getArgs();

		String key = createKey(className,methedName, args);
		ICache cache = EhCacheMgr.newInstance(cacheName);
		Object result = cache.get(key);
		if(result == null){
			result = point.proceed(args);
			cache.put(key, result);
		}
		return result;
	}

	private String createKey(String className,String methedName, Object[] args) {
		StringBuffer keyBuf = new StringBuffer();
		keyBuf.append(className);
		keyBuf.append(".");
		keyBuf.append(methedName);
		keyBuf.append("(");
		if (args != null) {
			for (Object arg : args) {
				keyBuf.append(arg.toString());
				keyBuf.append(",");
			}
			keyBuf.deleteCharAt(keyBuf.length() - 1);
		}
		keyBuf.append(")");

		return keyBuf.toString();
	}
}
