/*
 * @(#)com.cntest.fxpt.anlaysis.bean.TotalScore.java	1.0 2014年11月25日:下午2:14:26
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午2:14:26
 * @version 1.0
 */
public class TempTotalScore {
	private Long studentId;
	private Long analysisTestpaperId;
	private Boolean isQk;
	private Double totalScore;
	private Double zgScore;
	private Double kgScore;

	public Long getStudentId() {
		return studentId;
	}

	public Long getAnalysisTestpaperId() {
		return analysisTestpaperId;
	}

	public Boolean getIsQk() {
		return isQk;
	}

	public Double getTotalScore() {
		return totalScore;
	}

	public Double getZgScore() {
		return zgScore;
	}

	public Double getKgScore() {
		return kgScore;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public void setAnalysisTestpaperId(Long analysisTestpaperId) {
		this.analysisTestpaperId = analysisTestpaperId;
	}

	public void setIsQk(Boolean isQk) {
		this.isQk = isQk;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public void setZgScore(Double zgScore) {
		this.zgScore = zgScore;
	}

	public void setKgScore(Double kgScore) {
		this.kgScore = kgScore;
	}
}
