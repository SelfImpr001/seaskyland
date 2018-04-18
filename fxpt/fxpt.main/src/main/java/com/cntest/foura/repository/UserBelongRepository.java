/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository;

import java.util.List;

import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;
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
public interface UserBelongRepository extends Repository<UserBelong,Long> {

	/**
	 * 根据用户PK查找组织关系
	 * @param pk
	 * @return
	 */
	List<UserBelong> getBelongByUserPk(User user);
	
	/**
	 * 移除符合条件的记录
	 * @param user
	 * @param orgs
	 * @return 
	 */
	Integer removeBelongByUser(User user);
	/**
	 * 根据id窜获取
	 * @param orgIds
	 * @return
	 */
	public List<UserBelong> getBelongByUserAll(String orgIds);
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

