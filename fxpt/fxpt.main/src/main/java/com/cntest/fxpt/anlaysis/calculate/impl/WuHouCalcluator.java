/*
 * @(#)com.cntest.fxpt.anlaysis.calculate.impl.XinjiangCalcluator.java	1.0 2015年5月19日:上午9:46:38
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.ItemGroup;
import com.cntest.fxpt.anlaysis.bean.ItemScore;
import com.cntest.fxpt.anlaysis.bean.Score;
import com.cntest.fxpt.anlaysis.bean.ScoreInfo;
import com.cntest.fxpt.anlaysis.bean.StudentSubjectScore;
import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;
import com.cntest.fxpt.anlaysis.bean.TargetResult;
import com.cntest.fxpt.anlaysis.bean.TargetResultContainer;
import com.cntest.fxpt.anlaysis.calculate.ICalcluator;
import com.cntest.fxpt.anlaysis.filter.IStudentCjFilter;
import com.cntest.fxpt.anlaysis.uitl.CalculateHelper;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.anlaysis.uitl.OrgProxy;
import com.cntest.fxpt.domain.AnalysisResultSaveToTable;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年5月19日 上午9:46:38
 * @version 1.0
 */
public class WuHouCalcluator implements ICalcluator {
	private SubjectScoreContainer beCalculateCJ;
	private CalculateTask event;
	private CalculateResult result;
	
	private final String ABILITYFSDTABLE = "abilityfsd";
	private final String KNOWLEDGECONTENTFSDTABLE = "knowledgecontentfsd";
	private final String TITLETYPEFSDTABLE = "titletypefsd";

	@Override
	public void calculate(CalculateTask event, CalculateResult cr,
			SubjectScoreContainer ssc) {
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
		IStudentCjFilter filter = event.getContext().getStatRankFilter(
				event.getAnalysisTestpaper());
		beCalculateCJ = event.getSubjectScoreContainer()
				.getSubjectScoreContainer(filter);
	}

	private void calculate() {
		TargetResultContainer zfTrc = calculateZF(beCalculateCJ, 1);
		TargetResultContainer kgZFTrc = calculateZF(beCalculateCJ, 2);
		TargetResultContainer zgZFTrc = calculateZF(beCalculateCJ, 3);
		if (hasItem()) {
			List<TargetResultContainer> kgItemResults = calculateItem(
					beCalculateCJ, false);
			List<TargetResultContainer> zgItemResults = calculateItem(
					beCalculateCJ, true);
			calculateItemGroup(beCalculateCJ);
			calculateReliability(kgZFTrc, kgItemResults);
			calculateReliability(zgZFTrc, zgItemResults);
			ArrayList<TargetResultContainer> itemResults = new ArrayList<>();
			itemResults.addAll(kgItemResults);
			itemResults.addAll(zgItemResults);
			calculateReliability(zfTrc, zgItemResults);
		}
	}

	private void calculateReliability(TargetResultContainer zfTrc,
			List<TargetResultContainer> itemResults) {

		int titleNum = itemResults.size();
		if (titleNum > 0) {
			double varianceSum = 0;
			for (TargetResultContainer trc : itemResults) {
				TargetResult tr = trc.get("stds");
				double std = tr.getValue();
				varianceSum += std * std;
			}

			double std = zfTrc.get("stds").getValue();
			double variance = std * std;
			double re = titleNum * (1 - varianceSum / variance)
					/ (titleNum - 1);
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

	private TargetResultContainer calculateZF(
			SubjectScoreContainer beCalculateCJ, int scoreType) {
		CalculateHelper ch = null;
		CalculateHelper preCh27 = null;
		CalculateHelper backCh27 = null;
		if (scoreType == 1) {
			ch = beCalculateCJ.getCalculateHelper();
			preCh27 = ch.getCalculateHelper(0.27);
			backCh27 = ch.getCalculateHelperForBack(0.27);
			saveSegment(ch);
		} else if (scoreType == 2) {
			ch = beCalculateCJ.getCalculateHelper();
			preCh27 = ch.getCalculateHelper(0.27).getSubjectScoreContainer()
					.getKGCalculateHelper();
			backCh27 = ch.getCalculateHelperForBack(0.27)
					.getSubjectScoreContainer().getKGCalculateHelper();
			ch = beCalculateCJ.getKGCalculateHelper();
		} else if (scoreType == 3) {
			ch = beCalculateCJ.getCalculateHelper();
			preCh27 = ch.getCalculateHelper(0.27).getSubjectScoreContainer()
					.getZGCalculateHelper();
			backCh27 = ch.getCalculateHelperForBack(0.27)
					.getSubjectScoreContainer().getZGCalculateHelper();
			ch = beCalculateCJ.getZGCalculateHelper();
		} else {
			return null;
		}
		TargetResultContainer trc = new TargetResultContainer();
		trc.setTableName(event.getZfResultTableName());

		trc.put("skrs", new TargetResult("skrs", ch.getTaskNum()));
		trc.put("avgs", new TargetResult("avgs", ch.getAvg()));
		trc.put("maxs", new TargetResult("maxs", ch.getMax()));
		trc.put("mins", new TargetResult("mins", ch.getMin()));
		trc.put("stds", new TargetResult("stds", ch.getStd()));
		trc.put("medians", new TargetResult("medians", ch.getMedians()));
		trc.put("modes", new TargetResult("modes", ch.getModes()));
		trc.put("skewness", new TargetResult("skewness", ch.getSkewness()));
		trc.put("kurtosis", new TargetResult("kurtosis", ch.getKurtosis()));
		trc.put("quartile25",
				new TargetResult("quartile25", ch.getQuartile(0.25)));
		trc.put("quartile50",
				new TargetResult("quartile50", ch.getQuartile(0.5)));
		trc.put("quartile75",
				new TargetResult("quartile75", ch.getQuartile(0.75)));

		trc.put("hskrs", new TargetResult("hskrs", preCh27.getTaskNum()));
		trc.put("havgs", new TargetResult("havgs", preCh27.getAvg()));

		trc.put("lskrs", new TargetResult("lskrs", backCh27.getTaskNum()));
		trc.put("lavgs", new TargetResult("lavgs", backCh27.getAvg()));

		setInfo(trc);
		trc.put("scoreType", new TargetResult("scoreType", scoreType));
		result.addResult(trc);

		return trc;

	}
	
	private String getSaveTableName(String type) {
		AnalysisResultSaveToTable table = event.getSaveTableContainer().get(
				type);
		String tableName = (table == null || !table.isAvailable()
				|| table.getTableName() == null || table.getTableName().equals(
				"")) ? null : table.getTableName();
		return tableName;
	}

	private void saveSegment(CalculateHelper ch) {
		List<ScoreInfo> scoreInfos = ch.getScoreInfos();
		int taskNum = ch.getTaskNum();
		for (ScoreInfo info : scoreInfos) {
			TargetResultContainer trc = new TargetResultContainer();
			trc.setTableName(event.getSegmentTableName());
			trc.put("num", new TargetResult("num", info.getNum()));
			trc.put("rank", new TargetResult("rank", info.getRank()));
			trc.put("score", new TargetResult("score", info.getScore()));
			trc.put("skrs", new TargetResult("skrs", taskNum));
			setInfo(trc);
			result.addResult(trc);
		}

	}
	
	private void saveSegmentKnowledge(CalculateHelper ch,String tablename,String itemGroupTypeName) {
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
			setInfoknowledge(trc,itemGroupTypeName);
			result.addResult(trc);
		}
	}
	
	private void setInfoknowledge(TargetResultContainer trc,String itemGroupTypeName) {
		Exam exam = event.getContext().getExam();
		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		Subject subject = atp.getSubject();

		trc.put("examId", new TargetResult("examId", exam.getId()));
		if (subject != null) {
			trc.put("subjectId", new TargetResult("subjectId", subject.getId()));
		}
		trc.put("analysisTestpaperId", new TargetResult("analysisTestpaperId", atp.getId()));
		trc.put("name", new TargetResult("name", itemGroupTypeName));
		
		Container<String, Object> studentRangeValues = event
				.getStudentRangeValues();
		trc.put("wl", new TargetResult("wl", studentRangeValues.get("wl")));
	}

	private void setInfo(TargetResultContainer trc) {
		OrgProxy op = new OrgProxy(event.getObj());
		Exam exam = event.getContext().getExam();
		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		Subject subject = atp.getSubject();

		trc.put("examId", new TargetResult("examId", exam.getId()));
		trc.put("objId", new TargetResult("objId", op.getId()));
		trc.put("objType", new TargetResult("objType", op.getLevel()));
		if (subject != null) {
			trc.put("subjectId", new TargetResult("subjectId", subject.getId()));
		}
		trc.put("testpaperId", new TargetResult("testpaperId", atp.getId()));

		Container<String, Object> studentRangeValues = event
				.getStudentRangeValues();
		for (String rangeName : studentRangeValues.toListForKey()) {
			Object rangeValue = studentRangeValues.get(rangeName);
			trc.put(rangeName, new TargetResult(rangeName, rangeValue));
		}
	}

	private void calculateItemGroup(SubjectScoreContainer beCalculateCJ) {
		calculateItemGroup("ability", beCalculateCJ);
		calculateItemGroup("knowledge", beCalculateCJ);
		calculateItemGroup("knowledgeContent", beCalculateCJ);
		calculateItemGroup("titleType", beCalculateCJ);
	}

	private void calculateItemGroup(String itemGroupTypeName,
			SubjectScoreContainer beCalculateCJ) {

		CalculateHelper ch = beCalculateCJ.getCalculateHelper();
		SubjectScoreContainer hSSContainer = ch.getCalculateHelper(0.27)
				.getSubjectScoreContainer();
		SubjectScoreContainer lSSContainer = ch.getCalculateHelperForBack(0.27)
				.getSubjectScoreContainer();

		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		List<ItemGroup> itemGroups = ItemGroup.createItemGroupsWithItemAttr(
				itemGroupTypeName, atp.getItems(),event.getContext().isHasChoice());

		for (ItemGroup itemGroup : itemGroups) {
			ch = beCalculateCJ.getItemGroupCalculateHelper(itemGroup,false);

			if("3".equals(event.getStudentRangeValues().get("objType").toString())){
				if("ability".equals(itemGroupTypeName)){
					saveSegmentKnowledge(ch, ABILITYFSDTABLE,itemGroup.getName());
				}else if("knowledgeContent".equals(itemGroupTypeName)){
					saveSegmentKnowledge(ch, KNOWLEDGECONTENTFSDTABLE,itemGroup.getName());
				}else if("titleType".equals(itemGroupTypeName)){
					saveSegmentKnowledge(ch, TITLETYPEFSDTABLE,itemGroup.getName());
				}
			}
			
			TargetResultContainer trc = new TargetResultContainer();
			trc.setTableName(event.getItemGroupTableName());

			saveItemAndItemGroupTarget(trc, ch);

			ch = hSSContainer.getItemGroupCalculateHelper(itemGroup,false);
			saveItemAndItemGroupTargetForHL(trc, ch, "h");

			ch = lSSContainer.getItemGroupCalculateHelper(itemGroup,false);
			saveItemAndItemGroupTargetForHL(trc, ch, "l");

			setItemGroupInfo(trc, itemGroup, itemGroupTypeName);
			setInfo(trc);

			result.addResult(trc);
		}

	}

	private void setItemGroupInfo(TargetResultContainer trc,
			ItemGroup itemGroup, String itemGroupTypeName) {
		trc.put("name", new TargetResult("name", itemGroup.getName()));
		trc.put("fullScore",
				new TargetResult("fullScore", itemGroup.getFullScore()));
		trc.put("itemNos",
				new TargetResult("itemNos", itemGroup.getItemNoToString()));
		trc.put("itemNum", new TargetResult("itemNum", itemGroup.getItemNum()));
		trc.put("itemGroupType", new TargetResult("itemGroupType",
				itemGroupTypeName));
	}

	private List<TargetResultContainer> calculateItem(
			SubjectScoreContainer beCalculateCJ, boolean iszg) {
		CalculateHelper ch = beCalculateCJ.getCalculateHelper();
		SubjectScoreContainer hSSContainer = ch.getCalculateHelper(0.27)
				.getSubjectScoreContainer();
		SubjectScoreContainer lSSContainer = ch.getCalculateHelperForBack(0.27)
				.getSubjectScoreContainer();

		ArrayList<TargetResultContainer> itemResults = new ArrayList<>();
		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		List<Item> items = atp.getItems();
		
		String selOptions = atp.getTestPaper().getSelOptions();
		if(StringUtils.isEmpty(selOptions))
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
			trc.setTableName(event.getItemResultTableName());

			saveItemAndItemGroupTarget(trc, ch);
			statSelOptionAndSave(trc, item, "", beCalculateCJ, jsonOptions);

			ch = hSSContainer.getItemCalculateHelper(item);
			saveItemAndItemGroupTargetForHL(trc, ch, "h");
			statSelOptionAndSave(trc, item, "h", hSSContainer, jsonOptions);

			ch = lSSContainer.getItemCalculateHelper(item);
			saveItemAndItemGroupTargetForHL(trc, ch, "l");
			statSelOptionAndSave(trc, item, "l", lSSContainer, jsonOptions);

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

	private void saveItemAndItemGroupTarget(TargetResultContainer trc,
			CalculateHelper ch) {
		trc.put("skrs", new TargetResult("skrs", ch.getTaskNum()));
		trc.put("avgs", new TargetResult("avgs", ch.getAvg()));
		trc.put("maxs", new TargetResult("maxs", ch.getMax()));
		trc.put("mins", new TargetResult("mins", ch.getMin()));
		trc.put("stds", new TargetResult("stds", ch.getStd()));
	}

	private void saveItemAndItemGroupTargetForHL(TargetResultContainer trc,
			CalculateHelper ch, String hlType) {
		trc.put(hlType + "skrs",
				new TargetResult(hlType + "skrs", ch.getTaskNum()));
		trc.put(hlType + "avgs", new TargetResult(hlType + "avgs", ch.getAvg()));
	}

	private void statSelOptionAndSave(TargetResultContainer trc, Item item,
			String hlType, SubjectScoreContainer ssc, JSONArray jsonOptions) {
		if (item.getOptionType() == 1) {
			calculateSingleOption(item, ssc, trc, hlType, jsonOptions);
		} else if (item.getOptionType() == 2) {
			calcualteMultiOption(item, ssc, trc, hlType);
		}
	}

	/*private void calculateSingleOption(Item item, SubjectScoreContainer ssc,
			TargetResultContainer targetResults, String hlType) {
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		int o = 0;
		for (StudentSubjectScore sss : ssc.toList()) {
			ItemScore itemScore = sss.getItemScore(item);
			String selOption = itemScore.getSelOption();
			if (selOption.contains("A")) {
				a++;
			} else if (selOption.contains("B")) {
				b++;
			} else if (selOption.contains("C")) {
				c++;
			} else if (selOption.contains("D")) {
				d++;
			} else {
				o++;
			}
		}

		targetResults
				.put(hlType + "ANum", new TargetResult(hlType + "ANum", a));
		targetResults
				.put(hlType + "BNum", new TargetResult(hlType + "BNum", b));
		targetResults
				.put(hlType + "CNum", new TargetResult(hlType + "CNum", c));
		targetResults
				.put(hlType + "DNum", new TargetResult(hlType + "DNum", d));
		targetResults.put(hlType + "OtherNum", new TargetResult(hlType
				+ "OtherNum", o));
	}*/
	
	private void calculateSingleOption(Item item, SubjectScoreContainer ssc,
			TargetResultContainer targetResults, String hlType, JSONArray jsonOptions) {
		
		Collection<String> collection = JSONArray.toCollection(jsonOptions);
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		for (String key : collection) {
			map.put(key, 0);
		}
		
		for (StudentSubjectScore sss : ssc.toList()) {
			ItemScore itemScore = sss.getItemScore(item);
			String selOption = itemScore.getSelOption();
			if(selOption!=null && map.containsKey(selOption)){
				Integer val = map.get(selOption);
				map.put(selOption, ++val);
			}
		}
		
		int i = 0, o=0;
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
				o +=  entry.getValue();
				break;
			}
		}
		targetResults.put(hlType + "OtherNum", new TargetResult(hlType + "OtherNum", o));
		
		JSONArray array = JSONArray.fromObject(map);
		targetResults.put(hlType + "Nums", new TargetResult(hlType + "Nums", array.toString()));
	}
	

	private void calcualteMultiOption(Item item, SubjectScoreContainer ssc,
			TargetResultContainer targetResults, String hlType) {
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

		targetResults.put(hlType + "erro", new TargetResult(hlType + "erro",
				erro));
		targetResults.put(hlType + "halfRight", new TargetResult(hlType
				+ "halfRight", halfRight));
		targetResults.put(hlType + "allRight", new TargetResult(hlType
				+ "allRight", allRight));
	}
}
