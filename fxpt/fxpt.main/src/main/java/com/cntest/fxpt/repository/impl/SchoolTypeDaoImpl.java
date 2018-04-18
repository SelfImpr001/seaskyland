/*
 * @(#)com.cntest.fxpt.repository.impl.SubjectDaoImpl.java	1.0 2014年5月22日:下午1:44:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.RootEntityResultTransformer;
import org.springframework.stereotype.Repository;

import com.cntest.common.page.Page;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.SchoolType;
import com.cntest.fxpt.repository.ISchoolTypeDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午1:44:13
 * @version 1.0
 */
@Repository("ISchoolTypeDao")
public class SchoolTypeDaoImpl extends AbstractHibernateDao<SchoolType, Long>
		implements ISchoolTypeDao {

	@Override
	public List<SchoolType> list(Page<SchoolType> page) {
//		String hql = "from SchoolType order by ordernum";
//		return findByHql(hql);
		Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());

		Long rowCount = (Long) criteria.setProjection(
				Projections.countDistinct("id")).uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());
		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());
		criteria.addOrder(Order.desc("ordernum"));
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);

		List<SchoolType> list = criteria.list();
		page.setList(list);
		return list;
		
	}

	@Override
	public void add(SchoolType schoolType) {
		super.save(schoolType);
	}

	@Override
	public void delete(SchoolType schoolType) {
		super.delete(schoolType);
	}

	@Override
	public void update(SchoolType schoolType) {
		super.update(schoolType);
	}

	@Override
	public SchoolType get(Long id) {
		return super.get(id);
	}

	@Override
	public List<SchoolType> findSchoolTypeList(String name,Page<SchoolType> page) {
		List<SchoolType> listschoolTypes = new ArrayList<SchoolType>();
		String sql = "select * from kn_schooltype where name like '%"
				+ name + "%' order by ordernum";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				SchoolType schoolType = new SchoolType();
				schoolType.setId(Long.parseLong(obj[0].toString()));
				schoolType.setName(obj[1].toString());
				schoolType.setOrdernum(Integer.parseInt(obj[2].toString()));
				listschoolTypes.add(schoolType);
			}
		}
		page.setList(listschoolTypes);
		return listschoolTypes;
	}

	@Override
	public SchoolType findSchoolTypeWith(String name) {
		String hql = "from SchoolType where name=? order by id desc";
		SchoolType schoolType = findEntityByHql(hql, name);
		return schoolType;
	}

	@Override
	protected Class<SchoolType> getEntityClass() {
		return SchoolType.class;
	}

	@Override
	public List<SchoolType> getAllSchoolType() {
		String hql = "from SchoolType order by ordernum";
		return findByHql(hql);
	}



}
