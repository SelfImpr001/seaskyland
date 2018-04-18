/*
 * @(#)com.cntest.fxpt.anlaysis.calculate.impl.DefaultCalculateCombinationSubjectScoreImpl.java	1.0 2015年5月22日:下午1:35:20
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate.impl;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.StudentCj;
import com.cntest.fxpt.anlaysis.bean.StudentCjContainer;
import com.cntest.fxpt.anlaysis.bean.StudentCustomizeSubjectScore;
import com.cntest.fxpt.anlaysis.bean.StudentSubjectScore;
import com.cntest.fxpt.anlaysis.calculate.CalculateCombinationSubjectScore;
import com.cntest.fxpt.anlaysis.filter.OrgFilter;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.ScoreObjMgr;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.CombinationSubjectCalculateRule;
import com.cntest.fxpt.domain.ExamStudent;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年5月22日 下午1:35:20
 * @version 1.0
 */
public class XinjiangCombinationSubjectScoreCalculator implements
		CalculateCombinationSubjectScore {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.calculate.CalculateCombinationSubjectScore#calculate
	 * (com.cntest.fxpt.anlaysis.bean.CalculateTask)
	 */
	@Override
	public void calculate(CalculateTask event) {
		IExamContext context = event.getContext();
		StudentCjContainer scjc = context.getStudentCjContainer(new OrgFilter(
				event.getObj()));
		List<AnalysisTestpaper> atpList = context
				.getAnalysisTestpaperContainer()
				.getAnalysisTestpaperForCombinationSubject();
		for (StudentCj scj : scjc.toList()) {
			for (AnalysisTestpaper atp : atpList) {
				if (isCalculate(scj, atp)) {
					calcluateSubjectCj(atp, scj);
				}
			}
		}
	}

	private boolean isCalculate(StudentCj scj, AnalysisTestpaper atp) {
		boolean calculate = true;

		ExamStudent student = scj.getStudent();
		List<CombinationSubjectCalculateRule> rules = atp
				.getCombinationSubject().getCombinationSubjectCalculateRules();
		for (CombinationSubjectCalculateRule rule : rules) {
			calculate = compareStudentPropertyValue(student, rule);
			if (!calculate) {
				break;
			}
		}

		return calculate;
	}

	private boolean compareStudentPropertyValue(ExamStudent student,
			CombinationSubjectCalculateRule rule) {
		boolean equal = false;
		try {
			String tmpValue = BeanUtils.getProperty(student,
					rule.getStudentAttributeName());
			equal = tmpValue.equals(rule.getStudentAttributeValue());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return equal;
	}

	private void calcluateSubjectCj(AnalysisTestpaper atp, StudentCj scj) {
		List<AnalysisTestpaper> atpList = atp.getCombinationSubject()
				.getAnalysisTestpaper();

		int qkNum = 0;
		int zeroNum = 0;
		double zfScore = 0;
		double kgScore = 0;
		double zgScore = 0;
		for (AnalysisTestpaper tmp : atpList) {
			StudentSubjectScore score = scj.getStudentSubjectScore(tmp.getId());
			if (score == null || score.isQk()) {
				qkNum++;
				zeroNum++;
			} else if (score.getScore().getValue() == 0) {
				zeroNum++;
			} else {
				zfScore += score.getScore().getValue();
				kgScore += score.getKgScore().getValue();
				zgScore += score.getZgScore().getValue();
			}
		}

		ScoreObjMgr scoreMgr = ScoreObjMgr.newInstance();
		StudentCustomizeSubjectScore s4 = new StudentCustomizeSubjectScore();
		s4.setAnalysisTestpaper(atp);
		s4.setScore(scoreMgr.getScore(zfScore));
		s4.setKgScore(scoreMgr.getScore(kgScore));
		s4.setZgScore(scoreMgr.getScore(zgScore));
		s4.setQkNum(qkNum);
		s4.setZeroNum(zeroNum);

		scj.addCj(s4);
	}

}
