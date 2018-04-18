/*
 * @(#)com.cntest.fxpt.systemsetting.etl.domain.DataField.java	1.0 2014年5月13日:下午3:40:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月13日 下午3:40:45
 * @version 1.0
 */
public class DataField {
	private Long id;

	private String fieldName;
	private String asName;
	private String defaultName = "";
	private int sortNum;
	private boolean isValid;
	private boolean isNeed;
	private DataCategory dataCategory;
	// 下面2个变量在导入成绩的时候有用
	private boolean isSelItem = false;
	private boolean isSelOption = false;
	//字段描述：下载模板时对导入字段的说明
	private String description;

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSelItem() {
		return isSelItem;
	}

	public void setSelItem(boolean isSelItem) {
		this.isSelItem = isSelItem;
	}

	public boolean isSelOption() {
		return isSelOption;
	}

	public void setSelOption(boolean isSelOption) {
		this.isSelOption = isSelOption;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getAsName() {
		return asName;
	}

	public void setAsName(String asName) {
		this.asName = asName;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getDefaultName() {
		return defaultName;
	}

	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}

	public DataCategory getDataCategory() {
		return dataCategory;
	}

	public void setDataCategory(DataCategory dataCategory) {
		this.dataCategory = dataCategory;
	}

	public boolean isNeed() {
		return isNeed;
	}

	public void setNeed(boolean isNeed) {
		this.isNeed = isNeed;
	}

}
