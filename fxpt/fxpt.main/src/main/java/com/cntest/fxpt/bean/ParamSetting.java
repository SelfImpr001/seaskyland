/*
 * @(#)com.cntest.fxpt.bean.ParamSetting.java	1.0 2014年10月28日:上午9:02:02
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

import java.util.List;

import com.cntest.fxpt.domain.CombinationSubject;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamPaprameter;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.domain.UplineScore;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月28日 上午9:02:02
 * @version 1.0
 */
public class ParamSetting {
	private List<CombinationSubject> customizeSubjects;
	private List<UplineScore> uplineScores;
	private List<ExamPaprameter> params;
	private List<TestPaper> testPapers;
	//有效总分
	private String zfSubject;



	public List<TestPaper> getTestPapers() {
		return testPapers;
	}

	public void setTestPapers(List<TestPaper> testPapers) {
		this.testPapers = testPapers;
	}

	public List<CombinationSubject> getCustomizeSubjects() {
		return customizeSubjects;
	}

	public List<UplineScore> getUplineScores() {
		return uplineScores;
	}

	public List<ExamPaprameter> getParams() {
		return params;
	}

	public void setCustomizeSubjects(List<CombinationSubject> customizeSubjects) {
		this.customizeSubjects = customizeSubjects;
	}

	public void setUplineScores(List<UplineScore> uplineScores) {
		this.uplineScores = uplineScores;
	}

	public void setParams(List<ExamPaprameter> params) {
		this.params = params;
	}

	public void setExam(Exam exam) {
		if (params != null) {
			for (ExamPaprameter p : params) {
				p.setExam(exam);
			}
		}

		if (customizeSubjects != null) {
			for (CombinationSubject cs : customizeSubjects) {
				cs.setExam(exam);
			}
		}

		if (uplineScores != null) {
			for (UplineScore us : uplineScores) {
				us.setExam(exam);
			}
		}

	}
	public String getZfSubject() {
		return zfSubject;
	}

	public void setZfSubject(String zfSubject) {
		this.zfSubject = zfSubject;
	}


}
