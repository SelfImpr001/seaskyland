/*
 * @(#)com.cntest.fxpt.etl.module.DBLoad.java	1.0 2014年5月12日:上午10:36:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.util.ExceptionHelper;

/**
 * <Pre>
 * 辅助处理加载数据到数据库
 * </Pre>
 * 
 * @author 刘海林 2014年5月12日 上午10:36:13
 * @version 1.0
 */
public class DBLoadCj extends BaseStep {
	private static final Logger log = LoggerFactory.getLogger(DBLoadCj.class);
	private Connection conn;
	private DataSource dataScource;
	private PreparedStatement sqlStatement;
	private String[] fieldsName = { "examId", "testPaperId",
			"analysisTestpaperId", "subjectId", "studentId", "classId",
			"schoolId", "countyId", "cityId", "provinceId", "wl", "isQk",
			"totalScore", "zgScore", "kgScore" };
	private TestPaper testPaper;
	private List<Item> items;
	private Map<Long, AnalysisTestpaper> analysisTestpaperMap;
	private AnalysisTestpaper nalysisTestpaper;

	/**
	 * @param name
	 * @param context
	 */
	public DBLoadCj(String name, IEtlContext context) {
		super(name, context);
	}

	public DBLoadCj setDataSource(DataSource dataScource) {
		this.dataScource = dataScource;
		return this;
	}

	public void setTestPaper(TestPaper testPaper) {
		this.testPaper = testPaper;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void setAnalysisTestpapers(List<AnalysisTestpaper> analysisTestpapers) {

		analysisTestpaperMap = new HashMap<Long, AnalysisTestpaper>();
		AnalysisTestpaper tmpNalysisTestpaper = null;
		double fullScore = 0;
		for (AnalysisTestpaper tmp : analysisTestpapers) {
			analysisTestpaperMap.put(tmp.getId(), tmp);
			if (tmp.getFullScore() > fullScore) {
				fullScore = tmp.getFullScore();
				tmpNalysisTestpaper = tmp;
			}
		}

		nalysisTestpaper = tmpNalysisTestpaper;
		// if (analysisTestpapers.size() == 1) {
		// nalysisTestpaper = analysisTestpapers.get(0);
		// } else {
		//
		// }
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
		sql.append("insert into kn_examtestpapercj_tmp(");
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

		try {
			RowSet row = null;
			while ((row = getRow()) != null) {
				setSqlStatementValues(row);
				this.putRow(row);
			}
			sqlStatement.executeBatch();
			super.excuteStep();
		} catch (Exception e) {
			end();
			log.error(ExceptionHelper.trace2String(e));
			throw new Exception("执行步骤" + getName() + "提交数据到数据库的时候出错:"
					+ ExceptionHelper.trace2String(e));
		}
		log.debug("执行步骤成功");
	}

	private void setSqlStatementValues(RowSet row) throws Exception {
		int idx = 1;
		for (String name : fieldsName) {
			if (name.equals("examId")) {
				sqlStatement.setObject(idx++, testPaper.getExam().getId());
			} else if (name.equals("testPaperId")) {
				sqlStatement.setObject(idx++, testPaper.getId());
			} else if (name.equals("analysisTestpaperId")) {
				sqlStatement.setObject(idx++, nalysisTestpaper.getId());
			} else if (name.equals("subjectId")) {
				Long subjectId = null;
				if (nalysisTestpaper.getSubject() != null) {
					subjectId = nalysisTestpaper.getSubject().getId();
				}
				sqlStatement.setObject(idx++, subjectId);
			} else if (name.equals("isQk")) {
				String value = "1";
				try {
					value = row.getData(name);
				} catch (Exception e) {

				}
				sqlStatement.setObject(idx++, value);
			} else if (name.equals("zgScore")) {
				String value = "0";
				try {
					value = row.getData(name);
				} catch (Exception e) {

				}
				sqlStatement.setObject(idx++, value);
			} else if (name.equals("kgScore")) {
				String value = "0";
				try {
					value = row.getData(name);
				} catch (Exception e) {

				}
				sqlStatement.setObject(idx++, value);
			} else {
				sqlStatement.setObject(idx++, row.getData(name));
			}
		}
		sqlStatement.addBatch();

		if (analysisTestpaperMap.size() > 1) {
			setSplitTestPaper(row);
		}
	}

	private void setSplitTestPaper(RowSet row) throws Exception {
		Map<Long, Map<String, Double>> analysisTestpapersScore = getSplitTestPaperScore(row);

		for (Long id : analysisTestpapersScore.keySet()) {
			Map<String, Double> atpScore = analysisTestpapersScore.get(id);
			AnalysisTestpaper tmpAtp = analysisTestpaperMap.get(id);
			int idx = 1;
			for (String name : fieldsName) {
				if (name.equals("examId")) {
					sqlStatement.setObject(idx++, testPaper.getExam().getId());
				} else if (name.equals("testPaperId")) {
					sqlStatement.setObject(idx++, testPaper.getId());
				} else if (name.equals("analysisTestpaperId")) {
					sqlStatement.setObject(idx++, tmpAtp.getId());
				} else if (name.equals("subjectId")) {
					Long subjectId = null;
					if (tmpAtp.getSubject() != null) {
						subjectId = tmpAtp.getSubject().getId();
					}
					sqlStatement.setObject(idx++, subjectId);
				} else if (name.equals("isQk")) {
					String value = "0";
					try {
						value = row.getData(name);
					} catch (Exception e) {

					}
					sqlStatement.setObject(idx++, value);
				} else if (name.equals("totalScore")) {
					Double zf = atpScore.get("zgScore")
							+ atpScore.get("kgScore");
					sqlStatement.setObject(idx++, zf);
				} else if (name.equals("zgScore")) {
					sqlStatement.setObject(idx++, atpScore.get("zgScore"));
				} else if (name.equals("kgScore")) {
					sqlStatement.setObject(idx++, atpScore.get("kgScore"));
				} else {
					sqlStatement.setObject(idx++, row.getData(name));
				}
			}
			sqlStatement.addBatch();
		}
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
			if(score<0){
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
