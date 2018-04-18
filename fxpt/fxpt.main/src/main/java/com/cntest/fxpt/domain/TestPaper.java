/*
 * @(#)com.cntest.fxpt.domain.TestPaper.java	1.0 2014年5月14日:上午11:14:01
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <Pre>
 * 试卷
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 上午11:14:01
 * @version 1.0
 */
public class TestPaper {

	private Long id;
	private String name;
	private double fullScore;
	private int paperType;
	private Exam exam;
	private boolean hasItem;
	private boolean hasCj;
	private boolean masterSubject;
	private boolean containPaper;
	private boolean hasAnalysis;
	private int skrs;
	private int bkrs;
	@JsonIgnore
	private List<AnalysisTestpaper> analysisTestpapers;
	/* 全部选择题选项 */
	private String selOptions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getFullScore() {
		return fullScore;
	}

	public void setFullScore(double fullScore) {
		this.fullScore = fullScore;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public boolean isHasItem() {
		return hasItem;
	}

	public void setHasItem(boolean hasItem) {
		this.hasItem = hasItem;
	}

	public boolean isHasCj() {
		return hasCj;
	}

	public void setHasCj(boolean hasCj) {
		this.hasCj = hasCj;
	}

	public boolean isMasterSubject() {
		return masterSubject;
	}

	public void setMasterSubject(boolean masterSubject) {
		this.masterSubject = masterSubject;
	}

	public boolean isContainPaper() {
		return containPaper;
	}

	public void setContainPaper(boolean containPaper) {
		this.containPaper = containPaper;
	}

	public int getPaperType() {
		return paperType;
	}

	public void setPaperType(int paperType) {
		this.paperType = paperType;
	}

	public int getSkrs() {
		return skrs;
	}

	public int getBkrs() {
		return bkrs;
	}

	public void setSkrs(int skrs) {
		this.skrs = skrs;
	}

	public void setBkrs(int bkrs) {
		this.bkrs = bkrs;
	}

	public List<AnalysisTestpaper> getAnalysisTestpapers() {
		return analysisTestpapers;
	}

	public void setAnalysisTestpapers(List<AnalysisTestpaper> analysisTestpapers) {
		this.analysisTestpapers = analysisTestpapers;
	}

	public String getSelOptions() {
		return selOptions;
	}

	public void setSelOptions(String selOptions) {
		this.selOptions = selOptions;
	}

	public boolean isHasAnalysis() {
		return hasAnalysis;
	}

	public void setHasAnalysis(boolean hasAnalysis) {
		this.hasAnalysis = hasAnalysis;
	}

}
