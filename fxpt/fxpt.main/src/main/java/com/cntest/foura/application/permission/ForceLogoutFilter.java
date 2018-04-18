package com.cntest.foura.application.permission;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>
 * User: 董博野
 * <p>
 * Date: 14-3-16
 * <p>
 * Version: 1.0
 */
public class ForceLogoutFilter extends AccessControlFilter {
	private Logger logger = LoggerFactory.getLogger(ForceLogoutFilter.class);

	public static final String CURRENT_USER = "user";

	public static final String SESSION_FORCE_LOGOUT_KEY = "session.force.logout";

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		logger.debug("isAccessAllowed");
		Session session = getSubject(request, response).getSession(false);
		if (session == null) {
			return true;
		}
		return session.getAttribute(SESSION_FORCE_LOGOUT_KEY) == null;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		try {
			getSubject(request, response).logout();// 强制退出
		} catch (Exception e) {/* ignore exception */
			logger.error(ExceptionHelper.trace2String(e));
		}

		String loginUrl = getLoginUrl() + (getLoginUrl().contains("?") ? "&" : "?") + "forceLogout=1";
		logger.info(loginUrl);
		WebUtils.issueRedirect(request, response, loginUrl);
		return false;
	}
}
