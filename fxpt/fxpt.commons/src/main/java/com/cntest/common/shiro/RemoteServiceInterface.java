/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.cntest.common.shiro;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.session.Session;

import com.cntest.security.UserDetails;

/**
 * <p>
 * User: hhc
 * <p>
 * Date: 14-3-13
 * <p>
 * Version: 1.0
 */
public interface RemoteServiceInterface {

	public List<String> getResouces(String appKey, UserDetails user);

	public Session getSession(String appKey, Serializable sessionId);

	Serializable createSession(Session session);

	public void updateSession(String appKey, Session session);

	public void deleteSession(String appKey, Session session);

	public Set<Permission> getPermissions(String appKey, String username);
}
