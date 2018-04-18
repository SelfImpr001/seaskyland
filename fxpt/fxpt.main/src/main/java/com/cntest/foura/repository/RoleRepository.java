/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository;

import java.util.List;

import com.cntest.common.repository.Repository;
import com.cntest.foura.domain.Role;


/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
public interface RoleRepository extends Repository<Role,Long> {
	
	public List<Role> list();
	
	public List<Role> findRolesByName(String roleName);
	
	public List<Role> findRolesByCode(String roleCode);
	
	public void deleteResByRoleId(Long roleId);

	public void updateResource(Role role);
	
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
	public List<Role> findRolesById(Long userId);
	/**
	 * 根据pk删除角色
	 * @param pk
	 */
	public void deleteRoleByPk(Long pk);
	

	/**
	 * 用户关联角色
	 * @param roleId
	 * @param userId
	 */
	public void addUserToRole(String roleId,String userId);
	/**
	 * 查询角色对应的所有资源Id
	 * @param roleId
	 * @return
	 */
	public String getResourceByRoleId(String roleId);
	/**
	 * 根据角色id 关联的用户
	 * @param roleId
	 */
	
	public void deleteUserByRoleId(String roleId,String deleteUserIds);
	/**
	 * 删除用户的对应角色权限
	 * @param roleId
	 * @param roleRes
	 * @param userids
	 */
	public void deletePowerByRole(String roleId,String roleRes,String userids);
	/**
	 * 用户赋予角色权限
	 * @param userId
	 * @param roleRes
	 */
	public void addResToUser(String userId,String roleRes);
	/**
	 * 根据角色id获取用户id
	 * @param roleId
	 */
	public String getUserIdsByRole(String roleId);
}

