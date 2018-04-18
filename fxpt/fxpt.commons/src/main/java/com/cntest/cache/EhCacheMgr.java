/*
 * @(#)com.cntest.cache.EhCacheMgr.java	1.0 2014年7月24日:下午4:07:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年7月24日 下午4:07:28
 * @version 1.0
 */
public class EhCacheMgr implements ICache {
	private static final Logger log = LoggerFactory.getLogger(EhCacheMgr.class);
	private Cache cache;

	private EhCacheMgr(String cacheName) {
		cache = CacheManager.newInstance().getCache(cacheName);
	}

	public static ICache newInstance(String cacheName) {
		return new EhCacheMgr(cacheName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.cache.ICache#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public void put(String key, Object value) {
		log.debug("存放数据进入缓存中");
		cache.put(new Element(key, value));
		log.debug("存放数据进入缓存完毕");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.cache.ICache#get(java.lang.String)
	 */
	@Override
	public <T> T get(String key) {
		log.debug("获取缓存中的数据");
		Element e = cache.get(key);
		T value = null;
		if (e != null) {
			value = (T) e.getObjectValue();
		}
		log.debug("value:" + value);
		log.debug("获取缓存中的数据完毕");
		return value;
	}

	@Override
	public void clean() {
		log.debug("清空缓存数据");
		cache.removeAll();
		log.debug("清空缓存数据完毕");
	}

	@Override
	public void clean(String key) {
		log.debug("清空缓存数据---->" + key);
		cache.remove(key);
		log.debug("清空缓存数据完毕");
	}

}
