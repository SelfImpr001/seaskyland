/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.AbstractStudentCjFilter.java	1.0 2014年11月27日:下午4:14:21
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.filter;

import com.cntest.fxpt.anlaysis.bean.StudentCj;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月27日 下午4:14:21
 * @version 1.0
 */
public abstract class AbstractStudentCjFilter implements IStudentCjFilter {
	protected IStudentCjFilter filter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.uitl.IStudentCjFilter#next(com.cntest.fxpt.anlaysis
	 * .uitl.IStudentCjFilter)
	 */
	@Override
	public IStudentCjFilter next(IStudentCjFilter filter) {
		if (this.filter == null) {
			this.filter = filter;
		} else {
			this.filter.next(filter);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.uitl.IStudentCjFilter#execFilter(com.cntest.
	 * fxpt.anlaysis.bean.StudentCj)
	 */
	@Override
	public boolean execFilter(StudentCj studentCj) {
		return filter(studentCj) && execNextFilter(studentCj);
	}

	protected boolean execNextFilter(StudentCj studentCj) {
		boolean result = true;
		if (filter != null) {
			result = filter.execFilter(studentCj);
		}
		return result;
	}
}
