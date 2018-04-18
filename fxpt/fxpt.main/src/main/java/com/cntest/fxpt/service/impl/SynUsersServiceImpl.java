/*
 * @(#)com.cntest.fxpt.service.impl.StatisticSettingServiceImpl.java	1.0 2014年10月27日:下午3:08:03
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.FtpSetting;
import com.cntest.fxpt.domain.SynUsers;
import com.cntest.fxpt.repository.IFtpSettingDao;
import com.cntest.fxpt.repository.ISynUsersDao;
import com.cntest.fxpt.service.IFtpSettingService;
import com.cntest.fxpt.service.ISynUsersService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:08:03
 * @version 1.0
 */
@Service("ISynUsersService")
public class SynUsersServiceImpl implements ISynUsersService {
	@Autowired(required = false)
	@Qualifier("ISynUsersDao")
	private ISynUsersDao synUsersDao;

	public List<SynUsers> list() {
		List<SynUsers> list = synUsersDao.list();
		if(list!=null && list.size()>0)
			for (int i = 0; i < list.size(); i++) {
				SynUsers synUsers = list.get(i);
				synUsers.setNr(synUsers.getNr());
			}
		return list;
	}

	@Override
	public void save(SynUsers synUsers) {
		synUsersDao.save(synUsers);
	}

	@Override
	public void delete(SynUsers synUsers) {
		synUsersDao.delete(synUsers);
	}
	@Override
	public SynUsers load(Long pk) {
		return synUsersDao.findByid(pk);
	}
	@Override
	public void update(SynUsers synUsers) {
		synUsersDao.update(synUsers);
	}

	@Override
	public void createUsers(Long examid) {
		synUsersDao.createUsers(examid);
	}

	@Override
	public void downLoadExcelByZip(HttpServletRequest request,
			HttpServletResponse response) {
		 synUsersDao.downLoadExcelByZip(request,response);
		
	}

	@Override
	public int loadByCode(String code) {
		return synUsersDao.loadByCode(code);
	}

	@Override
	public void deleteSynByRoleId(Long id) {
		synUsersDao.deleteSynByRoleId(id);	
	}
}
