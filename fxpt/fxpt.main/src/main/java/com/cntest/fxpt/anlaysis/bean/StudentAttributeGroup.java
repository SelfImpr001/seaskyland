/*
 * @(#)com.cntest.fxpt.anlaysis.bean.StudentGroupAttribute.java	1.0 2015年5月20日:下午4:31:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import java.util.ArrayList;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年5月20日 下午4:31:10
 * @version 1.0
 */
public class StudentAttributeGroup {
	private ArrayList<StudentAttribute> studentAttributes = new ArrayList<>();

	public ArrayList<StudentAttribute> getStudentAttributes() {
		return studentAttributes;
	}

	public void setStudentAttributes(
			ArrayList<StudentAttribute> studentAttributes) {
		this.studentAttributes = studentAttributes;
	}

	public void add(StudentAttribute studentAttribute) {
		studentAttributes.add(studentAttribute);
	}

	public String getKey() {
		StringBuffer key = new StringBuffer();
		for (StudentAttribute studentAttribute : studentAttributes) {
			key.append(studentAttribute.getStudentAttributevalue()).append(".");
		}
		key.deleteCharAt(key.length() - 1);
		return key.toString();
	}

}
