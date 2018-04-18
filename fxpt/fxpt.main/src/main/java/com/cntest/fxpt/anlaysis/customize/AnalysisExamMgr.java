/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.ExamMgr.java	1.0 2014年11月24日:下午1:18:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.service.impl.ExamContext;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.domain.Exam;
import com.cntest.util.ExceptionHelper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:18:52
 * @version 1.0
 */
public class AnalysisExamMgr {
	private static AnalysisExamMgr mgr = null;
	private Logger log = LoggerFactory.getLogger(AnalysisExamMgr.class);
	private Lock loadLock = new ReentrantLock();
	private Container<Long, IExamContext> contextMap = new Container<>();

	private AnalysisExamMgr() {

	}

	public static AnalysisExamMgr getInstance() {
		synchronized (AnalysisExamMgr.class) {
			if (mgr == null) {
				mgr = new AnalysisExamMgr();
			}
		}
		return mgr;
	}

	public IExamContext get(Exam exam) {
		IExamContext ec = null;
		loadLock.lock();
		try {
			ec = contextMap.get(exam.getId());
			if (ec == null) {
				ec = new ExamContext(exam);
				contextMap.put(exam.getId(), ec);
				LoadExamData loadExamData = new LoadExamData();
				loadExamData.loadExamData(ec);
			}
		} catch (Exception e) {
			log.error(ExceptionHelper.trace2String(e));
		} finally {
			loadLock.unlock();
		}

		return ec;
	}

	public void remove(Long examId) {
		contextMap.remove(examId);
	}

	public int size() {
		return contextMap.size();
	}

}
