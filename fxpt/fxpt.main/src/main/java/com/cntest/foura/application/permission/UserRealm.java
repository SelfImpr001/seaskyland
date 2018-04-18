/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.security.UserDetails;
import com.cntest.util.SpringContext;

/** 
 * <pre>
 * Shiro的用户认证
 * </pre>
 *  
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
public class UserRealm extends AuthorizingRealm {
	private static Logger logger = LoggerFactory.getLogger(UserRealm.class);
	
	

	//不能使用IOC，会问题
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		
		User user = (User) principals.getPrimaryPrincipal();
		logger.info("Authorization For:{}",user);
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		//authorizationInfo.setRoles(userService.findRoles(username));
		//authorizationInfo.setStringPermissions(userService
		//		.findPermissions(username));
		
		logger.info(""+authorizationInfo);
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		String userName = (String) token.getPrincipal();
		if (userName == null) {
			throw new UnknownAccountException();// 没找到帐号
		}
		
		logger.info("User name:{}",userName);
		if(userService == null) {
			userService = SpringContext.getBean(UserService.class);
		}
		User user = userService.findUserBy(userName);//new User.Builder("admin").password("d50197973bd78adf65f6cd7534c36acf").create();//userService.findByUsername(username);
	
		if (user == null) {
			throw new UnknownAccountException();// 没找到帐号
		}

		if (user.isLocked()) {
			throw new LockedAccountException(); // 帐号锁定
		}
		
		
		UserDetails userDetail = user.toUserDetails();
		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				userDetail, // 用户名
				user.getPassword(), // 密码
				ByteSource.Util.bytes(user.getName()),// salt=username+salt
				getName() // realm name
		);
		logger.info(""+authenticationInfo);
		return authenticationInfo;
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

}
