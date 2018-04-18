/*
 * @(#)com.cntest.fxpt.anlaysis.bean.WritCalcualteResultToFile.java	1.0 2014年12月9日:下午2:26:23
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.ScoreInfo;
import com.cntest.fxpt.anlaysis.bean.TargetResult;
import com.cntest.fxpt.anlaysis.bean.TargetResultContainer;
import com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDBService;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.anlaysis.uitl.OrgProxy;
import com.cntest.fxpt.anlaysis.uitl.Util;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月9日 下午2:26:23
 * @version 1.0
 */
public class WritCalcualteResultToFile implements
		ISaveCalcluateResultToDBService {
	private File zfFile;
	private File scoreInfoFile;
	private File itemFile;
	private File abilityFile;
	private File knowledgeFile;
	private File knowledgecontentFile;
	private File titletypeFile;

	private Lock zfLock = new ReentrantLock();
	private Lock scoreInfoLock = new ReentrantLock();
	private Lock itemLock = new ReentrantLock();
	private Lock abilityLock = new ReentrantLock();
	private Lock knowledgeLock = new ReentrantLock();
	private Lock knowledgecontentLock = new ReentrantLock();
	private Lock titletypeLock = new ReentrantLock();

	public WritCalcualteResultToFile(Exam exam) {
		zfFile = getFile("zf", exam);
		scoreInfoFile = getFile("si", exam);
		itemFile = getFile("item", exam);
		abilityFile = getFile("ab", exam);
		knowledgeFile = getFile("k", exam);
		knowledgecontentFile = getFile("kc", exam);
		titletypeFile = getFile("tt", exam);
	}

	private File getFile(String prefix, Exam exam) {
		File file = new File(Util.getDataDir() + prefix + "_" + exam.getId()
				+ ".txt");
		if (file.exists()) {
			file.delete();
		}
		return file;
	}

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
		Long objId = op.getId();
		Long testPaperId = at.getId();
		String orgType = op.getTableType();
		Container<Integer, CalculateResult> crc = event.getCalculateResult();
		saveTotalScore(examId, objId, subjectId, testPaperId, orgType, crc);
		saveScoreInfo(examId, objId, subjectId, testPaperId, orgType, crc);
		saveItem(examId, objId, subjectId, testPaperId, orgType, crc);
		saveAbility(examId, objId, subjectId, testPaperId, orgType, crc);
		saveKnowledge(examId, objId, subjectId, testPaperId, orgType, crc);
		saveKnowledgecontent(examId, objId, subjectId, testPaperId, orgType,
				crc);
		saveTitletype(examId, objId, subjectId, testPaperId, orgType, crc);
	}

	private String getTableName(String orgType, String tableType) {
		return "dw_agg_" + orgType + "_" + tableType;
	}

	private void saveFile(List<String> sqls, Lock lock, File file) {
		if (sqls.isEmpty()) {
			return;
		}
		lock.lock();
		try {
			FileUtils.writeLines(file, "utf-8", sqls, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	private void saveTotalScore(Long examId, Long objId, Long subjectId,
			Long testPaperId, String orgType,
			Container<Integer, CalculateResult> crc) {
		ArrayList<String> sqls = new ArrayList<>();
		int idx = 0;
		for (Integer wl : crc.toListForKey()) {
			List<TargetResult> trs = crc.get(wl).getTotalScoreTargetResults()
					.toList();
			String sql = createSQL(getTableName(orgType, "totalscore"), examId,
					objId, subjectId, testPaperId, trs, wl);
			addSQLTo(sqls, idx++, sql);
		}
		saveFile(sqls, zfLock, zfFile);
	}

	private void saveScoreInfo(Long examId, Long objId, Long subjectId,
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
				addSQLTo(sqls, idx++, sql);

			}
		}
		saveFile(sqls, scoreInfoLock, scoreInfoFile);
	}

	private List<TargetResult> scoreInfoTargetResult(ScoreInfo si) {
		ArrayList<TargetResult> targetResults = new ArrayList<>();
		targetResults.add(new TargetResult("score", si.getScore().getValue()));
		targetResults.add(new TargetResult("rank", si.getRank()));
		targetResults.add(new TargetResult("num", si.getNum()));
		return targetResults;
	}

	private void saveItem(Long examId, Long objId, Long subjectId,
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
				addSQLTo(sqls, idx++, sql);
			}
		}
		saveFile(sqls, itemLock, itemFile);
	}

	private void saveAbility(Long examId, Long objId, Long subjectId,
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
				addSQLTo(sqls, idx++, sql);
			}
		}
		saveFile(sqls, abilityLock, abilityFile);
	}

	private void saveKnowledge(Long examId, Long objId, Long subjectId,
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
				addSQLTo(sqls, idx++, sql);
			}
		}
		saveFile(sqls, knowledgeLock, knowledgeFile);
	}

	private void saveKnowledgecontent(Long examId, Long objId, Long subjectId,
			Long testPaperId, String orgType,
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
				addSQLTo(sqls, idx++, sql);
			}
		}
		saveFile(sqls, knowledgecontentLock, knowledgecontentFile);
	}

	private void saveTitletype(Long examId, Long objId, Long subjectId,
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
				addSQLTo(sqls, idx++, sql);
			}
		}
		saveFile(sqls, titletypeLock, titletypeFile);
	}

	private void addSQLTo(List<String> sqls, int num, String sql) {
		// if (num != 1) {
		// sqls.add("\n");
		// }
		sqls.add(sql);
	}

	private String createSQL(String tableName, Long examId, Long objId,
			Long subjectId, Long testPaperId, List<TargetResult> targetResults,
			int wl) {
		StringBuffer sql1 = new StringBuffer("insert into " + tableName
				+ "(examId,objId,subjectId,testpaperId,wl");
		StringBuffer sql2 = new StringBuffer("VALUES(" + examId + "," + objId
				+ "," + subjectId + "," + testPaperId + "," + wl);

		for (TargetResult tr : targetResults) {
			sql1.append("," + tr.getName());
			if (tr.getValue() instanceof String) {
				sql2.append(",'" + tr.getValue() + "'");
			} else {
				sql2.append("," + tr.getValue());
			}
		}
		sql1.append(")  ");
		sql2.append(");");

		return sql1.toString() + sql2.toString();
	}

	private int getSkrs(CalculateResult cr) {
		int skrs = 0;
		TargetResult skrsTr = cr.getTotalScoreTargetResults().get("skrs");
		if (skrsTr != null) {
			skrs = Integer.parseInt(skrsTr.getValue().toString());
		}
		return skrs;
	}

	public List<File> getFiles() {
		ArrayList<File> files = new ArrayList<>();
		files.add(zfFile);
		files.add(scoreInfoFile);
		files.add(itemFile);
		files.add(abilityFile);
		files.add(knowledgeFile);
		files.add(knowledgecontentFile);
		files.add(titletypeFile);
		return files;
	}

	@Override
	public void clear() throws Exception {
		// TODO Auto-generated method stub

	}

}
