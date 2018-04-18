/*
 * @(#)com.cntest.fxpt.etl.module.DBLoad.java	1.0 2014年5月12日:上午10:36:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Iterator;
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
import com.cntest.fxpt.util.LikeHashMap;
import com.cntest.fxpt.util.SaveEtlProcessResultToFile;
import com.cntest.util.ExceptionHelper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * <Pre>
 * 辅助处理加载数据到数据库
 * </Pre>
 * 
 * @author 刘海林 2014年5月12日 上午10:36:13
 * @version 1.0
 */
public class DBLoadCjWuHou extends BaseStep {
	private static final Logger log = LoggerFactory.getLogger(DBLoadCjWuHou.class);
	private Connection conn;
	private DataSource dataScource;
	private PreparedStatement sqlStatement;
	private String[] fieldsName = { "examId", "testPaperId", "analysisTestpaperId", "subjectId", "studentId", "classId",
			"schoolId", "countyId", "cityId", "provinceId", "wl", "isQk", "totalScore", "zgScore", "kgScore", "paper" };
	private TestPaper testPaper;
	private List<Item> items;
	private Map<Long, AnalysisTestpaper> analysisTestpaperMap;
	private AnalysisTestpaper nalysisTestpaper;

	private CacheManager cacheMgr;
	private Cache cache;
	private HashMap<String, LikeHashMap<String, Map<String, Object>>> allCacheMap;
	LikeHashMap<String, Map<String, Object>> valuemap = null;
	Double valuea0 = 0.0;
	Double valueb0 = 0.0;
	Double valuea1 = 0.0;
	Double valuea2 = 0.0;
	Double valueb1 = 0.0;
	Double valueb2 = 0.0;
	Double valuea = 0.0;
	Double valueb = 0.0;
	Double valuex = 0.0;
	Double value0 = 0.0;
	Double value1 = 0.0;
	Double value2 = 0.0;
	Double valuefull = 0.0;

	public DBLoadCjWuHou(String name, IEtlContext context) {
		super(name, context);
	}

	public DBLoadCjWuHou setDataSource(DataSource dataScource) {
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
			if (tmp.getFullScore() > 0) {
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
		cacheMgr = CacheManager.newInstance();
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
		cache = cacheMgr.getCache("student-Cj");
		Element sds = cache.get("student-Cj");
		allCacheMap = (HashMap<String, LikeHashMap<String, Map<String, Object>>>) sds.getObjectValue();

		log.debug("执行步骤" + getName());
		try {
			RowSet row = null;
			while ((row = getRow()) != null) {
				// Collection<AnalysisTestpaper> cons =
				// analysisTestpaperMap.values();
				Iterator<AnalysisTestpaper> ite = analysisTestpaperMap.values().iterator();
				while (ite.hasNext()) {
					setSqlStatementValues(row, "", ite.next());
					this.putRow(row);
				}
				// setSqlStatementValues(row,"A");
				// setSqlStatementValues(row,"B");
				// setSqlStatementValues(row,"SX");
				// this.putRow(row);
				// this.putRow(row);
				// this.putRow(row);
			}
			cache.removeAll();
			allCacheMap = null;
			valuemap = null;
			sqlStatement.executeBatch();
			super.excuteStep();
		} catch (Exception e) {
			end();
			log.error(ExceptionHelper.trace2String(e));
			throw new Exception("执行步骤" + getName() + "提交数据到数据库的时候出错:" + ExceptionHelper.trace2String(e));
		}
		log.debug("执行步骤成功");
	}

	private void setSqlStatementValues(RowSet row, String isAB, AnalysisTestpaper nalysisTestpaper) throws Exception {
		int idx = 1;
		Long examid = testPaper.getExam().getId();
		String studentid = row.getData("studentId");
		System.out.println(studentid + "这个学生开始啦~~~~~~~~~~~" + row.getRowNumber());
		Long testpaperid = testPaper.getId();
		double sxScore = 0;
		valuefull = "".equals(row.getData("totalScore")) ? 0.0 : Double.parseDouble(row.getData("totalScore"));
		if (nalysisTestpaper.getName().contains("A")) {
			isAB = "A";
		} else if (nalysisTestpaper.getName().contains("B")) {
			isAB = "B";
		} else if (nalysisTestpaper.getName().contains("SX")) {
			isAB = "SX";
		} else {
			isAB = "";
		}

		LikeHashMap<String, Map<String, Object>> valuemap = allCacheMap.get(studentid);
		if (valuemap == null)
			return;

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
				// value = row.getData(name);
				// if("".equals(value)||value==null){
				// value = itemService.findRespectiveScoreByItemId(testpaperid,
				// examid, "0",studentid,"");
				valuea0 = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%A" + "%" + "0", true));
				valueb0 = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%B" + "%" + "0", true));
				if (isAB.isEmpty()) {
					value = valuea0 + valueb0 + "";
				} else {
					value0 = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%%0", true));
					value = value0 + "";
					if ("SX".equals(isAB)) {
						if (!testPaper.isContainPaper()) {
							value = parseSX(testPaper.getName(), "".equals(value) ? 0 : Double.parseDouble(value), 0.0)
									+ "";
							if (valuea0 == 0.0 && valueb0 == 0.0) {
								value = "0.0";
							}
						} else {
							// totalScoreA =
							// itemService.findRespectiveScoreByItemId(testpaperid,
							// examid, "0",studentid, "A");
							// totalScoreB =
							// itemService.findRespectiveScoreByItemId(testpaperid,
							// examid, "0",studentid, "B");
							value = SaveEtlProcessResultToFile.convertSubjectScoreToSX(valuea0, valueb0,
									testPaper.getName()) + "";
						}
					} else if ("A".equals(isAB)) {
						// value =
						// itemService.findRespectiveScoreByItemId(testpaperid,
						// examid, "0",studentid, "A");
						value = valuea0 + "";
					} else if ("B".equals(isAB)) {
						// value =
						// itemService.findRespectiveScoreByItemId(testpaperid,
						// examid, "0",studentid, "B");
						value = valueb0 + "";
					}
				}
				// }
				sqlStatement.setDouble(idx++, "".equals(value) ? 0.0 : Double.parseDouble(value));
			} else if (name.equals("kgScore")) {
				String value = "0";
				// value = row.getData(name);
				// if("".equals(value)||value==null){
				// 找到客观题的得分 主观题是1,2
				// value = itemService.findRespectiveScoreByItemId(testpaperid,
				// examid, "1,2",studentid,"");
				valuea1 = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%A" + "%" + "1", true));
				valuea2 = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%A" + "%" + "2", true));
				valueb1 = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%B" + "%" + "1", true));
				valueb2 = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%B" + "%" + "2", true));
				if (isAB.isEmpty()) {
					value = valuea1 + valuea2 + valueb1 + valueb2 + "";
				} else {
					value1 = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%%1", true));
					value2 = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%%2", true));
					value = value1 + value2 + "";
					if ("SX".equals(isAB)) {
						if (!testPaper.isContainPaper()) {
							value = parseSX(testPaper.getName(), "".equals(value) ? 0 : Double.parseDouble(value),
									"".equals(value) ? 0 : Double.parseDouble(value)) + "";
							if (valuea1 == 0.0 && valuea2 == 0.0 && valueb1 == 0.0 && valueb2 == 0.0) {
								value = "0.0";
							}
						} else {
							// totalScoreA =
							// itemService.findRespectiveScoreByItemId(testpaperid,
							// examid, "1,2",studentid, "A");
							// totalScoreB =
							// itemService.findRespectiveScoreByItemId(testpaperid,
							// examid, "1,2",studentid, "B");
							value = SaveEtlProcessResultToFile.convertSubjectScoreToSX(valuea1 + valuea2,
									valueb1 + valueb2, testPaper.getName()) + "";
						}
					} else if ("A".equals(isAB)) {
						// value =
						// itemService.findRespectiveScoreByItemId(testpaperid,
						// examid, "1,2",studentid, "A");
						value = valuea1 + valuea2 + "";
					} else if ("B".equals(isAB)) {
						// value =
						// itemService.findRespectiveScoreByItemId(testpaperid,
						// examid, "1,2",studentid, "B");
						value = valueb1 + valueb2 + "";
					}
				}
				// }
				sqlStatement.setDouble(idx++, "".equals(value) ? 0.0 : Double.parseDouble(value));
			} else if (name.equals("paper")) {
				String value = "";
				if (!isAB.isEmpty()) {
					sqlStatement.setString(idx++, isAB);
				} else {
					sqlStatement.setString(idx++, value);
				}
			} else {
				if (isAB.isEmpty()) {
					sqlStatement.setObject(idx++, row.getData(name));
				} else {
					if (name.equals("totalScore")) {
						valuea = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%A" + "%", true));
						valueb = sumList(valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%B" + "%", true));
						valuex = sumList(
								valuemap.get(examid + "%" + studentid + "%" + testpaperid + "%" + isAB + "%", true));
						// value = valuea + valueb + "";
						if ("SX".equals(isAB)) {
							if (!testPaper.isContainPaper()) {

								sxScore = parseSX(testPaper.getName(),
										"0".equals(row.getData(name)) ? 0 : Double.parseDouble(row.getData(name)),
										valuefull);
							} else {
								// totalScoreA =
								// itemService.findPaperTotalScoreByItemId(examid,
								// studentid, testpaperid, "A");
								// totalScoreB =
								// itemService.findPaperTotalScoreByItemId(examid,
								// studentid, testpaperid, "B");
								sxScore = SaveEtlProcessResultToFile.convertSubjectScoreToSX(valuea, valueb,
										testPaper.getName());
							}
							sqlStatement.setDouble(idx++, sxScore);
						} else {
							// String totalScoreAB =
							// itemService.findPaperTotalScoreByItemId(examid,
							// studentid, testpaperid, isAB);
							String totalScoreAB = valuex + "";
							sqlStatement.setDouble(idx++,
									"".equals(totalScoreAB) ? 0.0 : Double.parseDouble(totalScoreAB));
							// sqlStatement.setObject(idx++, row.getData(name));
						}
					} else {
						sqlStatement.setObject(idx++, row.getData(name));
					}
				}
			}
		}
		sqlStatement.addBatch();

		// if(idx%5000==0){
		// sqlStatement.executeBatch();
		// this.excuteStep();
		// conn.commit();
		// }

		// if (analysisTestpaperMap.size() > 1) {
		// setSplitTestPaper(row,isAB);
		// }
	}

	private double parseSX(String subjectName, Double score, Double valuefull) {
		double sum = 0;
		if ("语文".equals(subjectName) || "数学".equals(subjectName) || "英语".equals(subjectName)) {
			sum = valuefull;
		} else if ("化学".equals(subjectName)) {
			sum = score / 2;
		} else {
			if (score >= 80) {
				sum = 20;
			} else if (score >= 70) {
				sum = 16;
			} else if (score >= 60) {
				sum = 12;
			} else {
				sum = 8;
			}
		}
		return sum;
	}

	private double sumList(List<Map<String, Object>> itemScoreList) {
		double sum = 0;
		for (Map<String, Object> map : itemScoreList) {
			if (map.containsKey("score")) {
				double tempScore = (double) map.get("score");
				sum += tempScore;
			}
		}
		// sum =
		// itemScoreList.size()==0?99:(double)itemScoreList.get(0).get("score");
		return sum;
	}

	private void setSplitTestPaper(RowSet row, String isAB) throws Exception {
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
					Double zf = atpScore.get("zgScore") + atpScore.get("kgScore");
					sqlStatement.setObject(idx++, zf);
				} else if (name.equals("zgScore")) {
					sqlStatement.setObject(idx++, atpScore.get("zgScore"));
				} else if (name.equals("kgScore")) {
					sqlStatement.setObject(idx++, atpScore.get("kgScore"));
				} else if (name.equals("paper")) {
					String value = "";
					if (!isAB.isEmpty()) {
						sqlStatement.setString(idx++, isAB);
					} else {
						sqlStatement.setString(idx++, value);
					}
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
			Map<String, Double> analysisTestpaperScore = analysisTestpapersScore.get(analysisTestpaperId);
			if (analysisTestpaperScore == null) {
				analysisTestpaperScore = new HashMap<String, Double>();
				analysisTestpaperScore.put("kgScore", 0d);
				analysisTestpaperScore.put("zgScore", 0d);
				analysisTestpapersScore.put(analysisTestpaperId, analysisTestpaperScore);
			}

			String value = row.getData("score" + item.getSortNum());
			Double score = Double.parseDouble(value);
			if (item.getOptionType() == 0) {
				analysisTestpaperScore.put("zgScore", analysisTestpaperScore.get("zgScore") + score);
			} else {
				analysisTestpaperScore.put("kgScore", analysisTestpaperScore.get("kgScore") + score);
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
