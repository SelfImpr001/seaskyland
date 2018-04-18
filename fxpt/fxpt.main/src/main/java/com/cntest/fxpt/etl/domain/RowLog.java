/*
 * @(#)com.cntest.fxpt.etl.domain.Message.java	1.0 2014年5月10日:下午12:06:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.domain;

import java.util.ArrayList;

/**
 * <Pre>
 * 记录处理过程信息
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 下午12:06:04
 * @version 1.0
 */
public class RowLog {
	private String pk;
	private boolean isHasError = false;
	private boolean isHasWarn = false;
	private boolean isHasIgnore = false;

	private ArrayList<String> errors = new ArrayList<String>();
	private ArrayList<String> warns = new ArrayList<String>();
	private ArrayList<String> ignores = new ArrayList<String>();

	public void setPk(String pk) {
		this.pk = pk;
	}

	public boolean isHasError() {
		return isHasError;
	}

	public boolean isHasWarn() {
		return isHasWarn;
	}

	public boolean isHasIgnore() {
		return isHasIgnore;
	}

	public void error(String message) {
		isHasError = true;
		errors.add(message);
	}

	public void warn(String message) {
		isHasWarn = true;
		warns.add(message);
	}

	public void ignore(String message) {
		isHasIgnore = true;
		ignores.add(message);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (pk != null) {
			sb.append(pk + ",");
		}

		for (String s : errors) {
			sb.append("E:" + s + ",");
		}
		for (String s : warns) {
			sb.append("W:" + s + ",");
		}
		for (String s : ignores) {
			sb.append("I:" + s + ",");
		}

		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	public void addLog(RowLog log) {
		if (pk == null || pk.equals("")) {
			pk = log.pk;
		}
		isHasError = !isHasError && log.isHasError;
		isHasWarn = !isHasWarn && log.isHasWarn;
		isHasIgnore = !isHasIgnore && log.isHasIgnore;

		if (log.isHasError) {
			errors.addAll(log.errors);
		}
		if (log.isHasWarn) {
			warns.addAll(log.warns);
		}
		if (log.isHasIgnore) {
			ignores.addAll(log.ignores);
		}

	}

	public void clear() {
		errors.clear();
		warns.clear();
		ignores.clear();
		isHasError = false;
		isHasWarn = false;
		isHasIgnore = false;
	}
}
