/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.shiroimpl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月27日
 * @version 1.0
 **/
public class MyLogoutFilter extends LogoutFilter {
	public static final String LOGOUT_BACK_URL_PRAMA_NAME = "logoutbackurl";

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {

		String redirectUrl = getRedirectUrl();
		HttpSession session = WebUtils.toHttp(request).getSession();
		if (session.getAttribute(LOGOUT_BACK_URL_PRAMA_NAME) != null) {
			redirectUrl = session.getAttribute(LOGOUT_BACK_URL_PRAMA_NAME) + "";
			session.removeAttribute(LOGOUT_BACK_URL_PRAMA_NAME);
		}

		Subject subject = getSubject(request, response);
		subject.logout();
		issueRedirect(request, response, redirectUrl);
		return false;
	}
}
