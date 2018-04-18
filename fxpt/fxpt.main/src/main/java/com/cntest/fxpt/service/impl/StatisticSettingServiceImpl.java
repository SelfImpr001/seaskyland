/*
 * @(#)com.cntest.fxpt.service.impl.StatisticSettingServiceImpl.java	1.0 2014年10月27日:下午3:08:03
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.StatisticSetting;
import com.cntest.fxpt.repository.IStatisticSettingDao;
import com.cntest.fxpt.service.IStatisticSettingService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:08:03
 * @version 1.0
 */
@Service("IStatisticSettingService")
public class StatisticSettingServiceImpl implements IStatisticSettingService {
	@Autowired(required = false)
	@Qualifier("IStatisticSettingDao")
	private IStatisticSettingDao statisticSettingDao;

	public List<StatisticSetting> list() {
		return statisticSettingDao.list();
	}

	public void update(int single, int multi) {
		statisticSettingDao.update(single, multi);
	}

	public int getSubjectStatisticSettingValue(int type) {
		return statisticSettingDao.getSubjectStatisticSettingValue(type);
	}

	@Override
	public StatisticSetting getCheck(int stype) {
		return statisticSettingDao.getCheck(stype);
	}

}
