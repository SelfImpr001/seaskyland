/*
 * @(#)com.cntest.fxpt.bean.StudentCj.java	1.0 2014年11月24日:下午1:20:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.cntest.fxpt.domain.ExamStudent;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:20:49
 * @version 1.0
 */
public class StudentCj implements PrimaryKey<Long> {
	private ExamStudent student;
	private ConcurrentHashMap<Long, StudentSubjectScore> cjMap = new ConcurrentHashMap<>();

	public StudentCj(ExamStudent student) {
		this.student = student;
	}

	public void addCj(StudentSubjectScore cj) {
		cj.setStudentCj(this);
		cjMap.put(cj.getPk(), cj);
	}

	@Override
	public Long getPk() {
		return student.getId();
	}

	public StudentSubjectScore getStudentSubjectScore(Long testPaperId) {
		return cjMap.get(testPaperId);
	}

	public ExamStudent getStudent() {
		return student;
	}

	/******** test **************/
	public List<StudentSubjectScore> toSubjectCjList() {
		return new ArrayList<>(cjMap.values());
	}

	@Override
	public String toString() {
		return student.getId() + "-->" + student.getName() + "-->"
				+ student.getWl();
	}
}
