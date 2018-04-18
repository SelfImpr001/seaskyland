/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.shiroimpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;
import com.cntest.web.util.ServletUtil;
import com.cntest.web.view.ResponseStatus;
import com.google.gson.Gson;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年9月24日
 * @version 1.0
 **/
public class AjaxFormAuthenticationFilter extends FormAuthenticationFilter {
	private static Logger logger = LoggerFactory.getLogger(AjaxFormAuthenticationFilter.class);
	
	private String captchaParam = "kaptcha";//前台提交的验证码参数名
	
	private boolean captchaEbabled = true;//是否开启验证码支持
	
	private boolean enabled = true;
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		
        if (!"post".equalsIgnoreCase(WebUtils.toHttp(request).getMethod())) {
            return true;
        }
        
      //如果点浏览器后退按钮退到登录页面，再次登录，需要先执行退出
        Subject subject = SecurityUtils.getSubject();
		if (subject != null && subject.isAuthenticated()) {
			subject.logout();
		}
		
		if(!enabled)
			return true;
		if(ServletUtil.isAjaxRequest(request)) {
			HashMap<String,String> requestMap  = ServletUtil.requestBodyToJson(request,  HashMap.class);
			boolean b = vlidateVcode(requestMap,WebUtils.toHttp(request));
			if((!b)) {
				ResponseStatus rs = new ResponseStatus.Builder(Boolean.FALSE).code("Cntest-01001").msg("验证码错误").build();
				jsonOutput(response,rs);
				return false;
			}
			
			if (isLoginRequest(request, response)) {
	            if (isLoginSubmission(request, response)) {
	            	logger.trace("Login submission detected.  Attempting to execute login.");	                
	            	String username = requestMap.get(getUsernameParam());
	                String password = requestMap.get(getPasswordParam());
	                boolean rememberMe = isRememberMe(requestMap);
	                
	                String host = getHost(request);
	                AuthenticationToken token =  createToken(username, password, rememberMe, host);;
	            	
	                if (token == null) {
	                    String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
	                            "must be created in order to execute a login attempt.";
	                    throw new IllegalStateException(msg);
	                }
	                try {
	                    subject = getSubject(request, response);
	                    subject.login(token);
	                    return onLoginSuccess(token, subject, request, response);
	                } catch (AuthenticationException e) {
	                    return onLoginFailure(token, e, request, response);
	                }
	            } else {
	                logger.trace("Login page view.");
	                return true;
	            }
	        } else {
	            logger.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
	                        "Authentication url [" + getLoginUrl() + "]");

	            saveRequestAndRedirectToLogin(request, response);
	            return false;
	        }
		}else {
			return super.onAccessDenied(request, response, mappedValue);
		}
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		ResponseStatus rs = new ResponseStatus.Builder(Boolean.FALSE).code("Cntest-01002").msg("用户名或者密码错误").build();
	    jsonOutput(response,rs);
		return false;
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
            ServletRequest request, ServletResponse response) throws Exception {
		ResponseStatus rs = new ResponseStatus.Builder(Boolean.TRUE).code("Cntest-01000").msg("登录成功").build();
	    jsonOutput(response,rs);
		return false;
	}
	
	private boolean isRememberMe(HashMap<String, String> requestMap) {
		String value = requestMap.get(getRememberMeParam());
		return value != null &&
                (value.equalsIgnoreCase("true") ||
                        value.equalsIgnoreCase("t") ||
                        value.equalsIgnoreCase("1") ||
                        value.equalsIgnoreCase("enabled") ||
                        value.equalsIgnoreCase("y") ||
                        value.equalsIgnoreCase("yes") ||
                        value.equalsIgnoreCase("on"));
	}
	
	private boolean vlidateVcode(HashMap<String, String> requestMap,HttpServletRequest request) {
		if(!captchaEbabled) 
			return true;
		
		String captchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String vcode = (String) requestMap.get(captchaParam);
		logger.debug("vcode={}",vcode);
        return vcode == null?false:vcode.equalsIgnoreCase(captchaCode);
	}
	

	private void jsonOutput(ServletResponse response, ResponseStatus rs){
		HashMap<String, ResponseStatus> responseMap = new HashMap<String, ResponseStatus>();
		responseMap.put(ResponseStatus.NAME, rs);
		try {
			PrintWriter writer = response.getWriter();
			String json = new Gson().toJson(responseMap);
			writer.write(json);
			writer.flush();
			writer.close();
		}catch(IOException e) {
			logger.error(ExceptionHelper.trace2String(e));
		}
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
}

