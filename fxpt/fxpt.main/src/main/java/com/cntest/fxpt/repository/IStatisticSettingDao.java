/*
 * @(#)com.cntest.fxpt.repository.IStatisticSettingDao.java	1.0 2014年10月27日:下午3:09:26
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.StatisticSetting;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:09:26
 * @version 1.0
 */
public interface IStatisticSettingDao {
		public List<StatisticSetting> list();
		
		public void update(int single, int multi);
		
		public int getSubjectStatisticSettingValue(int type);
		
		public StatisticSetting getCheck(int stype);
		
}
