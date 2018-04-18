/*
 * @(#)com.cntest.fxpt.domain.Subject.java	1.0 2014年5月14日:下午1:25:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import java.util.Date;

/**
 * 
 * @author Cheny 2016/10/11 
 * 
 */
public class UserExam {
	private Long id;
	/*
	 * 用户ID
	 */
	private Long userId;
	/*
	 * 考试Id
	 */
	private Long examId;
	/*
	 * 
	 */
	private int order;
	/*
	 * 扩展字段
	 */
	private String extendproperty;
	/*
	 * 扩展字段
	 */
	private String extendproperty1;
	/*
	 * 扩展字段
	 */
	private String extendproperty2;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/*
	 * 用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/*
	 * 用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/*
	 * 考试ID
	 */
	public Long getExamId() {
		return examId;
	}
	/*
	 * 考试ID
	 */
	public void setExamId(Long examId) {
		this.examId = examId;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	/*
	 *扩展字段
	 */
	public String getExtendproperty() {
		return extendproperty;
	}
	/*
	 *扩展字段
	 */
	public void setExtendproperty(String extendproperty) {
		this.extendproperty = extendproperty;
	}
	public String getExtendproperty1() {
		return extendproperty1;
	}
	public void setExtendproperty1(String extendproperty1) {
		this.extendproperty1 = extendproperty1;
	}
	public String getExtendproperty2() {
		return extendproperty2;
	}
	public void setExtendproperty2(String extendproperty2) {
		this.extendproperty2 = extendproperty2;
	}

	
	
}
