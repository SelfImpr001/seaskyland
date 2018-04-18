/*
 * @(#)com.cntest.fxpt.bean.LeveScoreSetting.java	1.0 2015年4月13日:上午9:21:08
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月13日 上午9:21:08
 * @version 1.0
 */
public class LeveScoreSetting {
	private Long id;
	private Exam exam;
	private AnalysisTestpaper analysisTestpaper;
	private Subject subject;
	private int wl;
	private double beginScore;
	private double endScore;
	private String levelName;
	private double levelScore;
	private int skrs;
	private int num;

	public Exam getExam() {
		return exam;
	}

	public AnalysisTestpaper getAnalysisTestpaper() {
		return analysisTestpaper;
	}

	public Subject getSubject() {
		return subject;
	}

	public int getWl() {
		return wl;
	}

	public double getEndScore() {
		return endScore;
	}

	public String getLevelName() {
		return levelName;
	}

	public Long getId() {
		return id;
	}

	public double getLevelScore() {
		return levelScore;
	}

	public double getBeginScore() {
		return beginScore;
	}

	public int getSkrs() {
		return skrs;
	}

	public int getNum() {
		return num;
	}

	public void setSkrs(int skrs) {
		this.skrs = skrs;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setBeginScore(double beginScore) {
		this.beginScore = beginScore;
	}

	public void setLevelScore(double levelScore) {
		this.levelScore = levelScore;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public void setAnalysisTestpaper(AnalysisTestpaper analysisTestpaper) {
		this.analysisTestpaper = analysisTestpaper;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public void setWl(int wl) {
		this.wl = wl;
	}

	public void setEndScore(double endScore) {
		this.endScore = endScore;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
}
