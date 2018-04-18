/*
 * @(#)com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer.java	1.0 2014年11月25日:上午11:23:58
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import com.cntest.fxpt.anlaysis.event.handler.DefaultCalculateHandler;
import com.cntest.fxpt.anlaysis.filter.IStudentCjFilter;
import com.cntest.fxpt.anlaysis.uitl.CalculateHelper;
import com.cntest.fxpt.anlaysis.uitl.NumberTool;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 上午11:23:58
 * @version 1.0
 */
public class SubjectScoreContainer {
	private static final Logger log = LoggerFactory
			.getLogger(DefaultCalculateHandler.class);
	private ArrayList<StudentSubjectScore> scores = new ArrayList<>();
	private AnalysisTestpaper analysisTestpaper;

	public AnalysisTestpaper getAnalysisTestpaper() {
		return analysisTestpaper;
	}

	public SubjectScoreContainer setAnalysisTestpaper(
			AnalysisTestpaper analysisTestpaper) {
		this.analysisTestpaper = analysisTestpaper;
		return this;
	}

	public void add(StudentSubjectScore score) {
		scores.add(score);
	}

	public SubjectScoreContainer[] getSubjectScoreContainer() {
		sort();
		int scoreSize = scores.size();
		int p27 = NumberTool.toInt(scoreSize * 0.27);
		p27 = p27 == 0 ? 1 : p27;
		int idx = 0;
		double upScore = 0;
		SubjectScoreContainer hssc = new SubjectScoreContainer();
		hssc.setAnalysisTestpaper(analysisTestpaper);
		for (StudentSubjectScore cj : scores) {
			idx++;
			if (idx > p27 && upScore != cj.getScore().getValue()) {
				break;
			}
			hssc.add(cj);
		}

		idx = 0;
		upScore = 0;
		SubjectScoreContainer lssc = new SubjectScoreContainer();
		lssc.setAnalysisTestpaper(analysisTestpaper);
		for (int i = scoreSize - 1; i >= 0; i--) {
			idx++;
			StudentSubjectScore cj = scores.get(i);
			if (idx > p27 && upScore != cj.getScore().getValue()) {
				break;
			}
			lssc.add(cj);
		}

		return new SubjectScoreContainer[] { hssc, lssc };
	}

	/**********************************************/
	public CalculateHelper getCalculateHelper() {
		CalculateHelper ch = new CalculateHelper(getGroupScore());
		return ch;
	}

	public GroupScore getGroupScore() {
		HashMap<Double, ScoreInfo> scoreInfoMap = new HashMap<>();
		for (StudentSubjectScore sss : scores) {
			Score score = sss.getScore();
			ScoreInfo si = scoreInfoMap.get(score.getValue());
			if (si == null) {
				si = new ScoreInfo();
				si.setScore(score);
				scoreInfoMap.put(score.getValue(), si);
			}
			si.setNum(si.getNum() + 1);
		}
		Double fullScore = analysisTestpaper.getFullScore();
		GroupScore groupScore = new GroupScore(fullScore, new ArrayList<>(
				scoreInfoMap.values()));
		groupScore.setSubjectScoreContainer(this);
		return groupScore;
	}

	public CalculateHelper getKGCalculateHelper() {
		CalculateHelper ch = new CalculateHelper(getKGGroupScore());
		return ch;
	}

	public GroupScore getKGGroupScore() {
		HashMap<Double, ScoreInfo> scoreInfoMap = new HashMap<Double, ScoreInfo>();
		for (StudentSubjectScore sss : scores) {
			Score score = sss.getKgScore();
			ScoreInfo si = scoreInfoMap.get(score.getValue());
			if (si == null) {
				si = new ScoreInfo();
				si.setScore(score);
				scoreInfoMap.put(score.getValue(), si);
			}
			si.setNum(si.getNum() + 1);
		}
		Double fullScore = analysisTestpaper.getKgScore();
		GroupScore groupScore = new GroupScore(fullScore,
				new ArrayList<ScoreInfo>(scoreInfoMap.values()));
		groupScore.setSubjectScoreContainer(this);
		return groupScore;
	}

	public CalculateHelper getZGCalculateHelper() {
		CalculateHelper ch = new CalculateHelper(getZGGroupScore());
		return ch;
	}

	public GroupScore getZGGroupScore() {
		HashMap<Double, ScoreInfo> scoreInfoMap = new HashMap<Double, ScoreInfo>();
		for (StudentSubjectScore sss : scores) {
			Score score = sss.getZgScore();
			ScoreInfo si = scoreInfoMap.get(score.getValue());
			if (si == null) {
				si = new ScoreInfo();
				si.setScore(score);
				scoreInfoMap.put(score.getValue(), si);
			}
			si.setNum(si.getNum() + 1);
		}
		Double fullScore = analysisTestpaper.getZgScore();
		GroupScore groupScore = new GroupScore(fullScore,
				new ArrayList<ScoreInfo>(scoreInfoMap.values()));
		groupScore.setSubjectScoreContainer(this);
		return groupScore;
	}

	public CalculateHelper getItemGroupCalculateHelper(ItemGroup itemGroup,boolean ischoice) {
		CalculateHelper ch = null;
		if(ischoice){
			ch = new CalculateHelper(
					getItemGroupGroupScoreChoice(itemGroup));
		}else{
			ch = new CalculateHelper(
					getItemGroupGroupScore(itemGroup));
		}
		return ch;
	}

	public GroupScore getItemGroupGroupScore(ItemGroup itemGroup) {
		HashMap<Double, ScoreInfo> scoreInfoMap = new HashMap<Double, ScoreInfo>();
		for (StudentSubjectScore sss : scores) {
			Score score = sss.getItemGroupScore(itemGroup);
			if(itemGroup.getName().equals("主观题")){
				score = sss.getZgScore();
			}
			ScoreInfo si = scoreInfoMap.get(score.getValue());
			if (si == null) {
				si = new ScoreInfo();
				si.setScore(score);
				scoreInfoMap.put(score.getValue(), si);
			}
			si.setNum(si.getNum() + 1);
		}

		Double fullScore = itemGroup.getFullScore();
		GroupScore groupScore = new GroupScore(fullScore,
				new ArrayList<ScoreInfo>(scoreInfoMap.values()));
		groupScore.setSubjectScoreContainer(this);
		return groupScore;
	}

	public HashMap<Long, Double> getItemGroupGroupMapScore(ItemGroup itemGroup) {
		HashMap<Long, Double> scoreInfoMap = new HashMap<>();
		for (StudentSubjectScore sss : scores) {
			Score score = sss.getItemGroupScore(itemGroup);
			scoreInfoMap.put(sss.getStudentCj().getPk(),score.getValue());
		}
		return scoreInfoMap;
	}
	
	public GroupScore getItemGroupGroupScoreChoice(ItemGroup itemGroup) {
		HashMap<Double, ScoreInfo> scoreInfoMap = new HashMap<Double, ScoreInfo>();
		for (StudentSubjectScore sss : scores) {
			Score score = sss.getItemGroupScoreChoice(itemGroup);
			ScoreInfo si = scoreInfoMap.get(score.getValue());
			if (si == null) {
				si = new ScoreInfo();
				si.setScore(score);
				scoreInfoMap.put(score.getValue(), si);
			}
			si.setNum(si.getNum() + 1);
		}

		Double fullScore = itemGroup.getFullScoreChoice();
		GroupScore groupScore = new GroupScore(fullScore,
				new ArrayList<ScoreInfo>(scoreInfoMap.values()));
		groupScore.setSubjectScoreContainer(this);
		return groupScore;
	}

	public CalculateHelper getItemCalculateHelper(Item item) {
		CalculateHelper ch = new CalculateHelper(getItemGroupScore(item));
		return ch;
	}

	public CalculateHelper getItemCalculateHelperChoice(Item item) {
		CalculateHelper ch = new CalculateHelper(getItemGroupScore1(item));
		return ch;
	}

	public GroupScore getItemGroupScore(Item item) {
		HashMap<Double, ScoreInfo> scoreInfoMap = new HashMap<Double, ScoreInfo>();
		for (StudentSubjectScore sss : scores) {
			ItemScore itemScore = sss.getItemScore(item);
			if (itemScore == null) {
				StringBuffer text = new StringBuffer();
				text.append(";itemId:" + item.getId());
				text.append(";analysisTestPaperID:"
						+ item.getAnalysisTestpaper().getId());
				text.append(":studentId:"
						+ sss.getStudentCj().getStudent().getId());
				System.out.println(text.toString());
				log.error("itemScore为空："+text.toString());
			}
			Score score = itemScore.getScore();
			if(score.getValue()==-888.0){
				continue;
			}
			ScoreInfo si = scoreInfoMap.get(score.getValue());
			if (si == null) {
				si = new ScoreInfo();
				si.setScore(score);
				scoreInfoMap.put(score.getValue(), si);
			}
			
			si.setNum(si.getNum() + 1);
		}

		Double fullScore = item.getFullScore();
		GroupScore groupScore = new GroupScore(fullScore,
				new ArrayList<ScoreInfo>(scoreInfoMap.values()));
		groupScore.setSubjectScoreContainer(this);
		return groupScore;
	}


	public GroupScore getItemGroupScore1(Item item) {
		HashMap<Double, ScoreInfo> scoreInfoMap = new HashMap<Double, ScoreInfo>();
		ArrayList<StudentSubjectScore> tmpScores = new ArrayList<>();
		for (StudentSubjectScore sss : scores) {
			ItemScore itemScore = sss.getItemScore(item);
			if (itemScore == null) {
				StringBuffer text = new StringBuffer();
				text.append(";itemId:" + item.getId());
				text.append(";analysisTestPaperID:"
						+ item.getAnalysisTestpaper().getId());
				text.append(":studentId:"
						+ sss.getStudentCj().getStudent().getId());
				System.out.println(text.toString());
				log.error("itemScore为空："+text.toString());
			}
			Score itemscore = itemScore.getScore();
			if(itemscore.getValue()==-888.0){
				continue;
			}
			tmpScores.add(sss);
			Score score = sss.getScore();
			ScoreInfo si = scoreInfoMap.get(score.getValue());
			if (si == null) {
				si = new ScoreInfo();
				si.setScore(score);
				scoreInfoMap.put(score.getValue(), si);
			}

			si.setNum(si.getNum() + 1);
		}

		Double fullScore = item.getFullScore();
		GroupScore groupScore = new GroupScore(fullScore,
				new ArrayList<ScoreInfo>(scoreInfoMap.values()));
		SubjectScoreContainer ssc = new SubjectScoreContainer();
		ssc.setScores(tmpScores);
		ssc.setAnalysisTestpaper(this.getAnalysisTestpaper());
		groupScore.setSubjectScoreContainer(ssc);
		return groupScore;
	}

	private void sort() {
		Collections.sort(scores, new Comparator<StudentSubjectScore>() {
			@Override
			public int compare(StudentSubjectScore o1, StudentSubjectScore o2) {
				Double score2 = o2.getScore().getValue();
				Double score1 = o1.getScore().getValue();
				return score2.compareTo(score1);
			}
		});
	}

	public SubjectScoreContainer getSubjectScoreContainer(
			IStudentCjFilter filter) {
		SubjectScoreContainer result = new SubjectScoreContainer();
		result.setAnalysisTestpaper(analysisTestpaper);
		for (StudentSubjectScore score : scores) {
			if (score != null && filter.execFilter(score.getStudentCj())) {
				result.add(score);
			}
		}
		return result;
	}

	public List<StudentSubjectScore> toList() {
		return scores;
	}

	public int size() {
		return scores.size();
	}

	public boolean isEmpty() {
		return scores.isEmpty();
	}

	public boolean isNotEmpty() {
		return !scores.isEmpty();
	}

	public void setScores(ArrayList<StudentSubjectScore> scores) {
		this.scores = scores;
	}

	public ArrayList<StudentSubjectScore> getScores() {
		return scores;
	}
}
