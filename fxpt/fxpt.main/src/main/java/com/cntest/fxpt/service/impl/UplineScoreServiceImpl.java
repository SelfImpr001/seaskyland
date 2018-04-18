/*
 * @(#)com.cntest.fxpt.service.impl.UplineScoreServiceImpl.java 1.0 2014年10月27日:下午3:11:08
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import com.cntest.fxpt.domain.UplineScore;
import com.cntest.fxpt.repository.IUplineScoreDao;
import com.cntest.fxpt.service.IUplineScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月27日 下午3:11:08
 * @version 1.0
 */
@Service("IUplineScoreService")
public class UplineScoreServiceImpl implements IUplineScoreService {
  private static final Logger log = LoggerFactory.getLogger(UplineScoreServiceImpl.class);

  @Autowired(required = false)
  @Qualifier("IUplineScoreDao")
  private IUplineScoreDao uplineDao;

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.IUplineScoreService#add(java.util.List)
   */
  @Override
  public void add(Long examId, List<UplineScore> uplineScores) {
    uplineDao.delete(examId);
    if (uplineScores != null && !uplineScores.isEmpty()) {
      for (UplineScore us : uplineScores) {
        uplineDao.add(us);
      }
    } else {
      log.debug("uplineScores is null or is empty!");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.IUplineScoreService#list(java.lang.Long, int)
   */
  @Override
  public List<UplineScore> list(Long examId, Integer wlType, String scoreType, Integer level) {
    return uplineDao.list(examId, wlType, scoreType, level);
  }

	@Override
	public List<UplineScore> listByUpline(Long examId) {
		return uplineDao.listByUpline(examId);
	}

	@Override
	public void updateScore(UplineScore uplineScore){
		uplineDao.updateScore(uplineScore);
	}

}
