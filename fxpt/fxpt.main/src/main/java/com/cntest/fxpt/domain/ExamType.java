/*
 * @(#)com.cntest.fxpt.domain.ExamType.java	1.0 2014年5月17日:下午12:16:14
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 下午12:16:14
 * @version 1.0
 */
public class ExamType {

	private Long id;
	private String name;
	private boolean isValid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
