/*
 * @(#)com.cntest.fxpt.anlaysis.filter.OrgFilter.java	1.0 2014年12月1日:上午11:31:50
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.filter;

import com.cntest.fxpt.anlaysis.bean.StudentCj;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月1日 上午11:31:50
 * @version 1.0
 */
public class WLFilter extends AbstractStudentCjFilter {
	private int wl;

	public WLFilter(int wl) {
		this.wl = wl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.filter.IStudentCjFilter#filter(com.cntest.fxpt
	 * .anlaysis.bean.StudentCj)
	 */
	@Override
	public boolean filter(StudentCj studentCj) {
		return studentCj.getStudent().getWl() == wl;
	}

	@Override
	public String toString() {
		String text = "[wl:" + wl + "]";
		if (this.filter != null) {
			text += "->" + this.filter.toString();
		}
		return text;
	}
}
