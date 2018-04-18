/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.shiroimpl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月18日
 * @version 1.0
 **/
public class CaptchaValidateFilter extends AccessControlFilter {

	private String captchaParam = "kaptcha";//前台提交的验证码参数名
	
	private boolean captchaEbabled = true;//是否开启验证码支持


    private String failureKeyAttribute = "LoginFailure"; //验证码验证失败后存储到的属性名
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		//设置验证码是否开启属性，页面可以根据该属性来决定是否显示验证码
        request.setAttribute("captchaEbabled", captchaEbabled);
        
        //如果点浏览器后退按钮退到登录页面，再次登录，需要先执行退出
        Subject subject = SecurityUtils.getSubject();
		if (subject != null && subject.isAuthenticated()) {
			subject.logout();
		}
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //判断验证码是否禁用 或不是表单提交（允许访问）
        if (captchaEbabled == false || !"post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            return true;
        }
        //此时是表单提交，验证验证码是否正确  暂时不实现
        String vcode = (String) request.getParameter(captchaParam);
        String captchaCode = (String)httpServletRequest.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        
        return vcode == null?false:vcode.equalsIgnoreCase(captchaCode);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		//如果验证码失败了，存储失败key属性
        request.setAttribute(failureKeyAttribute, "验证码错误");
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("password", request.getParameter("password"));
        return true;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	public void setCaptchaEbabled(boolean captchaEbabled) {
		this.captchaEbabled = captchaEbabled;
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	public boolean isCaptchaEbabled() {
		return captchaEbabled;
	}

	public void setFailureKeyAttribute(String failureKeyAttribute) {
		this.failureKeyAttribute = failureKeyAttribute;
	}
	
	

}

