/*
 * @(#)com.cntest.fxpt.domain.ZFSubjectBuildRule.java	1.0 2015年6月11日:下午5:24:20
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 下午5:24:20
 * @version 1.0
 */
public class ZFSubjectBuildRule {
	private Long id;
	private String name;
	private String buildRuleSQL;
	private String classifyFiled;
	private String testPaperIdField;
	private boolean available;

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

	public String getBuildRuleSQL() {
		return buildRuleSQL;
	}

	public void setBuildRuleSQL(String buildRuleSQL) {
		this.buildRuleSQL = buildRuleSQL;
	}

	public String getClassifyFiled() {
		return classifyFiled;
	}

	public void setClassifyFiled(String classifyFiled) {
		this.classifyFiled = classifyFiled;
	}

	public String getTestPaperIdField() {
		return testPaperIdField;
	}

	public void setTestPaperIdField(String testPaperIdField) {
		this.testPaperIdField = testPaperIdField;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

}
