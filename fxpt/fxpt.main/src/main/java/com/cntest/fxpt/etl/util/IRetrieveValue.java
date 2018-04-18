/*
 * @(#)com.cntest.fxpt.etl.util.IRetrieveValue.java	1.0 2014年5月22日:下午4:09:37
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.util;

/**
 * <Pre>
 * 定义一个接口用于获取一个常量数据
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午4:09:37
 * @version 1.0
 */
public interface IRetrieveValue<T> {
	public T value();
}
