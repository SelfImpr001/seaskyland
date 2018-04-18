/*
 * @(#)com.cntest.fxpt.repository.ITestPaperDao.java	1.0 2014年5月22日:上午9:39:12
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.HashSet;
import java.util.List;

import com.cntest.fxpt.domain.TestPaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 上午9:39:12
 * @version 1.0
 */
public interface ITestPaperDao {
	public void add(TestPaper testPaper);

	public void delete(TestPaper testPaper);

	public void update(TestPaper testPaper);

	public TestPaper get(Long testPaperId);

	public TestPaper get(Long examId, String testPaperName);

	public List<TestPaper> list(Long examId);

	public int getCombinationSubjectCount(TestPaper testPaper);

	public int updateTestPaperImportCjStatus(Long testPaperId,
			boolean hasImportCj, int paperType);

	public int updateExamAllTestPaperImportCjStatus(Long examId,
			boolean hasImportCj, int paperType);

	public int updateTestPaperImportItemStatus(Long testPaperId,
			boolean hasImportItem);
	public void updateMainSubject(List<TestPaper> testPapers);

	public int findByid(Long examId);
	
	/** 更新选做题答案 */
	public void updateSelOptions(Long testPaperId);
	
	public int updateTestpaperStatus(Long testPaperId,Long examid);
	
	/**
	 * 根据考试Id和试卷Id查询试卷信息
	 * @param examId  考试Id
	 * @param testPaperId 试卷Id
	 * @return
	 */
	public TestPaper selectPaperByExamIdAndPaperId(Long examId,Long testPaperId);
	
}
