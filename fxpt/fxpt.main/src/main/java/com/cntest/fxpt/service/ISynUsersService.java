/*
 * @(#)com.cntest.fxpt.service.IStatisticSettingService.java	1.0 2014年10月27日:下午3:07:18
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cntest.fxpt.domain.SynUsers;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:07:18
 * @version 1.0
 */
public interface ISynUsersService {
	public List<SynUsers> list();
	public void save(SynUsers synUsers);
	public void delete(SynUsers synUsers);
	public SynUsers load(Long pk);
	public void update(SynUsers synUsers);
	public int loadByCode(String code);
	/**
	 * 根据角色Id删除
	 * @param id
	 */
	public void deleteSynByRoleId(Long id);
	
	
	/**
	* <Pre>
	* 根据考试ID ，参照用户同步设置表，初始创建所有人员账号
	* </Pre>
	* @param examid
	* @return void
	* @author:黄洪成 2016年4月14日 上午11:31:54
	 */
	public void createUsers(Long examid);
	
	
	public void downLoadExcelByZip(HttpServletRequest request, HttpServletResponse response);
	
}
