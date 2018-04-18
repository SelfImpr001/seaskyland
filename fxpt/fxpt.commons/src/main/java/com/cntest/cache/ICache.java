/*
 * @(#)com.cntest.cache.ICache.java	1.0 2014年7月24日:下午4:04:20
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.cache;

/**
 * <Pre>
 * 缓存管理接口
 * </Pre>
 * 
 * @author 刘海林 2014年7月24日 下午4:04:20
 * @version 1.0
 */
public interface ICache {
	public void put(String key, Object value);

	public <T> T get(String key);

	public void clean();

	public void clean(String key);
}
