/*
 * @(#)com.cntest.fxpt.domain.ExamOfSchoolTerm.java	1.0 2014年6月16日:下午2:07:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.web;

import java.util.ArrayList;
import java.util.List;

import com.cntest.fxpt.domain.Exam;

/**
 * <Pre>
 * 学期下面的考试
 * </Pre>
 * 
 * @author 刘海林 2014年6月16日 下午2:07:39
 * @version 1.0
 */
public class ExamOfSchoolTerm {
	private int schoolYear;
	private int schoolTerm;
	private List<Exam> exams = new ArrayList<Exam>();

	public int getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(int schoolYear) {
		this.schoolYear = schoolYear;
	}

	public int getSchoolTerm() {
		return schoolTerm;
	}

	public void setSchoolTerm(int schoolTerm) {
		this.schoolTerm = schoolTerm;
	}

	public List<Exam> getExams() {
		return exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public void addExam(Exam exam) {
		exams.add(exam);
	}

}
