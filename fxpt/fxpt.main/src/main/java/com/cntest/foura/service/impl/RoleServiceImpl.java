/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service.impl;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.page.Page;
import com.cntest.common.service.AbstractEntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.RoleResource;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.repository.RoleRepository;
import com.cntest.foura.repository.URLResourceRepository;
import com.cntest.foura.service.DataPermissionService;
import com.cntest.foura.service.RoleService;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserService;

import smartbi.sdk.demo.DeleteUserDemo;

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
public class RoleServiceImpl extends AbstractEntityService<Role,Long> implements RoleService {
	private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	private URLResourceService resourceService;
	
	
	@Autowired
	private URLResourceRepository resourceRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataPermissionService dataPermissionService;
	
	public RoleServiceImpl() {}
	
	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
		this.setRepository(this.roleRepository);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Role> list() {
		return roleRepository.list();
	}
	
	@Override
	@Transactional
	public void create(Role role) throws BusinessException {
		 roleRepository.save(role);
	}
	
	@Override
	@Transactional
	public void remove(Role role) throws BusinessException {
		if(role != null && role.getPk() != null)  {
			role = load(role.getPk());
			roleRepository.delete(role);
		}
	}
   
	@Override
	@Transactional
	public void update(Role role) throws BusinessException {
		roleRepository.update(role);
		this.setRepository(this.roleRepository);
	}
	
	@Override
	@Transactional
	public void updateRole(Role role,URLResource... urlResources) throws BusinessException {
		Set<User> users=userService.findUsersByRoleId(role.getPk());
		role.getResources().removeAll(role.getResources());
		String roleId=role.getPk().toString();
		//获取角色对应的权限
		String roleRes=roleRepository.getResourceByRoleId(roleId);
		String usersStr=roleRepository.getUserIdsByRole(roleId);
		if(usersStr.length()>0){
			usersStr=usersStr.substring(1,usersStr.length()-1);
		}
		if(roleRes.length()>0){
			roleRes=roleRes.substring(1,roleRes.length()-1);
		}
		//用户对此角色资源的更新（原来赋予的旧权限部分可能要删除）
		if(!"".equals(usersStr) && !"".equals(roleRes)){
			//删除用户对应角色权限
			roleRepository.deletePowerByRole(roleId,roleRes,usersStr);
		}
		for (URLResource urlResource : urlResources) {
			URLResource res = resourceService.load(urlResource.getPk());
			role.addResource(res);
			for (User user : users) {
				user.addResource(res);
			}
	    }
	}
	
	@Override
	public Role findRoleById(Long roleId) {
		return roleRepository.get(roleId);
	}
	
	@Override
	public List<Role> list(Page<Role> page)throws BusinessException{
		return roleRepository.list(page);
	}

	@Override
	public List<Role> findRolesByName(String roleName) {
		return roleRepository.findRolesByName(roleName);
	}
	
	@Override
	@Transactional
	public void deleteResByRoleId(Long roleId) {
		roleRepository.deleteResByRoleId(roleId);
		
	}
	@Override
	@Transactional
	public void updateRoleDataAuthorized(Role role, DataAuthorized[] dataAuthorizeds) throws BusinessException{
		dataPermissionService.upateAuthorized(role.getPk(),"role",dataAuthorizeds);
		if(dataAuthorizeds == null || dataAuthorizeds.length ==0) {
			return;
		}		
		//更新角色所对应的用户
		Set<User> users=userService.findUsersByRoleId(role.getPk());
		if(users != null) {
			for(User user:users) {
				List<DataAuthorized>  userdaeds = dataPermissionService.findDataAuthorizeds("user", user.getPk());
				//如果用户中没有包含角色中的数据权限，就要增加对应的数据权限
				for(DataAuthorized da:dataAuthorizeds) {
					
					DataAuthorized other = da.cloneFor("user",user.getPk());
					if(!userdaeds.contains(other)) {
						userdaeds.add(other);
					}
				}
				dataPermissionService.upateAuthorized(user.getPk(),"user",userdaeds.toArray(new DataAuthorized[] {}));
			}
		}
		
	}

	@Override
	public boolean findRoleByCode(String code,String roleId) {
		return roleRepository.findRoleByCode(code,roleId);
	}

	@Override
	public List<Role> findRolesById(Long userId) {
		return roleRepository.findRolesById(userId);
	}

	@Override
	public void deleteRoleAndNexus(Role role, String count) throws BusinessException {
		//存在关联用户时，去除关联关系
		if(role != null && role.getPk() != null)  {
			if(count!="0")
				resourceRepository.deleteUserRoleByPk(role.getPk());
			//存在权限时
			if(role.getResources().size()>0)
				resourceRepository.deleteRoleUrlByPk(role.getPk());
			//删除角色
			roleRepository.deleteRoleByPk(role.getPk());
		}
	}

	@Override
	public void updateRoleAndResource(String roleId, String deleteUserIds,String addUserIds) throws BusinessException {
		//新用户
		String[] userList = addUserIds.split(",");
		//获取角色对应的权限
		String roleRes=roleRepository.getResourceByRoleId(roleId);
		if(roleRes.length()>0){
			roleRes=roleRes.substring(1,roleRes.length()-1);
		}
		if(!"".equals(deleteUserIds) && !"".equals(roleRes)){
			//删除用户对应角色权限
			roleRepository.deletePowerByRole(roleId,roleRes,deleteUserIds);
		}
		if(!"".equals(deleteUserIds)){
			//删除用户的角色关联
			roleRepository.deleteUserByRoleId(roleId,deleteUserIds);
		}
		for (String userId : userList) {
			if(!"".equals(userId)){	
				//添加用户对应角色
				roleRepository.addUserToRole(roleId, userId);
				if(!"".equals(roleRes)){
					//添加角色权限进用户
					roleRepository.addResToUser(userId,roleRes);
				}
			}
		}
	}
}
