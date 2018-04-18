/*
 * @(#)com.cntest.fxpt.domain.Subject.java	1.0 2014年5月14日:下午1:25:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 下午1:25:49
 * @version 1.0
 */
public class Subject {
	private Long id;
	private String name;
	private boolean isZF;
	private Integer ordernum;

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

	public boolean isZF() {
		return isZF;
	}

	public void setZF(boolean isZF) {
		this.isZF = isZF;
	}

	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}



	

	
	

}
