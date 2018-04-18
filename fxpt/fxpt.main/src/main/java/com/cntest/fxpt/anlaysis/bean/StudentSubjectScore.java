/*
 * @(#)com.cntest.fxpt.anlaysis.bean.StudentSubjectScore.java	1.0 2014年11月24日:下午1:25:42
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import com.cntest.fxpt.anlaysis.uitl.ScoreObjMgr;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:25:42
 * @version 1.0
 */
public class StudentSubjectScore implements PrimaryKey<Long> {
	private StudentCj studentCj;
	private AnalysisTestpaper analysisTestpaper;
	private boolean isQk;
	private Score score;
	private Score kgScore;
	private Score zgScore;
	private Score zfScore;
	private ConcurrentHashMap<Long, ItemScore> itemScores = new ConcurrentHashMap<>();
	// 武侯区 主科缺考科目数
	private int masterQkNum = 0;
	private boolean oncePass = false;
	private boolean allPass = false;
	private Map<Integer,Integer> dd = new HashMap<>();
	private Map<Integer,Integer> sd = new HashMap<>();
	private Map<Integer,Integer> zd = new HashMap<>();
	private Score rankScore;


	public boolean isQk() {
		return isQk;
	}

	public void setQk(boolean isQk) {
		this.isQk = isQk;
	}

	@Override
	public Long getPk() {
		return analysisTestpaper.getId();
	}

	public StudentCj getStudentCj() {
		return studentCj;
	}

	public AnalysisTestpaper getAnalysisTestpaper() {
		return analysisTestpaper;
	}

	public Score getScore() {
		return score;
	}

	public void setStudentCj(StudentCj studentCj) {
		this.studentCj = studentCj;
	}

	public void setAnalysisTestpaper(AnalysisTestpaper analysisTestpaper) {
		this.analysisTestpaper = analysisTestpaper;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public Score getKgScore() {
		return kgScore;
	}

	public Score getZgScore() {
		return zgScore;
	}

	public void setKgScore(Score kgScore) {
		this.kgScore = kgScore;
	}

	public void setZgScore(Score zgScore) {
		this.zgScore = zgScore;
	}

	public Score getZfScore() { return zfScore; }

	public void setZfScore(Score zfScore) { this.zfScore = zfScore; }

	public void addItemScore(ItemScore itemScore) {
		itemScores.put(itemScore.getItem().getId(), itemScore);
	}

	public ItemScore getItemScore(Item item) {
		return itemScores.get(item.getId());
	}
	
	public int getMasterQkNum() {
		return masterQkNum;
	}

	public void setMasterQkNum(int masterQkNum) {
		this.masterQkNum = masterQkNum;
	}

	public boolean isOncePass() {
		return oncePass;
	}

	public void setOncePass(boolean oncePass) {
		this.oncePass = oncePass;
	}

	public boolean isAllPass() { return allPass; }

	public void setAllPass(boolean allPass) {
		this.allPass = allPass;
	}

	public Map<Integer, Integer> getDd() { return dd; }

	public void setDd(Map<Integer, Integer> dd) { this.dd = dd; }

	public Map<Integer, Integer> getSd() { return sd; }

	public void setSd(Map<Integer, Integer> sd) { this.sd = sd; }

	public Map<Integer, Integer> getZd() { return zd; }

	public void setZd(Map<Integer, Integer> zd) { this.zd = zd; }

	public Score getRankScore() { return rankScore; }

	public void setRankScore(Score rankScore) { this.rankScore = rankScore; }

	public Score getItemGroupScore(ItemGroup itemGroup) {
		Score score = new Score();
		for (Item item : itemGroup.getItems()) {
//			2018-02-26 加入选做题得分 hhc  
//			if(itemScores.get(item.getId()).getScore().getValue()!=-888){
//				score.add(itemScores.get(item.getId()).getScore());
//			}
			if(!item.isChoice()){
				score.add(itemScores.get(item.getId()).getScore());
			}
		}
		return ScoreObjMgr.newInstance().getScore(score);
	}
	
	public Score getItemGroupScoreChoice(ItemGroup itemGroup) {
		Score score = new Score();
		for (Item item : itemGroup.getItems()) {
			if(item.isChoice()){
				Score scorevalue = itemScores.get(item.getId()).getScore(); 
				if(scorevalue.getValue()==-888.0){
					continue;
				}
				score.add(scorevalue);
			}
		}
		return ScoreObjMgr.newInstance().getScore(score);
	}
	
	@Override
	public String toString() {
		ExamStudent es = studentCj.getStudent();
		String wl = "";
		if (analysisTestpaper.getPaperType() == 1) {
			wl = "(理)";
		} else if (analysisTestpaper.getPaperType() == 2) {
			wl = "(文)";
		} else {
			wl = "";
		}
		return es.getId() + "-->" + es.getName() + "-->"
				+ analysisTestpaper.getName() + wl + score.toString();
	}
}
