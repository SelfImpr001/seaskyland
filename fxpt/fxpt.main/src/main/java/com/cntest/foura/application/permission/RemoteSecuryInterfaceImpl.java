/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserBelong;
import com.cntest.foura.service.UserBelongService;
import com.cntest.foura.service.UserService;
import com.cntest.security.DefaultUserDetails;
import com.cntest.security.UserDetails;
import com.cntest.security.remote.RemoteSecuryInterface;
import com.cntest.util.ExceptionHelper;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月12日
 * @version 1.0
 **/
public class RemoteSecuryInterfaceImpl implements RemoteSecuryInterface {

	private static Logger logger = LoggerFactory.getLogger(RemoteSecuryInterfaceImpl.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserBelongService userBelongService;

	@Autowired
	private SessionDAO sessionDAO;

	@Override
	public List<String> getResouces(String appKey, UserDetails userDetails) {
		logger.debug("AppKye:{},User :{}", appKey,userDetails.getUserName());
		return null;
	}

	@Override
	public Session getSession(String appKey, Serializable sessionId) {
		logger.debug("sessionId:{}", sessionId);
		return sessionDAO.readSession(sessionId);
	}

	@Override
	public void updateSession(String appKey, Session session) {
		logger.debug("session:{}", session);
		sessionDAO.update(session);
	}

	@Override
	public void deleteSession(String appKey, Session session) {
		logger.debug("AppKye:{}", appKey);
		sessionDAO.delete(session);
	}

	@Override
	public Set<Permission> getPermissions(String appKey, String username) {
		logger.debug("AppKye:{}", appKey);
		return null;
	}

	@Override
	public Serializable createSession(Session session) {
		logger.debug("Session:{}", session);
		return sessionDAO.create(session);
	}

	@Override
	public UserDetails getUserDatails(String userName) {
		User user = userService.findUserBy(userName);
		
		//临时的处理方法
		List<UserBelong> belongs = userBelongService.findBelongFor(user);
		UserDetails uds = user.toUserDetails();
		if(belongs != null && belongs.size() >0) {
			for(UserBelong ub :belongs) {
				((DefaultUserDetails)uds).addOrgCode(ub.getOrg().getCode());
			}			
		}
		
		return uds;
	}
	
	@Override
	public Serializable updatePassword(String userName,String oldPassword,String newPassword) {
		User my = userService.findUserBy(userName);
		try {
			return userService.updatePassword(my, oldPassword,newPassword);
		} catch (BusinessException e) {
			logger.error(ExceptionHelper.trace2String(e));
		}
		return Boolean.FALSE;
	}

}
