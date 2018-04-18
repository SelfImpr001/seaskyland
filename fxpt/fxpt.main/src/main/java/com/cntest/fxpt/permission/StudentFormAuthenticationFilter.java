/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.permission;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.security.shiroimpl.MyLogoutFilter;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月27日
 * @version 1.0
 **/
public class StudentFormAuthenticationFilter extends FormAuthenticationFilter {
	private static Logger logger = LoggerFactory
			.getLogger(StudentFormAuthenticationFilter.class);

	private boolean enabled = true;

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		if (!enabled)
			return false;
		if (request.getAttribute(getFailureKeyAttribute()) != null) {
			logger.info("验证码不匹配");
			return true;
		}

		// this.createToken(request, response)
		boolean b = super.onAccessDenied(request, response, mappedValue);

		Cookie cookie = new Cookie(MyLogoutFilter.LOGOUT_BACK_URL_PRAMA_NAME,
				this.getLoginUrl());
		cookie.setPath(WebUtils.toHttp(request).getContextPath());
		WebUtils.toHttp(response).addCookie(cookie);
		// b = true;
		if (b) {
			// request.setAttribute("username",
			// request.getParameter("username"));
			// request.setAttribute("password",
			// request.getParameter("password"));
			String name = WebUtils.getCleanParam(request, "name");
			String identity = WebUtils.getCleanParam(request, "identity");
			String schoolName = WebUtils.getCleanParam(request, "schoolName");
			String schoolCode = WebUtils.getCleanParam(request, "schoolCode");
			request.setAttribute("name", name);
			request.setAttribute("identity", identity);
			request.setAttribute("schoolName", schoolName);
			request.setAttribute("schoolCode", schoolCode);
			Subject subject = getSubject(request, response);
			if (subject.getPrincipal() == null) {
				// HttpServletRequest req = WebUtils.toHttp(request);
				// String s = req.getRequestURI();
				HttpServletResponse res = WebUtils.toHttp(response);
				res.setHeader("sessionstatus", "timeout");
			}
		}
		WebUtils.toHttp(request)
				.getSession()
				.setAttribute(MyLogoutFilter.LOGOUT_BACK_URL_PRAMA_NAME,
						this.getLoginUrl());
		return b;
		// return super.onAccessDenied(request, response, mappedValue);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		request.setAttribute(getFailureKeyAttribute(), "个人信息有误，请检查输入是否正确！");
		return true;// super.onLoginFailure(token, e, request, response);
	}

	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) {

		String name = WebUtils.getCleanParam(request, "name");
		String identity = WebUtils.getCleanParam(request, "identity");
		String schoolName = WebUtils.getCleanParam(request, "schoolName");
		String schoolCode = WebUtils.getCleanParam(request, "schoolCode");
		StudentBaseToken token = new StudentBaseToken(name, identity,
				schoolCode);
		return token;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
