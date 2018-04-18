/**
 * <p><b>© </b></p>
 * 
 **/
package com.cntest.security.remote;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2014年6月15日
 * @version 1.0
 **/
public class RemoteSessionDAO extends CachingSessionDAO {
	private RemoteSecuryInterface remoteService;
	
	private String appKey;

	public void setRemoteService(RemoteSecuryInterface remoteService) {
		this.remoteService = remoteService;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	protected void doDelete(Session session) {
		this.remoteService.deleteSession(this.appKey, session);
	}

	protected void doUpdate(Session session) {
		this.remoteService.updateSession(this.appKey, session);
	}

	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.remoteService.createSession(session);
		assignSessionId(session, sessionId);
		return sessionId;
	}

	protected Session doReadSession(Serializable sessionId) {
		return this.remoteService.getSession(this.appKey, sessionId);
	}
}
