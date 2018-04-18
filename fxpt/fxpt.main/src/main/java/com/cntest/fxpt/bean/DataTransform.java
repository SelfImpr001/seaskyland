/*
 * @(#)com.cntest.fxpt.systemsetting.etl.domain.DataTransform.java	1.0 2014年5月14日:上午9:12:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 上午9:12:47
 * @version 1.0
 */
public class DataTransform {
	private Long id;
	private String name;
	private int type;
	private String content;
	private boolean isValid;
	private DataCategory dataCategory;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public DataCategory getDataCategory() {
		return dataCategory;
	}

	public void setDataCategory(DataCategory dataCategory) {
		this.dataCategory = dataCategory;
	}

}
