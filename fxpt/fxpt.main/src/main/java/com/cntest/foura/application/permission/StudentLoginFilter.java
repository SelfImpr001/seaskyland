package com.cntest.foura.application.permission;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月25日
 * @version 1.0
 **/
public class StudentLoginFilter extends AccessControlFilter {

	private static Logger logger = LoggerFactory.getLogger(StudentLoginFilter.class);
	
	private String studentLoginUrl = "/student/login";
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		HttpServletRequest req = WebUtils.toHttp(request);
        String path = req.getRequestURI();
        if(path != null && path.endsWith(studentLoginUrl)) {
        	req.getSession().setAttribute("isStudent", true);
        	//WebUtils.redirectToSavedRequest(request, response, studentLoginUrl);
        	//setLoginUrl(studentLoginUrl);
        }
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		setLoginUrl(studentLoginUrl);
		return false;
	}
	
	public void setStudentLoginUrl(String studentLoginUrl) {
		this.studentLoginUrl = studentLoginUrl;
	}
}

