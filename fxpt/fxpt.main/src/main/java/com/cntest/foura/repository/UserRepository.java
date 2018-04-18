/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository;

import java.util.List;
import java.util.Set;

import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月7日
 * @version 1.0
 **/
public interface UserRepository extends Repository<User, Long> {

    List<User> list();
    
    List<User> list(Long role);
    
	User selectUserBy(String userName);

//	/void findUsers(List<User> result, int fist, int max, Map<String, String[]> parameters);
	
	void findUsers(Query<User> query);

	void deleteNullUserRole();

	User findSameUser(String name);

	void deleteRolesByUserId(String userId); 
	
	Set<Role>  findRolesByUserId(Long userId);
	
	Set<URLResource>  findUrlsByUserId(Long userId);
	
	void deleteResByUserId(String userId);
	
	Set<User> findUsersByRoleId(Long roleId);
	
	void findUsersByRoleId(Query<User> query);
	/**
	 * 查找角色Id下的用户数
	 * @param result
	 * @return
	 */
	Query<Role> findUserCountByRoleId(Query<Role> result);
	/**
	 * 添加考试授权(批量)
	 * @param examid 考试ID
	 * @param pk     人员id
	 */
	void addUserExamByPKandExamids(String examids,Long pk);
	/**
	 * 删除考试授权
	 * @param pk     人员id
	 */
	void deleteUserExamByPk(String pk);
	/**
	 * 获取用户考试权限
	 * @param pk
	 */
	public String getUserExamByRoleId(Long pk);
	
	/**
	 * 根据人员考试Id删除考试授权
	 * @param examid     考试id
	 */
	public void deleteUserExamByExamId(Long examid);
	/**
	 * 根据用户id查询关联的角色
	 * @param userId
	 */
	public List findUserRoleByUserId(String pk); 
	
	/**
	 * 根据用户id查询关联的权限
	 * @param userId
	 */
	public List findUserResourceByUserId(String pk); 
	
	/**
	 * 批量删除用户
	 * @param pks
	 */
	public void deleteUserByPk(String pks);

	void findUsersByOrg(Query<User> query, Organization role);
	
	public void deleteUserLink(String pks);
	/**
	 * 查看用户的此处考试权限是否存在
	 * @param userid
	 * @param examid
	 * @return
	 * @throws BusinessException
	 */
	public boolean powerIsExist(String userid,String examid)  throws  BusinessException;
	
	public void evictSession(Object o );
	/**
	 * 删除发布的用户存储表对应的用户信息
	 * @param userName
	 */
	public void deletePublishUserByUserName(String userName);
	/**
	 * 根据用户pk值查询用户名字
	 * @param pks 可以多个-例："1，2，3"
	 *  @return 返回用户名字符串
	 */
	public String selectMoreUserNameByPks(String pks);
}

