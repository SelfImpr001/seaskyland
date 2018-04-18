/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.ScoreObjMgr.java	1.0 2014年11月25日:下午2:47:33
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.cntest.fxpt.anlaysis.bean.Score;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午2:47:33
 * @version 1.0
 */
public class ScoreObjMgr {
	private static Lock lock = new ReentrantLock();
	private static ScoreObjMgr mgr = null;
	private ConcurrentHashMap<Double, Score> scoreMap = new ConcurrentHashMap<>();
	private Lock scoreLock = new ReentrantLock();

	public static ScoreObjMgr newInstance() {
		lock.lock();
		try {
			if (mgr == null) {
				mgr = new ScoreObjMgr();
			}
		} finally {
			lock.unlock();
		}
		return mgr;
	}

	public Score getScore(Score score) {
		scoreLock.lock();
		Score result = null;
		try {
			result = scoreMap.get(score.getPk());
			if (result == null) {
				scoreMap.put(score.getPk(), score);
			}
			result = score;
		} finally {
			scoreLock.unlock();
		}
		return result;
	}

	public Score getScore(Double score) {
		Score tmpScore = new Score();
		tmpScore.setValue(score);
		return getScore(tmpScore);
	}

	public Map<Double, Score> getScoreMap() {
		return scoreMap;
	}

}
