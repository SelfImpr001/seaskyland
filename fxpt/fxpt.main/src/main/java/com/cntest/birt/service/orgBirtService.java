package com.cntest.birt.service;

import java.util.List;

import com.cntest.birt.domain.OrgBirt;
import com.cntest.birt.domain.ReportScript;
import com.cntest.common.query.Query;
import com.cntest.common.service.EntityService;
import com.cntest.exception.BusinessException;




public interface orgBirtService extends EntityService<OrgBirt,Long>  {

	public OrgBirt load(Long pk) throws BusinessException;



	void update(OrgBirt orgBirt);



	List<OrgBirt> getTopOrgList(String name) throws BusinessException;



	List<OrgBirt> nextOrgCount(List<OrgBirt> list);



	List<OrgBirt> getOrgChildrenNoLeaf(Long pk);



	List<OrgBirt> getOrgSubList(Long pk);

	void getOrgSubList(Long pk, Query<ReportScript> query);


	public List<ReportScript> getOrgSubList(Long valueOf, String qname);



	public OrgBirt getOrgByPk(Long valueOf);



	





	public void querys(Query<OrgBirt> query);



	void getOrgSubLists(Long pk, Query<OrgBirt> query);



	public void removeAll(OrgBirt orgBirt);









}
