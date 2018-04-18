/*
 * @(#)com.cntest.fxpt.domain.CombinationSubjectCalculateRule.java	1.0 2015年5月22日:下午2:32:21
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年5月22日 下午2:32:21
 * @version 1.0
 */
public class CombinationSubjectCalculateRule {
	private Long id;
	private String studentAttributeName;
	private String studentAttributeValue;
	private CombinationSubject combinationSubject;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentAttributeName() {
		return studentAttributeName;
	}

	public void setStudentAttributeName(String studentAttributeName) {
		this.studentAttributeName = studentAttributeName;
	}

	public String getStudentAttributeValue() {
		return studentAttributeValue;
	}

	public void setStudentAttributeValue(String studentAttributeValue) {
		this.studentAttributeValue = studentAttributeValue;
	}

	public CombinationSubject getCombinationSubject() {
		return combinationSubject;
	}

	public void setCombinationSubject(CombinationSubject combinationSubject) {
		this.combinationSubject = combinationSubject;
	}

}
