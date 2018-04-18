/**
 * <p><b>© 2001-2013 CC</b></p>
 * 
 **/

package com.cntest.exception;

/**
 * <pre>
 * 业务异常
 * </pre>
 * 
 * @author 李贵庆 2013-8-10
 * @version 1.0
 **/
public class BusinessException extends Exception {
	private String code;

	private String message;

	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
