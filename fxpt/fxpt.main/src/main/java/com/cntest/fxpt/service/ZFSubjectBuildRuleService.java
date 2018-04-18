/*
 * @(#)com.cntest.fxpt.service.ZFSubjectBuildRuleService.java	1.0 2015年6月11日:下午5:31:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.ZFSubjectBuildRule;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 下午5:31:39
 * @version 1.0
 */
public interface ZFSubjectBuildRuleService {
	public List<ZFSubjectBuildRule> list();
	/**
	 * 在classfield为wl和languageType中设置一项有效
	 * @param classfield
	 */
	public void updateZF(String classfield);
}
