/*
 * @(#)com.cntest.fxpt.etl.domain.WebFieldRelation.java	1.0 2014年5月19日:下午4:53:23
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月19日 下午4:53:23
 * @version 1.0
 */
public class WebFieldRelation {
	private String from;
	private String to;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String toString() {
		return from + "-" + to;
	}

}
