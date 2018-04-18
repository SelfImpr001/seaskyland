/*
 * @(#)com.cntest.fxpt.service.impl.CombinationSubjectImpl.java	1.0 2014年6月12日:下午3:44:02
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.CombinationSubject;
import com.cntest.fxpt.domain.CombinationSubjectCalculateRule;
import com.cntest.fxpt.domain.CombinationSubjectXTestPaper;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.repository.CombinationSubjectCalculateRuleDao;
import com.cntest.fxpt.repository.IAnalysisTestPaperDao;
import com.cntest.fxpt.repository.ICombinationSubjectDao;
import com.cntest.fxpt.repository.ICombinationSubjectXTestPaperDao;
import com.cntest.fxpt.repository.ISubjectDao;
import com.cntest.fxpt.service.ICombinationSubjectService;
import com.cntest.fxpt.util.SystemConfig;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午3:44:02
 * @version 1.0
 */
@Service("ICombinationSubjectService")
public class CombinationSubjectServiceImpl implements
		ICombinationSubjectService {

	private static final Logger log = LoggerFactory
			.getLogger(CombinationSubjectServiceImpl.class);

	@Autowired(required = false)
	@Qualifier("ICombinationSubjectDao")
	private ICombinationSubjectDao combinationSubjectDao;

	@Autowired(required = false)
	@Qualifier("ICombinationSubjectXTestPaperDao")
	private ICombinationSubjectXTestPaperDao combinationSubjectXTestPaperDao;
	@Autowired(required = false)
	@Qualifier("CombinationSubjectCalculateRuleDao")
	private CombinationSubjectCalculateRuleDao combinationSubjectCalculateRuleDao;

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperDao")
	private IAnalysisTestPaperDao analysisTestPaperDao;
	
	@Autowired(required = false)
	@Qualifier("ISubjectDao")
	private ISubjectDao subjectDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ICombinationSubject#add(com.cntest.fxpt.domain
	 * .CombinationSubject)
	 */
	@Override
	public void add(CombinationSubject combinationSubject) {
		saveOrUpdate(combinationSubject);
	}

	private void saveOrUpdate(CombinationSubject combinationSubject) {
		combinationSubjectDao.add(combinationSubject);
		for (CombinationSubjectXTestPaper csXtp : combinationSubject
				.getChildTestPaper()) {
			csXtp.setCombinationSubject(combinationSubject);
		}
		if (combinationSubject.getCombinationSubjectCalculateRules() != null) {
			for (CombinationSubjectCalculateRule rule : combinationSubject
					.getCombinationSubjectCalculateRules()) {
				rule.setCombinationSubject(combinationSubject);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ICombinationSubject#delete(com.cntest.fxpt.domain
	 * .CombinationSubject)
	 */
	@Override
	public void delete(CombinationSubject combinationSubject) {
		combinationSubjectDao.delete(combinationSubject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.ICombinationSubject#update(com.cntest.fxpt.domain
	 * .CombinationSubject)
	 */
	@Override
	public void update(CombinationSubject combinationSubject) {
		combinationSubjectXTestPaperDao
				.deleteByCombinationSubjectId(combinationSubject.getId());
		combinationSubjectCalculateRuleDao.delete(combinationSubject.getId());
		saveOrUpdate(combinationSubject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.ICombinationSubject#get(int)
	 */
	@Override
	public CombinationSubject get(Long combinationSubjectId) {
		return combinationSubjectDao.get(combinationSubjectId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.ICombinationSubject#listByExamId(int)
	 */
	@Override
	public List<CombinationSubject> listByExamId(Long examId) {
		return combinationSubjectDao.listByExamId(examId);
	}

	@Override
	public void add(Long examId, List<CombinationSubject> combinationSubjects) {

		HashMap<Long, Integer> beUpdateCombinationIdMap = new HashMap<>();
		for (CombinationSubject combinationSubject : combinationSubjects) {
			if (combinationSubject.getId() != null) {
				combinationSubjectXTestPaperDao
						.deleteByCombinationSubjectId(combinationSubject
								.getId());
			}
			add(combinationSubject);
			beUpdateCombinationIdMap.put(combinationSubject.getId(), 1);
		}

		combinationSubjectDao.flushSession();

		List<CombinationSubject> oldCombinationSubjects = combinationSubjectDao
				.list(examId, false);
		for (CombinationSubject combinationSubject : oldCombinationSubjects) {
			if (beUpdateCombinationIdMap.get(combinationSubject.getId()) == null) {
				combinationSubjectDao.delete(combinationSubject);
				analysisTestPaperDao
						.deleteWithCombinationSubjectId(combinationSubject
								.getId());
			} else {
				saveAndUpdateAnalysisTestpaper(combinationSubject);
			}
		}

	}

	private void saveAndUpdateAnalysisTestpaper(
		CombinationSubject combinationSubject) {
		Subject ss = null;
		
		AnalysisTestpaper testpaper = analysisTestPaperDao
				.getWithCombinationSubjectId(combinationSubject.getId());
		if (testpaper == null) {
			testpaper = new AnalysisTestpaper();
		}
		
		if("wuhou".equals(SystemConfig.newInstance().getValue(
				"area.org.code"))){
			if("全科毕业总分".equals(combinationSubject.getName())){
				ss = subjectDao.get(998L);
			}else if("主科毕业总分".equals(combinationSubject.getName())){
				ss = subjectDao.get(997L);
			}else if("升学总分".equals(combinationSubject.getName())){
				ss = subjectDao.get(999L);
			}
			testpaper.setSubject(ss);
		}
		testpaper.setExam(combinationSubject.getExam());
		testpaper.setName(combinationSubject.getName());
		testpaper.setFullScore(combinationSubject.getFullScore(analysisTestPaperDao));
		testpaper.setKgScore(combinationSubject.getKgScore(analysisTestPaperDao));
		testpaper.setZgScore(combinationSubject.getZgScore(analysisTestPaperDao));
		testpaper.setPaperType(combinationSubject.getPaperType());
		testpaper.setCombinationSubject(combinationSubject);
		analysisTestPaperDao.add(testpaper);
	}

	@Override
	public void delete(Long examId) {
		combinationSubjectXTestPaperDao.delete(examId);
		combinationSubjectDao.delete(examId);
	}

}
