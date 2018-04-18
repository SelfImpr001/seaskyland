/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.exception;

/**
 * <pre>
 * 系统异常
 * </pre>
 * 
 * @author 李贵庆 2013-9-5
 * @version 1.0
 **/
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code;

	private String message;

	public SystemException() {

	}

	public SystemException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;

	}

	public SystemException(String code, String message, Throwable cause) {
		super(cause);
		this.code = code;
		this.message = message;

	}

	/**
	 * @param message
	 */
	public SystemException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * @param cause
	 */
	public SystemException(Throwable cause) {
		super(cause);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public SystemException(String message, Throwable cause) {
		super(message, cause);
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
