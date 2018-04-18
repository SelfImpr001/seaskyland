/*
 * @(#)com.cntest.fxpt.service.IStandardScoreSettiongService.java	1.0 2015年4月20日:下午4:04:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;
import java.util.Map;


/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2015年4月20日 下午4:04:39
 * @version 1.0
 */
public interface IStandardScoreSettingService {
	public Map<String, List<Object[]>> findList(Long examid);
	public void save(String[] res);
	public String getZvalue(Long examid,int wl);
}
