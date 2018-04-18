/*
 * @(#)com.cntest.fxpt.etl.business.CacheMgr.java	1.0 2014年7月1日:上午8:45:33
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.cntest.util.ExceptionHelper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年7月1日 上午8:45:33
 * @version 1.0
 */
public class CacheMgr {
	private static final Logger log = LoggerFactory.getLogger(CacheMgr.class);
	private static final String ETL_CACHE_NAME = "fxptEtlCacheName";
	private static volatile CacheMgr singleton;
	private CacheManager manager;

	private CacheMgr(String cacheFilePath) {
		log.debug("实例化cache");
		URL path = createPath(cacheFilePath);

		log.debug("cache file path:" + path.toString());
		manager = CacheManager.newInstance(path);
		log.debug("实例化cache完成");
	}

	private URL createPath(String cacheFilePath) throws RuntimeException {
		URL path = null;
		try {
			if (cacheFilePath == null) {
				path = getDefaultCachConfigFile();
			} else {
				path = new URL(cacheFilePath);
			}

		} catch (Exception e) {
			throw new RuntimeException("初始化cach路径出错", e);
		}
		return path;
	}

	private URL getDefaultCachConfigFile() {
		String curDir = this.getClass().getResource("").toString();
		URL url = null;
		try {
			url = new URL(curDir + "SynCatch.xml");
		} catch (Exception e) {
			throw new CachException(e);
		}
		return url;
	}

	public static CacheMgr newInstance() {
		if (singleton != null) {
			return singleton;
		}
		synchronized (CacheManager.class) {
			if (singleton == null) {
				singleton = new CacheMgr(null);
			}
			return singleton;
		}
	}

	public void put(String key, Object value) {
		CacheManager tmpMgr = manager.getCacheManager(ETL_CACHE_NAME);
		Cache cache = tmpMgr.getCache("etl");
		cache.put(new Element(key, value));
	}

	public <T> T get(String key) {
		log.debug("缓存中获取数据");
		CacheManager tmpMgr = manager.getCacheManager(ETL_CACHE_NAME);
		Cache cache = tmpMgr.getCache("etl");
		Element e = cache.get(key);
		T value = null;
		if (e != null) {
			value = (T) e.getObjectValue();
		}
		log.debug("value:" + value);
		log.debug("缓存中获取数据完毕");
		return value;
	}

	public void clearCache() {
		log.debug("清空缓存中的数据");
		CacheManager tmpMgr = manager.getCacheManager(ETL_CACHE_NAME);
		tmpMgr.clearAll();
		log.debug("清空缓存中的数据完毕");
	}
}
