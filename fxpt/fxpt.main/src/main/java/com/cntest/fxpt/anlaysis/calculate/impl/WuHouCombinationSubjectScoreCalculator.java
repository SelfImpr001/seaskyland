/*
 * @(#)com.cntest.fxpt.anlaysis.calculate.impl.DefaultCalculateCombinationSubjectScoreImpl.java	1.0 2015年5月22日:下午1:35:20
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.repository.ITestPaperDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年5月22日 下午1:35:20
 * @version 1.0
 */
public class WuHouCombinationSubjectScoreCalculator implements
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
		//学生信息，无成绩
		StudentCjContainer scjc = context.getStudentCjContainer(new OrgFilter(
				event.getObj()));
		//组合科目信息 
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
			e.printStackTrace();
		}
		return equal;
	}

	private void calcluateSubjectCj(AnalysisTestpaper atp, StudentCj scj) {
		List<AnalysisTestpaper> atpList = atp.getCombinationSubject().getAnalysisTestpaperWuHou();

		int qkNum = 0;
		int masterQkNum = 0;
		int zeroNum = 0;
		double zfScore = 0;
		double kgScore = 0;
		double zgScore = 0;
		List<Long> masterQkList = new ArrayList<Long>();
		for (AnalysisTestpaper tmp : atpList) {
			//取到组合科目子科目的分数
			StudentSubjectScore score = scj.getStudentSubjectScore(tmp.getId());
			if(score==null){
				continue;
			}

			Double myscore = null;
			try {
//				myscore = atp.getCombinationSubject().getAnalysisTestpaperWuHou(tmp.getId(), score);
				myscore = score.getScore().getValue();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (score == null || score.isQk()) {
				if(!masterQkList.contains(tmp.getTestPaper().getId())) {
					masterQkList.add(tmp.getTestPaper().getId());
					masterQkNum++;
				}
				qkNum++;
				zeroNum++;
			} else if (score.getScore().getValue() == 0) {
				zeroNum++;
			} else {
				zfScore += score.getScore().getValue();
				kgScore += score.getKgScore().getValue() * myscore/score.getScore().getValue();
				zgScore += score.getZgScore().getValue() * myscore/score.getScore().getValue();
			}
		}
		
		/**
		 * 主科人数，全科人数，全卷人数，科目数量
		 */
		int masterNum=0,passSum=0,fullTestPaperNum=0;
		double  singleSubjectScore = 0.0, totalSubjectScore = 0.0;
		
		
		for (StudentSubjectScore studentSubjectScore : scj.toSubjectCjList()) {
			AnalysisTestpaper analysisTestpaper = studentSubjectScore.getAnalysisTestpaper();
			Subject subject = analysisTestpaper.getSubject();
			Long subjectId = subject.getId();
			String subjectName = analysisTestpaper.getName();
			//四科A卷信息(语文A，数学A，英语A，物理A)
			if (subjectName.indexOf("A") != -1 && (subjectId == 1 || subjectId == 2 || subjectId == 82 || subjectId == 93)) {
				singleSubjectScore += studentSubjectScore.getScore().getValue();
				totalSubjectScore += analysisTestpaper.getFullScore();
				if (studentSubjectScore.getScore().getValue() >= analysisTestpaper.getFullScore() * 0.6) {
					passSum++;
					if (subject.getId() == 1 || subject.getId() == 82) {
						masterNum++;
					}
				}
			//
			} 
//			else if (subjectName.indexOf("A") != -1 && (subjectId != 1 && subjectId != 2 && subjectId != 82 && subjectId != 93)) {
//				singleSubjectScore += studentSubjectScore.getScore().getValue();
//				totalSubjectScore += analysisTestpaper.getFullScore();
//			//
//			} else if (subjectName.indexOf("B") != -1 && (subjectId != 1 && subjectId != 2 && subjectId != 82 && subjectId != 93)) {
//				singleSubjectScore += studentSubjectScore.getScore().getValue();
//				totalSubjectScore += analysisTestpaper.getFullScore();
//			//化学，历史，地理，生物，思品
//			}
			else if (subjectName.indexOf("A") == -1 && subjectName.indexOf("B") == -1 && subjectName.indexOf("SX") == -1 
					&& (subjectId != 1 && subjectId != 2 && subjectId != 82 && subjectId != 93 && subjectId !=998 && subjectId !=997 && subjectId !=999 && subjectId !=98 )) {
				singleSubjectScore += studentSubjectScore.getScore().getValue();
				totalSubjectScore += analysisTestpaper.getFullScore();
				if (studentSubjectScore.getScore().getValue() >= analysisTestpaper.getFullScore() * 0.6) {
					fullTestPaperNum++;
				}
			}
		}
		
		ScoreObjMgr scoreMgr = ScoreObjMgr.newInstance();
		StudentCustomizeSubjectScore s4 = new StudentCustomizeSubjectScore();
		
		//一次合格(四科A卷成绩和其余科目全卷成绩满足 1、九科有六颗合格  2、语文，数学至少一科合格 3、九科平均成绩大于等于60 4、体育合格)
		if(passSum+fullTestPaperNum >= 6 && masterNum >= 1 && singleSubjectScore/9 >= 60) {
			s4.setOncePass(true);
		}
		
		//全科合格
		if(passSum+fullTestPaperNum >= 9) {
			s4.setAllPass(true);
		}

		s4.setAnalysisTestpaper(atp);
		s4.setScore(scoreMgr.getScore(zfScore));
		s4.setKgScore(scoreMgr.getScore(kgScore));
		s4.setZgScore(scoreMgr.getScore(zgScore));
		s4.setQkNum(qkNum);
		s4.setZeroNum(zeroNum);
		s4.setMasterQkNum(masterQkNum);

		scj.addCj(s4);
	}

}
