/*
 * @(#)com.cntest.fxpt.repository.impl.SubjectDaoImpl.java	1.0 2014年5月22日:下午1:44:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.SchoolSegment;
import com.cntest.fxpt.repository.ISchoolSegmentDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午1:44:13
 * @version 1.0
 */
@Repository("ISchoolSegmentDao")
public class SchoolSegmentDaoImpl extends AbstractHibernateDao<SchoolSegment, Long>
		implements ISchoolSegmentDao {

	@Override
	public List<SchoolSegment> getAllSchoolSegment() {
		String hql = "from SchoolSegment order by ordernum";
		return findByHql(hql);
	}

	@Override
	protected Class<SchoolSegment> getEntityClass() {
		return SchoolSegment.class;
	}



}
