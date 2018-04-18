/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.shiroimpl;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * <pre>
 * 身份匹配，可以扩展多种匹配方式
 * 如重试次数等
 * </pre>
 * 
 * @author 李贵庆2014年6月18日
 * @version 1.0
 **/
public class CredentialsMatcher extends HashedCredentialsMatcher {
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		//密码匹配实现
		boolean matches = super.doCredentialsMatch(token, info);
		return matches;
	}

}
