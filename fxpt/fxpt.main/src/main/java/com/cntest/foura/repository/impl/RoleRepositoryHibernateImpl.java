/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.RoleResource;
import com.cntest.foura.domain.UserRole;
import com.cntest.foura.repository.RoleRepository;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
@Repository
public class RoleRepositoryHibernateImpl extends AbstractHibernateDao<Role, Long> implements RoleRepository {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(RoleRepositoryHibernateImpl.class);
	
	@Override
	protected Class<Role> getEntityClass() {
		return Role.class;
	}

	@Override
	public List<Role> list() {
		return findByHql("from Role where available=true order by pk desc");
	}
	
	@Override
	public List<Role> findRolesByName(String roleName) {
		StringBuffer sb=new StringBuffer();
		sb.append("from Role where name like ? order by pk desc");
		return findByHql(sb.toString(),"%" + roleName + "%");
	}
	
	@Override
	public List<Role> findRolesByCode(String roleCode) {
		StringBuffer sb=new StringBuffer();
		sb.append("from Role where code like ? order by pk desc");
		return findByHql(sb.toString(),"%" + roleCode + "%");
	}

	@Override
	public void deleteResByRoleId(Long roleId) {
		StringBuffer sb=new StringBuffer();
		sb.append("delete from 4a_role_resource where role_id=").append(roleId).append("");
		createSQLQuery(sb.toString()).executeUpdate();
	}

	@Override
	public void updateResource(Role role) {
		if(role.getResources() != null) {
			String sql  = "delete from 4a_role_resource where role_id = ?";
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setLong(0, role.getPk());
			query.executeUpdate();
			for(RoleResource rr:role.getResources()) {
				getSession().save(rr);
			}
		}
	}

	@Override
	public boolean findRoleByCode(String code,String roleId) {
		boolean flg=true;
		//不为空表示此操作为修改，否则为新增
		Role role = new Role();
		String codeEdit ="";
		if(roleId!="0" || !"0".equals(roleId)){
			String hql ="from Role where role_id=?";
			List<Role> roleList=findByHql(hql, roleId);
			if(roleList.size()>0){
				role=roleList.get(0);
			    codeEdit =role.getCode();
			}
		}
		//角色编号为在修改时无变化
		if(codeEdit!="" && codeEdit.equalsIgnoreCase(code)){
			flg=true;
		}else{
			String hql ="from Role where role_code=?";
			List<Role> roleList=findByHql(hql, code);
			if(roleList.size()>0){
				flg=false;
			}
		}
		return flg;
	}

	@Override
	public List<Role> findRolesById(Long userId) {
		List<Role> newList=new ArrayList<Role>();
		String sql  = "select a.* from 4a_role a left join 4a_user_role b on a.role_id=b.role_id  where b.user_id="+userId;
		List<Object[]> list= getSession().createSQLQuery(sql).list();
		for(int i=0;i<list.size();i++){
			Object[] objs=(Object[])list.get(i);
			Role role=new Role();
			role.setName(objs[1].toString());
			newList.add(role);
		}
		return newList;
//		String ids="";
//		String sql  = "select * from 4a_user_role  where user_id="+userId;
//		SQLQuery query = getSession().createSQLQuery(sql);
//		List roleList=query.list();
//		if(roleList!=null && roleList.size()>0 ){
//			for(int i=0;i<roleList.size();i++){
//				ids+=roleList.get(i)+",";
//			}
//			ids=ids.substring(0, ids.length()-1);
//		}
//		sql="from Role where role_id in("+ ids+")";
//		return findByHql(sql);
	}

	@Override
	public void deleteRoleByPk(Long pk) {
		String sql = "DELETE FROM 4a_role  WHERE role_id = "+pk; //删除角色
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public void addUserToRole(String roleId, String userId) {
		String sql = "INSERT INTO 4a_user_role (user_id, role_id) VALUES ("+userId+","+roleId+");"; 
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public String getResourceByRoleId(String roleId) {
		String sql = "SELECT res_id FROM 4a_role_resource WHERE role_id="+roleId; 
		Query query = getSession().createSQLQuery(sql);
		return query.list()!=null?query.list().toString():"";
	}

	@Override
	public void deleteUserByRoleId(String roleId,String deleteUserIds) {
		deleteUserIds=deleteUserIds!=""?deleteUserIds.substring(0,deleteUserIds.length()-1):"";
		String sql = "DELETE FROM 4a_user_role  WHERE role_id = "+roleId; 
		if(deleteUserIds!=""){
			sql+=" AND user_id in("+deleteUserIds+")";
		}
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public void deletePowerByRole(String roleId,String roleRes, String userids) {
		String[] usStr=userids.split(",");
		String SQl="";
		String deleteNOT="";
		for(String userId:usStr){
			 SQl ="SELECT res_id FROM 4a_role_resource  WHERE role_id "
						+"IN(SELECT role_id FROM 4a_user_role  WHERE user_id="+userId+")"  
						+" GROUP  BY  res_id  HAVING  COUNT(res_id)>1";
			 Query query = getSession().createSQLQuery(SQl);
			 List resIds=query.list();
			 SQl = "DELETE FROM 4a_user_resource WHERE res_id IN ("+roleRes+") AND user_id="+userId;
			 if(resIds.size()>0){
				 deleteNOT=resIds.toString().substring(1,resIds.toString().length()-1);
				 SQl+=" AND res_id NOT IN("+deleteNOT+")";
			 }
			 query = getSession().createSQLQuery(SQl);
			 query.executeUpdate();
		}
	}

	@Override
	public void addResToUser(String userId, String roleRes) {
		String[] roleResource = null;
		if(!"".equals(roleRes)){
			roleResource = roleRes.split(",");
		}
		for(int i=0;i<roleResource.length;i++){
			//该角色是否存在此权限，存在则不保存
			String sql=" select * from 4a_user_resource where user_id="+userId+" and res_id="+roleResource[i];
			Query query = getSession().createSQLQuery(sql);
			if(query.list().size()==0 || query.list()==null){
				sql = "INSERT INTO 4a_user_resource (user_id, res_id) VALUES ("+userId+","+roleResource[i]+")"; 
				query = getSession().createSQLQuery(sql);
				query.executeUpdate();
			}
		}
	}

	@Override
	public String getUserIdsByRole(String roleId) {
		String sql = "SELECT user_id FROM 4a_user_role WHERE role_id="+roleId; 
		Query query = getSession().createSQLQuery(sql);
		return query.list()!=null?query.list().toString():"";
	}

}

