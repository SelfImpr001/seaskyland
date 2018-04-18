/*
 * @(#)com.cntest.fxpt.etl.business.impl.AbstractValidate.java	1.0 2014年10月16日:下午1:59:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.List;

import com.cntest.fxpt.bean.DataField;
import com.cntest.util.ListUtil;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月16日 下午1:59:28
 * @version 1.0
 */
public abstract class AbstractValidate {
	protected boolean isValidateFiled(DataField d, List<String> head) {
		boolean result = false;
		String[] defNames = d.getDefaultName().split("\\|");
		for (String defName : defNames) {
			if (ListUtil.hasValue(head, defName)) {
				result = true;
				break;
			}
		}
		return result;
	}
}
