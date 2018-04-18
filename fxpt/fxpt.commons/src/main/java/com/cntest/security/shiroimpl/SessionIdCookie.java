/*
 * @(#)com.cntest.security.shiroimpl.SessionIdCookie.java	1.0 2014年10月8日:下午1:16:32
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.security.shiroimpl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月8日 下午1:16:32
 * @version 1.0
 */
public class SessionIdCookie extends SimpleCookie {
	private List<String> filterURLs;
	private PathMatcher pathMatcher = new AntPathMatcher();
	private UrlPathHelper urlPathHelper = new UrlPathHelper();

	public List<String> getFilterURLs() {
		return filterURLs;
	}

	public void setFilterURLs(List<String> filterURLs) {
		this.filterURLs = filterURLs;
	}

	public SessionIdCookie() {
		super();
	}

	public SessionIdCookie(String name) {
		super(name);
	}

	public SessionIdCookie(Cookie cookie) {
		super(cookie);
	}

	@Override
	public String readValue(HttpServletRequest request,
			HttpServletResponse ignored) {

		String sessionId = null;
		if (validateURL(request)) {
			Map<String, String[]> paraObj = request.getParameterMap();
			String name = getName();
			sessionId = request.getParameter(name);
		}

		if (sessionId == null) {
			sessionId = super.readValue(request, ignored);
		}
		return sessionId;
	}

	private boolean validateURL(HttpServletRequest request) {
		String url = urlPathHelper.getLookupPathForRequest(request);

		if (filterURLs != null) {
			for (String urlTemplate : filterURLs) {
				if (pathMatcher.match(urlTemplate, url)) {
					return true;
				}
			}
		}

		return false;
	}

	// public String readValue(HttpServletRequest request, HttpServletResponse
	// ignored) {
	// String name = getName();
	// String value = null;
	// javax.servlet.http.Cookie cookie = getCookie(request, name);
	// if (cookie != null) {
	// value = cookie.getValue();
	// log.debug("Found '{}' cookie value [{}]", name, value);
	// } else {
	// log.trace("No '{}' cookie value", name);
	// }
	//
	// return value;
	// }

}
