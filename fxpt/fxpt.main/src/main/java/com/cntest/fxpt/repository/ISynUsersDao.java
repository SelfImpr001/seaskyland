/*
 * @(#)com.cntest.fxpt.repository.ISynUsersDao.java	1.0 2016年4月8日:下午3:45:46
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cntest.fxpt.domain.SynUsers;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2016年4月8日 下午3:45:46
 * @version 1.0
 */
public interface ISynUsersDao {
	public List<SynUsers> list();
	public void save(SynUsers synUsers);
	public void delete(SynUsers synUsers);
	public SynUsers findByid(Long pk);
	public void update(SynUsers synUsers);
	public int loadByCode(String code);
	
	public void deleteSynByRoleId(Long id);
	
	public void createUsers(Long examid);
	
	public void downLoadExcelByZip(HttpServletRequest request, HttpServletResponse response);
}
