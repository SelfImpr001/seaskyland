/*
 * @(#)com.cntest.fxpt.service.impl.SaveCalcluateResultServiceImpl.java	1.0 2014年11月28日:下午3:09:16
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.ScoreInfo;
import com.cntest.fxpt.anlaysis.bean.TargetResult;
import com.cntest.fxpt.anlaysis.bean.TargetResultContainer;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.anlaysis.uitl.OrgProxy;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.repository.ICalcluateResultDao;
import com.cntest.fxpt.service.ICalcluateResultService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 下午3:09:16
 * @version 1.0
 */
@Service("ICalcluateResultService")
public class CalcluateResultServiceImpl implements ICalcluateResultService {

	@Autowired(required = false)
	@Qualifier("ICalcluateResultDao")
	private ICalcluateResultDao saveDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ISaveCalcluateResultService#save(com.cntest.fxpt
	 * .anlaysis.bean.CalculateTask)
	 */
	@Override
	public void save(CalculateTask event) {

		AnalysisTestpaper at = event.getAnalysisTestpaper();
		Subject subject = at.getSubject();
		Long subjectId = null;
		if (subject != null) {
			subjectId = subject.getId();
		}

		OrgProxy op = new OrgProxy(event.getObj());
		Exam exam = event.getContext().getExam();
		Long examId = exam.getId();
		Long orgId = op.getId();
		Long testPaperId = at.getId();
		String orgType = op.getTableType();

		Container<Integer, CalculateResult> crc = event.getCalculateResult();
		for (Integer wl : crc.toListForKey()) {
			CalculateResult cr = crc.get(wl);
			saveTotalScore(examId, orgId, testPaperId, subjectId, orgType, cr,
					wl);
			saveScoreInfo(examId, orgId, testPaperId, subjectId, orgType, cr,
					wl);
			saveItem(examId, orgId, testPaperId, subjectId, orgType, cr, wl);
			saveAbility(examId, orgId, testPaperId, subjectId, orgType, cr, wl);
			saveKnowledge(examId, orgId, testPaperId, subjectId, orgType, cr,
					wl);
			saveKnowledgeContent(examId, orgId, testPaperId, subjectId,
					orgType, cr, wl);
			saveTitleType(examId, orgId, testPaperId, subjectId, orgType, cr,
					wl);
		}
	}

	private void saveTotalScore(Long examId, Long orgId, Long testPaperId,
			Long subjectId, String orgType, CalculateResult cr, int wl) {
		String tableName = "dw_agg_" + orgType + "_totalscore";
		// saveDao.deleteResult(examId, orgId, testPaperId, tableName, wl);
		saveDao.saveTotalScoreResult(examId, orgId, subjectId, testPaperId, cr
				.getTotalScoreTargetResults().toList(), tableName, wl);
	}

	private void saveScoreInfo(Long examId, Long orgId, Long testPaperId,
			Long subjectId, String orgType, CalculateResult cr, int wl) {

		int skrs = 0;
		TargetResult skrsTr = cr.getTotalScoreTargetResults().get("skrs");
		if (skrsTr != null) {
			skrs = Integer.parseInt(skrsTr.getValue().toString());
		}
		List<ScoreInfo> sis = cr.getTotalScoreScoreInfo();

		String tableName = "dw_agg_" + orgType + "_scoreinfo";
		// saveDao.deleteResult(examId, orgId, testPaperId, tableName, wl);
		for (ScoreInfo si : sis) {
			saveDao.saveScoreInfo(examId, orgId, subjectId, testPaperId, skrs,
					si, tableName, wl);
		}
	}

	private void saveItem(Long examId, Long orgId, Long testPaperId,
			Long subjectId, String orgType, CalculateResult cr, int wl) {
		String tableName = "dw_agg_" + orgType + "_item";
		// saveDao.deleteResult(examId, orgId, testPaperId, tableName, wl);
		Container<Long, TargetResultContainer> itemResults = cr
				.getItemTargetResult();
		for (Long itemId : itemResults.toListForKey()) {
			TargetResultContainer trc = itemResults.get(itemId);
			saveDao.saveItem(examId, orgId, subjectId, testPaperId, itemId,
					trc.toList(), tableName, wl);
		}
	}

	private void saveAbility(Long examId, Long orgId, Long testPaperId,
			Long subjectId, String orgType, CalculateResult cr, int wl) {

		String tableName = "dw_agg_" + orgType + "_ability";
		// saveDao.deleteResult(examId, orgId, testPaperId, tableName, wl);
		List<TargetResultContainer> itemGroupResults = cr
				.getAbilityTargetResult().toList();
		for (TargetResultContainer trc : itemGroupResults) {
			saveDao.saveItemGroup(examId, orgId, subjectId, testPaperId,
					trc.toList(), tableName, wl);
		}
	}

	private void saveKnowledge(Long examId, Long orgId, Long testPaperId,
			Long subjectId, String orgType, CalculateResult cr, int wl) {
		String tableName = "dw_agg_" + orgType + "_knowledge";
		// saveDao.deleteResult(examId, orgId, testPaperId, tableName, wl);
		List<TargetResultContainer> itemGroupResults = cr
				.getKnowledgeTargetResult().toList();
		for (TargetResultContainer trc : itemGroupResults) {
			saveDao.saveItemGroup(examId, orgId, subjectId, testPaperId,
					trc.toList(), tableName, wl);
		}
	}

	private void saveKnowledgeContent(Long examId, Long orgId,
			Long testPaperId, Long subjectId, String orgType,
			CalculateResult cr, int wl) {
		String tableName = "dw_agg_" + orgType + "_knowledgecontent";
		// saveDao.deleteResult(examId, orgId, testPaperId, tableName, wl);
		List<TargetResultContainer> itemGroupResults = cr
				.getKnowledgeContentTargetResult().toList();
		for (TargetResultContainer trc : itemGroupResults) {
			saveDao.saveItemGroup(examId, orgId, subjectId, testPaperId,
					trc.toList(), tableName, wl);
		}
	}

	private void saveTitleType(Long examId, Long orgId, Long testPaperId,
			Long subjectId, String orgType, CalculateResult cr, int wl) {
		String tableName = "dw_agg_" + orgType + "_titletype";
		// saveDao.deleteResult(examId, orgId, testPaperId, tableName, wl);
		List<TargetResultContainer> itemGroupResults = cr
				.getTitleTypeTargetResult().toList();
		for (TargetResultContainer trc : itemGroupResults) {
			saveDao.saveItemGroup(examId, orgId, subjectId, testPaperId,
					trc.toList(), tableName, wl);
		}
	}

}
