/*
 * @(#)com.cntest.fxpt.repository.impl.UplineScoreDaoImpl.java 1.0 2014年10月27日:下午3:03:32
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.UplineScore;
import com.cntest.fxpt.repository.IUplineScoreDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月27日 下午3:03:32
 * @version 1.0
 */
@Repository("IUplineScoreDao")
public class UplineScoreDaoImpl extends AbstractHibernateDao<UplineScore, Long>
    implements IUplineScoreDao {

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.repository.IUplineScoreDao#add(com.cntest.fxpt.domain .UplineScore)
   */
  @Override
  public void add(UplineScore uplineScore) {
    save(uplineScore);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.repository.IUplineScoreDao#delete(java.lang.Long)
   */
  @Override
  public void delete(Long examId) {
    String hql = "delete from UplineScore where exam.id=?";
    Query query = getSession().createQuery(hql);
    query.setLong(0, examId);
    query.executeUpdate();
  }

  @Override
  public void updateScore(UplineScore uplineScore) {
    super.update(uplineScore);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.repository.IUplineScoreDao#list(java.lang.Long, int)
   */
  @Override
  public List<UplineScore> list(Long examId, Integer wlType, String scoreType, Integer Level) {
    String hql = "from UplineScore where exam.id=? and wlType=? and  scoreType=? ";
    List<UplineScore> result = null;
    if (Level == null) {
      result = findByHql(hql, examId, wlType, scoreType);
    } else {
      hql = hql + " and level = ? ";
      result = findByHql(hql, examId, wlType, scoreType, Level);
    }
    return result;
  }

  @Override
  protected Class<UplineScore> getEntityClass() {
    return UplineScore.class;
  }

  @Override
  public List<UplineScore> listByUpline(Long examId) {
    String hql = "from UplineScore where exam.id=?";
    List<UplineScore> result = findByHql(hql, examId);
    return result;
  }

}
