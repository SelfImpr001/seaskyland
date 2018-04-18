/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
public interface OrganizationRepository extends Repository<Organization, Long> {
	/**
	 * 获取所有没有上级的组织
	 * @return
	 */
	List<Organization> listByParentIsNull(String name);
	/**
	 * 获取所选中组织的下一级组织
	 * @param pk
	 * @return
	 */
	List<Organization> getNextOrgList(Long pk);
	/**
	 * 获取组织的下级
	 * @param pk 上级id
	 * @return
	 */
	List<Organization> listByParentFor(Long pk,String name);
	
	/**
	 * 获取当前组织的下级组织个数
	 * @param pk
	 * @return
	 */
	List<Organization>  nextOrgCountOfParent(List<Organization> list); 
	
	/**
	 * 根据pk获取组织(下级及本身)
	 * @param valueOf
	 * @return
	 */
	List<Organization> getOrgAllByPk(Long pk,Long type);
	/**
	 * 根据组织代码获取组织
	 * @param code  组织代码
	 * @return
	 */
	Organization getOrgByCode(String code);
	/**
	 * 查询是否存在相同的orgcode
	 * @param code
	 * @param orgId
	 * @return
	 */
	boolean findOrgByCode(String code, String orgId);
	
	/**
	 * 查询是否存在相同的Orgcode
	 * @param code
	 * @return
	 */
	boolean findOrgByCode(String code);

	void listByParentFor(Long pk, Query<Organization> query);
	
	List<Organization> selectChildrenNotLeafFor(Long parentPk);
	
	public void createOrgExcel(Long pk,Long type,HttpServletRequest request);
	
	public int findOrgList();
	
	public void deleteAllOrg();
	
	public void saveOrg(Organization org);
	/**
	 * 根据名称获取组织
	 * @param name
	 * @return
	 */
	public Organization findOrgByName(String name);
	/**
	 * 查询是否存在该组织
	 * @param code
	 * @return
	 */
	boolean findOrgByOrgId(Long id);
	int findCountuser(String in);
	
	/**
	 * 根据组织id获取所有上级的组织
	 * @param orgId
	 * @return
	 */
	public List<Organization> getAllTotByMySelf(String orgId)  throws BusinessException ;
	
	/**
	 * 名字模糊插寻
	 */
	List<Organization> listByNameFor(String name);
	
	
	public List<Organization> getOrgAllByName(Long pk,Long type,String name) ;
}

