/**
 * <p><b>© </b></p>
 * 
 **/
package com.cntest.security.remote;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.cntest.security.UserDetails;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2014年6月15日
 * @version 1.0
 **/

public class RemoteRealm extends AuthorizingRealm {
	private RemoteSecuryInterface remoteService;
	
	private String appKey;

	public void setRemoteService(RemoteSecuryInterface remoteService) {
		this.remoteService = remoteService;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		return authorizationInfo;
	}

	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		String username = (String)token.getPrincipal();
		
		if(username == null)
			throw new UnknownAccountException("用户名不存在或者密码输入有误，请重新输入！");//没找到帐号
		UserDetails user = remoteService.getUserDatails(username);
		if(user == null)
			throw new UnknownAccountException("用户名不存在或者密码输入有误，请重新输入！");//没找到帐号
		
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getUserName()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
	}
}
