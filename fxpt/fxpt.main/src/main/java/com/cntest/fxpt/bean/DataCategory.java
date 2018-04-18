/*
 * @(#)com.cntest.fxpt.systemsetting.etl.domain.DataCategory.java	1.0 2014年5月13日:下午3:37:24
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月13日 下午3:37:24
 * @version 1.0
 */
public class DataCategory {
	private Long id;
	private String name;
	private String tableName;
	private int schemeType;
	private List<DataField> dataFields;

	/**
	 * 
	 */
	public DataCategory() {
		// TODO Auto-generated constructor stub
	}

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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<DataField> getDataFields() {
		return dataFields;
	}

	public void setDataFields(List<DataField> dataFields) {
		this.dataFields = dataFields;
	}

	public int getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(int schemeType) {
		this.schemeType = schemeType;
	}

}
