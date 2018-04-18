/*
 * @(#)com.cntest.fxpt.domain.StatisticSetting.java	1.0 2014年10月27日:下午2:33:24
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午2:33:24
 * @version 1.0
 */
public class StatisticSetting {
	private int id;
	private int stype;
	private String sname;
	private int svalue;
	private int checked;
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStype() {
		return stype;
	}

	public void setStype(int stype) {
		this.stype = stype;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public int getSvalue() {
		return svalue;
	}

	public void setSvalue(int svalue) {
		this.svalue = svalue;
	}

	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}




	

	
}
