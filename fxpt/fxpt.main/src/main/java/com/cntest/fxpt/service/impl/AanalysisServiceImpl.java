/*
 * @(#)com.cntest.fxpt.service.impl.AanalysisServiceImpl.java	1.0 2014年12月4日:下午3:08:37
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cntest.exception.BusinessException;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.service.impl.ExamMgr;
import com.cntest.fxpt.anlaysis.service.impl.StartAnalysis;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.repository.AnalysisThemeDao;
import com.cntest.fxpt.repository.ICalcluateResultDao;
import com.cntest.fxpt.service.IAanalysisService;
import com.cntest.fxpt.service.ICalcualteAnalysisTestPaperService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.web.view.Progress;
import com.cntest.web.view.ProgressListener;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月4日 下午3:08:37
 * @version 1.0
 */
@Service("IAanalysisService")
public class AanalysisServiceImpl implements IAanalysisService,
		ProgressListener {

	@Autowired
	private ICalcualteAnalysisTestPaperService CATService;

	@Autowired
	private IExamService examService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ICalcluateResultDao crDao;

	@Autowired
	private AnalysisThemeDao analysDao;
	
	@Override
	public void clarenAnalysisResult(Long examId) {
		crDao.clearAnalysisResult(examId);
		// CATService.deleteHasCombinationSubjectAnalysisTestPaper(examId);

	}

	@Override
	public void prepareAnalysis(Long examId) {
		CATService.addAndCalcualteAnalysisTestPaper(examId);//增加和计算分析试卷
		examService.updateStatus(examId, 2);//状态置为正在分析
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IAanalysisService#startAnalysis(java.lang.Long)
	 */
	@Override
	public void startAnalysis(Long examId) {
		try {
		Exam exam = examService.findById(examId);
		StartAnalysis.start(exam);
		//分析成功之后,设置科目 语言 省市 区 校的SQL处理
		if("xinjiang".equals(SystemConfig.newInstance().getValue(
				"area.org.code"))){
			setSubjectAndLanguagetype(examId);
		}
		//给用户加指定考试的权限
			userService.givePowerToUserByExam(exam);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setSubjectAndLanguagetype(long examId) {
		analysDao.setSubjectAndLanguagetype(examId);
	}
	@Override
	public Progress on(Map<String, String> params) {
		Long examId = Long.parseLong(params.get("examId"));
		IExamContext c = ExamMgr.getInstance().get(examId);
		Progress p = null;
		if (c != null) {
			int totalNum = c.getTaskTotalNum();
			int cur = c.getCompleteTaskNum();

			if (totalNum == cur && !c.isAllComplate()) {
				cur = cur - 1;
			}

			if (totalNum == cur && c.isAllComplate()) {
				ExamMgr.getInstance().remove(examId);
			}

			totalNum = totalNum <= 0 ? 100 : totalNum;
			cur = cur <= 0 ? 1 : cur;
			p = new Progress(totalNum, cur);
		} else {
			p = new Progress(1, 1);
		}
		return p;
	}

}
