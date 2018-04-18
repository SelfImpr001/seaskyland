/*
 * @(#)com.cntest.fxpt.repository.impl.TestPaperDaoImpl.java	1.0 2014年5月22日:上午9:40:34
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.repository.ITestPaperDao;
import com.cntest.fxpt.util.OptionHelper;

import net.sf.json.JSONArray;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 上午9:40:34
 * @version 1.0
 */
@Repository("ITestPaperDao")
public class TestPaperDaoImpl extends AbstractHibernateDao<TestPaper, Long> implements ITestPaperDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.ITestPaperDao#add(com.cntest.fxpt.domain.
	 * TestPaper )
	 */
	@Override
	public void add(TestPaper testPaper) {
		this.save(testPaper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ITestPaperDao#delete(com.cntest.fxpt.domain
	 * .TestPaper)
	 */
	@Override
	public void delete(TestPaper testPaper) {
		String sql = "delete from kn_combinationsubjectxtestpaper where testPaperId=?";
		SQLQuery query = createSQLQuery(sql);
		query.setParameter(0, testPaper.getId());
		query.executeUpdate();
		super.delete(testPaper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.ITestPaperDao#update(com.cntest.fxpt.domain
	 * .TestPaper)
	 */
	@Override
	public void update(TestPaper testPaper) {
		super.update(testPaper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.ITestPaperDao#listByExamId(int)
	 */
	@Override
	public List<TestPaper> list(Long examId) {
		String hql = "from TestPaper as tp where tp.exam.id=?";
		return findByHql(hql, examId);
	}

	@Override
	protected Class<TestPaper> getEntityClass() {
		return TestPaper.class;
	}

	@Override
	public TestPaper get(Long testPaperId) {
		return super.get(testPaperId);
	}

	@Override
	public TestPaper get(Long examId, String testPaperName) {
		String hql = "from TestPaper as tp where tp.exam.id=? and tp.name=?";
		return findEntityByHql(hql, examId, testPaperName);
	}

	@Override
	public int getCombinationSubjectCount(TestPaper testPaper) {
		String sql = "select count(1) from tb_combinationsubjectxtestpaper where testPaperId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, testPaper.getId());
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
	public int findByid(Long examId) {
		String hql = "SELECT COUNT(DISTINCT(testPaperid)) FROM dw_testpapercj_fact WHERE examid=?";
		SQLQuery sqlQuery = createSQLQuery(hql);
		sqlQuery.setParameter(0, examId);
		Object obj = sqlQuery.uniqueResult();
		int result = 0;
		if (obj instanceof Integer) {
			result = (Integer) obj;
		} else if (obj instanceof BigInteger) {
			BigInteger tmp = (BigInteger) obj;
			result = tmp.intValue();
		}
		return result;
	}

	@Override
	public int updateTestPaperImportCjStatus(Long testPaperId, boolean hasImportCj, int paperType) {
		String hql = "update TestPaper set hasCj=?,paperType=? where id=? ";
		Query query = getSession().createQuery(hql);
		query.setBoolean(0, hasImportCj);
		query.setInteger(1, paperType);
		query.setLong(2, testPaperId);
		int num = query.executeUpdate();
		return num;
	}

	@Override
	public int updateExamAllTestPaperImportCjStatus(Long examId, boolean hasImportCj, int paperType) {
		String hql = "update TestPaper set hasCj=?,paperType=? where exam.id=? ";
		Query query = getSession().createQuery(hql);
		query.setBoolean(0, hasImportCj);
		query.setInteger(1, paperType);
		query.setLong(2, examId);
		int num = query.executeUpdate();
		return num;
	}

	@Override
	public int updateTestPaperImportItemStatus(Long testPaperId, boolean hasImportItem) {
		String hql = "update TestPaper set hasItem=? where id=? ";
		Query query = getSession().createQuery(hql);
		query.setBoolean(0, hasImportItem);
		query.setLong(1, testPaperId);
		int num = query.executeUpdate();
		return num;
	}

	@Override
	public void updateMainSubject(List<TestPaper> testPapers) {

		for (int i = 0; i < testPapers.size(); i++) {
			TestPaper testPaper = testPapers.get(i);
			String hql = "update TestPaper set masterSubject=?,containPaper=?,hasAnalysis=? where id=? ";

			Query query = getSession().createQuery(hql);
			query.setBoolean(0, testPaper.isMasterSubject());
			query.setBoolean(1, testPaper.isContainPaper());
			query.setBoolean(2, testPaper.isHasAnalysis());
			query.setLong(3, testPaper.getId());
			query.executeUpdate();
		}
	}

	@Override
	public void updateSelOptions(Long testPaperId) {
		// 根据细目表和成绩库查找所有选项
		String sql = " SELECT DISTINCT rightOptioin FROM dw_dim_item WHERE titleType = '选择题' AND testPaperId = ? "
				+ " UNION ALL"
				+ " SELECT DISTINCT selOption FROM dw_itemcj_fact WHERE selOption IS NOT NULL AND selOption <> '' AND testPaperId = ? ";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setLong(0, testPaperId);
		sqlQuery.setLong(1, testPaperId);

		List list = sqlQuery.list();
		if (list.size() > 0) {
			// 根据选项查找对应选项列表转换成json数据
			List<String> options = OptionHelper.getOrderList(list);
			JSONArray array = JSONArray.fromObject(options);

			// 存储json数据
			String hql = "update TestPaper set selOptions=? where id=? ";
			Query query = getSession().createQuery(hql);
			query.setString(0, array.toString());
			query.setLong(1, testPaperId);
			query.executeUpdate();
		}
	}

	public int updateTestpaperStatus(Long testPaperId, Long examid) {
		String hql = "UPDATE TestPaper SET containPaper=1 WHERE examid=? AND id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, examid);
		query.setLong(1, testPaperId);
		int num = query.executeUpdate();
		return num;
	}

	@Override
	public TestPaper selectPaperByExamIdAndPaperId(Long examId, Long testPaperId) {
		String hql = "from TestPaper as tp where tp.exam.id=? and tp.id=?";
		return findEntityByHql(hql, examId, testPaperId);
	}
}
