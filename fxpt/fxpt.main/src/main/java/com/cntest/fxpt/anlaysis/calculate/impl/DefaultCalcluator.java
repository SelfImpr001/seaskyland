/*
 * @(#)com.cntest.fxpt.anlaysis.calculate.impl.XinjiangCalcluator.java	1.0 2015年5月19日:上午9:46:38
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate.impl;

import com.cntest.fxpt.anlaysis.bean.*;
import com.cntest.fxpt.anlaysis.calculate.ICalcluator;
import com.cntest.fxpt.anlaysis.filter.IStudentCjFilter;
import com.cntest.fxpt.anlaysis.uitl.CalculateHelper;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.domain.*;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.fxpt.util.combinationUtil.Combination;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年5月19日 上午9:46:38
 * @version 1.0
 */
public class DefaultCalcluator implements ICalcluator {

	private final String KMZFCJTABLE = "kmzfcj";
	private final String KMKGCJTABLE = "kmkgcj";
	private final String KMZGCJTABLE = "kmzgcj";
	private final String ITEMTABLE = "item";
	private final String ABILITYTABLE = "ability";
	private final String KNOWLEDGETABLE = "knowledge";
	private final String KNOWLEDGECONTENTTABLE = "knowledgeContent";
	private final String TITLETYPETABLE = "titleType";
	private final String CHOICETYPE = "choiceType";
	private final String KMZFCJFSDTABLE = "kmzfcjfsd";

	private final String ABILITYFSDTABLE = "abilityfsd";
	private final String KNOWLEDGECONTENTFSDTABLE = "knowledgecontentfsd";
	private final String TITLETYPEFSDTABLE = "titletypefsd";

	private final String XGXSABILITY = "xgxs_ability";
	private final String XGXSKNOWLEDGECONTENT = "xgxs_knowledgecontent";
	private final String XGXSTITLETYPE = "xgxs_titletype";

	private SubjectScoreContainer beCalculateCJ;
	private CalculateTask event;
	private CalculateResult result;

	@Override
	public void calculate(CalculateTask event, CalculateResult cr, SubjectScoreContainer ssc) {
		if (ssc.isEmpty()) {
			return;
		}
		setResult(cr);
		setEvent(event);
		findCurSSC(event, cr);
		calculate();
	}

	private void setResult(CalculateResult result) {
		this.result = result;
	}

	private void setEvent(CalculateTask event) {
		this.event = event;
	}

	private void findCurSSC(CalculateTask event, CalculateResult cr) {
		IStudentCjFilter filter = event.getContext().getStatRankFilter(event.getAnalysisTestpaper());
		beCalculateCJ = event.getSubjectScoreContainer().getSubjectScoreContainer(filter);
	}

	private void calculate() {
		TargetResultContainer zfTrc = calculateZF(beCalculateCJ, 1);
		TargetResultContainer kgZFTrc = calculateZF(beCalculateCJ, 2);
		TargetResultContainer zgZFTrc = calculateZF(beCalculateCJ, 3);
		if (hasItem()) {
			List<TargetResultContainer> kgItemResults = calculateItem(beCalculateCJ, false);
			List<TargetResultContainer> zgItemResults = calculateItem(beCalculateCJ, true);

			ArrayList<TargetResultContainer> itemResults = new ArrayList<>();
			itemResults.addAll(kgItemResults);
			itemResults.addAll(zgItemResults);

			calculateReliability(kgZFTrc, kgItemResults);
			calculateReliability(zgZFTrc, zgItemResults);
			calculateReliability(zfTrc, itemResults);

			calculateItemGroup(beCalculateCJ);
		}
	}

	private void calculateReliability(TargetResultContainer zfTrc, List<TargetResultContainer> itemResults) {
		if (zfTrc == null || itemResults == null || itemResults.isEmpty()) {
			return;
		}

		int titleNum = 0;
		int i = 1;
		// if("foshan".equals(SystemConfig.newInstance().getValue(
		// "area.org.code"))){
		if (event != null && event.getContext().isHasChoice()) {
			for (TargetResultContainer trc : itemResults) {
				String choicenum = trc.getChoicenum();
				if (choicenum == null || "".equals(choicenum) || choicenum.isEmpty() || choicenum.length() == 0) {
					titleNum++;
				} else {
					int choicecount = Integer.parseInt(choicenum.split("\\|")[1]);
					if (choicecount == i) {
						titleNum++;
					} else {
						break;
					}
					i = choicecount;
				}
			}
		} else {
			titleNum = itemResults.size();
		}
		if (titleNum > 0) {
			double varianceSum = 0;
			for (TargetResultContainer trc : itemResults) {
				TargetResult tr = trc.get("stds");
				double std = tr.getValue();
				varianceSum += std * std;
			}
			double std = zfTrc.get("stds").getValue();
			double variance = std * std;
			double re = titleNum * (1 - varianceSum / variance) / (titleNum - 1);
			re = Double.isNaN(re) ? 0 : re;
			re = Double.isInfinite(re) ? 0 : re;
			zfTrc.put("reliability", new TargetResult("reliability", re));
		}
	}

	private boolean hasItem() {
		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		List<Item> items = atp.getItems();
		return items != null && !items.isEmpty();
	}

	private TargetResultContainer calculateZF(SubjectScoreContainer beCalculateCJ, int scoreType) {

		String tableName = null;
		CalculateHelper ch = null;
		CalculateHelper preCh27 = null;
		CalculateHelper backCh27 = null;
		CalculateHelper upChJZS = null;
		CalculateHelper downChDFD = null;
		if (scoreType == 1) {
			ch = beCalculateCJ.getCalculateHelper();
			
			preCh27 = ch.getCalculateHelper(0.27);
			backCh27 = ch.getCalculateHelperForBack(0.27);
			Object objType = event.getStudentRangeValues().get("objType");
			if (objType != null) {
				if (objType.equals(event.getContext().getExam().getLevelCode())) {

//算全市全排名存在这个里面   学科    studentid   rank
//					event.getContext().

//					ConcurrentMap<String,Map<String,Integer>> scoreRank  = new ConcurrentHashMap<>();
//					Map<String,Integer> scoreInfoMap  = new HashMap<>();
//					for (StudentSubjectScore sss:ch.getSubjectScoreContainer().getScores()){
//						Score score = sss.getScore();
//						for (ScoreInfo si:ch.getScoreInfos()){
//							if(score.getValue()==si.getScore()){
//								scoreInfoMap.put(sss.getStudentCj().getStudent().getStudentId(),si.getRank());
//							}
//						}
//					}
//					scoreRank.put(ch.getGroupScore().getSubjectScoreContainer().getAnalysisTestpaper().getSubject()+""
//							+ch.getSubjectScoreContainer().getScores().get(0).getStudentCj().getStudent().getWl(),scoreInfoMap);

					if (event.getAnalysisTestpaper().getSubject().isZF()) {
						saveUplineScore(ch, event.getAnalysisTestpaper());
					}
					saveUplineScoreJZSDFD(ch, event.getAnalysisTestpaper());

				}
			}
			tableName = getSaveTableName(KMZFCJTABLE);
			saveSegment(ch, "");
			
			//新增所有级别整体学生排名表hhc 20180205
			saveSegment_all(ch, "dw_agg_xinjiang_segment_students");
			
//			int wl = Integer.parseInt(event.getStudentRangeValues().get("wl").toString());
//			Long subjectId = ch.getSubjectScoreContainer().getAnalysisTestpaper().getSubjectId();
//			List<UplineScore> uplineScores = event.getContext().getUplineScores().stream()
//					.filter(c -> c.getScoreType() == 3).filter(c -> c.getSubject().getId() == subjectId)
//					.filter(c -> c.getWlType() == wl).collect(Collectors.toList());
//
//			upChJZS = ch.getCalculateHelperForTopAndDown(1, uplineScores);
//			downChDFD = ch.getCalculateHelperForTopAndDown(2, uplineScores);
//			saveSegment(upChJZS, "up");
//			saveSegment(downChDFD, "down");
		} else if (scoreType == 2) {
			ch = beCalculateCJ.getCalculateHelper();
			preCh27 = ch.getCalculateHelper(0.27).getSubjectScoreContainer().getKGCalculateHelper();
			backCh27 = ch.getCalculateHelperForBack(0.27).getSubjectScoreContainer().getKGCalculateHelper();
			ch = beCalculateCJ.getKGCalculateHelper();
			tableName = getSaveTableName(KMKGCJTABLE);
		} else if (scoreType == 3) {
			ch = beCalculateCJ.getCalculateHelper();
			preCh27 = ch.getCalculateHelper(0.27).getSubjectScoreContainer().getZGCalculateHelper();
			backCh27 = ch.getCalculateHelperForBack(0.27).getSubjectScoreContainer().getZGCalculateHelper();
			ch = beCalculateCJ.getZGCalculateHelper();
			tableName = getSaveTableName(KMZGCJTABLE);
		} else {
			return null;
		}
		if (tableName == null) {
			return null;
		}
		TargetResultContainer trc = new TargetResultContainer();
		trc.setTableName(tableName);
		trc.put("fullScore", new TargetResult("fullScore", event.getAnalysisTestpaper().getFullScore()));
		trc.put("bkrs", new TargetResult("bkrs", event.getBkrs()));
		trc.put("skrs", new TargetResult("skrs", ch.getTaskNum()));
		trc.put("avgs", new TargetResult("avgs", ch.getAvg()));
		trc.put("maxs", new TargetResult("maxs", ch.getMax()));
		trc.put("mins", new TargetResult("mins", ch.getMin()));
		trc.put("stds", new TargetResult("stds", ch.getStd()));
		trc.put("medians", new TargetResult("medians", ch.getMedians()));
		trc.put("modes", new TargetResult("modes", ch.getModes()));
		trc.put("skewness", new TargetResult("skewness", ch.getSkewness()));
		trc.put("kurtosis", new TargetResult("kurtosis", ch.getKurtosis()));
		if ("wuhou".equals(SystemConfig.newInstance().getValue("area.org.code"))) {
			trc.put("ychgrs", new TargetResult("ychgrs", ch.getOnepassNum()));
			trc.put("qkhgrs", new TargetResult("qkhgrs", ch.getAllpassNum()));
		}
		trc.put("quartile25", new TargetResult("quartile25", ch.getQuartile(0.75)));
		trc.put("quartile50", new TargetResult("quartile50", ch.getQuartile(0.5)));
		trc.put("quartile75", new TargetResult("quartile75", ch.getQuartile(0.25)));
		trc.put("hskrs", new TargetResult("hskrs", preCh27.getTaskNum()));
		trc.put("havgs", new TargetResult("havgs", preCh27.getAvg()));
		trc.put("lskrs", new TargetResult("lskrs", backCh27.getTaskNum()));
		trc.put("lavgs", new TargetResult("lavgs", backCh27.getAvg()));
		trc.put("scoreType", new TargetResult("scoreType", scoreType));
		for (int i = 1; i <= 4; i++) {
			trc.put(i + "_ddrs", new TargetResult(i + "_ddrs", ch.getddrs(i)));
			trc.put(i + "_sdrs", new TargetResult(i + "_sdrs", ch.getsdrs(i)));
			trc.put(i + "_zdrs", new TargetResult(i + "_zdrs", ch.getzdrs(i)));
		}
		setInfo(trc);
		result.addResult(trc);
		return trc;

	}

	private String getSaveTableName(String type) {
		AnalysisResultSaveToTable table = event.getSaveTableContainer().get(type);
		String tableName = (table == null || !table.isAvailable() || table.getTableName() == null
				|| table.getTableName().equals("")) ? null : table.getTableName();
		return tableName;
	}

	private void saveSegment(CalculateHelper ch, String flag) {
		String tableName = getSaveTableName(KMZFCJFSDTABLE);
		if (tableName == null) {
			return;
		}
		List<ScoreInfo> scoreInfos = ch.getScoreInfos();
		int taskNum = ch.getTaskNum();
		for (ScoreInfo info : scoreInfos) {
			TargetResultContainer trc = new TargetResultContainer();
			trc.setTableName(tableName);
			trc.put("num", new TargetResult("num", info.getNum()));
			trc.put("rank", new TargetResult("rank", info.getRank()));
			trc.put("score", new TargetResult("score", info.getScore()));
			trc.put("skrs", new TargetResult("skrs", taskNum));
			trc.put("flag", new TargetResult("flag", flag));
			setInfo(trc);
			result.addResult(trc);
		}
	}
	
	private void saveSegment_all(CalculateHelper ch, String tableName) {
		if (tableName == null) {
			return;
		}

		List<StudentSubjectScore> scoreList = ch.getSubjectScoreContainer().toList();
		List<Double> scores = new ArrayList<Double>();
		for (StudentSubjectScore sss : scoreList) {
			if(ch.getSubjectScoreContainer().getAnalysisTestpaper().getSubject().getId()==98){
				double score = sss.getRankScore().getValue();
				scores.add(score);
			}else{
				double score = sss.getScore().getValue();
				scores.add(score);
			}
		}
		Map<Double, Integer> rankMap = this.calculationRank(scores);
		Map<Double, Integer> reverseRankMap = this.calculationRankReverse(scores);
		for (StudentSubjectScore sss:ch.getSubjectScoreContainer().toList()){
			Score score = sss.getScore();
			TargetResultContainer trc = new TargetResultContainer();
			trc.setTableName(tableName);
			double scoreValue = sss.getScore().getValue();
			if(ch.getSubjectScoreContainer().getAnalysisTestpaper().getSubject().getId()==98) {
				scoreValue = sss.getRankScore().getValue();
			}
			ExamStudent student = sss.getStudentCj().getStudent();
			trc.put("rank", new TargetResult("rank", rankMap.get(scoreValue)));
			trc.put("reverseRank", new TargetResult("reverseRank", reverseRankMap.get(scoreValue)));
			trc.put("objId", new TargetResult("objId", student.getId()));
			trc.put("score", new TargetResult("score", score));
			trc.put("schoolId", new TargetResult("schoolId", student.getSchool().getId()));
			trc.put("wl", new TargetResult("wl", student.getWl()));
			if (student.getClazz() != null) {
				trc.put("classId", new TargetResult("classId", student.getClazz().getId()));
				trc.put("className", new TargetResult("className", student.getClazz().getName()));
				trc.put("schoolName", new TargetResult("schoolName", student.getClazz().getSchoolName()));
			}
			trc.put("studentid", new TargetResult("studentid", student.getStudentId()));
			setInfo(trc);
			result.addResult(trc);
		}
	}
	
	
	/**
	 * 
	* <Pre>
	* 排名计算（含并列） 升序排名
	* </Pre>
	* 
	* @param list
	* @return
	* @return Map   key=分数(Double类型)   value=名次（int类型）
	* @author:黄洪成 2016年7月7日 上午10:43:22
	 */
	public static Map calculationRankReverse(List list){
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				Double d1 = Double.parseDouble(o1.toString());
				Double d2 = Double.parseDouble(o2.toString());
				return d1.compareTo(d2);
			}
		});
		Map map = new HashMap();
		int rank = 1;
		double temp = 0;
		for (int i = 0; i < list.size(); i++) {
		   Double value = Double.parseDouble(list.get(i).toString());
		   if(value>temp){
			   map.put(value, rank);
		   }else{
			   if(!map.containsKey(value)){
				   map.put(value, rank);
			   }
		   }
		   rank++;
		   temp = value;
		}
		
		
		List<Map.Entry<Double, Integer>> res = new LinkedList<Map.Entry<Double, Integer>>(map.entrySet());
		Collections.sort(res,new Comparator<Map.Entry<Double, Integer>>() {
			public int compare(Map.Entry<Double, Integer> o1,Map.Entry<Double, Integer> o2){
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		map = new LinkedHashMap<Double, Integer>();
		for(Map.Entry<Double, Integer> entry : res){
			map.put(entry.getKey(), entry.getValue());	
		}
		
		return map;
	}
	
	/**
	 * 
	* <Pre>
	* 排名计算（含并列） 降序排名
	* </Pre>
	* 
	* @param list
	* @return
	* @return Map   key=分数(Double类型)   value=名次（int类型）
	* @author:黄洪成
	 */
	public static Map calculationRank(List list){
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				Double d1 = Double.parseDouble(o1.toString());
				Double d2 = Double.parseDouble(o2.toString());
				return d2.compareTo(d1);
			}
		});
		Map map = new HashMap();
		int rank = 1;
		double temp = 0;
		for (int i = 0; i < list.size(); i++) {
		   Double value = Double.parseDouble(list.get(i).toString());
		   if(value>temp){
			   map.put(value, rank);
		   }else{
			   if(!map.containsKey(value)){
				   map.put(value, rank);
			   }
		   }
		   rank++;
		   temp = value;
		}
		
		
		List<Map.Entry<Double, Integer>> res = new LinkedList<Map.Entry<Double, Integer>>(map.entrySet());
		Collections.sort(res,new Comparator<Map.Entry<Double, Integer>>() {
			public int compare(Map.Entry<Double, Integer> o1,Map.Entry<Double, Integer> o2){
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		map = new LinkedHashMap<Double, Integer>();
		for(Map.Entry<Double, Integer> entry : res){
			map.put(entry.getKey(), entry.getValue());	
		}
		return map;
	}

	private double calcualteUpAndDownLineUpScore(CalculateHelper ch, double uplineScore, double ration, boolean flag) {
		int num = ch.getGEScoreNum(uplineScore);
		double ratio = (num + 0d) / (ch.getTaskNum() + 0d);
		if (flag) {
			ration = ratio + ration;
		} else {
			ration = ratio - ration;
		}
		List<Double> listScore = new ArrayList<Double>();
		List<ScoreInfo> scoreinfoList = ch.getGroupScore().getScoreInfos();
		for (int i = 0; i < scoreinfoList.size(); i++) {
			ScoreInfo scoreInfo = scoreinfoList.get(i);
			if ((scoreInfo.getRank() + 0d) / (ch.getTaskNum() + 0d) <= ration) {
				listScore.add(scoreInfo.getScore().getValue());
			}
		}
		Collections.sort(listScore);
		if(!flag) {
			if(listScore.size()>0)
				return listScore.get(0);
			else
				return 0;
		}else{
			return listScore.get(0);
		}
	}

	private void saveUplineScoreJZSDFD(CalculateHelper ch, AnalysisTestpaper analysisTestpaper) {
		List<UplineScore> uplineScores = event.getContext().getUplineScores();
		uplineScores = uplineScores.stream().filter(c -> c.getScoreType() == 3)
				.filter(c -> c.getSubject().getId() == analysisTestpaper.getSubjectId())
				.filter(c -> c.getWlType() == Integer.parseInt(event.getStudentRangeValues().get("wl").toString()))
				.collect(Collectors.toList());

		for (UplineScore uplineScore : uplineScores) {
			double divideRank = uplineScore.getDivideScale();
			double levelRank = divideRank == 0 ? 20 : divideRank;
			double resultScore = 0.0;
			if (uplineScore.getLevel() == 1) {
				resultScore = ch.getRankScore((int) levelRank);
			} else {
				resultScore = ch.getBackRankScore((int) levelRank);
			}
			uplineScore.setDivideScore(resultScore);
		}
	}

	private void saveUplineScore(CalculateHelper ch, AnalysisTestpaper analysisTestpaper) {
		List<UplineScore> uplineScores = event.getContext().getUplineScores();
		
//		uplineScores = uplineScores.stream().filter(c -> c.getScoreType() == 2)
//				.filter(c -> c.getSubject().getId() == analysisTestpaper.getSubjectId())
//				.filter((c -> c.getWlType() == (analysisTestpaper.getPaperType() == 0 ? c.getWlType()
//						: analysisTestpaper.getPaperType())))
//				.collect(Collectors.toList());
		for (UplineScore uplineScore : uplineScores) {
			if(uplineScore.getScoreType()==4 && uplineScore.getWlType()==analysisTestpaper.getPaperType() && uplineScore.getSubject().getId()==98){
				double divideScore =uplineScore.getDivideScore();
				if(divideScore==0){
					divideScore = ch.getQuartile(uplineScore.getDivideScale());
				}
				double sf = calcualteUpAndDownLineUpScore(ch, divideScore, uplineScore.getRatio(), true);
				double xt = calcualteUpAndDownLineUpScore(ch, divideScore, uplineScore.getRatio(), false);
				uplineScore.setDivideScore(divideScore);
				uplineScore.setUpScore(sf);
				uplineScore.setDownScore(xt);
			}
		}
	}

	private void saveSegmentKnowledge(CalculateHelper ch, String tablename, String itemGroupTypeName) {
		String tableName = getSaveTableName(tablename);
		if (tableName == null) {
			return;
		}

		List<ScoreInfo> scoreInfos = ch.getScoreInfos();
		int taskNum = ch.getTaskNum();
		for (ScoreInfo info : scoreInfos) {
			TargetResultContainer trc = new TargetResultContainer();
			trc.setTableName(tableName);
			trc.put("num", new TargetResult("num", info.getNum()));
			trc.put("rank", new TargetResult("rank", info.getRank()));
			trc.put("score", new TargetResult("score", info.getScore()));
			setInfoknowledge(trc, itemGroupTypeName);
			result.addResult(trc);
		}

	}

	private void setInfoknowledge(TargetResultContainer trc, String itemGroupTypeName) {
		Exam exam = event.getContext().getExam();
		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		Subject subject = atp.getSubject();

		trc.put("examId", new TargetResult("examId", exam.getId()));
		if (subject != null) {
			trc.put("subjectId", new TargetResult("subjectId", subject.getId()));
		}
		trc.put("analysisTestpaperId", new TargetResult("analysisTestpaperId", atp.getId()));
		trc.put("name", new TargetResult("name", itemGroupTypeName));

		Container<String, Object> studentRangeValues = event.getStudentRangeValues();
		trc.put("wl", new TargetResult("wl", studentRangeValues.get("wl")));
	}

	private void setInfo(TargetResultContainer trc) {
		Exam exam = event.getContext().getExam();
		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		Subject subject = atp.getSubject();

		trc.put("examId", new TargetResult("examId", exam.getId()));
		if (subject != null) {
			trc.put("subjectId", new TargetResult("subjectId", subject.getId()));
		}
		trc.put("testpaperId", new TargetResult("testpaperId", atp.getId()));
		Container<String, Object> studentRangeValues = event.getStudentRangeValues();
		for (String rangeName : studentRangeValues.toListForKey()) {
			Object rangeValue = studentRangeValues.get(rangeName);
			trc.put(rangeName, new TargetResult(rangeName, rangeValue));
		}
	}

	private void calculateItemGroup(SubjectScoreContainer beCalculateCJ) {
		calculateItemGroupCorrelation("ability", beCalculateCJ, getSaveTableName(XGXSABILITY));
		calculateItemGroupCorrelation("knowledgeContent", beCalculateCJ, getSaveTableName(XGXSKNOWLEDGECONTENT));
		calculateItemGroupCorrelation("titleType", beCalculateCJ, getSaveTableName(XGXSTITLETYPE));

		calculateItemGroup("ability", beCalculateCJ, getSaveTableName(ABILITYTABLE));
		calculateItemGroup("knowledge", beCalculateCJ, getSaveTableName(KNOWLEDGETABLE));
		calculateItemGroup("knowledgeContent", beCalculateCJ, getSaveTableName(KNOWLEDGECONTENTTABLE));
		calculateItemGroup("titleType", beCalculateCJ, getSaveTableName(TITLETYPETABLE));
	}

	private void calculateItemGroup(String itemGroupTypeName, SubjectScoreContainer beCalculateCJ, String tableName) {
		if (tableName == null) {
			return;
		}
		Exam exam = event.getContext().getExam();

		CalculateHelper ch = beCalculateCJ.getCalculateHelper();

		SubjectScoreContainer hSSContainer = ch.getCalculateHelper(0.27).getSubjectScoreContainer();
		SubjectScoreContainer lSSContainer = ch.getCalculateHelperForBack(0.27).getSubjectScoreContainer();

		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		List<ItemGroup> itemGroups = ItemGroup.createItemGroupsWithItemAttr(itemGroupTypeName, atp.getItems(),
				event.getContext().isHasChoice());

		// 此处要分两组来分析，第一组只包含选做题，第二种只包含非选做题，必须区分开来
		for (ItemGroup itemGroup : itemGroups) {
			TargetResultContainer trc = new TargetResultContainer();
			trc.setTableName(tableName);

			// 选做题itemgroup分析
			if (itemGroup.isChoiceType()) {
				// 总的计算器
				ch = beCalculateCJ.getItemGroupCalculateHelper(itemGroup, true);
				if (ch.getTaskNum() == 0) {
					return;
				}
				saveItemAndItemGroupTarget(trc, ch);

				// 高分组计算器
				ch = hSSContainer.getItemGroupCalculateHelper(itemGroup, true);
				saveItemAndItemGroupTargetForHL(trc, ch, "h");

				// 低分组计算器
				ch = lSSContainer.getItemGroupCalculateHelper(itemGroup, true);
				saveItemAndItemGroupTargetForHL(trc, ch, "l");

				setItemGroupInfo(trc, itemGroup, itemGroupTypeName, true);
			} else {
				// 总的计算器
				ch = beCalculateCJ.getItemGroupCalculateHelper(itemGroup, false);
				String highestLevel = exam.getLevelCode() + "";

				if ("".equals(highestLevel)) {
					highestLevel = "3";
				}
				if (highestLevel.equals(event.getStudentRangeValues().get("objType").toString())) {
					if ("ability".equals(itemGroupTypeName)) {
						saveSegmentKnowledge(ch, ABILITYFSDTABLE, itemGroup.getName().replaceAll("'", "\\\\'"));
					} else if ("knowledgeContent".equals(itemGroupTypeName)) {
						saveSegmentKnowledge(ch, KNOWLEDGECONTENTFSDTABLE,
								itemGroup.getName().replaceAll("'", "\\\\'"));
					} else if ("titleType".equals(itemGroupTypeName)) {
						saveSegmentKnowledge(ch, TITLETYPEFSDTABLE, itemGroup.getName().replaceAll("'", "\\\\'"));
					}
				}

				saveItemAndItemGroupTarget(trc, ch);

				trc.put("quartile25", new TargetResult("quartile25", ch.getQuartile(0.75)));
				trc.put("quartile50", new TargetResult("quartile50", ch.getQuartile(0.5)));
				trc.put("quartile75", new TargetResult("quartile75", ch.getQuartile(0.25)));

				// 高分组计算器
				ch = hSSContainer.getItemGroupCalculateHelper(itemGroup, false);
				saveItemAndItemGroupTargetForHL(trc, ch, "h");

				// 低分组计算器
				ch = lSSContainer.getItemGroupCalculateHelper(itemGroup, false);
				saveItemAndItemGroupTargetForHL(trc, ch, "l");

				setItemGroupInfo(trc, itemGroup, itemGroupTypeName, false);

			}

			setInfo(trc);

			result.addResult(trc);
		}

	}

	private void calculateItemGroupCorrelation(String itemGroupTypeName, SubjectScoreContainer beCalculateCJ,
			String tableName) {
		if (tableName == null) {
			return;
		}

		CalculateHelper ch = beCalculateCJ.getCalculateHelper();

		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		List<ItemGroup> itemGroups = ItemGroup.createItemGroupsWithItemAttr(itemGroupTypeName, atp.getItems(),
				event.getContext().isHasChoice());
		ConcurrentHashMap<String, Map<Long, Double>> cordatasets = new ConcurrentHashMap<>();
		List<String> itemGroupName = new ArrayList<>();
		for (ItemGroup itemGroup : itemGroups) {
			cordatasets.put(itemGroup.getName(), beCalculateCJ.getItemGroupGroupMapScore(itemGroup));
			itemGroupName.add(itemGroup.getName());
		}

		TargetResultContainer trc = null;
		if (itemGroupName.size() >= 2) {
			for (List<String> itemGroupCom : Combination.of(itemGroupName, 2)) {
				trc = new TargetResultContainer();
				trc.setTableName(tableName);
				double corvaule = ch.getCorrelation(cordatasets, itemGroupCom.get(0), itemGroupCom.get(1));
				trc.put("mk1", new TargetResult("mk1", itemGroupCom.get(0)));
				trc.put("mk2", new TargetResult("mk2", itemGroupCom.get(1)));
				trc.put("xgxs", new TargetResult("xgxs", corvaule));
				setInfo(trc);
				result.addResult(trc);
			}
			for (List<String> itemGroupCom : Combination.of(itemGroupName, 2)) {
				trc = new TargetResultContainer();
				trc.setTableName(tableName);
				double corvaule = ch.getCorrelation(cordatasets, itemGroupCom.get(1), itemGroupCom.get(0));
				trc.put("mk1", new TargetResult("mk1", itemGroupCom.get(1)));
				trc.put("mk2", new TargetResult("mk2", itemGroupCom.get(0)));
				trc.put("xgxs", new TargetResult("xgxs", corvaule));
				setInfo(trc);
				result.addResult(trc);
			}
			// 模块自己VS自己
			for (int i = 0; i < itemGroupName.size(); i++) {
				trc = new TargetResultContainer();
				trc.setTableName(tableName);
				double corvaulemk0 = ch.getCorrelation(cordatasets, itemGroupName.get(i), itemGroupName.get(i));
				trc.put("mk1", new TargetResult("mk1", itemGroupName.get(i)));
				trc.put("mk2", new TargetResult("mk2", itemGroupName.get(i)));
				trc.put("xgxs", new TargetResult("xgxs", corvaulemk0));
				setInfo(trc);
				result.addResult(trc);
			}
		}
	}

	private void setItemGroupInfo(TargetResultContainer trc, ItemGroup itemGroup, String itemGroupTypeName,
			boolean ischoice) {
		if (ischoice) {
			trc.put("fullScore", new TargetResult("fullScore", itemGroup.getFullScoreChoice()));
			trc.put("itemGroupType", new TargetResult("itemGroupType", itemGroupTypeName + "_choice"));
			trc.put("itemNos", new TargetResult("itemNos", itemGroup.getItemNoToStringchoice()));
		} else {
			trc.put("fullScore", new TargetResult("fullScore", itemGroup.getFullScore()));
			trc.put("itemGroupType", new TargetResult("itemGroupType", itemGroupTypeName));
			trc.put("itemNos", new TargetResult("itemNos", itemGroup.getItemNoToString()));
		}

		trc.put("itemNum", new TargetResult("itemNum", itemGroup.getItemNum()));
		trc.put("name", new TargetResult("name", itemGroup.getName().replaceAll("'", "\\\\'")));
	}

	private List<TargetResultContainer> calculateItem(SubjectScoreContainer beCalculateCJ, boolean iszg) {

		String tableName = getSaveTableName(ITEMTABLE);
		if (tableName == null) {
			return new ArrayList<>();
		}

		CalculateHelper ch = beCalculateCJ.getCalculateHelper();
		CalculateHelper itemch = null;
		CalculateHelper itemchb = null;
		SubjectScoreContainer hSSContainer = ch.getCalculateHelper(0.27).getSubjectScoreContainer();
		SubjectScoreContainer lSSContainer = ch.getCalculateHelperForBack(0.27).getSubjectScoreContainer();

		ArrayList<TargetResultContainer> itemResults = new ArrayList<>();
		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		List<Item> items = atp.getItems();

		String selOptions = atp.getTestPaper().getSelOptions();
		if (StringUtils.isEmpty(selOptions))
			selOptions = "[\"A\",\"B\",\"C\",\"D\"]";
		JSONArray jsonOptions = JSONArray.fromObject(selOptions);
		for (Item item : items) {
			if (iszg && item.getOptionType() == 0) {
				;
			} else if (!iszg && item.getOptionType() != 0) {
				;
			} else {
				continue;
			}

			ch = beCalculateCJ.getItemCalculateHelper(item);
			TargetResultContainer trc = new TargetResultContainer();
			trc.setTableName(tableName);
			trc.setChoicenum(item.getChoiceNumber());

			saveItemAndItemGroupTarget(trc, ch);
			statSelOptionAndSave(ch, trc, item, "", beCalculateCJ, jsonOptions);

//			ch = hSSContainer.getItemCalculateHelper(item);
			ch = beCalculateCJ.getItemCalculateHelperChoice(item);
			itemch = ch.getCalculateHelper(0.27).getSubjectScoreContainer().getItemCalculateHelper(item);
			saveItemAndItemGroupTargetForHL(trc, itemch, "h");
			statSelOptionAndSave(itemch, trc, item, "h", hSSContainer, jsonOptions);

//			ch = lSSContainer.getItemCalculateHelper(item);
			itemchb = ch.getCalculateHelperForBack(0.27).getSubjectScoreContainer().getItemCalculateHelper(item);
			saveItemAndItemGroupTargetForHL(trc, itemchb, "l");
			statSelOptionAndSave(itemchb, trc, item, "l", lSSContainer, jsonOptions);

			setItemInfo(trc, item);
			setInfo(trc);

			result.addResult(trc);
			itemResults.add(trc);
		}
		return itemResults;
	}

	private void setItemInfo(TargetResultContainer trc, Item item) {
		trc.put("itemId", new TargetResult("itemId", item.getId()));
		trc.put("name", new TargetResult("name", item.getItemNo()));
		trc.put("fullScore", new TargetResult("fullScore", item.getFullScore()));
	}

	private void saveItemAndItemGroupTarget(TargetResultContainer trc, CalculateHelper ch) {
		trc.put("skrs", new TargetResult("skrs", ch.getTaskNum()));
		trc.put("avgs", new TargetResult("avgs", ch.getAvg()));
		trc.put("maxs", new TargetResult("maxs", ch.getMax()));
		trc.put("mins", new TargetResult("mins", ch.getMin()));
		trc.put("stds", new TargetResult("stds", ch.getStd()));
		// if("wuhou".equals(SystemConfig.newInstance().getValue(
		// "area.org.code"))){
		// trc.put("minsExceptZero", new TargetResult("minsExceptZero",
		// ch.getMinsExceptZero()));
		// }
	}

	private void saveItemAndItemGroupTargetForHL(TargetResultContainer trc, CalculateHelper ch, String hlType) {
		trc.put(hlType + "skrs", new TargetResult(hlType + "skrs", ch.getTaskNum()));
		trc.put(hlType + "avgs", new TargetResult(hlType + "avgs", ch.getAvg()));
	}

	private void statSelOptionAndSave(CalculateHelper ch, TargetResultContainer trc, Item item, String hlType,
			SubjectScoreContainer ssc, JSONArray jsonOptions) {
		trc.put(hlType + "choicenum", new TargetResult(hlType + "choicenum", ch.getChoiceNum()));
		if (item.getOptionType() == 1) {
			calculateSingleOption(item, ch, ssc, trc, hlType, jsonOptions);
		} else if (item.getOptionType() == 2) {
			calcualteMultiOption(item, ssc, trc, hlType);
		}
	}

	/*
	 * private void calculateSingleOption(Item item, SubjectScoreContainer ssc,
	 * TargetResultContainer targetResults, String hlType) { int a = 0; int b =
	 * 0; int c = 0; int d = 0; int o = 0; for (StudentSubjectScore sss :
	 * ssc.toList()) { ItemScore itemScore = sss.getItemScore(item); String
	 * selOption = itemScore.getSelOption(); if(selOption!=null){ if
	 * (selOption.contains("A")) { a++; } else if (selOption.contains("B")) {
	 * b++; } else if (selOption.contains("C")) { c++; } else if
	 * (selOption.contains("D")) { d++; } else { o++; } } }
	 * 
	 * targetResults.put(hlType + "ANum", new TargetResult(hlType + "ANum", a));
	 * targetResults.put(hlType + "BNum", new TargetResult(hlType + "BNum", b));
	 * targetResults.put(hlType + "CNum", new TargetResult(hlType + "CNum", c));
	 * targetResults.put(hlType + "DNum", new TargetResult(hlType + "DNum", d));
	 * targetResults.put(hlType + "OtherNum", new TargetResult(hlType +
	 * "OtherNum", o));
	 * 
	 * Map<String, Integer> map = new HashMap<String, Integer>(); map.put("A",
	 * a); map.put("B", b); map.put("C", c); map.put("D", d); JSONArray array =
	 * JSONArray.fromObject(map); targetResults.put(hlType + "Nums", new
	 * TargetResult(hlType + "Nums", array.toString())); }
	 */

	private void calculateSingleOption(Item item, CalculateHelper ch, SubjectScoreContainer ssc,
			TargetResultContainer targetResults, String hlType, JSONArray jsonOptions) {

		Collection<String> collection = JSONArray.toCollection(jsonOptions);
		String allOptions = item.getAllOptions();
		if (allOptions == null || "".equals(allOptions)) {
			allOptions = "A,B,C,D";
		}
		String[] ao = allOptions.split(",");
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		for (String key : ao) {
			map.put(key, 0);
		}

		int othernum = 0;
		for (StudentSubjectScore sss : ssc.toList()) {
			ItemScore itemScore = sss.getItemScore(item);
			String selOption = itemScore.getSelOption();
			if (selOption != null && map.containsKey(selOption)) {
				Integer val = map.get(selOption);
				map.put(selOption, ++val);
			} else {
				map.put("other", ++othernum);
			}
		}

		int i = 0, o = 0;
		for (Entry<String, Integer> entry : map.entrySet()) {
			switch (i++) {
			case 0:
				targetResults.put(hlType + "ANum", new TargetResult(hlType + "ANum", entry.getValue()));
				break;
			case 1:
				targetResults.put(hlType + "BNum", new TargetResult(hlType + "BNum", entry.getValue()));
				break;
			case 2:
				targetResults.put(hlType + "CNum", new TargetResult(hlType + "CNum", entry.getValue()));
				break;
			case 3:
				targetResults.put(hlType + "DNum", new TargetResult(hlType + "DNum", entry.getValue()));
				break;
			default:
				o += entry.getValue();
				break;
			}
		}
		targetResults.put(hlType + "OtherNum", new TargetResult(hlType + "OtherNum", o));
		JSONArray array = JSONArray.fromObject(map);
		targetResults.put(hlType + "Nums", new TargetResult(hlType + "Nums", array.toString()));
	}

	private void calcualteMultiOption(Item item, SubjectScoreContainer ssc, TargetResultContainer targetResults,
			String hlType) {
		int erro = 0;
		int halfRight = 0;
		int allRight = 0;

		for (StudentSubjectScore sss : ssc.toList()) {
			ItemScore itemScore = sss.getItemScore(item);
			Score score = itemScore.getScore();
			if (score.getValue() == 0) {
				erro++;
			} else if (score.getValue() == item.getFullScore()) {
				allRight++;
			} else {
				halfRight++;
			}
		}

		targetResults.put(hlType + "erro", new TargetResult(hlType + "erro", erro));
		targetResults.put(hlType + "halfRight", new TargetResult(hlType + "halfRight", halfRight));
		targetResults.put(hlType + "allRight", new TargetResult(hlType + "allRight", allRight));
	}
}
