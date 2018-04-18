/*
 * @(#)com.cntest.security.shiroimpl.SessionTimeoutFilter.java 1.0 2015年2月5日:下午2:38:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.security.shiroimpl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cntest.security.remote.IUserResourceService;

public class UrlCheckFilter extends AdviceFilter {
  @Autowired
  private IUserResourceService urlResourceService;
  private String failureUrl;

  public String getFailureUrl() {
    return failureUrl;
  }

  public void setFailureUrl(String failureUrl) {
    this.failureUrl = failureUrl;
  }

  @Override
  protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    String url = ((HttpServletRequest) request).getServletPath();
    Subject subject = SecurityUtils.getSubject();
    if (subject.getPrincipal() != null) {
      boolean b = urlResourceService.getUserResourceByUrl(url);
      if (!b) {
        String filePath = ((HttpServletRequest) request).getRequestURL().toString();
        filePath = filePath.replaceAll(url, "/login");
        WebUtils.issueRedirect(request, response, filePath);
        // WebUtils.issueRedirect(request, response, failureUrl);
        return false;
      } else {
        return true;
      }
    }
    return false;
  }

}
