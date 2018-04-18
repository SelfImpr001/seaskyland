/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.Container.java	1.0 2014年11月24日:下午5:05:26
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午5:05:26
 * @version 1.0
 */
public class Container<K, V> {
	protected ConcurrentHashMap<K, V> c = new ConcurrentHashMap<>();

	public void putAll(Container<K, V> container) {
		c.putAll(container.c);
	}

	public void put(K key, V value) {
		c.put(key, value);
	}

	public V get(K key) {
		return c.get(key);
	}

	public List<V> toList() {
		ArrayList<V> result = new ArrayList<>(c.values());
		return result;
	}

	public List<K> toListForKey() {
		ArrayList<K> result = new ArrayList<>(c.keySet());
		return result;
	}

	public void remove(K key) {
		c.remove(key);
	}

	public int size() {
		return c.size();
	}

	public boolean isEmpty() {
		return c.isEmpty();
	}

}
