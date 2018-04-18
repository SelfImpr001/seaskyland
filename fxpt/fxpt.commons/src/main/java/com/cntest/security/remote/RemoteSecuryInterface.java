/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.remote;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.session.Session;

import com.cntest.security.UserDetails;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月16日
 * @version 1.0
 **/
public interface RemoteSecuryInterface {

	public List<String> getResouces(String appKey, UserDetails user);

	public Session getSession(String appKey, Serializable sessionId);

	Serializable createSession(Session session);

	public void updateSession(String appKey, Session session);

	public void deleteSession(String appKey, Session session);

	public Set<Permission> getPermissions(String appKey, String userName);
	
	public UserDetails getUserDatails(String userName);
	
	public Serializable updatePassword(String userName,String oldPassword,String newPassword);
}

