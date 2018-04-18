/*
 * @(#)com.cntest.fxpt.anlaysis.bean.StudentItemScore.java	1.0 2014年11月24日:下午1:33:21
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import com.cntest.fxpt.domain.Item;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:33:21
 * @version 1.0
 */
public class ItemScore {
	private Item item;
	private Score score;
	private String selOption;

	public Item getItem() {
		return item;
	}

	public Score getScore() {
		return score;
	}

	public String getSelOption() {
		return selOption;
	}

	public void setSelOption(String selOption) {
		this.selOption = selOption;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setScore(Score score) {
		this.score = score;
	}

}
