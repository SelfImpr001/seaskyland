/*
 * @(#)com.cntest.fxpt.repository.IUplineScoreDao.java 1.0 2014年10月27日:下午3:00:31
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import com.cntest.fxpt.domain.UplineScore;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月27日 下午3:00:31
 * @version 1.0
 */
public interface IUplineScoreDao {
  public void add(UplineScore uplineScore);

  public void delete(Long examId);

  public List<UplineScore> list(Long examId, Integer wlType, String scoreType, Integer Level);

	public List<UplineScore> listByUpline(Long examId);

	public void updateScore(UplineScore uplineScore);

}
