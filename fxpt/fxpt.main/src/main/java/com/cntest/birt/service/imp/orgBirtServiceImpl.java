package com.cntest.birt.service.imp;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.birt.domain.OrgBirt;
import com.cntest.birt.domain.ReportScript;
import com.cntest.birt.repository.ReportScriptRepository;
import com.cntest.birt.repository.orgBirtRepository;
import com.cntest.birt.service.orgBirtService;
import com.cntest.common.query.Query;
import com.cntest.common.service.AbstractEntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;

@Transactional
@Service
public class orgBirtServiceImpl extends AbstractEntityService<OrgBirt, Long>implements orgBirtService {

	private static Logger logger = LoggerFactory.getLogger(orgBirtServiceImpl.class);

	@Autowired
	private orgBirtRepository orgBirtRepository;
	@Autowired
	private ReportScriptRepository ReportScriptRepository;

	@Autowired
	public orgBirtServiceImpl(orgBirtRepository orgBirtRepository) {
		this.setRepository(orgBirtRepository);
		this.orgBirtRepository = orgBirtRepository;
	}

	@Override
	public void query(Query<OrgBirt> query) {
		// TODO Auto-generated method stub
		orgBirtRepository.findReport(query);
		// super.query(query);
	}

	public void save(OrgBirt reportScript) {
		orgBirtRepository.save(reportScript);
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional(readOnly = true)
	public OrgBirt load(Long pk) throws BusinessException {
		return super.load(pk);
	}

	@Override
	@Transactional
	public void update(OrgBirt reportScript) {
		long begin = System.currentTimeMillis();
		String name = reportScript.getName();
		OrgBirt re = orgBirtRepository.load(reportScript.getPk());
		
		orgBirtRepository.cleatSession();
		orgBirtRepository.update(reportScript);

	}

	
/*新开始**/
	@Override
	public List<OrgBirt> getTopOrgList(String name) throws BusinessException {
		logger.info("OrganizationServiceImpl --");
		List<OrgBirt> listOrg =	orgBirtRepository.listByParentIsNull(name);
//		listOrg=	finCountUser(listOrg, null);
		return listOrg;
	}
	@Override
	public List<OrgBirt> nextOrgCount(List<OrgBirt> list) {		
		return orgBirtRepository.nextOrgCountOfParent(list);
	}
	public List<OrgBirt> getOrgChildrenNoLeaf(Long pk) {
		return orgBirtRepository.selectChildrenNotLeafFor(pk);
	}
	@Override
	public List<OrgBirt> getOrgSubList(Long pk) {
		return orgBirtRepository.listByParentFor(pk,"");
	}

	@Override
	public List<ReportScript> getOrgSubList(Long pk,String name) {
		List<ReportScript> tempOrgs  =	ReportScriptRepository.getPrens(pk);
		return tempOrgs;
		
	}
	
	@Override
	public void getOrgSubList(Long pk,Query<ReportScript> query) {
		
	
		ReportScriptRepository.getPrens(pk,query);
	}
	@Override
	public void getOrgSubLists(Long pk,Query<OrgBirt> query) {
		
	
		orgBirtRepository.listByParentFor(pk,query);
	}
	@Override
	public OrgBirt getOrgByPk(Long pk) {
		OrgBirt org	= orgBirtRepository.get(pk);
		return org;
	}

	@Override
	public void querys(Query<OrgBirt> query) {
		orgBirtRepository.findReport(query);
		
	}

	@Override
	public void removeAll(OrgBirt orgBirt) {
		orgBirtRepository.delete(orgBirt);
		orgBirtRepository.delete(orgBirt.getPk());
		
	}
	

}
