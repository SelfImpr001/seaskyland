package com.cntest.birt.service;

import java.io.IOException;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;

import com.cntest.birt.domain.OrgBirt;
import com.cntest.birt.domain.ReportScript;
import com.cntest.common.query.Query;
import com.cntest.common.service.EntityService;
import com.cntest.exception.BusinessException;




public interface ReportScriptService extends EntityService<ReportScript,Long>  {

	public ReportScript load(Long pk) throws BusinessException;

	void mutiRoport(ReportScript reportScript) throws BirtException, IOException;


	ReportScript mutiRoportWord(ReportScript ReportScript) throws BirtException, BusinessException;

	void update(ReportScript reportScript);

	void query(Query<ReportScript> query, List<OrgBirt> orgList);


}
