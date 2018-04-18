/*
 * @(#)com.cntest.fxpt.anlaysis.repository.impl.AbstractDao.java	1.0 2014年6月26日:下午4:53:54
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月26日 下午4:53:54
 * @version 1.0
 */
public class AbstractDao {
	@Autowired(required = false)
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	protected Session getSession() {
		try {
			return this.sessionFactory.getCurrentSession();
		} catch (Exception e) {

		}

		return this.sessionFactory.openSession();
	}
}
