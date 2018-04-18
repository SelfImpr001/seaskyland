/*
 * @(#)com.cntest.fxpt.anlaysis.customize.bean.Formula.java	1.0 2015年4月22日:上午10:19:07
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.bean;

/**
 * <Pre>
 * 
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 上午10:19:07
 * @version 1.0
 */
public class Formula {
	private FormulaType formulaType;
	private String name;

	public String getName() {
		return name;
	}

	public FormulaType getFormulaType() {
		return formulaType;
	}

	public Formula setFormulaType(FormulaType formulaType) {
		this.formulaType = formulaType;
		return this;
	}

	public Formula setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return name;
	}

}
