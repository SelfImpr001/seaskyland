/*
 * @(#)com.cntest.fxpt.service.impl.ZFSubjectBuildRuleServiceImpl.java	1.0 2015年6月11日:下午5:32:05
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.ZFSubjectBuildRule;
import com.cntest.fxpt.repository.ZFSubjectBuildRuleDao;
import com.cntest.fxpt.service.ZFSubjectBuildRuleService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年6月11日 下午5:32:05
 * @version 1.0
 */
@Service("ZFSubjectBuildRuleService")
public class ZFSubjectBuildRuleServiceImpl implements ZFSubjectBuildRuleService {

	@Autowired(required = false)
	@Qualifier("ZFSubjectBuildRuleDao")
	private ZFSubjectBuildRuleDao zfSubjectBuildRuleDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.ZFSubjectBuildRuleService#list()
	 */
	@Override
	public List<ZFSubjectBuildRule> list() {
		return zfSubjectBuildRuleDao.list();
	}

	@Override
	public void updateZF(String classfield) {
		zfSubjectBuildRuleDao.updateZF(classfield);
		
	}

}
