/*
 * @(#)com.cntest.security.remote.IUserResourceService.java	1.0 2014年6月17日:下午2:10:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.security.remote;

import java.io.Serializable;
import java.util.List;


import com.cntest.security.UserDetails;
import com.cntest.security.UserResource;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月17日 下午2:10:04
 * @version 1.0
 */
public interface IUserResourceService {
	/**
	 * <Pre>
	 * 获取URL对应的资源
	 * </Pre>
	 * 
	 * @param user
	 * @param url
	 * @return
	 * @return UserResource 如果没有 或者没有权限就返回null
	 * @author:刘海林 2014年6月17日 下午2:15:00
	 */
	public UserResource getUserResourceByUrl(UserDetails user, String url);

	/**
	 * <Pre>
	 * </Pre>
	 * 
	 * @param user
	 * @param uuId
	 * @return
	 * @return UserResource 如果没有 或者没有权限就返回null
	 * @author:刘海林 2014年6月17日 下午2:18:21
	 */
	public UserResource getUserResourceByUUId(UserDetails user, String uuId);

	/**
	 * <Pre>
	 * 获取URL对应资源下面的所有子资源
	 * </Pre>
	 * 
	 * @param user
	 * @param url
	 * @return
	 * @return List<UserResource> 如果没有 或者没有权限就返回空List列表
	 * @author:刘海林 2014年6月17日 下午2:15:33
	 */
	public List<UserResource> getChildrenUserResourceByUrl(UserDetails user,
			String url);

	/**
	 * <Pre>
	 * 获取UUID下面的子资源
	 * </Pre>
	 * 
	 * @param user
	 * @param uuId
	 * @return
	 * @return List<UserResource> 如果没有 或者没有权限就返回空List列表
	 * @author:刘海林 2014年6月17日 下午2:17:05
	 */
	public List<UserResource> getResourcesFor(UserDetails user,
			String uuId,Type type,Level level);

	/**
	* <Pre>
	* 获取URL下面的对应类别的子资源
	* </Pre>
	* 
	* @param user
	* @param url
	* @param type
	* @return
	* @return List<UserResource> 如果没有 或者没有权限就返回空List列表
	* @author:刘海林 2014年6月17日 下午2:21:02
	*/
	public List<UserResource> getChildrenUserResourceByUrlAnType(
			UserDetails user, String url, String type);

	/**
	* <Pre>
	* 获取UUID下面的相同类型的子资源
	* </Pre>
	* 
	* @param user
	* @param uuId
	* @param type
	* @return
	* @return List<UserResource>
	* @author:刘海林 2014年6月17日 下午2:22:08
	*/
	public List<UserResource> getChildrenUserResourceByUUIdAndType(
			UserDetails user, String uuId, String type);
	
	public static enum Type implements Serializable{
		APP,MODULE,MENU,PAGE,METHOD,DATA
	}
	
	public static enum Level implements Serializable{
		FIRST,ALL
	}
	
	/**
	 * <Pre>
	 * 获取URL对应的资源
	 * </Pre>
	 * 
	 * @param user
	 * @param url
	 * @return
	 * @return UserResource 如果没有 或者没有权限就返回null
	 */
	public boolean getUserResourceByUrl(String url);
	
}
