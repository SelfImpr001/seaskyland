/*
 * @(#)com.cntest.fxpt.service.impl.TestPaperServiceImpl.java	1.0 2014年5月22日:上午10:36:00
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.repository.IAnalysisTestPaperDao;
import com.cntest.fxpt.repository.ICjDao;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.repository.IITemDao;
import com.cntest.fxpt.repository.IShowMessageDao;
import com.cntest.fxpt.repository.ITestPaperDao;
import com.cntest.fxpt.service.ITestPaperService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 上午10:36:00
 * @version 1.0
 */
@Service("ITestPaperService")
public class TestPaperServiceImpl implements ITestPaperService {

	@Autowired(required = false)
	@Qualifier("IExamDao")
	private IExamDao examDao;

	@Autowired(required = false)
	@Qualifier("ITestPaperDao")
	private ITestPaperDao testPaperDao;

	@Autowired(required = false)
	@Qualifier("IITemDao")
	private IITemDao itemDao;

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperDao")
	private IAnalysisTestPaperDao analysisTestPaperDao;

	@Autowired(required = false)
	@Qualifier("ICjDao")
	private ICjDao cjDao;
	
	@Autowired(required = false)
	@Qualifier("IShowMessageDao")
	private IShowMessageDao showMessageDao;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ITestPaperService#add(com.cntest.fxpt.domain.
	 * TestPaper)
	 */
	@Override
	public void add(TestPaper testPaper) {
		testPaperDao.add(testPaper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ITestPaperService#delete(com.cntest.fxpt.domain
	 * .TestPaper)
	 */
	@Override
	public void delete(TestPaper testPaper) {
		cjDao.deleteCj(testPaper.getId());
		itemDao.deleteItem(testPaper.getId());
		analysisTestPaperDao.deleteByTestPaperId(testPaper.getId());
		testPaperDao.delete(testPaper);
		Exam exam = examDao.findById(testPaper.getExam().getId());
		showMessageDao.deleteMessageByExamid(exam.getId(), 2, testPaper.getId());
		if (testPaper.isHasCj()) {
			if (exam.getImpCjCount() > 0) {
				exam.setImpCjCount(exam.getImpCjCount() - 1);
			} else {
				exam.setImpCjCount(0);
			}
		}

		if (exam.getImpItemCount() > 0) {
			exam.setImpItemCount(exam.getImpItemCount() - 1);
		} else {
			exam.setImpItemCount(0);
		}
		examDao.update(exam);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ITestPaperService#update(com.cntest.fxpt.domain
	 * .TestPaper)
	 */
	@Override
	public void update(TestPaper testPaper) {
		testPaperDao.update(testPaper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.ITestPaperService#listByExamId(int)
	 */
	@Override
	public List<TestPaper> listByExamId(Long examId) {
		return testPaperDao.list(examId);
	}

	@Override
	public TestPaper get(Long testPaperId) {
		return testPaperDao.get(testPaperId);
	}

	@Override
	public TestPaper get(Long examId, String testPaperName) {
		return testPaperDao.get(examId, testPaperName);
	}

	@Override
	public boolean isParticipateCombinationSubject(TestPaper testPaper) {
		int countNum = testPaperDao.getCombinationSubjectCount(testPaper);
		return countNum > 0 ? true : false;
	}

	@Override
	public void updateMainSubject(List<TestPaper> testPapers) {
		testPaperDao.updateMainSubject(testPapers);
		
	}

	@Override
	public void updateSelOptions(Long examId) {
		List<TestPaper> testPapers = testPaperDao.list(examId);
		for (TestPaper testPaper : testPapers) {
			testPaperDao.updateSelOptions(testPaper.getId());
		}
	}

	@Override
	public TestPaper selectPaperByExamIdAndPaperId(Long examId, Long testPaperId) {
		return testPaperDao.selectPaperByExamIdAndPaperId(examId, testPaperId);
	}

}
