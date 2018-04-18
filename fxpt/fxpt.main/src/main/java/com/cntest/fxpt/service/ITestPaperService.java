/*
 * @(#)com.cntest.fxpt.service.ITestPaperService.java	1.0 2014年5月22日:上午9:44:22
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.TestPaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 上午9:44:22
 * @version 1.0
 */
public interface ITestPaperService {
	public void add(TestPaper testPaper);

	public void delete(TestPaper testPaper);

	public void update(TestPaper testPaper);

	public TestPaper get(Long testPaperId);

	public TestPaper get(Long examId, String testPaperName);

	public List<TestPaper> listByExamId(Long examId);

	public void updateMainSubject(List<TestPaper> testPapers);
	
	public boolean isParticipateCombinationSubject(TestPaper testPaper);
	
	/** 更新选做题答案 */
	public void updateSelOptions(Long examId);
	/**
	 * 根据考试Id和试卷Id查询试卷信息
	 * @param examId  考试Id
	 * @param testPaperId 试卷Id
	 * @return
	 */
	public TestPaper selectPaperByExamIdAndPaperId(Long examId,Long testPaperId);
	
}
