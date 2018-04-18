/*
 * @(#)com.cntest.fxpt.bean.FileInfo.java	1.0 2014年10月11日:上午10:39:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月11日 上午10:39:47
 * @version 1.0
 */
public class UploadFileInfo {
	private String name;
	private String desFileName;
	private String path;
	private String suffix;

	public String getName() {
		return name;
	}

	public String getNameNoSuffix() {
		return name.substring(0, name.lastIndexOf("."));
	}

	public String getDesFileName() {
		return desFileName;
	}

	public String getPath() {
		return path;
	}

	public String getSuffix() {
		return suffix;
	}

	public UploadFileInfo setName(String name) {
		this.name = name;
		return this;
	}

	public UploadFileInfo setDesFileName(String desFileName) {
		this.desFileName = desFileName;
		return this;
	}

	public UploadFileInfo setPath(String path) {
		this.path = path;
		return this;
	}

	public UploadFileInfo setSuffix(String suffix) {
		this.suffix = suffix;
		return this;
	}

	public boolean isExcel() {
		return suffix.equalsIgnoreCase(".xls")
				|| suffix.equalsIgnoreCase(".xlsx");
	}
}
