/*
 * @(#)com.cntest.fxpt.service.IStatisticSettingService.java	1.0 2014年10月27日:下午3:07:18
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.StatisticSetting;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:07:18
 * @version 1.0
 */
public interface IStatisticSettingService {
	public List<StatisticSetting> list();

	public void update(int single, int multi);

	public int getSubjectStatisticSettingValue(int type);

	public StatisticSetting getCheck(int stype);

}
