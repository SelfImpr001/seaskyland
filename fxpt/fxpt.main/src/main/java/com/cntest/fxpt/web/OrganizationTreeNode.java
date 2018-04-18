/*
 * @(#)com.cntest.fxpt.web.EducationTreeNode.java	1.0 2014年6月27日:上午9:52:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.web;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月27日 上午9:52:45
 * @version 1.0
 */
public class OrganizationTreeNode {
	private String code;
	private String name;
	private String parentCode;
	private int type;
	private boolean isParent;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isIsParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	

}
