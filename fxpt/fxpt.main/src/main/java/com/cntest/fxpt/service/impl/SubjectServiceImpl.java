/*
 * @(#)com.cntest.fxpt.service.impl.SubjectServiceImpl.java	1.0 2014年5月22日:下午1:58:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.repository.ISubjectDao;
import com.cntest.fxpt.service.ISubjectService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午1:58:47
 * @version 1.0
 */
@Service("ISubjectService")
public class SubjectServiceImpl implements ISubjectService {
	@Autowired(required = false)
	@Qualifier("ISubjectDao")
	private ISubjectDao subjectDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ISubjectService#add(com.cntest.fxpt.domain.Subject
	 * )
	 */
	@Override
	public boolean add(Subject subject) {
		boolean has = hasName(subject);
		if (!has) {
			subjectDao.add(subject);
		}
		return has;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ISubjectService#delete(com.cntest.fxpt.domain
	 * .Subject)
	 */
	@Override
	public boolean delete(Subject subject) {
		// TODO 检查关联的试卷和试题
		boolean hasTestPaper = hasTestPaper(subject);
		// boolean hasItem = hasItem(subject);
		if (!hasTestPaper) {
			subjectDao.delete(subject);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ISubjectService#update(com.cntest.fxpt.domain
	 * .Subject)
	 */
	@Override
	public boolean update(Subject subject) {
		boolean has = hasName(subject);
		if (!has) {
			subjectDao.update(subject);
		}
		return has;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.ISubjectService#list()
	 */
	@Override
	public List<Subject> list() {
		return subjectDao.list();
	}

	@Override
	public Subject get(Long subjectId) {
		return subjectDao.get(subjectId);
	}

	@Override
	public boolean hasTestPaper(Subject subject) {
		int testPaperNum = subjectDao.getTestPaperNum(subject);
		return testPaperNum > 0 ? true : false;
	}

	@Override
	public boolean hasItem(Subject subject) {
		int itemNum = subjectDao.getItemNum(subject);
		return itemNum > 0 ? true : false;
	}

	public List<Subject> findSubjectList(String subjectName) {
		return subjectDao.findSubjectList(subjectName);
	}

	public boolean hasName(Subject subject) {
		return subjectDao.hasName(subject);
	}

	@Override
	public Subject getZF() {
		return subjectDao.getZF();
	}
}
