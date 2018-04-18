/*
 * @(#)com.cntest.fxpt.service.IUplineScoreService.java 1.0 2014年10月27日:下午3:08:53
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import com.cntest.fxpt.domain.UplineScore;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月27日 下午3:08:53
 * @version 1.0
 */
public interface IUplineScoreService {
  public void add(Long examId, List<UplineScore> uplineScores);

  public List<UplineScore> list(Long examId, Integer wlType, String scoreType, Integer level);

	public List<UplineScore> listByUpline(Long examId);

	public void updateScore(UplineScore uplineScore);
}
