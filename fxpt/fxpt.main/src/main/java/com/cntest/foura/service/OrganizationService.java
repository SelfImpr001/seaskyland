/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cntest.common.query.Query;
import com.cntest.common.service.EntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
public interface OrganizationService extends EntityService<Organization,Long>{

	/**
	 * 获取所有顶级组织
	 * @return
	 * @throws BusinessException 
	 */
	List<Organization> getTopOrgList(String orgName) throws BusinessException;
	
	/**
	 * 获取所选中组织的下一级组织
	 * @return
	 */
	List<Organization> getNextOrgList(Long pk);

	/**
	 * 根据pk 获取该组织的下级组织
	 * @param valueOf
	 * @return
	 */
	List<Organization> getOrgSubList(Long valueOf);

	/**
	 * 根据pk获取组织
	 * @param valueOf
	 * @return
	 */
	Organization getOrgByPk(Long valueOf);
	
	/**
	 * 根据组织代码获取组织
	 * @param code  组织代码
	 * @return
	 */
	Organization getOrgByCode(String code);
	/**
	 *计算下一级组织个数
	 * @param list
	 * @return
	 */
	List<Organization> nextOrgCount(List<Organization> list);
	
	/**
	 * 根据pk获取组织(下级及本身)
	 * @param valueOf
	 * @return
	 */
	List<Organization> getOrgAllByPk(Long pk,Long type);
	/**
	 * 查询是否存在相同的Orgcode
	 * @param code
	 * @param orgId
	 * @return
	 */
	boolean findOrgByCode(String code,String orgId);
	/**
	 * 查询是否存在相同的Orgcode
	 * @param code
	 * @return
	 */
	boolean findOrgByCode(String code);
	/**
	 * 查询是否存在该组织
	 * @param code
	 * @return
	 */
	boolean findOrgByOrgId(Long id);
	/**
	 * 根据pk值去验证该组织是否关联了考试，用户，权限
	 * @param pk
	 * @return
	 */
	public String initOrganization(String pk);

	List<Organization> getOrgSubList(Long pk, String name) throws BusinessException;

	void getOrgSubList(Long pk, Query<Organization> query);
	
	List<Organization> getOrgChildrenNoLeaf(Long pk);
	
	public void createOrgExcel(Long pk,Long type,HttpServletRequest request);

	public int findOrgList();
	
	public boolean deleteAllOrg();
	
	public void saveOrg(Organization org);
	
	public void deleteOrg(Organization org);
	/**
	 * 删除组织及其所有下级组织
	 * @param pk
	 */
	public void deleteOrgAndChild(Long pk)  throws BusinessException;
	/**
	 * 根据名称更改树形结构
	 * @param name 组织名称
	 * @param parentName  上级组织名称
	 * @throws BusinessException
	 */
	public void updateOrgByName(String name,String parentName) throws BusinessException;

	List<Organization> finCountUser(List<Organization> orgs, Organization org) throws BusinessException;



	List<Organization> toUserResources(List<Organization> contOrg, List<Organization> orgtmp);

	void setUrl(List<Organization> contOrg, Organization org, Organization code);

	Organization toUserorg(List<Organization> contOrg, Organization orgtmp);
	/**
	 * 根据组织id获取所有上级的组织
	 * @param orgId
	 * @return
	 */
	public List<Organization> getAllTotByMySelf(String orgId)  throws BusinessException ;

	List<Organization> listByNameFor(String name) throws BusinessException;
	public List<Organization> getOrgAllByName(Long pk,Long type,String name);
}


