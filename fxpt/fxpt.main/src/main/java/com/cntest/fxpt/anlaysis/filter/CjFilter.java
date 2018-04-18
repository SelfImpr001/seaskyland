/*
 * @(#)com.cntest.fxpt.anlaysis.filter.CjFilter.java	1.0 2014年12月1日:下午3:50:21
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.filter;

import com.cntest.fxpt.anlaysis.bean.StudentCj;
import com.cntest.fxpt.anlaysis.bean.StudentCustomizeSubjectScore;
import com.cntest.fxpt.anlaysis.bean.StudentSubjectScore;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.CombinationSubjectXTestPaper;
import com.cntest.fxpt.domain.StatisticSetting;
import com.cntest.fxpt.domain.TestPaper;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月1日 下午3:50:21
 * @version 1.0
 */
public class CjFilter extends AbstractStudentCjFilter {
	private AnalysisTestpaper atp;
	private StatisticSetting param;

	public CjFilter(AnalysisTestpaper atp, StatisticSetting param) {
		this.atp = atp;
		this.param = param;
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
		StudentSubjectScore score = studentCj.getStudentSubjectScore(atp.getId());
		if (score == null) {
			return false;
		} else if (atp.getCombinationSubject() != null) {
			return validateCustomizeSubject((StudentCustomizeSubjectScore) score,studentCj);
		} else {
			return validate(score);
		}
	}

	private boolean validate(StudentSubjectScore score) {
		boolean result = false;
		if (param.getSvalue() == 1) {
			// 统计缺考
			result = true;
		} else if (param.getSvalue() == 2) {
			// 不统计缺考
			result = score.isQk() ? false : true;
		} else if (param.getSvalue() == 3) {
			// 不统计缺考，且分数大于0
			result = !score.isQk() && score.getScore().getValue() > 0;
		}
		return result;
	}

	private boolean validateCustomizeSubject(StudentCustomizeSubjectScore scss,StudentCj studentCj) {
		boolean result = false;
		if (param.getSvalue() == 1) {
			// 包含缺考
			result = true;
		} else if (param.getSvalue() == 2) {
			// 所有科目均不缺考
			result = scss.getQkNum() == 0;
		} else if (param.getSvalue() == 3) {
			// 所有科目均不缺考，且分数大于0
			result = scss.getQkNum() == 0 && scss.getScore().getValue() > 0;
		} else if (param.getSvalue() == 4) {
			// 至少一科不缺考
			result = scss.getQkNum() != atp.getCombinationSubject()
					.getChildTestPaper().size();
		} else if (param.getSvalue() == 5) {
			// 至少一科分数大于0
			result = scss.getZeroNum() != atp.getCombinationSubject()
					.getChildTestPaper().size();
		} else if (param.getSvalue() == 6) {
			// 至少一科不缺考，且分数大于0
			result = scss.getQkNum() != atp.getCombinationSubject()
					.getChildTestPaper().size()
					&& scss.getScore().getValue() > 0;
		} else if (param.getSvalue() == 8) {
			// 所有科目分数大于0
			List<StudentSubjectScore> studentCjList = scss.getStudentCj().toSubjectCjList();
			boolean scoreFlag = true;
			for (StudentSubjectScore studentSubjectScore:studentCjList){
				if(!studentSubjectScore.getAnalysisTestpaper().isSplitSubject()){
					if (studentSubjectScore.getScore().getValue() <= 0) {
						scoreFlag = false;
						break;
					}
				}
			}
			result = scss.getQkNum() == 0 && scoreFlag;
		} else if (param.getSvalue() == 7) {
			if(scss.getAnalysisTestpaper().getName().indexOf("主科") != -1){
				int masterSubjectNum = 0;
				//主科 二科以上为0 剔除
				List<CombinationSubjectXTestPaper> csTestpapers = atp.getCombinationSubject().getChildTestPaper();
				for (CombinationSubjectXTestPaper combinationSubjectXTestPaper : csTestpapers) {
					TestPaper tp = combinationSubjectXTestPaper.getTestPaper();
					if(tp.getAnalysisTestpapers().size()>0){
						StudentSubjectScore score = null;
						if(tp.getAnalysisTestpapers().size() > 2){
							score = studentCj.getStudentSubjectScore(tp.getAnalysisTestpapers().get(1).getId());
						}else{
							score = studentCj.getStudentSubjectScore(tp.getAnalysisTestpapers().get(0).getId());
						}
						if(score==null){
							masterSubjectNum++;
						}else if(score.getScore().getValue()==0){
							masterSubjectNum++;
						}else if(score.isQk()){
							masterSubjectNum++;
						}
					}
				}
				if(masterSubjectNum >= 2 ){
					return false;
				}else{
					return true;
				}
			}else{
				int normalSubjectNum = 0;
				//全科 三科以上为0 剔除
				//所有的子卷子
				List<CombinationSubjectXTestPaper> csTestpapers = atp.getCombinationSubject().getChildTestPaper();
				for (CombinationSubjectXTestPaper combinationSubjectXTestPaper : csTestpapers) {
					TestPaper tp = combinationSubjectXTestPaper.getTestPaper();
					if(tp.getAnalysisTestpapers().size()>0){
						StudentSubjectScore score = null;
						if(atp.getName().indexOf("毕业")!=0){
							if(tp.getAnalysisTestpapers().size() > 2){
								score = studentCj.getStudentSubjectScore(tp.getAnalysisTestpapers().get(1).getId());
							}else{
								score = studentCj.getStudentSubjectScore(tp.getAnalysisTestpapers().get(0).getId());
							}
						}else{
							score = studentCj.getStudentSubjectScore(tp.getAnalysisTestpapers().get(1).getId());
						}
						if(score==null){
							normalSubjectNum++;
						}else if(score.getScore().getValue()==0){
							normalSubjectNum++;
						}else if(score.isQk()){
							normalSubjectNum++;
						}
					}
				}
				if(normalSubjectNum >= 4 ){
					return false;
				}else{
					return true;
				}
				
			}
			
		}
		return result;
	}

	@Override
	public String toString() {
		String text = "[CjFilter:" + atp.toString() + "]";
		if (this.filter != null) {
			text += "->" + this.filter.toString();
		}
		return text;
	}
}
