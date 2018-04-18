/*
 * @(#)com.cntest.fxpt.service.impl.SchoolServiceImpl.java	1.0 2014年6月3日:上午8:51:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.remote.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.service.OrganizationService;
import com.cntest.remote.domain.NanShanOrgData;
import com.cntest.remote.repository.INanShanOrgDataDao;
import com.cntest.remote.service.INanShanOrgDataService;

/**
 * @author cheny 2016年12月6日
 * @version 1.0
 */
@Service("INanShanOrgDataService")
public class NanShanOrgDataServiceImpl implements INanShanOrgDataService {

	@Autowired(required = false)
	@Qualifier("INanShanOrgDataDao")
	private INanShanOrgDataDao nanShanOrgDataDao;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Override
	public void add(NanShanOrgData nanShanOrgData) throws BusinessException {
		//4a_org添加组织信息
		Organization org= new Organization();
		//根据所有父级数据判断本身的组织类型
		Organization orgParent= null;
		//根据父级组织代码获取对应的id
		if(nanShanOrgData.getParent()!=null && nanShanOrgData.getParent()!="")
			orgParent=organizationService.getOrgByCode(nanShanOrgData.getParent());
		int type=1;
		if(orgParent!=null){
			type=orgParent.getType()>0?orgParent.getType()+1:1;
		}
		org.setCode(nanShanOrgData.getOrgCode());
		org.setName(nanShanOrgData.getDisplayName());
		org.setType(type);
		org.setParent(orgParent);
		organizationService.create(org);
	}
	@Override
	public void updateState(String  orgCode) throws BusinessException {
		Organization org = organizationService.getOrgByCode(orgCode);
		organizationService.deleteOrg(org);
	}
	@Override
	public void delete(NanShanOrgData nanShanOrgData) {
		nanShanOrgDataDao.delete(nanShanOrgData);
	}

	@Override
	public NanShanOrgData findByCode(String orgCode) {
		return nanShanOrgDataDao.findByCode(orgCode);
	}

	@Override
	public boolean isExistData(NanShanOrgData nanShanOrgData) {
		nanShanOrgData = findByCode(nanShanOrgData.getOrgCode());
		return nanShanOrgData==null?false:true;
	}
	@Override
	public void update(NanShanOrgData nanShanOrgData) throws Exception {
		//4a_org添加组织信息
		Organization org= organizationService.getOrgByCode(nanShanOrgData.getOrgCode());
		Organization orgParent= null;
		//根据父级组织代码获取对应的id
		if(nanShanOrgData.getParent()!=null && nanShanOrgData.getParent()!="")
			orgParent=organizationService.getOrgByCode(nanShanOrgData.getParent());
		int type=1;
		if(orgParent!=null){
			type=orgParent.getType()>0?orgParent.getType()+1:1;
		}
		//组织类型做了修改并且存在下级
		if(org.getType()!=type){
			updateChild(org.getPk(),org.getType()-type);
		}
		org.setCode(nanShanOrgData.getOrgCode());
		org.setName(nanShanOrgData.getDisplayName());
		org.setType(type);
		org.setParent(orgParent);
		organizationService.update(org);
	}
	
	private void updateChild(Long pk, int type) throws BusinessException{
		List<Organization> childs = organizationService.getNextOrgList(pk);
		for (Organization organization : childs) {
			organization.setType(organization.getType()-type);			
			organizationService.update(organization);
			updateChild(organization.getPk(), type);
		}
	}
	

}
