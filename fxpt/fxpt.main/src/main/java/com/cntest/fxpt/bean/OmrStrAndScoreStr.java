/*
 * @(#)com.cntest.fxpt.bean.OrmStrAndScoreStr.java	1.0 2014年10月16日:下午2:23:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月16日 下午2:23:49
 * @version 1.0
 */
public class OmrStrAndScoreStr {
	private boolean isHasOmrStr = false;
	private boolean isHasScoreStr = false;
	private String omrStr;
	private String scoreStr;

	public boolean isHasOmrStr() {
		return isHasOmrStr;
	}

	public boolean isHasScoreStr() {
		return isHasScoreStr;
	}

	public String getOmrStr() {
		return omrStr;
	}

	public String getScoreStr() {
		return scoreStr;
	}

	public OmrStrAndScoreStr setScoreStr(String scoreStr) {
		this.scoreStr = scoreStr;
		return this;
	}

	public OmrStrAndScoreStr setOmrStr(String omrStr) {
		this.omrStr = omrStr;
		return this;
	}

	public OmrStrAndScoreStr setHasOmrStr(boolean isHasOmrStr) {
		this.isHasOmrStr = isHasOmrStr;
		return this;
	}

	public OmrStrAndScoreStr setHasScoreStr(boolean isHasScoreStr) {
		this.isHasScoreStr = isHasScoreStr;
		return this;
	}

}
