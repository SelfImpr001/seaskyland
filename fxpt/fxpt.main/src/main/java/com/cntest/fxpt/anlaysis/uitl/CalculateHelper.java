/*
 * @(#)com.hyt.cntest.n.helper.CalculateHelper.java	1.0 2014年11月13日:上午9:48:31
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import com.cntest.fxpt.anlaysis.bean.*;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.UplineScore;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月13日 上午9:48:31
 * @version 1.0
 */
public class CalculateHelper {
	private int taskNum = 0;// 实考人数
	private int choiceNum = 0;//选择人数
	private double standardDeviation = 0; // 标准差
	private double scoreSum = 0;
	private double kurtosis = 0;// 峰度
	private double skewness = 0;// 偏度
	private GroupScore scoreGroup;
	private boolean onepass;
	private boolean allpass;
	private int ddrs = 0;
	private int sdrs = 0;
	private int zdrs = 0;



	public CalculateHelper(GroupScore scoreGroup) {
		this.scoreGroup = scoreGroup;
		init();
	}

	public GroupScore getGroupScore() {
		return scoreGroup;
	}

	public List<ScoreInfo> getScoreInfos() {
		return scoreGroup.getScoreInfos();
	}

	public CalculateHelper getCalculateHelper(double quartile) {
		List<ScoreInfo> scoreInfos = scoreGroup
				.getScoreInfos(taskNum, quartile);
		Score minScore = ScoreObjMgr.newInstance().getScore(Double.MAX_VALUE);
		if (scoreInfos.size() > 0) {
			minScore = scoreInfos.get(scoreInfos.size() - 1).getScore();
		}
		GroupScore scoreGroup = new GroupScore(getFullScore(), scoreInfos);
		scoreGroup.setSubjectScoreContainer(this.scoreGroup
				.getGEminScoreSubjectScoreContainer(minScore));
		CalculateHelper ch = new CalculateHelper(scoreGroup);
		return ch;
	}

	public CalculateHelper getCalculateHelperForBack(double quartile) {
		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfosForBack(taskNum,
				quartile);
		Score maxScore = ScoreObjMgr.newInstance().getScore(
				Double.NEGATIVE_INFINITY);
		if (scoreInfos.size() > 0) {
			maxScore = scoreInfos.get(scoreInfos.size() - 1).getScore();
		}
		GroupScore scoreGroup = new GroupScore(getFullScore(), scoreInfos);
		scoreGroup.setSubjectScoreContainer(this.scoreGroup
				.getLEMaxScoreSubjectScoreContainer(maxScore));
		CalculateHelper ch = new CalculateHelper(scoreGroup);
		return ch;
	}


	public CalculateHelper getCalculateHelperForTopAndDown(int upOrDown, List<UplineScore> paramuu) { //upOrDown  true > up
		SubjectScoreContainer subjectScoreContainer = new SubjectScoreContainer();
		subjectScoreContainer.setAnalysisTestpaper(getSubjectScoreContainer().getAnalysisTestpaper());
		int wl = getSubjectScoreContainer().getAnalysisTestpaper().getPaperType();
		List<UplineScore> us = paramuu.stream().filter(u -> u.getLevel()==upOrDown).limit(1)
				.filter(u -> u.getSubject().getId()==getSubjectScoreContainer().getAnalysisTestpaper().getSubjectId())
				.filter(u -> u.getWlType()==(wl==0?u.getWlType():wl))
				.collect(Collectors.toList());

		double levelScore = us.size()==0|us==null?20:us.get(0).getDivideScore();
		ArrayList<StudentSubjectScore> tmpScores = scoreGroup.getSubjectScoreContainer().getScores();
		for(StudentSubjectScore studentSubjectScore:tmpScores){
			if(studentSubjectScore.getZfScore()!=null){
				if(upOrDown==1){
					if(studentSubjectScore.getZfScore().getValue()>=levelScore) {
						subjectScoreContainer.add(studentSubjectScore);
					}
				}else{
					if(studentSubjectScore.getZfScore().getValue()< levelScore)
						subjectScoreContainer.add(studentSubjectScore);
				}
			}
		}
		return subjectScoreContainer.getCalculateHelper();
	}


	public SubjectScoreContainer getSubjectScoreContainer() {
		return scoreGroup.getSubjectScoreContainer();
	}

	private void init() {
		// 如果没有数据就不计算了
		if (scoreGroup.isEmpty()) {
			return;
		}
		scoreGroup.sort();

		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfos();

		double sum = 0;
		double sum2 = 0;
		for (ScoreInfo si : scoreInfos) {
			int num = si.getNum();
			double score = si.getScore().getValue();
			taskNum += num;
			if(score == -888.0){
				continue;
			}
			scoreSum += score * num;
			sum2 += score * score * num;
			choiceNum += num;
		}

		standardDeviation = Math.sqrt((choiceNum * sum2 - scoreSum * scoreSum)
				/ (choiceNum*1.0* choiceNum-choiceNum));
		calculateKurtosisAndSkewness();

	}

	private void calculateKurtosisAndSkewness() {
		double avg = getAvg();
		double sum3 = 0;
		double sum4 = 0;
		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfos();
		for (ScoreInfo si : scoreInfos) {
			int num = si.getNum();
			double score = si.getScore().getValue();
			double dd = (score - avg) / standardDeviation;
			dd = Double.isNaN(dd) ? 0 : dd;
			sum3 += Math.pow(dd, 3) * num;
			sum4 += Math.pow(dd, 4) * num;
		}
		kurtosis = sum4 / taskNum - 3;// 峰度
		skewness = sum3 / taskNum;// 偏度
	}

	/******************** 指标 **********************/
	public int getTaskNum() {
		return taskNum;
	}
	
	public int getChoiceNum() {
		return choiceNum;
	}

	public double getStd() {
		Double result = standardDeviation;
		return toDoubleValue(result);
	}

	public double getAvg() {
		Double result = scoreSum / choiceNum;
		return toDoubleValue(result);
	}
	
	public double getOnepassNum() {
		int onepassnum = 0;
		SubjectScoreContainer onepassData = scoreGroup.getSubjectScoreContainer();
		List<StudentSubjectScore> sssList = onepassData.toList();
		for (StudentSubjectScore studentSubjectScore : sssList) {
			if(studentSubjectScore.isOncePass()){
				onepassnum++;
			}
		}
		return onepassnum;
	}
	
	public int getAllpassNum() {
		int allpassnum = 0;
		SubjectScoreContainer allpassData = scoreGroup.getSubjectScoreContainer();
		List<StudentSubjectScore> sssList = allpassData.toList();
		for (StudentSubjectScore studentSubjectScore : sssList) {
			if(studentSubjectScore.isAllPass()){
				allpassnum++;
			}
		}
		return allpassnum;
	}

	public int getddrs(int level) {
		int ddrs = 0;
		SubjectScoreContainer ddData = scoreGroup.getSubjectScoreContainer();
		List<StudentSubjectScore> ddList = ddData.toList();
		for (StudentSubjectScore studentSubjectScore : ddList) {
			if(studentSubjectScore.getDd().get(level)!=null){
				if(studentSubjectScore.getDd().get(level)==1){
					ddrs++;
				}
			}
		}
		return ddrs;
	}

	public int getsdrs(int level) {
		int sdrs = 0;
		SubjectScoreContainer sdData = scoreGroup.getSubjectScoreContainer();
		List<StudentSubjectScore> sdList = sdData.toList();
		for (StudentSubjectScore studentSubjectScore : sdList) {
			if(studentSubjectScore.getSd().get(level)!=null){
				if(studentSubjectScore.getSd().get(level)==1){
					sdrs++;
				}
			}
		}
		return sdrs;
	}

	public int getzdrs(int level) {
		int zdrs = 0;
		SubjectScoreContainer zdData = scoreGroup.getSubjectScoreContainer();
		List<StudentSubjectScore> zdList = zdData.toList();
		for (StudentSubjectScore studentSubjectScore : zdList) {
			if(studentSubjectScore.getZd().get(level)!=null){
				if(studentSubjectScore.getZd().get(level)==1){
					zdrs++;
				}
			}
		}
		return zdrs;
	}

	// 中位数
	public double getMedians() {
		Double result = getQuartile(0.5);
		return toDoubleValue(result);
	}

	public String getModes() {
		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfos();
		StringBuffer result = new StringBuffer();
		int num = 0;
		for (ScoreInfo si : scoreInfos) {
			int tmpNum = si.getNum();
			if (tmpNum == num) {
				result.append(NumberFormat.clearZero(si.getScore().toString())
						+ ",");
			} else if (tmpNum > num) {
				result.setLength(0);
				result.append(NumberFormat.clearZero(si.getScore().toString())
						+ ",");
				num = tmpNum;
			}

			if (result.length() > 90) {
				break;
			}
		}

		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		}
		return result.toString();
	}

	public double getMax() {
		Double result = 0d;
		if (!scoreGroup.isEmpty()) {
			result = scoreGroup.getScoreInfos().get(0).getScore().getValue();
		}
		return toDoubleValue(result==-888.0?0:result);
	}
	//取分数的最小值（非“0”）
	public double getMinsExceptZero() {
		Double result = 0d;
		int sizes =scoreGroup.size();
		if (!scoreGroup.isEmpty()) {
			for(int i=1;i<=sizes;i++) {
				result = scoreGroup.getScoreInfos().get(sizes - i)
						.getScore().getValue();
				if(result==0) {
					continue;
				}else {
					break;
				}
			}
		}
		return toDoubleValue(result);
	}
	public double getMin() {
		Double result = 0d;
		try {
			if (!scoreGroup.isEmpty()) {
				result = scoreGroup.getScoreInfos().get(scoreGroup.size() - 1)
						.getScore().getValue();
				if(result==-888.0&&scoreGroup.size()!=1){
					result = scoreGroup.getScoreInfos().get(scoreGroup.size() - 2)
							.getScore().getValue();
				}else if(result==-888.0&&scoreGroup.size()==1){
					result = 0d;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("scoreGroup.getScoreInfos().size():" + scoreGroup.getScoreInfos().size());
			System.out.println("scoreGroup.size() - 2:"+(scoreGroup.size() - 2));
		}
		return toDoubleValue(result);
	}

	public double getDD() {
		// 区分度
		final double criticalValue = 0.27;
		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfos();
		int num = 1;
		double sumScore = 0;
		for (ScoreInfo si : scoreInfos) {
			if (getPercent(num) <= criticalValue) {
				num += si.getNum();
				sumScore += si.getNum() * si.getScore().getValue();
			} else {
				break;
			}
		}
		double avg1 = sumScore / num;

		num = 1;
		sumScore = 0;
		int size = scoreInfos.size();
		for (int i = size - 1; i >= 0; i--) {
			ScoreInfo si = scoreInfos.get(i);
			if (getPercent(num) <= criticalValue) {
				num += si.getNum();
				sumScore += si.getNum() * si.getScore().getValue();
			} else {
				break;
			}
		}
		double avg2 = sumScore / num;
		Double result = (avg1 - avg2) / scoreGroup.getFullScore();
		return toDoubleValue(result);
	}

	public double getFullScore() {
		return scoreGroup.getFullScore();
	}

	public double getVariance() {
		// 方差
		return toDoubleValue(Math.pow(standardDeviation, 2));
	}

	public double getKurtosis() {
		// 峰度
		return toDoubleValue(kurtosis);
	}

	public double getSkewness() {
		// 偏度
		return toDoubleValue(skewness);
	}

	public double calculateReliability(SubjectScoreContainer scoreC) {
		// 信度
		return toDoubleValue(calculateReliability(scoreC, 0));
	}

	public double calculateKGReliability(SubjectScoreContainer scoreC) {
		// 客观信度
		return toDoubleValue(calculateReliability(scoreC, 1));
	}

	public double calculateZGReliability(SubjectScoreContainer scoreC) {
		// 主观题信度
		return toDoubleValue(calculateReliability(scoreC, 2));
	}

	public List<ScoreSegment> getSegment(double step) {
		return getSegment(0, scoreGroup.getFullScore(), step);
	}

	public List<ScoreSegment> getSegment(double begin, double end, double step) {
		StatSegment cs = new StatSegment(begin, end, step);
		cs.setScoreGroup(scoreGroup);
		return cs.getScoreSegment();
	}

	/********** 题目专用的 **************/
	public static double calculateKGDD(AnalysisTestpaper atp,
			SubjectScoreContainer scoreC) {
		if (atp.getKgScore() == 0) {
			return 0;
		}
		return toDoubleValue(calculateDD(atp, atp.getKgScore(), scoreC, true));
	}

	public static double calculateZGDD(AnalysisTestpaper atp,
			SubjectScoreContainer scoreC) {
		if (atp.getZgScore() == 0) {
			return 0;
		}
		return toDoubleValue(calculateDD(atp, atp.getZgScore(), scoreC, false));
	}

	public static double calculateItemDD(Item item, SubjectScoreContainer scoreC) {
		return toDoubleValue(calculateDD(item, item.getFullScore(), scoreC,
				false));
	}

	public static double calculateSdDD(ItemGroup itemGroup,
			SubjectScoreContainer scoreC) {
		return toDoubleValue(calculateDD(itemGroup, itemGroup.getFullScore(),
				scoreC, false));
	}

	private static double calculateDD(Object obj, double fullScore,
			SubjectScoreContainer scoreC, boolean isKg) {
		if (scoreC.isEmpty()) {
			return 0;
		}

		int personNum = scoreC.size();
		final double criticalValue = 0.27;

		List<StudentSubjectScore> scores = scoreC.toList();
		double score = 0;
		double sumScore = 0;
		int sumNum = 0;
		for (int i = 0; i < personNum; i++) {
			StudentSubjectScore cj = scores.get(i);
			double tmpScore = cj.getScore().getValue();
			if (1.0 * sumNum / personNum <= criticalValue) {
				sumScore += getCjScore(obj, cj, isKg);
				sumNum++;
				score = tmpScore;
			} else if (score == tmpScore) {
				sumScore += getCjScore(obj, cj, isKg);
				sumNum++;
			} else {
				break;
			}
		}
		double avg1 = sumScore / sumNum;

		score = 0;
		sumScore = 0;
		sumNum = 0;
		for (int i = personNum - 1; i >= 0; i--) {
			StudentSubjectScore cj = scores.get(i);
			double tmpScore = cj.getScore().getValue();
			if (1.0 * sumNum / personNum <= criticalValue) {
				sumScore += getCjScore(obj, cj, isKg);
				sumNum++;
				score = tmpScore;
			} else if (score == tmpScore) {
				sumScore += getCjScore(obj, cj, isKg);
				sumNum++;
			} else {
				break;
			}
		}
		double avg2 = sumScore / sumNum;

		return toDoubleValue((avg1 - avg2) / fullScore);
	}

	private static double getCjScore(Object obj, StudentSubjectScore cj,
			boolean isKg) {
		double result = 0;
		if (obj instanceof AnalysisTestpaper) {
			if (isKg) {
				result = cj.getKgScore().getValue();
			} else {
				result = cj.getZgScore().getValue();
			}
		} else if (obj instanceof Item) {
			Item item = (Item) obj;
			result = cj.getItemScore(item).getScore().getValue();
		} else if (obj instanceof ItemGroup) {
			ItemGroup itemGroup = (ItemGroup) obj;
			result = cj.getItemGroupScore(itemGroup).getValue();
		}
		return toDoubleValue(result);
	}

	/*************************/

	private double calculateReliability(SubjectScoreContainer scoreC,
			int statType) {
		List<Item> items = scoreC.getAnalysisTestpaper().getItems();
		int itemSize = 0;
		double varianceSum = 0;
		int choicecount = 0;
		int i  = 1;//只支持多选一
		for (Item item : items) {
			boolean isStat = false;
			
			if (statType == 1 && item.getOptionType() != 0) {
//				if(item.isChoice()){
//					String choicenum = item.getChoiceNumber();
//					choicecount = Integer.parseInt(choicenum.split("\\|")[1]);//题组
//					if(choicecount == i){
//						itemSize++;
//					}else{
//						break;
//					}
//					i = choicecount;
//				}else{
					itemSize++;
					isStat = true;
//				}
			} else if (statType == 2 && item.getOptionType() == 0) {
//				if(item.isChoice()){
//					String choicenum = item.getChoiceNumber();
//					choicecount = Integer.parseInt(choicenum.split("\\|")[1]);//题组
//					if(choicecount == i){
//						itemSize++;
//					}else{
//						break;
//					}
//					i = choicecount;
//				}else{
					itemSize++;
					isStat = true;
//				}
			} else if (statType == 0) {
//				if(item.isChoice()){
//					String choicenum = item.getChoiceNumber();
//					choicecount = Integer.parseInt(choicenum.split("\\|")[1]);//题组
//					if(choicecount == i){
//						itemSize++;
//					}else{
//						break;
//					}
//					i = choicecount;
//				}else{
					itemSize++;
					isStat = true;
//				}
			}
			
			if (isStat) {
				GroupScore itemGroupScore = scoreC.getItemGroupScore(item);
				CalculateHelper ch = scoreC.getItemCalculateHelper(item);
				varianceSum += Math.pow(ch.getStd(), 2);
			}
		}

		double variance = Math.pow(getStd(), 2);
		double re = itemSize * (1 - varianceSum / variance) / (itemSize - 1);
		re = Double.isNaN(re) ? 0 : re;
		re = Double.isInfinite(re) ? 0 : re;

		return toDoubleValue(re);
	}

	private double getPercent(int num) {
		return toDoubleValue(num * 1.0 / taskNum);
	}

	public double getQuartile(double quartile) {
		if (quartile > 1) {
			quartile = quartile / 100;
		}

		double option = taskNum * quartile;
		int option1 = NumberTool.floor(option);
		int option2 = NumberTool.ceil(option);
		boolean isEquals = false;
		if (option2 == option1) {
			option2++;
			isEquals = true;
		}

		double score1 = 0;
		double score2 = 0;
		boolean findScore1 = false;
		boolean findScore2 = false;
		int sumNum = 0;
		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfos();
		for (ScoreInfo si : scoreInfos) {
			sumNum += si.getNum();
			if (sumNum >= option1) {
				score1 = si.getScore().getValue();
				findScore1 = true;
			}
			if (sumNum >= option2) {
				score2 = si.getScore().getValue();
				findScore2 = true;
			}

			if (findScore2 && findScore1) {
				break;
			}
		}

		if (isEquals) {
			return (score1 + score2) / 2;
		} else {
			return score2;
		}
	}

	private static double toDoubleValue(double d) {
		Double result = d;
		return result.isNaN() || result.isInfinite() ? 0 : result;
	}

	public int getGEScoreNum(double score) {
		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfos();
		int sumNum = 0;
		for (ScoreInfo si : scoreInfos) {
			if (si.getScore().getValue() >= score) {
				sumNum += si.getNum();
			}
		}
		return sumNum;
	}

	public double getCorrelation(ConcurrentHashMap<String, Map<Long, Double>> cordatasets,String person1, String person2) {
		List<Long> list = new ArrayList<>();
		for (Map.Entry<Long, Double> p1 : cordatasets.get(person1).entrySet()) {
			if (cordatasets.get(person2).containsKey(p1.getKey())) {
				list.add(p1.getKey());
			}
		}

		double sumX = 0.0;
		double sumY = 0.0;
		double sumX_Sq = 0.0;
		double sumY_Sq = 0.0;
		double sumXY = 0.0;
		int N = list.size();

		for (Long name : list) {
			Map<Long, Double> p1Map = cordatasets.get(person1);
			Map<Long, Double> p2Map = cordatasets.get(person2);

			sumX += p1Map.get(name);
			sumY += p2Map.get(name);
			sumX_Sq += Math.pow(p1Map.get(name), 2);
			sumY_Sq += Math.pow(p2Map.get(name), 2);
			sumXY += p1Map.get(name) * p2Map.get(name);
		}

		double numerator = sumXY - sumX * sumY / N;
		double denominator = Math.sqrt((sumX_Sq - sumX * sumX / N)
				* (sumY_Sq - sumY * sumY / N));

		if (denominator == 0) {
			return 0;
		}
		return numerator / denominator;
	}

	public double getRankScore(int rank) {
		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfos();
		double result = 0;
		if(scoreInfos!=null && scoreInfos.size()!=0){
			sort(scoreInfos);
			Score score = scoreInfos.get(scoreInfos.size()-1).getScore();
			if(score!=null){
				result = score.getValue();
			}
		}
		int sumNum = 0;
		for (ScoreInfo si : scoreInfos) {
			sumNum += si.getNum();
			if (sumNum >= rank) {
				result = si.getScore().getValue();
				break;
			}
		}
		return result;
	}

	public void sort(List<ScoreInfo> scoreInfos) {
		Collections.sort(scoreInfos, new Comparator<ScoreInfo>() {
			@Override
			public int compare(ScoreInfo e1, ScoreInfo e2) {
				Double value1 = e1.getScore().getValue();
				Double value2 = e2.getScore().getValue();
				return -value1.compareTo(value2);
			}
		});
	}

	public double getBackRankScore(int rank) {
		List<ScoreInfo> scoreInfos = scoreGroup.getScoreInfos();
		double result = Double.MAX_VALUE;
		int sumNum = 0;
		int size = scoreInfos.size();
		for (int i = size - 1; i >= 0; i--) {
			ScoreInfo si = scoreInfos.get(i);
			sumNum += si.getNum();
			if (sumNum >= rank) {
				result = si.getScore().getValue();
				break;
			}
		}
		return result;
	}

}
