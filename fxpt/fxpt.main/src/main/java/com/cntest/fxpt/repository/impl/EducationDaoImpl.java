/*
 * @(#)com.cntest.fxpt.repository.impl.PartOfEducationDaoImpl.java	1.0 2014年5月31日:上午9:57:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.RootEntityResultTransformer;
import org.springframework.stereotype.Repository;

import com.cntest.common.page.Page;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.repository.IEducationDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月31日 上午9:57:47
 * @version 1.0
 */
@Repository("IEducationDao")
public class EducationDaoImpl extends AbstractHibernateDao<Education, Long>
		implements IEducationDao {

	@Override
	public void add(Education education) {
		this.save(education);
	}

	@Override
	public void update(Education education) {
		super.update(education);
	}

	@Override
	public void delete(Education education) {
		super.delete(education);
	}

	@Override
	public Education get(Long educationId) {
		return super.get(educationId);
	}

	@Override
	protected Class<Education> getEntityClass() {
		return Education.class;
	}

	@Override
	public List<Education> list(int educationType) {
		String hql = "from Education where type=?";
		return findByHql(hql, educationType);
	}
	/***
	 * 添加国家级，一级包括国家（有国家级的省不展示）级和省级
	 * @param educationType
	 * @param o
	 * @return
	 */
	@Override
	public List<Education> list(int educationType,Object... o){
		String hql = "SELECT * FROM 4a_org WHERE p_id IS NULL ";
		for (int i = 0; i < o.length; i++) {
			if(i==0){
				hql+= " and org_type= ?";
			}else{
				hql+= " || org_type= ?";
			}
		}
		SQLQuery sqlQuery = this.getSession().createSQLQuery(hql);
		
		int position = 0;
		for(Object val:o) {
			if(val instanceof String)
				sqlQuery.setString(position++, val+"");
			else if(val instanceof Long)
				sqlQuery.setLong(position++, (Long)val);
			else if(val instanceof Integer)
				sqlQuery.setInteger(position++, (Integer)val);
			else if(val instanceof Date)
				sqlQuery.setDate(position++, (Date)val);
			else if(val instanceof Boolean)
				sqlQuery.setBoolean(position++, (Boolean)val);
		}
		
		sqlQuery.addEntity(Education.class);
		return sqlQuery.list();
	}
	@Override
	public List<Education> childList(Education parentEducation) {
		String hql = "from Education where parent.code=?";
		return findByHql(hql, parentEducation.getCode());
	}

	@Override
	public Education getWithCode(String code) {
		String hql = "from Education where code=?";
		return findEntityByHql(hql, code);
	}

	@Override
	public int getChildEducationNum(Education education) {
		String sql = "select count(1) from tb_education where parentCode=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, education.getCode());
		Object obj = sqlQuery.uniqueResult();
		int result = -1;
		if (obj instanceof Integer) {
			result = (Integer) obj;
		} else if (obj instanceof BigInteger) {
			BigInteger tmp = (BigInteger) obj;
			result = tmp.intValue();
		}
		return result;
	}

	@Override
	public int getSchoolNum(Education education) {
		String sql = "select count(1) from tb_school where educationCode=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, education.getCode());
		Object obj = sqlQuery.uniqueResult();
		int result = -1;
		if (obj instanceof Integer) {
			result = (Integer) obj;
		} else if (obj instanceof BigInteger) {
			BigInteger tmp = (BigInteger) obj;
			result = tmp.intValue();
		}
		return result;
	}

	@Override
	public List<Education> list(int educationType, Page<Education> page) {
		Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());

		criteria.add(Restrictions.eq("type", educationType));

		Map<String, String> parameter = page.getParameter();
		String educationName = parameter.get("educationName");
		if (!educationName.equals("")) {
			if (educationType == 1) {
				criteria.add(Restrictions.like("name", "%" + educationName
						+ "%"));
			}
			if (educationType == 2) {
				criteria.createAlias("parent", "parent");
				criteria.add(Restrictions.or(
						Restrictions.like("name", "%" + educationName + "%"),
						Restrictions.like("parent.name", "%" + educationName
								+ "%")));
			}
			if (educationType == 3) {
				criteria.createAlias("parent", "parent");
				criteria.createAlias("parent.parent", "parent1");
				criteria.add(Restrictions.or(
						Restrictions.like("name", "%" + educationName + "%"),
						Restrictions.or(
								Restrictions.like("parent.name", "%"
										+ educationName + "%"),
								Restrictions.like("parent1.name", "%"
										+ educationName + "%"))));
			}
		}

		Long rowCount = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());
		
		int first = (page.getCurpage() - 1) * page.getPagesize();
		first=first<0?0:first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);
		List<Education> list = criteria.list();

		page.setList(list);
		return list;
	}

}
