/*
 * @(#)com.cntest.fxpt.anlaysis.bean.StudentCjContainer.java	1.0 2014年11月24日:下午1:38:22
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.ExamStudent;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:38:22
 * @version 1.0
 */
public class StudentCjContainer extends Container<Long, StudentCj> {

	public void addStudentCj(StudentCj studentCj) {
		put(studentCj.getPk(), studentCj);
	}

	public void addStudentCj(ExamStudent student) {
		StudentCj studentCj = new StudentCj(student);
		addStudentCj(studentCj);
	}

	public void add(StudentCjContainer sCjC) {
		c.putAll(sCjC.c);
	}

	public StudentCj getStudentCj(Long pk) {
		return get(pk);
	}

	public boolean hasClassStudentCj(Long classId) {
		boolean result = false;
		for (StudentCj scj : c.values()) {
			if (scj.getStudent().getClazz().getId().compareTo(classId) == 0) {
				result = true;
			}
		}
		return result;
	}

	public StudentCjContainer getStudentCjContainer(
			AbstractStudentCjFilter filter) {
		StudentCjContainer result = new StudentCjContainer();
		for (StudentCj scj : c.values()) {
			if (filter.execFilter(scj)) {
				result.addStudentCj(scj);
			}
		}
		return result;
	}

	public SubjectScoreContainer getSubjectScoreContainer(
			AnalysisTestpaper apt, AbstractStudentCjFilter filter) {
		SubjectScoreContainer result = new SubjectScoreContainer();
		result.setAnalysisTestpaper(apt);
		for (StudentCj scj : c.values()) {
			if (filter == null || filter.execFilter(scj)) {
				StudentSubjectScore sss = scj.getStudentSubjectScore(apt
						.getId());
				if (sss != null) {
					result.add(sss);
				}
			}
		}
		return result;
	}
}
