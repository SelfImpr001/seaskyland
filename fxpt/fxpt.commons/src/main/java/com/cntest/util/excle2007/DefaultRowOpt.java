/*
 * @(#)com.excle2007.DefaultRowOpt.java	1.0 2014年5月30日:上午8:38:40
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.util.excle2007;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月30日 上午8:38:40
 * @version 1.0
 */
public class DefaultRowOpt implements IRowOpt {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.excle2007.IRowOpt#row(int, java.util.List)
	 */
	@Override
	public void row(int rowIndex, List<String> row) {
		StringBuffer sb = new StringBuffer();
		sb.append(rowIndex + "\t");
		for (String cell : row) {
			sb.append(cell + "\t");
		}
		System.out.println(sb.toString());
	}

}
