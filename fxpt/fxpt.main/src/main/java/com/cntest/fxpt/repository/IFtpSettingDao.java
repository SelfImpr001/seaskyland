/*
 * @(#)com.cntest.fxpt.repository.IStatisticSettingDao.java	1.0 2014年10月27日:下午3:09:26
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.FtpSetting;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:09:26
 * @version 1.0
 */
public interface IFtpSettingDao {
		public List<FtpSetting> ftpList();
		public void updateFtp(FtpSetting ftpSetting);
		public FtpSetting findFtpSetByStatus(int status);
		public boolean upload(Long examid);
}
