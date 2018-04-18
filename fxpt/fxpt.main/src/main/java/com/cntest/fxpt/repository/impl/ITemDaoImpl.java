/*
 * @(#)com.cntest.fxpt.repository.impl.ITemDaoImpl.java	1.0 2014年5月22日:下午4:18:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.ItemMessage;
import com.cntest.fxpt.repository.IITemDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午4:18:28
 * @version 1.0
 */
@Repository("IITemDao")
public class ITemDaoImpl extends AbstractHibernateDao<Item, Long> implements
		IITemDao {
	private static final Logger log = LoggerFactory
			.getLogger(ITemDaoImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.IITemDao#deleteByTestPaperId(int)
	 */
	@Override
	public int deleteItem(Long testPaperId) {
		String hql = "delete from Item  where testPaper.id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, testPaperId);
		return query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.IITemDao#listByTestPaperId(int)
	 */
	@Override
	public List<Item> list(Long testPaperId) {
		log.debug("执行获取小题信息的dao");

		String hql = "from Item as i left join fetch i.subject left join fetch i.testPaper as tp where tp.id=? order by sortNum";
		List<Item> items = findByHql(hql, testPaperId);

		log.debug("执行完毕获取小题信息的dao");

		return items;
	}

	@Override
	protected Class<Item> getEntityClass() {
		return Item.class;
	}

	@Override
	public int updateItemAnaysisTestPaperId(AnalysisTestpaper analysisTestpaper) {
		String hql = "update Item set analysisTestpaper.id=? where testPaper.id=? AND subject.id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, analysisTestpaper.getId());
		query.setLong(1, analysisTestpaper.getTestPaper().getId());
		query.setLong(2, analysisTestpaper.getSubject().getId());
		return query.executeUpdate();
	}

	@Override
	public List<Item> listByAnlaysisTestPaperId(Long AnlaysisTestPaperId) {
		String hql = "from Item where analysisTestpaper.id=?";
		return findByHql(hql, AnlaysisTestPaperId);
	}
	
	@Override
	public String findPaperByItemId(Long itemId,Long examid,String studentid){
		String sql = "SELECT paper FROM dw_dim_item ddi WHERE ddi.id=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter(0, itemId);
		Object obj = sqlQuery.uniqueResult();
		if(obj==null){
			return "";
		}
		return obj.toString();
	}
	
	@Override
	public String findOptionTypeByItemId(Long itemId,Long examid,String studentid){
		String sql = "SELECT optionType FROM dw_dim_item ddi WHERE ddi.id=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter(0, itemId);
		Object obj = sqlQuery.uniqueResult();
		if(obj==null){
			return "";
		}
		return obj.toString();
	}

	@Override
	public String findPaperTotalScoreByItemId(Long examid,
			String studentid, Long testpaperid,String paper) {
		String sql = "SELECT SUM(score) FROM dw_itemcj_fact WHERE examid=? AND studentid=? AND testpaperid=? AND paper=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter(0, examid);
		sqlQuery.setParameter(1, studentid);
		sqlQuery.setParameter(2, testpaperid);
		sqlQuery.setParameter(3, paper);
		Object obj = sqlQuery.uniqueResult();
		if(obj==null){
			return "";
		}
		return obj.toString();
	}
	
	@Override
	public String findRespectiveScoreByItemId(Long testpaperid,Long examid,String optiontype,String studentid,String paper){
		String sql = "";
		if("".equals(paper)){
			sql = " SELECT SUM(score) FROM dw_itemcj_fact  dif "
					+ "INNER JOIN  dw_dim_item ddi ON dif.itemid = ddi.id "
					+ "WHERE dif.examid = ? AND ddi.optiontype IN (?) AND dif.testpaperid=? AND dif.studentid=?";
		}else{
			sql = " SELECT SUM(score) FROM dw_itemcj_fact  dif "
					+ "INNER JOIN  dw_dim_item ddi ON dif.itemid = ddi.id "
					+ "WHERE dif.examid = ? AND ddi.optiontype IN (?) AND dif.testpaperid=? AND dif.studentid=? AND dif.paper=?";
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter(0, examid);
		sqlQuery.setParameter(1, optiontype);
		sqlQuery.setParameter(2, testpaperid);
		sqlQuery.setParameter(3, studentid);
		if(!"".equals(paper)){
			sqlQuery.setParameter(4, paper);
		}
		Object obj = sqlQuery.uniqueResult();
		if(obj==null){
			return "";
		}
		return obj.toString();
	}

	@Override
	public List<Item> findAllQk(Long examId,Long testPaperId) {
		//查询所有的选做题
		String hql ="From Item where ischoice=1 and examId=? and testPaperId=?";
		List<Item> itemList = findByHql(hql,examId,testPaperId);		
		return itemList;
	}
	
	public boolean isContainABpaper(Long examId,Long testPaperId){
		String sql = "SELECT GROUP_CONCAT(DISTINCT( paper)) FROM dw_dim_item ddi WHERE ddi.testpaperid=? AND ddi.examid = ?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter(0, testPaperId);
		sqlQuery.setParameter(1, examId);
		Object obj = sqlQuery.uniqueResult();
		if(obj!=null){
			if(obj.toString().equals("A,B")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean validate(String subjectName, String examId) {
		boolean flag=false;
		String sql = "SELECT it.* FROM dw_dim_item it LEFT JOIN kn_subject su ON su.id=it.subjectid  "
				+ "WHERE it.examid="+examId+" AND su.name='"+subjectName+"'";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		flag=sqlQuery.list().size()>0?true:false;
		return flag;
	}

}
