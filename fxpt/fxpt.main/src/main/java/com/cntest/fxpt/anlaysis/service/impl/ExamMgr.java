/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.ExamMgr.java	1.0 2014年11月24日:下午1:18:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.Container;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:18:52
 * @version 1.0
 */
public class ExamMgr {
	private static ExamMgr mgr = null;

	private Container<Long, IExamContext> contextMap = new Container<>();

	private ExamMgr() {

	}

	public static ExamMgr getInstance() {
		synchronized (ExamMgr.class) {
			if (mgr == null) {
				mgr = new ExamMgr();
			}
		}
		return mgr;
	}

	public void put(Long examId, IExamContext context) {
		contextMap.put(examId, context);
	}

	public IExamContext get(Long examId) {
		return contextMap.get(examId);
	}

	public void remove(Long examId) {
		contextMap.remove(examId);
	}

	public int size() {
		return contextMap.size();
	}

}
