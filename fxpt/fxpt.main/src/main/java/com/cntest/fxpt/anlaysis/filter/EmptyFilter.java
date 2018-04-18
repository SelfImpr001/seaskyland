/*
 * @(#)com.cntest.fxpt.anlaysis.filter.StudentAttrFilter.java	1.0 2015年4月22日:下午1:36:26
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.StudentCj;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 下午1:36:26
 * @version 1.0
 */
public class EmptyFilter extends AbstractStudentCjFilter {
	private Logger log = LoggerFactory.getLogger(EmptyFilter.class);

	public EmptyFilter() {
	}

	@Override
	public boolean filter(StudentCj studentCj) {
		return true;
	}

	@Override
	public String toString() {
		return "";
	}
}
