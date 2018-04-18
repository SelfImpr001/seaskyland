/*
 * @(#)com.cntest.fxpt.etl.domain.UplineScore.java 1.0 2014年10月27日:下午2:55:27
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import com.cntest.foura.domain.Organization;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月27日 下午2:55:27
 * @version 1.0
 */
public class UplineScore {
	private Long id;
	private Exam exam;
	private int wlType;
	private String name;
	private Subject subject;
	private Organization org;
	private Double divideScore;
	private Double divideScale;
	private int level;
	private int scoreType;
	private Double ratio;
	private Double upScore;
	private Double downScore;

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public UplineScore() {
	}

	public Long getId() {
		return id;
	}

	public UplineScore setId(Long id) {
		this.id = id;
		return this;
	}

	public Exam getExam() {
		return exam;
	}

	public UplineScore setExam(Exam exam) {
		this.exam = exam;
		return this;
	}

	public int getWlType() {
		return wlType;
	}

	public UplineScore setWlType(int wlType) {
		this.wlType = wlType;
		return this;
	}

	public String getName() {
		return name;
	}

	public UplineScore setName(String name) {
		this.name = name;
		return this;
	}

	public Organization getOrg() {
		return org;
	}

	public UplineScore setOrg(Organization org) {
		this.org = org;
		return this;
	}

	public Double getDivideScore() {
		return divideScore;
	}

	public UplineScore setDivideScore(Double divideScore) {
		this.divideScore = divideScore;
		return this;
	}

	public Double getDivideScale() {
		return divideScale;
	}

	public UplineScore setDivideScale(Double divideScale) {
		this.divideScale = divideScale;
		return this;
	}

	public int getLevel() {
		return level;
	}

	public UplineScore setLevel(int level) {
		this.level = level;
		return this;
	}

	public int getScoreType() {
		return scoreType;
	}

	public UplineScore setScoreType(int scoreType) {
		this.scoreType = scoreType;
		return this;
	}

	public Double getRatio() {
		return ratio;
	}

	public UplineScore setRatio(Double ratio) {
		this.ratio = ratio;
		return this;
	}

	public Double getUpScore() {
		return upScore;
	}

	public UplineScore setUpScore(Double upScore) {
		this.upScore = upScore;
		return this;
	}

	public Double getDownScore() {
		return downScore;
	}

	public UplineScore setDownScore(Double downScore) {
		this.downScore = downScore;
		return this;
	}
}
