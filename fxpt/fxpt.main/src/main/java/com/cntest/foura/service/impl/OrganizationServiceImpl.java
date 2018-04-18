/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service.impl;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.query.Query;
import com.cntest.common.service.AbstractEntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.UserBelong;
import com.cntest.foura.repository.DataPermissionRepository;
import com.cntest.foura.repository.OrganizationRepository;
import com.cntest.foura.repository.UserBelongRepository;
import com.cntest.foura.service.OrganizationService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.repository.IExamDao;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
@Transactional
@Service														   
public class OrganizationServiceImpl extends AbstractEntityService<Organization,Long> implements OrganizationService {
	private static Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private IExamDao examDao;

	@Autowired
	private UserBelongRepository userBelong;
	
	@Autowired
	private DataPermissionRepository dataPermissionRepository;
	
	public OrganizationServiceImpl() {}
	@Autowired
	public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
		this.setRepository(this.organizationRepository);
	}
	
	@Override
	public List<Organization> getTopOrgList(String name) throws BusinessException {
		logger.info("OrganizationServiceImpl --");
		List<Organization> listOrg =	organizationRepository.listByParentIsNull(name);
//		listOrg=	finCountUser(listOrg, null);
		return listOrg;
	}
	@Override
	public List<Organization> finCountUser(List<Organization> orgs,Organization org) throws BusinessException{
		List<Organization> contOrg = this.list();
		toUserResources(contOrg, orgs);
		int count = 0;
		for (Organization organization : orgs) {
			if(StringUtils.isNotBlank(organization.getChildren())){
				count=	organizationRepository.findCountuser(organization.getChildren()+","+organization.getPk());
			}else{
				count=	organizationRepository.findCountuser(organization.getPk()+"" );
			}
	
			organization.setUserCount(count);
		}
		return orgs;
		
	}
	
	/**
	 * 获取所选中组织的下一级组织
	 * @return
	 */
	@Override
	public List<Organization> getNextOrgList(Long pk){
		return organizationRepository.getNextOrgList(pk);
	}
	@Override
	public List<Organization> getOrgChildrenNoLeaf(Long pk) {
		return organizationRepository.selectChildrenNotLeafFor(pk);
	}
	
	@Override
	public List<Organization> getOrgSubList(Long pk) {
		return organizationRepository.listByParentFor(pk,"");
	}
	
	@Override
	public void getOrgSubList(Long pk,Query<Organization> query) {
		
		organizationRepository.listByParentFor(pk,query);
		List<Organization> results = query.getResults();
		for (Organization organization : results) {
			
			organization.setUserCount(organizationRepository.findCountuser(organization.getPk()+""));
		}
		query.setResults(results);
	}
	
	
	@Override
	public List<Organization> getOrgSubList(Long pk,String name) throws BusinessException {
		List<Organization> tempOrgs  =	organizationRepository.listByParentFor(pk,name);
		tempOrgs=this.finCountUser(tempOrgs, null);
		return tempOrgs;
		
	}
	@Override
	public List<Organization> listByNameFor(String name) throws BusinessException {
		List<Organization> tempOrgs  =	organizationRepository.listByNameFor(name);
		tempOrgs=this.finCountUser(tempOrgs, null);
		return tempOrgs;
		
	}
	@Override
	public Organization getOrgByPk(Long pk) {
		Organization org	= organizationRepository.get(pk);
		List<Organization > orgs = organizationRepository.list();
		org = this.toUserorg(orgs, org);
		int count = 0;
		if(StringUtils.isNotBlank(org.getChildren())){
			count=	organizationRepository.findCountuser(org.getChildren()+","+org.getPk());
		}else{
			count=	organizationRepository.findCountuser(org.getPk()+"" );
		}

		org.setUserCount(count);
		return organizationRepository.get(pk);
	}
	
	@Override
	@Transactional
	public void remove(Organization t) throws BusinessException {
		myRemove(t);
	}
	
	private void myRemove(Organization t)throws BusinessException {
		List<Organization>  orgList = organizationRepository.listByParentFor(t.getPk(),"");
		if(orgList.size() != 0) {
			for (Organization organization : orgList) {
				myRemove(organization);
			}
		}
		organizationRepository.delete(t);
	}
	
	@Override
	@Transactional
	public void create(Organization org) throws BusinessException{
		organizationRepository.save(org);
	}
	
	@Override
	@Transactional
	public void update(Organization org) throws BusinessException{
		organizationRepository.update(org);
	}
	@Override
	public List<Organization> getOrgAllByPk(Long pk,Long type) {
		return organizationRepository.getOrgAllByPk(pk,type);
	}
	@Override
	public void createOrgExcel(Long pk, Long type, HttpServletRequest request) {
		organizationRepository.createOrgExcel(pk, type, request);
		
	}
	@Override
	public int findOrgList() {
		return organizationRepository.findOrgList();
	}
	@Override
	public boolean deleteAllOrg() {
	organizationRepository.deleteAllOrg();
	return true;
		
	}
	@Override
	public boolean findOrgByCode(String code) {
		return organizationRepository.findOrgByCode(code);
	}
	@Override
	public void saveOrg(Organization org) {
		organizationRepository.saveOrg(org);
	}
	@Override
	public List<Organization> nextOrgCount(List<Organization> list) {		
		return organizationRepository.nextOrgCountOfParent(list);
	}
	@Override
	public boolean findOrgByCode(String code, String orgId) {
		return organizationRepository.findOrgByCode(code,orgId);
	}
	@Override
	public Organization getOrgByCode(String code) {
		return organizationRepository.getOrgByCode(code);
	}
	@Override
	public void deleteOrg(Organization org) {
		organizationRepository.delete(org);
		
	}
	@Override
	public String initOrganization(String pk) {
		String  message="";
		try {
			Organization org = organizationRepository.load(Long.parseLong(pk));
			//获取此组织本身和所有下级的code
			String strCode= getAllChildCode(Long.parseLong(pk));
			strCode+="'"+org.getCode()+"'";
			boolean flg=false;
			if(strCode!="" && strCode!=null){
				//查看是否关联了考试信息(kn_exam),保存的是组织代码和组织名称
				List<Exam> examList=examDao.getExamByorgCodes(strCode);
				if(examList.size()>0){
					flg=true;
					return "此组织关联了考试信息！";
				}
			}
			if(!flg){
				String strId= getAllChildId(Long.parseLong(pk));
				strId+=org.getPk();
				//查看是否关联了数据权限(4a_data_authorized)保存的是组织id和组织名称
				List<DataAuthorized> dataList=dataPermissionRepository.selectAllDataAuthorizeds("4a_org",strId);
				if(dataList.size()>0){
					flg=true;
					return "此组织关联了数据权限信息！";
				}
				//查看用户基本信息是否关联词组织（4a_userbelong）
				List<UserBelong> userBelonList =userBelong.getBelongByUserAll(strId);
				if(userBelonList.size()>0){
					flg=true;
					return "此组织关联了用户信息！";
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return message;
	}
	@Override
	public void deleteOrgAndChild(Long pk) throws BusinessException {
		deleteOrg(pk);
		organizationRepository.delete(organizationRepository.load(pk));
	}
	//递归获取组织及所有下级组织代码，英文逗号隔开
	private String getAllChildCode(Long pk) throws BusinessException{
		StringBuilder str = new StringBuilder();
		List<Organization> childs = organizationRepository.getNextOrgList(pk);
		for (Organization organization : childs) {
			str.append("'"+organization.getCode()+"',");
			str.append(getAllChildCode(organization.getPk()));
		}
		return str.toString();
	}
	//递归获取组织及所有下级组织id，英文逗号隔开
	private String getAllChildId(Long pk) throws BusinessException{
		StringBuilder str = new StringBuilder();
		List<Organization> childs = organizationRepository.getNextOrgList(pk);
		for (Organization organization : childs) {
			str.append(organization.getPk()+",");
			str.append(getAllChildId(organization.getPk()));
		}
		return str.toString();
	}
	//递归删除组织及所有下级组织
	private void deleteOrg(Long pk) throws BusinessException{
		List<Organization> childs = organizationRepository.getNextOrgList(pk);
		for (Organization organization : childs) {
			organizationRepository.delete(organization);
			deleteOrg(organization.getPk());
		}
	}
	//递归更新组织及所有下级组织
	private void updateOrg(Long pk,int type) throws BusinessException{
		List<Organization> childs = organizationRepository.getNextOrgList(pk);
		for (Organization organization : childs) {
			organization.setType(organization.getType()-type);
			organizationRepository.update(organization);
			updateOrg(organization.getPk(),type);
		}
	}
	@Override
	public void updateOrgByName(String name, String parentName) throws BusinessException {
		Organization  org =organizationRepository.findOrgByName(name);
		Organization  parentOrg =organizationRepository.findOrgByName(parentName);
		int parentType=1;//父级层级
		if(parentOrg==null){
			//更新下级组织
			updateOrg(org.getPk(),org.getType()-1);
			//更新本身
			org.setType(1);
			org.setParent(null);
			organizationRepository.update(org);
		}else{
			parentType=parentOrg.getType();
			//更新下级组织
			updateOrg(org.getPk(),org.getType()-parentType-1);
			//更新本身
			org.setType(parentType+1);
			org.setParent(parentOrg);
			organizationRepository.update(org);
		}
		
	}
	@Override
	public boolean findOrgByOrgId(Long id) {
		return organizationRepository.findOrgByOrgId(id);
	}
	//迭代树形菜单方法变更
	@Override
	public List<Organization> toUserResources(List<Organization> contOrg,List<Organization> orgtmp) {
		for (Organization org : orgtmp) {
				//递归找出菜单栏
				org.setChildren("");;
				setUrl(contOrg,org,org);
			
		}

    	return orgtmp;
    	
    }
	@Override
	public Organization toUserorg(List<Organization> contOrg,Organization orgtmp) {
		
				//递归找出菜单栏
				orgtmp.setChildren("");
				setUrl(contOrg,orgtmp,orgtmp);
	

    	return orgtmp;
    	
    }
	@Override
	public void setUrl(List<Organization> contOrg, Organization org, Organization code) {
		String gradeid = "";
		String examtypeid = "";
		if (contOrg != null)
			for (Organization urlResource : contOrg) {
				if (urlResource.getParent() != null)
					if (org.getPk() == urlResource.getParent().getPk()) {
						if(StringUtils.isNotBlank(code.getChildren())){
							code.setChildren(code.getChildren()+",");
						}
						code.setChildren(code.getChildren()+urlResource.getPk());
						setUrl(contOrg, urlResource,code);

					}
			}
		
	}
	@Override
	public List<Organization> getAllTotByMySelf(String orgId) throws BusinessException {
		return organizationRepository.getAllTotByMySelf(orgId);
	}
	@Override
	public List<Organization> getOrgAllByName(Long pk, Long type, String name) {
		// TODO Auto-generated method stub
		return organizationRepository.getOrgAllByName(pk, type, name);
	}
}
