/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.remote;

import java.util.List;

import com.cntest.security.UserDetails;
import com.cntest.security.UserOrg;
import com.cntest.security.UserResource;

/** 
 * <pre>
 * 用户访问接口
 * V2.0+ 代替 IUserResourceService，UserOrgService,对应的API全部集中到此接口中
 * </pre>
 *  
 * @author 李贵庆2014年12月18日
 * @version 1.0
 **/
public interface UserDetailsService {

	UserDetails findUserDetailsBy(String userName);
	
	UserResource getDefaultApp(UserDetails user);
	
	/**
	 * 查找用户的组织机构
	 * @param user
	 * @return
	 */
	List<UserOrg> findUserOrgs(UserDetails user);
	
	/**
	 * 查找用户可用资源（页面资源，数据资源等）
	 * @param user
	 * @return
	 */
	List<UserResource> findUserResource(UserDetails user,IUserResourceService.Type resType,
			IUserResourceService.Level resLevel);
	
	List<UserResource> findUserResourcefilter(UserDetails user,IUserResourceService.Type resType,
			IUserResourceService.Level resLevel,Long examid);
	
}

