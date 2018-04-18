/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.service.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.common.service.AbstractEntityService;
import com.cntest.exception.BusinessException;
import com.cntest.fxpt.domain.EducationMonitor;
import com.cntest.fxpt.domain.EducationSurvey;
import com.cntest.fxpt.domain.FSDataNnalysis;
import com.cntest.fxpt.repository.EducationMonitorDao;
import com.cntest.fxpt.repository.EducationSurveyRepository;
import com.cntest.fxpt.repository.IAnalysisDao;
import com.cntest.fxpt.service.EducationSurveyService;


@Transactional
@Service														   
public class EducationSurveyServiceImpl extends AbstractEntityService<EducationSurvey,Long> implements EducationSurveyService {
	private static Logger logger = LoggerFactory.getLogger(EducationSurveyServiceImpl.class);

	@Autowired
	private EducationSurveyRepository educationSurveyRepository;
	
	@Autowired
	private IAnalysisDao analysisDao;
	@Autowired
	private EducationMonitorDao educationmonitordao;
	
	public EducationSurveyServiceImpl() {}
	@Autowired
	public EducationSurveyServiceImpl(EducationSurveyRepository educationSurveyRepository) {
		this.educationSurveyRepository = educationSurveyRepository;
		this.setRepository(this.educationSurveyRepository);
	}
	
	@Override
	public List<EducationSurvey> getTopEduList(String name) {
		logger.info("OrganizationServiceImpl --");
		return educationSurveyRepository.listByParentIsNull(name);
	}
	@Override
	public List<EducationSurvey> getEduSubList(Long pk) {
		return educationSurveyRepository.listByParentFor(pk, "");
	}
	@Override
	public EducationSurvey getEduByPk(Long pk) {
		return educationSurveyRepository.get(pk);
	}
	@Override
	public void savaEdu(EducationSurvey edu) {
		educationSurveyRepository.savaEdu(edu);
	}
	@Override
	@Transactional
	public void create(EducationSurvey org) throws BusinessException{
		educationSurveyRepository.save(org);
	}
	@Override
	public int findOrgList() {
		return educationSurveyRepository.findOrgList();
	}
	@Override
	public List<EducationSurvey> getEduSubList(Long pk, String name) {
		return educationSurveyRepository.listByParentFor(pk, name);
	}
	@Override
	public void getEduSubList(Long pk, Query<EducationSurvey> query) {
		educationSurveyRepository.listByParentFor(pk, query);
	}
	@Override
	public List<EducationSurvey> getEduChildrenNoLeaf(Long pk) {
		return educationSurveyRepository.selectChildrenNotLeafFor(pk);
	}
	@Override
	public void list(Long pk,Query<FSDataNnalysis> query) {
		analysisDao.list(pk, query);
	}
	@Override
	public List<FSDataNnalysis> reportgeneratelist(Page<FSDataNnalysis> page) {
		// TODO Auto-generated method stub
		return analysisDao.reportgeneratelist(page);
	}
	
	public EducationMonitor get(Long pk) {
		return educationmonitordao.get(pk);
	}
	
	public void deletem(Long pk) {
		educationmonitordao.deletem(pk);
	}
	
	public void updatem(EducationMonitor m) {
		educationmonitordao.updatem(m);
	}
	
	public void savem(EducationMonitor m ) {
		educationmonitordao.savem(m);
	}
	@Override
	public void saveReportData(FSDataNnalysis fsdn) {
		educationmonitordao.saveReportData(fsdn);
	}
	@Override
	public FSDataNnalysis getFSDataNnalysisById(Long id) {
		// TODO Auto-generated method stub
		return educationSurveyRepository.getFSDataNnalysisById(id);
	}
}

