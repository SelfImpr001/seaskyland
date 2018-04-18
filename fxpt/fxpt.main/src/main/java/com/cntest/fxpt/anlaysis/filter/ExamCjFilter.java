/*
 * @(#)com.cntest.fxpt.anlaysis.filter.CjFilter.java	1.0 2014年12月1日:下午3:50:21
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.filter;

import com.cntest.fxpt.anlaysis.bean.StudentCj;
import com.cntest.fxpt.anlaysis.bean.StudentSubjectScore;
import com.cntest.fxpt.domain.AnalysisTestpaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月1日 下午3:50:21
 * @version 1.0
 */
public class ExamCjFilter extends AbstractStudentCjFilter {
	private AnalysisTestpaper atp;

	public ExamCjFilter(AnalysisTestpaper atp) {
		this.atp = atp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.filter.IStudentCjFilter#filter(com.cntest.fxpt
	 * .anlaysis.bean.StudentCj)
	 */
	@Override
	public boolean filter(StudentCj studentCj) {
		StudentSubjectScore score = studentCj.getStudentSubjectScore(atp
				.getId());
		if (atp.getCombinationSubject() != null) {
			return score.getScore().getValue() > -1;

		} else {
			return !score.isQk();
		}
	}

	@Override
	public String toString() {
		String text = "[ExamCjFilter:" + atp.toString() + "]";
		if (this.filter != null) {
			text += "->" + this.filter.toString();
		}
		return text;
	}

}
