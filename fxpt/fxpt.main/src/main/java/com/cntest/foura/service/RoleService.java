/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service;


import java.util.List;

import com.cntest.common.service.EntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.URLResource;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
public interface RoleService extends EntityService<Role,Long>{
	public List<Role> list();
	
	public Role findRoleById(Long roleId);
	
	public List<Role> findRolesByName(String roleName);
	
	public void deleteResByRoleId(Long roleId);

	void updateRole(Role role, URLResource[] urlResources) throws BusinessException;

	public void updateRoleDataAuthorized(Role role, DataAuthorized[] dataAuthorizeds)throws BusinessException;
	/**
	 * 查看角色编码是否唯一
	 * @param code
	 * @return
	 */
	public boolean findRoleByCode(String code,String roleId);
	
	/**
	 * 根据用户ID查询所属角色
	 * @param userId
	 * @return
	 */
	public List<Role> findRolesById(Long  userId);
	
	/**
	 * 删除角色信息
	 * @param role  角色信息
	 * @param count 关联用户条数
	 */
	public void deleteRoleAndNexus(Role role,String count)  throws BusinessException ;
	/**
	 * 权限增量覆盖（用于成员管理）
	 * @param roleId角色Id 
	 * @param deleteUserIds 移除的用户ids
	 * @param addUserIds 添加的用户ids
	 * @throws BusinessException
	 */
	
	public void updateRoleAndResource(String roleId,String deleteUserIds,String addUserIds) throws BusinessException;
}

