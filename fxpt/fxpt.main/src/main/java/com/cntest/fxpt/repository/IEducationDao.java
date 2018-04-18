/*
 * @(#)com.cntest.fxpt.repository.IPartOfEducationDao.java	1.0 2014年5月31日:上午9:55:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.Education;
import com.cntest.common.page.Page;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月31日 上午9:55:45
 * @version 1.0
 */
public interface IEducationDao {
	public void add(Education education);

	public void update(Education education);

	public void delete(Education education);

	public Education get(Long educationId);

	public Education getWithCode(String code);

	public List<Education> list(int educationType);

	public List<Education> list(int educationType, Page<Education> page);
	
	public List<Education> list(int educationType,Object... o);

	public List<Education> childList(Education parentEducation);

	public int getChildEducationNum(Education education);

	public int getSchoolNum(Education education);
}
