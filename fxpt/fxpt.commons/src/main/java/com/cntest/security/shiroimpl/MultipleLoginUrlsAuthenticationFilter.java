/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.shiroimpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月25日
 * @version 1.0
 **/
public class MultipleLoginUrlsAuthenticationFilter extends CaptchaFormAuthenticationFilter {

	private static Logger logger = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);
	
	private Map<String,String> loginUrls = new HashMap<String,String>();
	
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response )throws IOException{
		String loginUrl = getLoninUrl(request);
		logger.debug("Login url is {}",loginUrl);
		WebUtils.issueRedirect(request, response, loginUrl);
	}

	@Override
	protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        return pathsMatch(path, requestURI);
    } 
	
	private String getLoninUrl(ServletRequest request) {
		HttpServletRequest req = WebUtils.toHttp(request);
        String path = req.getRequestURI();
        if(path.length() < 1)
        	return getLoginUrl();
        String contextPath = req.getContextPath();
        String relativePath = path.substring(contextPath.length(),path.length());
        return loginUrls.containsValue(relativePath)?relativePath:getLoginUrl();
	}

	public void setLoginUrls(Map<String, String> loginUrls) {
		this.loginUrls = loginUrls;
	}
	
}

