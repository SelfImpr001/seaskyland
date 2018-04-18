/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.web.view;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2013-6-10
 * @version 1.0
 **/
public class ResponseStatus {

	public final static String NAME = "status";

	private boolean success;

	private String code;

	private String msg;

	private String msgDetail;

	public static class Builder {

		private boolean success;

		private String code = "";

		private String msg = "";

		private String msgDetail = "";

		public Builder(boolean success) {
			this.success = success;
		}

		public Builder code(String code) {
			this.code = code;
			return this;
		}

		public Builder msg(String msg) {
			this.msg = msg;
			return this;
		}

		public Builder msgDetail(String msgDetail) {
			this.msgDetail = msgDetail;
			return this;
		}

		public ResponseStatus build() {
			return new ResponseStatus(this);
		}
	}

	private ResponseStatus(Builder builder) {
		this.code = builder.code;
		this.msg = builder.msg;
		this.success = builder.success;
		this.msgDetail = builder.msgDetail;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public String getMsgDetail() {
		return msgDetail;
	}

}
