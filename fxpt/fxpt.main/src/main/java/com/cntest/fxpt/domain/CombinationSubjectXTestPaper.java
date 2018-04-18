/*
 * @(#)com.cntest.fxpt.domain.CombinationSubjectXTestPaper.java	1.0 2014年6月12日:下午2:49:05
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午2:49:05
 * @version 1.0
 */
public class CombinationSubjectXTestPaper {
	private Long id;
	private CombinationSubject combinationSubject;
	private TestPaper testPaper;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CombinationSubject getCombinationSubject() {
		return combinationSubject;
	}

	public void setCombinationSubject(CombinationSubject combinationSubject) {
		this.combinationSubject = combinationSubject;
	}

	public TestPaper getTestPaper() {
		return testPaper;
	}

	public void setTestPaper(TestPaper testPaper) {
		this.testPaper = testPaper;
	}

}
