/*
 * @(#)com.cntest.fxpt.anlaysis.customize.anlaysis.FormulaCalcluator.java	1.0 2015年4月23日:下午2:48:46
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.anlaysis;

import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;
import com.cntest.fxpt.anlaysis.customize.bean.Formula;
import com.cntest.fxpt.anlaysis.uitl.CalculateHelper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月23日 下午2:48:46
 * @version 1.0
 */
public class FormulaCalcluator {
	private Formula f;
	private CalculateHelper ch;
	private SubjectScoreContainer cjc;

	public FormulaCalcluator(SubjectScoreContainer cjc, CalculateHelper ch,
			Formula f) {
		this.f = f;
		this.ch = ch;
		this.cjc = cjc;
	}

	public double getValue() {
		double result = 0;
		switch (f.getFormulaType()) {
		case AVG:
			result = ch.getAvg();
			break;
		case STD:
			result = ch.getStd();
			break;
		case MIN:
			result = ch.getMin();
			break;
		case MAX:
			result = ch.getMax();
			break;
		case DFL:
			result = ch.getAvg() / ch.getFullScore() * 100;
			break;
		case SKRS:
			result = ch.getTaskNum();
			break;

		default:
			break;
		}
		return result;
	}
}
