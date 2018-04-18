/*
 * @(#)com.cntest.fxpt.anlaysis.bean.TempItemScore.java	1.0 2014年11月25日:下午2:18:00
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午2:18:00
 * @version 1.0
 */
public class TempItemScore {
	private Long studentId;
	private Long analysisTestpaperId;
	private Long itemId;
	private Double score;
	private String selOption;

	public Long getStudentId() {
		return studentId;
	}

	public Long getAnalysisTestpaperId() {
		return analysisTestpaperId;
	}

	public Long getItemId() {
		return itemId;
	}

	public Double getScore() {
		return score;
	}

	public String getSelOption() {
		return selOption;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public void setAnalysisTestpaperId(Long analysisTestpaperId) {
		this.analysisTestpaperId = analysisTestpaperId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public void setSelOption(String selOption) {
		this.selOption = selOption;
	}

}
