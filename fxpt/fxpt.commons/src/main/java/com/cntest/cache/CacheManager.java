/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.cache;



/** 
 * <pre>
 * 缓存管理器
 * </pre>
 *  
 * @author 李贵庆2014年11月4日
 * @version 1.0
 **/
public interface CacheManager {
	public <K, V> Cache<K, V> getCache(String name) throws CacheException;
}

