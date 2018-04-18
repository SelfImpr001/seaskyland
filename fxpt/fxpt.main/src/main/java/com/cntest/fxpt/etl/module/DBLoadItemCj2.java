/*
 * @(#)com.cntest.fxpt.etl.module.DBLoad.java	1.0 2014年5月12日:上午10:36:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.RowSet;

/**
 * <Pre>
 * 辅助处理加载数据到数据库
 * </Pre>
 * 
 * @author 刘海林 2014年5月12日 上午10:36:13
 * @version 1.0
 */
public class DBLoadItemCj2 extends BaseStep {
	private static final Logger log = LoggerFactory
			.getLogger(DBLoadItemCj2.class);
	private Connection conn;
	private DataSource dataScource;
	private PreparedStatement sqlStatement;
	private List<Item> items = new ArrayList<Item>();
	private String[] fieldsName = { "examId", "testPaperId",
			"analysisTestpaperId", "subjectId", "studentId", "classId",
			"schoolId", "countyId", "cityId", "provinceId", "itemId", "wl",
			"isQk", "totalScore", "score", "selOption" };
	private Map<Long, AnalysisTestpaper> analysisTestpaperMap;

	/**
	 * @param name
	 * @param context
	 */
	public DBLoadItemCj2(String name, IEtlContext context) {
		super(name, context);
	}

	public DBLoadItemCj2 setDataSource(DataSource dataScource) {
		this.dataScource = dataScource;
		return this;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void setAnalysisTestpapers(List<AnalysisTestpaper> analysisTestpapers) {

		analysisTestpaperMap = new HashMap<Long, AnalysisTestpaper>();
		for (AnalysisTestpaper tmp : analysisTestpapers) {
			analysisTestpaperMap.put(tmp.getId(), tmp);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#begin()
	 */
	@Override
	public void begin() throws Exception {
		log.debug("初始化步骤" + getName());
		StringBuffer fields = new StringBuffer();
		StringBuffer values = new StringBuffer();

		for (String name : fieldsName) {
			fields.append(name + ",");
			values.append("?,");
		}

		fields.deleteCharAt(fields.length() - 1);
		values.deleteCharAt(values.length() - 1);

		StringBuffer sql = new StringBuffer();
		sql.append("insert into kn_examitemcj_tmp(");
		sql.append(fields.toString());
		sql.append(")");
		sql.append(" values(");
		sql.append(values.toString());
		sql.append(")");

		try {
			conn = dataScource.getConnection();
			conn.setAutoCommit(false);

			sqlStatement = conn.prepareStatement(sql.toString());
		} catch (Exception e) {
			end();
			throw e;
		}

		log.debug("sql:" + sql.toString());
		log.debug("初始化成功");

	}

	@Override
	public void excuteStep() throws Exception {
		log.debug("执行步骤" + getName());

		long b = System.currentTimeMillis();
		if (!items.isEmpty()) {
			try {
				int itemsSize = items.size();
				RowSet row = null;
				int rowNum = 1;
				while ((row = getRow()) != null) {
					rowNum = dealWithItem(row, rowNum);
					this.putRow(row);
				}
				sqlStatement.executeBatch();
				sqlStatement.clearBatch();
				conn.commit();
			} catch (Exception e) {
				end();
				throw new Exception("执行步骤" + getName() + "提交数据到数据库的时候出错", e);
			}
			super.excuteStep();

		}
		long e = System.currentTimeMillis();
		log.debug("执行步骤成功;" + ((e - b) * 1.0 / 1000) + "秒.");
	}

	private int dealWithItem(RowSet row, int rowNum) throws Exception {

		Map<Long, Map<String, Double>> splitTestPaperScoreMap = null;
		if (analysisTestpaperMap.size() > 1) {
			splitTestPaperScoreMap = getSplitTestPaperScore(row);
		}

		for (Item item : items) {
			int idx = 1;
			for (String name : fieldsName) {
				if (name.equals("examId")) {
					sqlStatement.setLong(idx++, item.getExam().getId());
				} else if (name.equals("testPaperId")) {
					sqlStatement.setLong(idx++, item.getTestPaper().getId());
				} else if (name.equals("analysisTestpaperId")) {
					sqlStatement.setLong(idx++, item.getAnalysisTestpaper()
							.getId());
				} else if (name.equals("subjectId")) {
					sqlStatement.setLong(idx++, item.getSubject().getId());
				} else if (name.equals("itemId")) {
					sqlStatement.setLong(idx++, item.getId());
				} else if (name.equals("isQk")) {
					String value = "0";
					try {
						value = row.getData(name);
					} catch (Exception e) {

					}
					sqlStatement.setInt(idx++, Integer.parseInt(value));
				} else if (name.equals("totalScore")) {
					String zf = row.getData("totalScore");
					if (analysisTestpaperMap.size() > 1) {
						Map<String, Double> atpScore = splitTestPaperScoreMap
								.get(item.getAnalysisTestpaper().getId());
						zf = (atpScore.get("zgScore") + atpScore.get("kgScore"))
								+ "";
					}
					sqlStatement.setDouble(idx++, Double.parseDouble(zf));
				} else if (name.equals("score")) {
					String value = row.getData("score" + item.getSortNum());
					sqlStatement.setDouble(idx++, Double.parseDouble(value));
				} else if (name.equals("selOption")) {
					String value = "";
					if (item.getOptionType() != 0) {
						value = row.getData("sel" + item.getSortNum());
					}
					sqlStatement.setString(idx++, value);
				} else {
					Object tmpValueObject = row.getData(name);
					if (tmpValueObject == null) {
						sqlStatement.setObject(idx++, null);
					} else {
						sqlStatement.setLong(idx++,
								Long.parseLong(row.getData(name)));
					}

				}
			}
			sqlStatement.addBatch();

			if (rowNum++ % 10000 == 0) {
				try {
					sqlStatement.executeBatch();
					sqlStatement.clearBatch();
					conn.commit();
				} catch (Exception e) {
					end();
					throw new Exception("执行步骤" + getName() + "提交数据到数据库的时候出错", e);
				}
			}
		}
		return rowNum;
	}

	private Map<Long, Map<String, Double>> getSplitTestPaperScore(RowSet row) {
		HashMap<Long, Map<String, Double>> analysisTestpapersScore = new HashMap<Long, Map<String, Double>>();
		for (Item item : items) {
			Long analysisTestpaperId = item.getAnalysisTestpaper().getId();
			Map<String, Double> analysisTestpaperScore = analysisTestpapersScore
					.get(analysisTestpaperId);
			if (analysisTestpaperScore == null) {
				analysisTestpaperScore = new HashMap<String, Double>();
				analysisTestpaperScore.put("kgScore", 0d);
				analysisTestpaperScore.put("zgScore", 0d);
				analysisTestpapersScore.put(analysisTestpaperId,
						analysisTestpaperScore);
			}

			String value = row.getData("score" + item.getSortNum());
			Double score = Double.parseDouble(value);
			//综合科目拆分计算时过滤 -888 无效分数
			if(score==-888){
				score=0D;
			}
			if (item.getOptionType() == 0) {
				analysisTestpaperScore.put("zgScore",
						analysisTestpaperScore.get("zgScore") + score);
			} else {
				analysisTestpaperScore.put("kgScore",
						analysisTestpaperScore.get("kgScore") + score);
			}
		}
		return analysisTestpapersScore;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#end()
	 */
	@Override
	public void end() throws Exception {
		if (sqlStatement != null) {
			sqlStatement.close();
			sqlStatement = null;
		}
		if (conn != null) {
			conn.commit();
			conn.setAutoCommit(true);
			conn.close();
			conn = null;
		}

	}

}
