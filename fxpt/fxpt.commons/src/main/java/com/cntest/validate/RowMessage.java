/**
 * <p><b>© 1997-2016 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.validate;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 @2016年1月24日
 * @version 1.0
 **/
public class RowMessage {

	private MessageType type;

	private String message;
	
	public RowMessage(String message) {
		this.message = message;
		this.type = MessageType.ERROR;
	}
	
	public RowMessage(String message,MessageType type) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public MessageType getType() {
		return type;
	}
	
	public String toString() {
		return this.type +"-"+this.message;
	}

	public static enum MessageType {
		SUCCESS, WARNING, ERROR
	}
}
