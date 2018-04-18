/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service;


import java.util.List;
import java.util.Set;

import com.cntest.common.query.Query;
import com.cntest.common.service.EntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.fxpt.domain.Exam;
import com.cntest.security.UserDetails;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月7日
 * @version 1.0
 **/
public interface UserService extends EntityService<User,Long>  {

	public User load(Long pk) throws BusinessException;
	/**
	 * 通过用户名查询用户
	 * @param userName
	 * @return
	 */
	public User findUserBy(String userName);

	/**
	 * 得到用户的缺省访问url
	 * @param user
	 * @author liguiqing
	 * @return
	 */
	public List<User> list();
	
	public URLResource getDefaultUrlResource(User user);
	
	public void updatePassword(User user,String newPassword)throws  BusinessException;

	public void updateRoles(User user, Role... roles)throws  BusinessException;

	public void updateUrls(User user, URLResource... urlResources)throws  BusinessException;
	
	public Boolean updatePassword(User my, String oldPassword, String newPassword)throws  BusinessException;;

	public void deleteRolesByUserId(String userId)throws  BusinessException;
	
	public Set<Role> findRolesByUserId(Long userId)throws  BusinessException;
	
	public Set<URLResource> findUrlsByUserId(Long userId)throws  BusinessException;
	
	public Set<User> findUsersByRoleId(Long roleId)throws  BusinessException;
	
	public void deleteResByUserId(String userId)throws  BusinessException;
	
	public void queryUsersByRoleId(Query<User> query) throws  BusinessException;

	public void updateRoleDataAuthorized(User user, DataAuthorized[] dataAuthorizeds);
	
	UserDetails getCurrentLoginedUser();
	
	/**
	 * 查找角色Id下的用户数
	 * @param result
	 * @return
	 */
	Query<Role> findUserCountByRoleId(Query<Role> result);
	
	/**
	 * 授权
	 */
	public void givePower(String examids,Long pk);
	
	/**
	 * 添加考试授权(批量)
	 * @param examid 考试ID
	 * @param pk     人员id
	 */
	public void addUserExamByPKandExamids(String examids,Long pk);
	/**
	 * 根据人员Id删除考试授权
	 * @param pk     人员id
	 */
	public void deleteUserExamByPk(String pk);
	/**
	 * 获取用户考试权限 （考试ID串）
	 * @param pk
	 */
	public String getUserExamByRoleId(Long pk);
	
	/**
	 * 根据人员考试Id删除考试授权
	 * @param examid     考试id
	 */
	public void deleteUserExamByExamId(Long examid);
	
	/** 新增南山接口数据 
	 * @throws BusinessException */
	public void createNanshan(User user, String roleCode) throws BusinessException;
	/**
	 * 根据用户pk去验证是否有关联到角色，权限，考试等信息
	 * @param pk
	 */
	public String initUserHasMessage(String pk);
	/**
	 * 删除用户信息（包括用户基本信息，角色关联信息，权限关联信息，考试关联信息等）
	 * @param user
	 */
	public void gotoMoveUserAndMessage(User user) throws BusinessException ;
	/**
	 * 批量删除用户
	 * @param pks
	 */
	public void delteMoreUser(String pks) throws BusinessException;
	void queryUsersByOrg(Query<User> query, Organization role);
	
	/**
	 * 获取非本角色用户
	 * */
	public List<User> list(Long role);
	/**
	 * 根据角色所属的组织，赋予对应的考试权限
	 * @param org
	 * @param userok
	 * @throws BusinessException
	 */
    public void giveExamPowerToNewUser(String[] org,Long userok) throws BusinessException;
    
	/**
	 * 根据考试信息检索那些用户有此考试权限
	 * @param exam
	 * @throws BusinessException
	 */
    public void givePowerToUserByExam(Exam exam) throws BusinessException;
     
    public void evictSession(Object o );
    
	public int synchronous();
	void updateIsupPassword(User user, String newPassword, String isloginPss) throws BusinessException;
 }

