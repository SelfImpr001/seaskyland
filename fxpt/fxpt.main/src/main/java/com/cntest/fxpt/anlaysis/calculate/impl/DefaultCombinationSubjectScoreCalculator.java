/*
 * @(#)com.cntest.fxpt.anlaysis.calculate.impl.DefaultCalculateCombinationSubjectScoreImpl.java	1.0 2015年5月22日:下午1:35:20
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate.impl;

import com.cntest.fxpt.anlaysis.bean.*;
import com.cntest.fxpt.anlaysis.calculate.CalculateCombinationSubjectScore;
import com.cntest.fxpt.anlaysis.event.handler.EndHandler;
import com.cntest.fxpt.anlaysis.filter.OrgFilter;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.ScoreObjMgr;
import com.cntest.fxpt.domain.*;
import com.cntest.util.ExceptionHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年5月22日 下午1:35:20
 * @version 1.0
 */
public class DefaultCombinationSubjectScoreCalculator implements
		CalculateCombinationSubjectScore {
	private static final Logger log = LoggerFactory.getLogger(EndHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.calculate.CalculateCombinationSubjectScore#calculate
	 * (com.cntest.fxpt.anlaysis.bean.CalculateTask)
	 */
	@Override
	public void calculate(CalculateTask event) {
		try {
			IExamContext context = event.getContext();
			StudentCjContainer scjc = context.getStudentCjContainer(new OrgFilter(
					event.getObj()));
			List<AnalysisTestpaper> atpList = context
					.getAnalysisTestpaperContainer()
					.getAnalysisTestpaperForCombinationSubject();
			for (StudentCj scj : scjc.toList()) {
				for (AnalysisTestpaper atp : atpList) {
					if (isCalculate(scj, atp)) {
						if (isCalcualteZF(atp)) {
							//计算组合科目总分
							calculateZFSubjectCj(atp, scj);
						} else {
							//计算单科总分
							calcluateSubjectCj(atp, scj,context);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DefaultCombinationSubjectScoreCalculator.class" + ExceptionHelper.trace2String(e));
		}
	}

	private boolean isCalcualteZF(AnalysisTestpaper atp) {
		return atp.getCombinationSubject().isSysCreate()
				&& (atp.getCombinationSubject()
						.getCombinationSubjectCalculateRules() == null || atp
						.getCombinationSubject()
						.getCombinationSubjectCalculateRules().isEmpty());
	}

	private boolean isCalculate(StudentCj scj, AnalysisTestpaper atp) {
		boolean calculate = true;
		try {
			ExamStudent student = scj.getStudent();
			if (atp.getCombinationSubject().isSysCreate()) {
				List<CombinationSubjectCalculateRule> rules = atp
						.getCombinationSubject()
						.getCombinationSubjectCalculateRules();
				for (CombinationSubjectCalculateRule rule : rules) {
					calculate = compareStudentPropertyValue(student, rule);
					if (!calculate) {
						break;
					}
				}
			} else {
				calculate = atp.getPaperType() == 0
						|| atp.getPaperType() == scj.getStudent().getWl();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DefaultCombinationSubjectScoreCalculator.class" + ExceptionHelper.trace2String(e));
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

	private void calculateZFSubjectCj(AnalysisTestpaper atp, StudentCj scj) {
		try {
			List<StudentSubjectScore> cjScores = scj.toSubjectCjList();
			int qkNum = 0;
			int zeroNum = 0;
			int hasCjSubjectNum = 0;
			double zfScore = 0;
			double kgScore = 0;
			double zgScore = 0;
			for (StudentSubjectScore cjScore : cjScores) {
				AnalysisTestpaper analysisTestpaper = cjScore
						.getAnalysisTestpaper();
				if (analysisTestpaper.getTestPaper() != null
						&& !analysisTestpaper.isSplitSubject()) {
					hasCjSubjectNum++;
					
					if (cjScore == null || cjScore.isQk()) {
						qkNum++;
						zeroNum++;
					} else if (cjScore.getScore().getValue() == 0) {
						zeroNum++;
					} else {
						zfScore += cjScore.getScore().getValue();
						kgScore += cjScore.getKgScore().getValue();
						zgScore += cjScore.getZgScore().getValue();
					}
				}
			}
			
			int subjectSize = atp.getCombinationSubject().getChildTestPaper()
					.size();
			qkNum += subjectSize - hasCjSubjectNum;
			zeroNum += subjectSize - hasCjSubjectNum;
			
			ScoreObjMgr scoreMgr = ScoreObjMgr.newInstance();
			StudentCustomizeSubjectScore s4 = new StudentCustomizeSubjectScore();
			s4.setAnalysisTestpaper(atp);
			s4.setScore(scoreMgr.getScore(zfScore));
			s4.setKgScore(scoreMgr.getScore(kgScore));
			s4.setZgScore(scoreMgr.getScore(zgScore));
			s4.setQkNum(qkNum);
			s4.setZeroNum(zeroNum);
			scj.addCj(s4);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DefaultCombinationSubjectScoreCalculator.class" + ExceptionHelper.trace2String(e));
		}
	}

	private void calcluateSubjectCj(AnalysisTestpaper atp, StudentCj scj,IExamContext context) {
		try {
			List<AnalysisTestpaper> atpList = atp.getCombinationSubject()
					.getAnalysisTestpaper();
			
			
			int qkNum = 0;
			int zeroNum = 0;
			double zfScore = 0;
			double kgScore = 0;
			double zgScore = 0;
			double rankScore = 0;
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

					if(scj.getStudent().getWl()==1){
//				#rank.L=lishu,lizong,yuwen,yingyu
						if(tmp.getSubject().getId()==100){
							rankScore += score.getScore().getValue() * 1000000;
						}else if(tmp.getSubject().getId()==99){
							rankScore += score.getScore().getValue() * 1000000000;
						}else if(tmp.getSubject().getId()==1){
							rankScore += score.getScore().getValue() * 1000;
						}else{
							rankScore += score.getScore().getValue();
						}
					}else{
//				#rank.W=yuwen,wenzong,wenshu,yingyu
						if(tmp.getSubject().getId()==102){
							rankScore += score.getScore().getValue() * 1000000;
						}else if(tmp.getSubject().getId()==1){
							rankScore += score.getScore().getValue() * 1000000000;
						}else if(tmp.getSubject().getId()==101){
							rankScore += score.getScore().getValue() * 1000;
						}else{
							rankScore += score.getScore().getValue();
						}
					}

				}
			}
			rankScore += zfScore * 1000000000000L;

			ScoreObjMgr scoreMgr = ScoreObjMgr.newInstance();
			StudentCustomizeSubjectScore s4 = new StudentCustomizeSubjectScore();

			s4.setAnalysisTestpaper(atp);
			s4.setScore(scoreMgr.getScore(zfScore));
			s4.setKgScore(scoreMgr.getScore(kgScore));
			s4.setZgScore(scoreMgr.getScore(zgScore));
			s4.setQkNum(qkNum);
			s4.setZeroNum(zeroNum);
			s4.setRankScore(scoreMgr.getScore(rankScore));
			scj.addCj(s4);

			List<UplineScore> uplineScores = context.getUplineScores().stream().filter(c -> c.getScoreType()==2).collect(Collectors.toList());
//			Map<Integer, Integer> levelScoreMap = new HashMap<Integer, Integer>();
			/**有效分去0 加入zeroNum的判断 不等于0的情况下剔除人数*/
			for (StudentSubjectScore studentSubjectScore : scj.toSubjectCjList()) {
				List<UplineScore> zfus = uplineScores.stream().filter(c -> c.getSubject().getId()==98)
						.filter(c -> c.getWlType()==studentSubjectScore.getStudentCj().getStudent().getWl()).collect(Collectors.toList());
				studentSubjectScore.setZfScore(scoreMgr.getScore(zfScore));
				List<UplineScore> dkus = uplineScores.stream().filter(c -> c.getWlType()==studentSubjectScore.getStudentCj().getStudent().getWl())
						.filter(c -> c.getSubject().getId()==studentSubjectScore.getAnalysisTestpaper().getSubject().getId()).collect(Collectors.toList());
				for (int i = 0; i < dkus.size(); i++) {
					if(studentSubjectScore.getScore().getValue() >= dkus.get(i).getDivideScore()&&zeroNum==0){
						 studentSubjectScore.getDd().put(dkus.get(i).getLevel(),1);
					}
					if(zfScore>= zfus.get(i).getDivideScore()&&zeroNum==0){
						studentSubjectScore.getZd().put(dkus.get(i).getLevel(),1);
					}
					if(studentSubjectScore.getScore().getValue() >= dkus.get(i).getDivideScore() &&zfScore>= zfus.get(i).getDivideScore()&&zeroNum==0){
						studentSubjectScore.getSd().put(dkus.get(i).getLevel(),1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DefaultCombinationSubjectScoreCalculator.class" + ExceptionHelper.trace2String(e));
		}
	}

}
