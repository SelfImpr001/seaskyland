package com.cntest.birt.repository;

import java.util.List;

import com.cntest.birt.domain.OrgBirt;
import com.cntest.birt.domain.ReportScript;
import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;
import com.cntest.foura.domain.Organization;

	public interface orgBirtRepository extends Repository<OrgBirt, Long> {

	void findReport(Query<OrgBirt> query);

	List<OrgBirt> findByIn(String directory);

	void cleatSession();

	/**
	 * 获取所有没有上级的组织
	 * @return
	 */
	List<OrgBirt> listByParentIsNull(String name);
	/**
	 * 获取所选中组织的下一级组织
	 * @param pk
	 * @return
	 */
	List<OrgBirt> getNextOrgList(Long pk);
	/**
	 * 获取组织的下级
	 * @param pk 上级id
	 * @return
	 */
	List<OrgBirt> listByParentFor(Long pk,String name);
	
	/**
	 * 获取当前组织的下级组织个数
	 * @param pk
	 * @return
	 */
	List<OrgBirt>  nextOrgCountOfParent(List<OrgBirt> list); 
	
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

	List<OrgBirt> selectChildrenNotLeafFor(Long parentPk);

	List<OrgBirt> listByParentFors(Long pk, String name);






	void listByParentFor(Long pk, Query<OrgBirt> query);

	void delete(Long pk);

	
}
