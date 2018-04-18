/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.shiroimpl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月18日
 * @version 1.0
 **/
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {
	private static Logger logger = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);

	private boolean enabled = true;
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		if(!enabled)
			return false;
		if (request.getAttribute(getFailureKeyAttribute()) != null) {
			logger.info("验证码不匹配");
			return true;
		}
		
		Cookie cookie = new Cookie(MyLogoutFilter.LOGOUT_BACK_URL_PRAMA_NAME,
				this.getLoginUrl());
		cookie.setPath(WebUtils.toHttp(request).getContextPath());
		WebUtils.toHttp(response).addCookie(cookie);
		
		//this.createToken(request, response)
		boolean b =  super.onAccessDenied(request, response, mappedValue);
		//b = true;
		if(b) {
			request.setAttribute("username", request.getParameter("username"));
			request.setAttribute("password", request.getParameter("password"));
			
			Subject subject = getSubject(request, response);
			if(subject.getPrincipal() == null) {
				 HttpServletRequest req = (HttpServletRequest)request;
				 //String s = req.getRequestURI();
			     HttpServletResponse res = (HttpServletResponse)response;
			     res.setHeader("sessionstatus", "timeout");
			}
		}
		return b;
		//return super.onAccessDenied(request, response, mappedValue);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		request.setAttribute(getFailureKeyAttribute(), "用户名或者密码有误，请重新输入！");
		return true;//super.onLoginFailure(token, e, request, response);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
