package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.repository.IAnalysisTestPaperDao;

@Repository("IAnalysisTestPaperDao")
public class IAnalysisTestPaperDaoImpl extends
		AbstractHibernateDao<AnalysisTestpaper, Long> implements
		IAnalysisTestPaperDao {

	@Override
	public void add(AnalysisTestpaper analysisTestpaper) {
		saveOrUpdate(analysisTestpaper);
	}

	@Override
	public void delete(AnalysisTestpaper analysisTestpaper) {
		super.delete(analysisTestpaper);
	}

	@Override
	public void deleteWithCombinationSubjectId(Long combinationSubjectId) {
		String hql = "delete from AnalysisTestpaper where combinationSubject.id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, combinationSubjectId);
		query.executeUpdate();
	}

	@Override
	public void deleteByTestPaperId(Long testPaperId) {
		String hql = "delete from AnalysisTestpaper  where testPaper.id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, testPaperId);
		query.executeUpdate();
	}

	@Override
	protected Class<AnalysisTestpaper> getEntityClass() {
		return AnalysisTestpaper.class;
	}

	@Override
	public List<AnalysisTestpaper> list(Long testPaperId) {
		String hql = "from AnalysisTestpaper where testPaper.id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, testPaperId);
		return query.list();
	}

	@Override
	public List<AnalysisTestpaper> listAllWith(Long examId) {
		String hql = "from AnalysisTestpaper at "
				+ "left join fetch at.subject km "
				+ "left join fetch at.testPaper "
				+ "left join fetch at.combinationSubject "
				+ "where at.exam.id=? order by km.ordernum";
		Query query = getSession().createQuery(hql);
		query.setLong(0, examId);
		return query.list();
	}

	@Override
	public void updatePaperType(Long testPaperId, int paperType) {
		String hql = "update AnalysisTestpaper set paperType=?  where testPaper.id=?";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, paperType);
		query.setLong(1, testPaperId);
		query.executeUpdate();
	}

	@Override
	public void updateExamAllAnalysisTestPaperPaperType(Long examId,
			int paperType) {
		String hql = "update AnalysisTestpaper set paperType=?  where exam.id=?";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, paperType);
		query.setLong(1, examId);
		query.executeUpdate();
	}

	@Override
	public List<AnalysisTestpaper> getAndDeleteHasCombinationSubject(Long exam) {
		List<AnalysisTestpaper> result = listAndCombinationSubjectIsNotNull(exam);
		String hql = "delete from AnalysisTestpaper  where exam.id=? and combinationSubject is not null";
		Query query = getSession().createQuery(hql);
		query.setLong(0, exam);
		query.executeUpdate();
		return result;
	}

	@Override
	public List<AnalysisTestpaper> listAndCombinationSubjectIsNotNull(
			Long examId) {
		String hql = "from AnalysisTestpaper at "
				+ "left join fetch at.subject "
				+ "left join fetch at.testPaper "
				+ "left join fetch at.combinationSubject "
				+ "where at.exam.id=? and at.combinationSubject is not null ";
		Query query = getSession().createQuery(hql);
		query.setLong(0, examId);
		return query.list();
	}

	@Override
	public AnalysisTestpaper getWithCombinationSubjectId(
			Long combinationSubjectId) {
		String hql = "from AnalysisTestpaper where combinationSubject.id=?";
		return findEntityByHql(hql, combinationSubjectId);
	}
	
	@Override
	public double findCombinationSubjectZFFromDwdimitem(Long examId,Long analysistestpaperid,String paper,String optionType){
		String hql = "SELECT SUM(fullscore) FROM dw_dim_item  where examid=? and testpaperid=? and paper=? and  optiontype IN (?)";
		SQLQuery query = getSession().createSQLQuery(hql);
		query.setParameter(0, examId);
		query.setParameter(1, analysistestpaperid);
		query.setParameter(2, paper);
		query.setParameter(3, optionType);
		Object obj = query.uniqueResult();
		if(obj==null){
			return 0;
		}
		return (Double)obj;
	}

}
