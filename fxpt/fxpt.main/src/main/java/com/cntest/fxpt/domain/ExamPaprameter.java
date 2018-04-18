/*
 * @(#)com.cntest.fxpt.domain.ExamPaprameter.java	1.0 2014年6月12日:下午5:21:55
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午5:21:55
 * @version 1.0
 */
public class ExamPaprameter {
	private Long id;
	private Exam exam;
	private int paramType;
	private String paramName;
	private String paramValue;
	private String paramAsName;
	

	public Long getId() {
		return id;
	}

	public Exam getExam() {
		return exam;
	}

	public int getParamType() {
		return paramType;
	}

	public String getParamName() {
		return paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public String getParamAsName() {
		return paramAsName;
	}

	public ExamPaprameter setId(Long id) {
		this.id = id;
		return this;
	}

	public ExamPaprameter setExam(Exam exam) {
		this.exam = exam;
		return this;
	}

	public ExamPaprameter setParamType(int paramType) {
		this.paramType = paramType;
		return this;
	}

	public ExamPaprameter setParamName(String paramName) {
		this.paramName = paramName;
		return this;
	}

	public ExamPaprameter setParamValue(String paramValue) {
		this.paramValue = paramValue;
		return this;
	}

	public ExamPaprameter setParamAsName(String paramAsName) {
		this.paramAsName = paramAsName;
		return this;
	}

}
