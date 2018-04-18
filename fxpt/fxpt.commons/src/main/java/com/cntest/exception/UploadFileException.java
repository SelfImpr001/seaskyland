/*
 * @(#)com.cntest.exception.UploadFileException.java	1.0 2014年7月2日:上午11:38:16
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.exception;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年7月2日 上午11:38:16
 * @version 1.0
 */
public class UploadFileException extends SystemException {
	private static final long serialVersionUID = 1L;

	public UploadFileException() {

	}

	public UploadFileException(String code, String message) {
		super(code, message);
	}

	/**
	 * @param message
	 */
	public UploadFileException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UploadFileException(Throwable cause) {
		super(cause);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public UploadFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadFileException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
