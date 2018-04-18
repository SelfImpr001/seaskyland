/*
 * @(#)com.cntest.security.shiroimpl.SessionTimeoutFilter.java	1.0 2015年2月5日:下午2:38:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.security.shiroimpl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年2月5日 下午2:38:39
 * @version 1.0
 */
public class SessionTimeoutFilter extends AdviceFilter {
	private String defaultRedirectUrl;

	public String getDefaultRedirectUrl() {
		return defaultRedirectUrl;
	}

	public void setDefaultRedirectUrl(String defaultRedirectUrl) {
		this.defaultRedirectUrl = defaultRedirectUrl;
	}

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {

		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() != null) {
			return true;
		}

		String url = defaultRedirectUrl;
		javax.servlet.http.Cookie[] cookies = WebUtils.toHttp(request)
				.getCookies();
//		for (javax.servlet.http.Cookie c : cookies) {
//			if (MyLogoutFilter.LOGOUT_BACK_URL_PRAMA_NAME.equals(c.getName())) {
//				url = c.getValue();
//				break;
//			}
//		}
		WebUtils.issueRedirect(request, response, url);
		return false;
	}

}
