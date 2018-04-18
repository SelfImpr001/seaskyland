/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.BatchSaveCalcluateResultToDBService.java	1.0 2014年12月12日:上午10:33:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.ScoreInfo;
import com.cntest.fxpt.anlaysis.bean.TargetResult;
import com.cntest.fxpt.anlaysis.bean.TargetResultContainer;
import com.cntest.fxpt.anlaysis.calculate.impl.ExamDataCalculate;
import com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDBService;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.anlaysis.uitl.OrgProxy;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月12日 上午10:33:28
 * @version 1.0
 */
public class BatchSaveCalcluateResultToDBService implements
		ISaveCalcluateResultToDBService {
	private ArrayList<String> sqls = new ArrayList<>();
	private Lock lock = new ReentrantLock();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDBService#save
	 * (com.cntest.fxpt.anlaysis.bean.CalculateTask)
	 */
	@Override
	public void save(CalculateTask event) throws Exception {
		AnalysisTestpaper at = event.getAnalysisTestpaper();
		Subject subject = at.getSubject();
		Long subjectId = null;
		if (subject != null) {
			subjectId = subject.getId();
		}

		OrgProxy op = new OrgProxy(event.getObj());
		Exam exam = event.getContext().getExam();
		Long examId = exam.getId();
		Long objId = op.getId();
		Long testPaperId = at.getId();
		String orgType = op.getTableType();
		if(orgType==null){
			return;
		}
		Container<Integer, CalculateResult> crc = event.getCalculateResult();

		ArrayList<String> tmpSqls1 = new ArrayList<>();
		tmpSqls1.addAll(saveTotalScore(examId, objId, subjectId, testPaperId,
				orgType, crc));
		tmpSqls1.addAll(saveScoreInfo(examId, objId, subjectId, testPaperId,
				orgType, crc));
		tmpSqls1.addAll(saveItem(examId, objId, subjectId, testPaperId,
				orgType, crc));
		tmpSqls1.addAll(saveAbility(examId, objId, subjectId, testPaperId,
				orgType, crc));
		tmpSqls1.addAll(saveKnowledge(examId, objId, subjectId, testPaperId,
				orgType, crc));
		tmpSqls1.addAll(saveKnowledgecontent(examId, objId, subjectId,
				testPaperId, orgType, crc));
		tmpSqls1.addAll(saveTitletype(examId, objId, subjectId, testPaperId,
				orgType, crc));

		if (op.getLevel() == exam.getLevelCode()) {
			ExamDataCalculate edc = new ExamDataCalculate();
			tmpSqls1.addAll(edc.getSQL(event));
		}

		ArrayList<String> tmpSqls = new ArrayList<>();
		lock.lock();
		try {
			sqls.addAll(tmpSqls1);
			if (sqls.size() > 10000) {
				tmpSqls.addAll(this.sqls);
				sqls.clear();
			}
		} finally {
			lock.unlock();
		}

		if (tmpSqls.size() > 0) {
			saveToDB(tmpSqls);
		}
	}

	private void saveToDB(List<String> tmpSqls) throws Exception {
		DataSource ds = SpringContext.getBean("ds");
		Connection con = ds.getConnection();
		con.setAutoCommit(false);
		PreparedStatement ps = con.prepareStatement("");
		try {
			for (String sql : tmpSqls) {
				ps.addBatch(sql);
			}
			ps.executeBatch();
			con.commit();
			ps.clearBatch();
		} finally {
			ps.close();
			con.close();
		}
	}

	@Override
	public void clear() throws Exception {
		ArrayList<String> tmpSqls = new ArrayList<>();
		lock.lock();
		try {
			tmpSqls.addAll(this.sqls);
			sqls.clear();
		} finally {
			lock.unlock();
		}

		if (tmpSqls.size() > 0) {
			saveToDB(tmpSqls);
		}
	}

	private List<String> saveItem(Long examId, Long objId, Long subjectId,
			Long testPaperId, String orgType,
			Container<Integer, CalculateResult> crc) {
		String tableName = getTableName(orgType, "item");
		ArrayList<String> sqls = new ArrayList<>();
		int idx = 0;
		for (Integer wl : crc.toListForKey()) {
			CalculateResult cr = crc.get(wl);
			Container<Long, TargetResultContainer> trcs = cr
					.getItemTargetResult();
			for (Long itemId : trcs.toListForKey()) {
				TargetResultContainer trc = trcs.get(itemId);
				List<TargetResult> targetResults = trc.toList();
				targetResults.add(new TargetResult("itemId", itemId));
				String sql = createSQL(tableName, examId, objId, subjectId,
						testPaperId, targetResults, wl);
				sqls.add(sql);
			}
		}
		return sqls;
	}

	private List<String> saveAbility(Long examId, Long objId, Long subjectId,
			Long testPaperId, String orgType,
			Container<Integer, CalculateResult> crc) {
		String tableName = getTableName(orgType, "ability");
		ArrayList<String> sqls = new ArrayList<>();
		int idx = 0;
		for (Integer wl : crc.toListForKey()) {
			CalculateResult cr = crc.get(wl);
			Container<String, TargetResultContainer> trcs = cr
					.getAbilityTargetResult();
			for (String name : trcs.toListForKey()) {
				TargetResultContainer trc = trcs.get(name);
				List<TargetResult> targetResults = trc.toList();
				String sql = createSQL(tableName, examId, objId, subjectId,
						testPaperId, targetResults, wl);
				sqls.add(sql);
			}
		}
		return sqls;
	}

	private List<String> saveKnowledge(Long examId, Long objId, Long subjectId,
			Long testPaperId, String orgType,
			Container<Integer, CalculateResult> crc) {
		String tableName = getTableName(orgType, "knowledge");
		ArrayList<String> sqls = new ArrayList<>();
		int idx = 0;
		for (Integer wl : crc.toListForKey()) {
			CalculateResult cr = crc.get(wl);
			Container<String, TargetResultContainer> trcs = cr
					.getKnowledgeTargetResult();
			for (String name : trcs.toListForKey()) {
				TargetResultContainer trc = trcs.get(name);
				List<TargetResult> targetResults = trc.toList();
				String sql = createSQL(tableName, examId, objId, subjectId,
						testPaperId, targetResults, wl);
				sqls.add(sql);
			}
		}
		return sqls;
	}

	private List<String> saveKnowledgecontent(Long examId, Long objId,
			Long subjectId, Long testPaperId, String orgType,
			Container<Integer, CalculateResult> crc) {
		String tableName = getTableName(orgType, "knowledgecontent");
		ArrayList<String> sqls = new ArrayList<>();
		int idx = 0;
		for (Integer wl : crc.toListForKey()) {
			CalculateResult cr = crc.get(wl);
			Container<String, TargetResultContainer> trcs = cr
					.getKnowledgeContentTargetResult();
			for (String name : trcs.toListForKey()) {
				TargetResultContainer trc = trcs.get(name);
				List<TargetResult> targetResults = trc.toList();
				String sql = createSQL(tableName, examId, objId, subjectId,
						testPaperId, targetResults, wl);
				sqls.add(sql);
			}
		}
		return sqls;
	}

	private List<String> saveTitletype(Long examId, Long objId, Long subjectId,
			Long testPaperId, String orgType,
			Container<Integer, CalculateResult> crc) {
		String tableName = getTableName(orgType, "titletype");
		ArrayList<String> sqls = new ArrayList<>();
		int idx = 0;
		for (Integer wl : crc.toListForKey()) {
			CalculateResult cr = crc.get(wl);
			Container<String, TargetResultContainer> trcs = cr
					.getKnowledgeContentTargetResult();
			for (String name : trcs.toListForKey()) {
				TargetResultContainer trc = trcs.get(name);
				List<TargetResult> targetResults = trc.toList();
				String sql = createSQL(tableName, examId, objId, subjectId,
						testPaperId, targetResults, wl);
				sqls.add(sql);
			}
		}
		return sqls;
	}

	private List<String> saveScoreInfo(Long examId, Long objId, Long subjectId,
			Long testPaperId, String orgType,
			Container<Integer, CalculateResult> crc) {
		String tableName = getTableName(orgType, "scoreinfo");
		ArrayList<String> sqls = new ArrayList<>();
		int idx = 0;
		for (Integer wl : crc.toListForKey()) {
			CalculateResult cr = crc.get(wl);
			List<ScoreInfo> sis = cr.getTotalScoreScoreInfo();
			int skrs = getSkrs(cr);
			for (ScoreInfo si : sis) {
				List<TargetResult> trs = scoreInfoTargetResult(si);
				trs.add(new TargetResult("skrs", skrs));
				String sql = createSQL(tableName, examId, objId, subjectId,
						testPaperId, trs, wl);
				sqls.add(sql);
			}
		}
		return sqls;
	}

	private List<TargetResult> scoreInfoTargetResult(ScoreInfo si) {
		ArrayList<TargetResult> targetResults = new ArrayList<>();
		targetResults.add(new TargetResult("score", si.getScore().getValue()));
		targetResults.add(new TargetResult("rank", si.getRank()));
		targetResults.add(new TargetResult("num", si.getNum()));
		return targetResults;
	}

	private List<String> saveTotalScore(Long examId, Long objId,
			Long subjectId, Long testPaperId, String orgType,
			Container<Integer, CalculateResult> crc) {
		ArrayList<String> sqls = new ArrayList<>();
		int idx = 0;
		for (Integer wl : crc.toListForKey()) {
			List<TargetResult> trs = crc.get(wl).getTotalScoreTargetResults()
					.toList();
			String sql = createSQL(getTableName(orgType, "totalscore"), examId,
					objId, subjectId, testPaperId, trs, wl);
			sqls.add(sql);
		}
		return sqls;
	}

	private String createSQL(String tableName, Long examId, Long objId,
			Long subjectId, Long testPaperId, List<TargetResult> targetResults,
			int wl) {
		StringBuffer sql1 = new StringBuffer("insert into ");
		sql1.append(tableName);
		sql1.append("(examId,objId,subjectId,testpaperId,wl");

		StringBuffer sql2 = new StringBuffer("VALUES(");
		sql2.append(examId);
		sql2.append(",");
		sql2.append(objId);
		sql2.append(",");
		sql2.append(subjectId);
		sql2.append(",");
		sql2.append(testPaperId);
		sql2.append(",");
		sql2.append(wl);

		for (TargetResult tr : targetResults) {
			sql1.append("," + tr.getName());
			if (tr.getValue() instanceof String) {
				sql2.append(",'" + tr.getValue() + "'");
			} else {
				sql2.append("," + tr.getValue());
			}
		}
		sql1.append(")  ");
		sql2.append(")");

		return sql1.append(sql2).toString();
	}

	private String getTableName(String orgType, String tableType) {
		return "dw_agg_" + orgType + "_" + tableType;
	}

	private int getSkrs(CalculateResult cr) {
		int skrs = 0;
		TargetResult skrsTr = cr.getTotalScoreTargetResults().get("skrs");
		if (skrsTr != null) {
			skrs = Integer.parseInt(skrsTr.getValue().toString());
		}
		return skrs;
	}
}
