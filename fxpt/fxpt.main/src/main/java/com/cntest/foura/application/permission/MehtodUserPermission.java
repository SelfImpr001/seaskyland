/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.fxpt.domain.Exam;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService.Level;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月21日
 * @version 1.0
 **/
public class MehtodUserPermission extends AbstractUserPermission {
	private static Logger logger = LoggerFactory.getLogger(MehtodUserPermission.class);
	
	@Override
	public List<UserResource> query(User user, String uuid,Exam exam) {
		setMyLevel(Level.FIRST);
		logger.info("Get User {} MehtodUserPermission with {}",user,uuid);
		List<URLResource> reses = getUrlResourceService().getMenuChildren(user, uuid);	
		logger.info("User {} MehtodUserPermission for {}",getMyLevel()+ " " + user,reses.size());
		return toUserResource(reses,user,uuid,exam);
	}

	
	private final static class SingletonHolder{
		private final static MehtodUserPermission instance = new MehtodUserPermission();
	}
	
	public final static UserPermisson getInstance() {
		return SingletonHolder.instance;
	}
}

