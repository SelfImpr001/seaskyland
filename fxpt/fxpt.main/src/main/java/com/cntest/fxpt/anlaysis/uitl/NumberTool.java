/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.NumberTool.java	1.0 2014年11月28日:上午9:39:43
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 上午9:39:43
 * @version 1.0
 */
public class NumberTool {
	public static int toInt(double d) {
		DecimalFormat df = new DecimalFormat("0");
		df.setRoundingMode(RoundingMode.HALF_UP);
		int tmp = Integer.parseInt(df.format(d));
		return tmp;
	}

	public static int ceil(double d) {
		BigDecimal n = new BigDecimal(d);
		return n.setScale(0, RoundingMode.CEILING).intValue();
	}

	public static int floor(double d) {
		BigDecimal n = new BigDecimal(d);
		return n.setScale(0, RoundingMode.FLOOR).intValue();
	}
}
