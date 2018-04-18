/*
 * @(#)com.cntest.exception.ReadXLSException.java	1.0 2014年5月9日:下午5:05:05
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.exception;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月9日 下午5:05:05
 * @version 1.0
 */
public class ReadFileException extends RuntimeException {

	/**
	 * 
	 */
	public ReadFileException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ReadFileException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ReadFileException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReadFileException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
