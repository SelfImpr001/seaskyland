/*
 * @(#)com.cntest.fxpt.etl.business.CachException.java	1.0 2014年7月1日:上午9:21:29
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年7月1日 上午9:21:29
 * @version 1.0
 */
public class CachException extends RuntimeException {
	public CachException(Throwable cause) {
		super(cause);
	}

	public CachException(String message, Throwable cause) {
		super(message, cause);
	}
}
