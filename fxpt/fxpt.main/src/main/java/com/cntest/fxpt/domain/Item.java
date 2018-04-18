/*
 * @(#)com.cntest.fxpt.domain.Item.java	1.0 2014年5月14日:上午11:14:23
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

/**
 * <Pre>
 * 题目
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 上午11:14:23
 * @version 1.0
 */
public class Item {
	private Long id;
	private String itemNo;
	private String bigTitleNo;
	private double fullScore;
	private String allOptions;  //客观题全部选项
	private String rightOptioin;
	private String knowledge;
	private String knowledgeContent;
	private String ability;
	private String titleType;
	private double forecastDifficulty;
	private int optionType;// '1 单项选择题,2多项选择题 0 主观题',
	private String cjField;// 选择可以用分数字段|选项字段来表示
	private boolean isChoice;// '0不是 1是',
	private int choiceOrNot;
	private String choiceGroup;
	private String choiceModule;
	private String choiceNumber;// '格式为:4|2表示4选2',
	private double choiceFullScore;
	private int sortNum;
	private String paperType;
	private String paper;

	private Subject subject;
	private TestPaper testPaper;
	private AnalysisTestpaper analysisTestpaper;
	private Exam exam;


	public int getChoiceOrNot() {
		return choiceOrNot;
	}

	public void setChoiceOrNot(int choiceOrNot) {
		this.choiceOrNot = choiceOrNot;
	}

	public String getTitleType() {
		return titleType;
	}

	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getBigTitleNo() {
		return bigTitleNo;
	}

	public void setBigTitleNo(String bigTitleNo) {
		this.bigTitleNo = bigTitleNo;
	}

	public double getFullScore() {
		return fullScore;
	}

	public void setFullScore(double fullScore) {
		this.fullScore = fullScore;
	}
	
	public String getAllOptions() {
		return allOptions;
	}

	public void setAllOptions(String allOptions) {
		this.allOptions = allOptions;
	}

	public String getRightOptioin() {
		return rightOptioin;
	}

	public void setRightOptioin(String rightOptioin) {
		this.rightOptioin = rightOptioin;
	}

	public String getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}

	public String getKnowledgeContent() {
		return knowledgeContent;
	}

	public void setKnowledgeContent(String knowledgeContent) {
		this.knowledgeContent = knowledgeContent;
	}

	public String getAbility() {
		return ability;
	}

	public void setAbility(String ability) {
		this.ability = ability;
	}

	public double getForecastDifficulty() {
		return forecastDifficulty;
	}

	public void setForecastDifficulty(double forecastDifficulty) {
		this.forecastDifficulty = forecastDifficulty;
	}

	public int getOptionType() {
		return optionType;
	}

	public boolean isSelTitle() {
		return optionType == 0 ? false : true;
	}

	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}

	public String getCjField() {
		return cjField;
	}

	public void setCjField(String cjField) {
		this.cjField = cjField;
	}

	public String getChoiceGroup() {
		return choiceGroup;
	}

	public void setChoiceGroup(String choiceGroup) {
		this.choiceGroup = choiceGroup;
	}

	public String getChoiceModule() {
		return choiceModule;
	}

	public void setChoiceModule(String choiceModule) {
		this.choiceModule = choiceModule;
	}

	public String getChoiceNumber() {
		return choiceNumber;
	}

	public void setChoiceNumber(String choiceNumber) {
		this.choiceNumber = choiceNumber;
	}

	public double getChoiceFullScore() {
		return choiceFullScore;
	}

	public void setChoiceFullScore(double choiceFullScore) {
		this.choiceFullScore = choiceFullScore;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public TestPaper getTestPaper() {
		return testPaper;
	}

	public void setTestPaper(TestPaper testPaper) {
		this.testPaper = testPaper;
	}

	public boolean isChoice() {
		return isChoice;
	}

	public void setChoice(boolean isChoice) {
		this.isChoice = isChoice;
	}

	public AnalysisTestpaper getAnalysisTestpaper() {
		return analysisTestpaper;
	}

	public Exam getExam() {
		return exam;
	}

	public void setAnalysisTestpaper(AnalysisTestpaper analysisTestpaper) {
		this.analysisTestpaper = analysisTestpaper;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public String getPaperType() {
		return paperType;
	}

	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}

	public String getPaper() {
		return paper;
	}

	public void setPaper(String paper) {
		this.paper = paper;
	}
}
