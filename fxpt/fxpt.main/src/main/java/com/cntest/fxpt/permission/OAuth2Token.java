/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.permission;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2015年3月19日
 * @version 1.0
 **/
public class OAuth2Token implements AuthenticationToken {

	public OAuth2Token(String authCode) {
		this.authCode = authCode;
	}

	private String authCode;
	private String principal;

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Object getCredentials() {
		return authCode;
	}

}
