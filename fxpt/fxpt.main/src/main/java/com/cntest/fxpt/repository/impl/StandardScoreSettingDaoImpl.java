/*
 * @(#)com.cntest.fxpt.repository.impl.StandardScoreSettingDaoImpl.java	1.0 2015年4月20日:下午5:06:20
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.LeveScoreSetting;
import com.cntest.fxpt.repository.IStandardScoreSettingDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2015年4月20日 下午5:06:20
 * @version 1.0
 */
@Repository("StandardScoreSettingDao")
public class StandardScoreSettingDaoImpl implements IStandardScoreSettingDao{
	SessionFactory sessionFactory;
	@Resource(name="sessionFactory")
	public void setSessionFactory0(SessionFactory sessionFactory){
	  this.sessionFactory = sessionFactory;
	}
	
	public List<Object[]> findList(Long examid) {
		List<Object[]> list = new ArrayList<Object[]>();
		String sql = " SELECT t.id,t.subjectid,t.name,t.paperType,t.examid,t.fullscore,s.zvalue FROM dw_dim_analysis_testpaper t "+
					 " LEFT JOIN kn_standardScoreSetting s "+
    				 " ON t.examid=s.examid AND t.subjectid=s.subjectid  AND t.paperType=s.wl"+
    				 " WHERE t.examid="+examid+
    				 " and t.subjectid<>98"+ 
    				 " GROUP BY t.paperType,t.subjectid";
		SQLQuery sqlQuery = createSQLQuery(sql);
		list = sqlQuery.list();
		return list;
	}
	
	public String getZvalue(Long examid,int wl){
		String sql = "SELECT zvalue FROM kn_standardscoresetting WHERE examid="+examid+" AND subjectid=98 and wl="+wl;
		SQLQuery sqlQuery = createSQLQuery(sql);
		String str = (String) sqlQuery.uniqueResult();
		return str;
	}
	
	
		public List<String>  getWl(Long examid){
		List<String> list = new ArrayList<String>();
		String sql = "SELECT wl FROM dw_examstudent_fact WHERE examid="+examid+" GROUP BY wl";
		SQLQuery sqlQuery = createSQLQuery(sql);
		List<Object[]> reslist = sqlQuery.list();
		for (int i = 0; i < reslist.size(); i++) {
			Object obj = reslist.get(i);
			list.add(obj.toString());
		}
		return list;
	}
	

	
	
	
	protected SQLQuery createSQLQuery(String queryString) {
		Session session = getSession();
		return session.createSQLQuery(queryString);
	}
	protected Session getSession() {
		try {
			return this.sessionFactory.getCurrentSession();
		}catch(Exception e) {
			
		}

		return this.sessionFactory.openSession();
	}
	

	public void save(Long examid, int subjectid, String zvalue,int wl) {
	 String sql = " SELECT * FROM kn_standardscoresetting WHERE examid="+examid+" AND subjectid="+subjectid+" AND wl="+wl;
	 SQLQuery sqlQuery = createSQLQuery(sql);
	 if(sqlQuery.list()!=null && sqlQuery.list().size()>0){
		 sql = "UPDATE kn_standardscoresetting SET zvalue='"+zvalue+"' WHERE examid="+examid+" AND subjectid="+subjectid+" AND wl="+wl;
	 }else{
		 sql = "INSERT INTO kn_standardscoresetting(subjectid,examid,zvalue,wl) VALUES("+subjectid+","+examid+",'"+zvalue+"',"+wl+")";
	 }
	 sqlQuery = createSQLQuery(sql);
	 sqlQuery.executeUpdate();
	}
}
