/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.security.shiroimpl.CaptchaFormAuthenticationFilter;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月13日
 * @version 1.0
 **/
public class ServerFormAuthenticationFilter extends CaptchaFormAuthenticationFilter {
	private static Logger logger = LoggerFactory.getLogger(ServerFormAuthenticationFilter.class);
	
	private String studentLoginUrl = "/student/login";
	
//	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
//        Subject subject = getSubject(request, response);
//        Session session = subject.getSession();
//
//		String fallbackUrl = (String)session.getAttribute("authc.fallbackUrl");
//        logger.debug("Fallback Url:{}",fallbackUrl);
//        //fallbackUrl= "http://localhost:8080/fxpt/index?userId=1";
//        if(StringUtils.isEmpty(fallbackUrl)) {
//            fallbackUrl = getSuccessUrl();
//        }
//        WebUtils.redirectToSavedRequest(request, response, fallbackUrl);
//    }
	
//	@Override
//	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//
//		return super.onAccessDenied(request, response, mappedValue);
//	}
	

	@Override
	protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest req = WebUtils.toHttp(request);
        String path = req.getPathInfo();
        if(studentLoginUrl.equalsIgnoreCase(path)) {
        	req.getSession().setAttribute("isStudent", true);
        }
//        /HttpServletResponse res = WebUtils.toHttp(response);
        //if(req.get)
		saveRequest(request);
        redirectToLogin(request, response);
    }

	public void setStudentLoginUrl(String studentLoginUrl) {
		this.studentLoginUrl = studentLoginUrl;
	}

}

