/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.IStudentFilter.java	1.0 2014年11月27日:下午4:05:11
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.filter;

import com.cntest.fxpt.anlaysis.bean.StudentCj;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月27日 下午4:05:11
 * @version 1.0
 */
public interface IStudentCjFilter {
	public IStudentCjFilter next(IStudentCjFilter filter);

	public boolean execFilter(StudentCj studentCj);

	public boolean filter(StudentCj studentCj);
}
