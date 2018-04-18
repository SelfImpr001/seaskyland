/*
 * @(#)com.cntest.fxpt.repository.impl.ClazzDaoImpl.java	1.0 2014年10月10日:下午3:03:21
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.Clazz;
import com.cntest.fxpt.repository.IClazzDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月10日 下午3:03:21
 * @version 1.0
 */
@Repository("IClazzDao")
public class ClazzDaoImpl extends AbstractHibernateDao<Clazz, Integer>
		implements IClazzDao {

	public void add(Clazz clazz) {
		
		this.save(clazz);
	}

	public void delete(Clazz clazz) {
		super.delete(clazz);
	}
	
	public void update(Clazz clazz) {
		super.update(clazz);
	}
	
	public Clazz get(Integer clazzId) {
		return super.get(clazzId);
	}

	public List<Clazz> list() {
		String hql = "from Clazz as c order by c.id";
		return findByHql(hql);
	}

	protected Class<Clazz> getEntityClass() {
		return Clazz.class;
	}
	public List<Clazz> findByName(String clazzName) {
		List<Clazz> listsubjects = new ArrayList<Clazz>();
		String sql = "select * from kn_class where name like '%"+clazzName+"%'";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				Clazz clazz = new Clazz();
				clazz.setId(Integer.parseInt(obj[0].toString()));
				clazz.setCode(obj[1].toString());
				clazz.setName(obj[2].toString());
				listsubjects.add(clazz);
			}
		}
		
		return listsubjects;
	}
	public boolean getHasNum(Clazz clazz) {
		String sql = "select count(*) as num from kn_studentbase where classCode ='"+clazz.getCode()+"'";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		int num = Integer.parseInt(list.get(0).toString());
		if(num>0){
			return true;
		}
		return false;
	}

	public Clazz findWithCode(String code) {
		String sql = "select * from kn_class where code='"+code+"'";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		Clazz clazz = new Clazz();
		if(list!=null && list.size()>0){
				Object[] obj = (Object[]) list.get(0);
				clazz.setId(Integer.parseInt(obj[0].toString()));
				clazz.setCode(obj[1].toString());
				clazz.setName(obj[2].toString());
		}
		return clazz;
	}

	public boolean hasName(Clazz clazz) {
		String sql = "SELECT COUNT(1) FROM kn_class WHERE NAME='"+clazz.getName()+"'";
		if(clazz!=null && clazz.getId()!=null && clazz.getId()>0){
			sql = "SELECT COUNT(1) FROM kn_class WHERE NAME='"+clazz.getName()+"' AND id<>"+clazz.getId();
		}
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && Integer.parseInt(list.get(0).toString())>0){
			return false;
		}
		return true;
	}
	
	public boolean hasCode(Clazz clazz) {
		String sql = "SELECT COUNT(1) FROM kn_class WHERE code='"+clazz.getCode()+"'";
		if(clazz!=null && clazz.getId()!=null && clazz.getId()>0){
			sql = "SELECT COUNT(1) FROM kn_class WHERE code='"+clazz.getCode()+"' AND id<>"+clazz.getId();
		}
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && Integer.parseInt(list.get(0).toString())>0){
			return false;
		}
		return true;
	}
	
	public  List<Clazz> getchildren(String orgid,String examid) {
		String sql = "SELECT clazz.id,clazz.examid,clazz.schoolCode,clazz.schoolName,clazz.code,if(clazz.name is null,org.org_name,clazz.name) name,clazz.wl,clazz.classType FROM `dw_dim_class` clazz LEFT JOIN 4a_org org ON clazz.schoolCode = org.org_code "
				+ "WHERE org.org_id = ? and examid=?  ";
		
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setString(0, orgid);
		sqlQuery.setString(1, examid);
		List list = sqlQuery.list();
		List<Clazz> listsubjects = new ArrayList<Clazz>();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				Clazz clazz = new Clazz();
				clazz.setId(Integer.parseInt(obj[0].toString()));
				clazz.setCode(obj[1].toString());
				clazz.setName(obj[5].toString());
				listsubjects.add(clazz);
			}
		}
		return listsubjects;
	}
	
	

}
