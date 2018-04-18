/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.cache;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.CacheException;

/** 
 * <pre>
 * 缓存
 * </pre>
 *  
 * @author 李贵庆2014年11月4日
 * @version 1.0
 **/
public interface Cache<K,V> {

    public V get(K key) throws CacheException;

    public V put(K key, V value) throws CacheException;

    public V remove(K key) throws CacheException;

    public void clear() throws CacheException;

    public int size();

    public Set<K> keys();

    public Collection<V> values();

}

