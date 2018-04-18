/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service;


import java.util.List;

import com.cntest.common.service.EntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserBelong;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
public interface UserBelongService extends EntityService<UserBelong,Long>{

	/**
	 * 根据用户查找组织关系
	 * @param user
	 * @return
	 */
	List<UserBelong> findBelongFor(User user);
	
	/**
	 * 更新用户的组织关系
	 * @param user
	 * @return
	 */
	void updateBelongFor(User user, List<Organization> orgs);


	String[] getBelongCodes(String userName);
	
	/**
	 * 根据组织关系id删除组织关系
	 * @param id
	 */
	void deleteBelongUser(Long id);
	/**
	* 根据考试群体获取有权限的用户
	 * @param orgList
	 * @return
	 * @throws BusinessException
	 */
	public List getUserByOrg(String orgList) throws BusinessException;
}

