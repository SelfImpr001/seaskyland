/*
 * @(#)com.cntest.fxpt.service.impl.EducationServiceImpl.java	1.0 2014年5月31日:上午10:09:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.repository.IEducationDao;
import com.cntest.fxpt.service.IEducationService;
import com.cntest.common.page.Page;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月31日 上午10:09:04
 * @version 1.0
 */
@Service("IEducationService")
public class EducationServiceImpl implements IEducationService {

	@Autowired(required = false)
	@Qualifier("IEducationDao")
	private IEducationDao educationDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IEducationService#add(com.cntest.fxpt.domain.
	 * Education)
	 */
	@Override
	public void add(Education education) {
		educationDao.add(education);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IEducationService#update(com.cntest.fxpt.domain
	 * .Education)
	 */
	@Override
	public void update(Education education) {
		educationDao.update(education);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IEducationService#delete(com.cntest.fxpt.domain
	 * .Education)
	 */
	@Override
	public void delete(Education education) {
		educationDao.delete(education);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.IEducationService#get(int)
	 */
	@Override
	public Education get(Long educationId) {
		return educationDao.get(educationId);
	}

	@Override
	public List<Education> list(int educationType) {
		return educationDao.list(educationType);
	}
	@Override
	public List<Education> list(int educationType, Object... o) {
		return educationDao.list(educationType,o);
	}
	@Override
	public List<Education> childList(Education parentEducation) {
		return educationDao.childList(parentEducation);
	}

	@Override
	public boolean isExistEducation(Education education) {
		Education tmpEducation = educationDao.getWithCode(education.getCode());
		return tmpEducation == null ? false : true;
	}

	@Override
	public boolean hasEducation(Education education) {
		int childEducationNum = educationDao.getChildEducationNum(education);
		return childEducationNum > 0 ? true : false;
	}

	@Override
	public boolean hasSchool(Education education) {
		int schoolNum = educationDao.getSchoolNum(education);
		return schoolNum > 0 ? true : false;
	}

	@Override
	public List<Education> list(int educationType, Page<Education> page) {
		return educationDao.list(educationType, page);
	}

}
