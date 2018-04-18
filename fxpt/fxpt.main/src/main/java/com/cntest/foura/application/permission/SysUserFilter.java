package com.cntest.foura.application.permission;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.security.UserDetails;

/**
 * <p>User: 董博野
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
public class SysUserFilter extends PathMatchingFilter {
	private Logger logger = LoggerFactory.getLogger(SysUserFilter.class);
    

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    	
    	UserDetails user = (UserDetails)SecurityUtils.getSubject().getPrincipal();
        logger.info(user.toString());
        
        return true; 
    }
}
