/*
 * @(#)com.cntest.fxpt.repository.impl.SchoolDaoImpl.java	1.0 2014年6月3日:上午8:46:37
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.math.BigInteger;
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
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.repository.ISchoolDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月3日 上午8:46:37
 * @version 1.0
 */
@Repository("ISchoolDao")
public class SchoolDaoImpl extends AbstractHibernateDao<School, Long> implements
		ISchoolDao {

	@Override
	protected Class<School> getEntityClass() {
		return School.class;
	}

	@Override
	public void add(School school) {
		this.save(school);

	}

	@Override
	public School get(Long schoolId) {
		return super.get(schoolId);
	}

	@Override
	public List<School> list() {
		String hql = "from School";
		return findByHql(hql);
	}

	@Override
	public School findWithCode(String code) {
		String hql = "from School where code=?";
		return findEntityByHql(hql, code);
	}

	@Override
	public List<School> listWithEducationCode(String educationCode) {
		String hql = "from School where education.code=?";
		return findByHql(hql, educationCode);
	}

	@Override
	public int getExamStudentNum(School school) {
		String sql = "select count(1) from tb_examstudent where schoolCode=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, school.getCode());
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
	public List<School> list(Page<School> page) {
		Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());
		Map<String, String> parameter = page.getParameter();

		String schoolName = parameter.get("schoolName");
		if (schoolName != null && !schoolName.toString().equals("")) {
			criteria.add(Restrictions.like("name", "%" + schoolName + "%"));
		}

		String educationName = parameter.get("educationName");
		if (educationName != null && !educationName.toString().equals("")) {
			criteria.createAlias("education", "education");
			criteria.createAlias("education.parent", "parent");
			criteria.createAlias("education.parent.parent", "parent1");
			criteria.add(Restrictions.or(
					Restrictions.like("education.name", "%" + educationName
							+ "%"),
					Restrictions.or(
							Restrictions.like("parent.name", "%"
									+ educationName + "%"),
							Restrictions.like("parent1.name", "%"
									+ educationName + "%"))));
		}

		Long rowCount = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());

		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);
		List<School> list = criteria.list();

		page.setList(list);
		return list;
	}

	@Override
	public List<School> list(int type, String code) {
		String sql = "SELECT s.id id,s.code code,s.name name,s.schoolType schoolType,s.countyCode countyCode FROM dw_dim_school s";
		if (type == 1) {
			sql += " WHERE provinceCode='" + code + "'";
		} else if (type == 2) {
			sql += " WHERE cityCode='" + code + "'";
		} else if (type == 3) {
			sql += " WHERE countyCode='" + code + "'";
		} else if (type == 4) {
			sql += " WHERE code='" + code + "'";
		}

		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(School.class);
		return query.list();
	}

	@Override
	public List<School> examSchoolList(Long examId) {
		String sql = "SELECT xx.id id,xx.code code,xx.name name,xx.schoolType schoolType,xx.countyCode countyCode FROM dw_examstudent_fact xs "
				+ "INNER JOIN dw_dim_school xx ON xs.schoolId = xx.id "
				+ "WHERE examId= ? GROUP BY xx.id";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, examId);
		query.addEntity(School.class);
		return query.list();
	}

}
