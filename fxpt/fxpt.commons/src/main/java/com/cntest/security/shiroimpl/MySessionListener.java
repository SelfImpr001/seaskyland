/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.shiroimpl;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月16日
 * @version 1.0
 **/
public class MySessionListener implements SessionListener {
	private Logger logger = LoggerFactory.getLogger(MySessionListener.class);
	@Override
	public void onStart(Session session) {
		logger.info("Session Start For ID:{}",session.getId());
	}

	@Override
	public void onStop(Session session) {
		logger.info("Session Stop For ID:{}",session.getId());
	}

	@Override
	public void onExpiration(Session session) {
		logger.info("Session Expiration For ID:{}",session.getId());
	}

}

