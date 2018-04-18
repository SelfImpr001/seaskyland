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

import com.cntest.fxpt.domain.FtpSetting;
import com.cntest.fxpt.repository.IFtpSettingDao;
import com.cntest.fxpt.service.IFtpSettingService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:08:03
 * @version 1.0
 */
@Service("IFtpSettingService")
public class FtpSettingServiceImpl implements IFtpSettingService {
	@Autowired(required = false)
	@Qualifier("IFtpSettingDao")
	private IFtpSettingDao ftpSettingDao;

	@Override
	public List<FtpSetting> ftpList() {
		return ftpSettingDao.ftpList();
	}

	@Override
	public void updateFtp(FtpSetting ftpSetting) {
		ftpSettingDao.updateFtp(ftpSetting);
	}

	@Override
	public FtpSetting findFtpSetByStatus(int status) {
		return ftpSettingDao.findFtpSetByStatus(status);
	}

	@Override
	public boolean upload(Long examid) {
		return ftpSettingDao.upload(examid);
	}


}
