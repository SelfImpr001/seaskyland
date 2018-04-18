/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.config.SystemRuntimeConfiguration;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserBelong;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserBelongService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.anlaysis.service.IReportExamService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.security.DefaultUserOrg;
import com.cntest.security.UserDetails;
import com.cntest.security.UserOrg;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService;
import com.cntest.security.remote.IUserResourceService.Level;
import com.cntest.security.remote.IUserResourceService.Type;
import com.cntest.security.remote.UserDetailsService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年12月18日
 * @version 1.0
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private URLResourceService urlResourceService;

	@Autowired
	private UserBelongService userBelongService;
	
	@Autowired(required = false)
	private IReportExamService reportExamService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails findUserDetailsBy(String userName) {
		User user = userService.findUserBy(userName);
		return user.toUserDetails();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserOrg> findUserOrgs(UserDetails userDetails) {
		User user = userService.findUserBy(userDetails.getUserName());
		List<UserBelong> belongs = userBelongService.findBelongFor(user);
		ArrayList<UserOrg> userOrgs = new ArrayList<UserOrg>();
		if (belongs != null) {
			for (UserBelong belong : belongs) {
				userOrgs.add(new DefaultUserOrg(belong.getOrg().getCode(), belong.getOrg().getName(), belong.getOrg().getName(), belong.getOrg()
						.getType(),userDetails));
			}
		}

		return userOrgs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserResource> findUserResource(UserDetails userDetails, Type resType, Level resLevel) {
		List<UserResource> ss = new ArrayList<UserResource>();
		try {
			User user = userService.findUserBy(userDetails.getUserName());
			String appKey = SystemRuntimeConfiguration.getAppKey();
			logger.info("GET {}'s Resource with {}", user.getName(), appKey);
			ss = UserPermissonFactory.getInstanceOf(resType).inject(urlResourceService).with(resLevel).query(user, appKey,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ss;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<UserResource> findUserResourcefilter(UserDetails userDetails, Type resType, Level resLevel,Long examid) {
		Exam exam = reportExamService.getExam(examid);
		User user = userService.findUserBy(userDetails.getUserName());
		String appKey = SystemRuntimeConfiguration.getAppKey();
		logger.info("GET {}'s Resource with {}", user.getName(), appKey);
		UserPermisson userpes =	UserPermissonFactory.getInstanceOf(resType).inject(urlResourceService).with(resLevel);
		List<UserResource> list = userpes.query(user, appKey,exam);
		return list;
	}

	@Override
	public UserResource getDefaultApp(UserDetails user) {
		String appKey = SystemRuntimeConfiguration.getAppKey();
		List<UserResource> apps = this.findUserResource(user, IUserResourceService.Type.APP, IUserResourceService.Level.FIRST);
		if (apps == null || apps.size() == 0)
			return null;
		UserResource defaultApp = apps.get(0);
		if (appKey.equalsIgnoreCase(defaultApp.getUuid()))
			return defaultApp;
		for (UserResource app : apps) {
			if (appKey.equalsIgnoreCase(app.getUuid()))
				return app;
		}
		return defaultApp;
	}

}
