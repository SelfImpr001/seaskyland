/*
 * @(#)com.cntest.fxpt.service.ISchoolService.java	1.0 2014年6月3日:上午8:50:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.service.impl.ExamMgr;
import com.cntest.web.view.Progress;
import com.cntest.web.view.ProgressListener;


/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月3日 上午8:50:10
 * @version 1.0
 */
@Service("IBachService")
public class BachServiceImpl implements  ProgressListener{
	@Override
	public Progress on(Map<String, String> params) {
		Long examId = Long.parseLong(params.get("examId"));
		IExamContext c = ExamMgr.getInstance().get(examId);
		Progress p = null;
		if (c != null) {
			int totalNum = c.getTaskTotalNum();
			int cur = c.getCompleteTaskNum();

			if (totalNum == cur && !c.isAllComplate()) {
				cur = cur - 1;
			}

			if (totalNum == cur && c.isAllComplate()) {
				ExamMgr.getInstance().remove(examId);
			}

			totalNum = totalNum <= 0 ? 100 : totalNum;
			cur = cur <= 0 ? 1 : cur;
			p = new Progress(totalNum, cur);
		} else {
			p = new Progress(1, 1);
		}
		return p;
	}
}
