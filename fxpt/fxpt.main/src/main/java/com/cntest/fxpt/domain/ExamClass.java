/*
 * @(#)com.cntest.fxpt.domain.ExamClass.java	1.0 2014年10月29日:上午9:01:14
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月29日 上午9:01:14
 * @version 1.0
 */
public class ExamClass {
	private Long id;
	private String name;
	private String code;
	private int wl;
	private String classType;
	private String schoolCode;
	private String schoolName;
	private Exam exam;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public int getWl() {
		return wl;
	}

	public String getClassType() {
		return classType;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public Exam getExam() {
		return exam;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setWl(int wl) {
		this.wl = wl;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

}
