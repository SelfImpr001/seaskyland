/*
 * @(#)com.cntest.fxpt.service.IStatisticSettingService.java	1.0 2014年10月27日:下午3:07:18
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.FtpSetting;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:07:18
 * @version 1.0
 */
public interface IFtpSettingService {
	public List<FtpSetting> ftpList();
	public FtpSetting findFtpSetByStatus(int status);
	public void updateFtp(FtpSetting ftpSetting);
	public boolean upload(Long examid);
	
}
