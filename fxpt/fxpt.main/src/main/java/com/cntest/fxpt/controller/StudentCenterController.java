/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.controller;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.RoleService;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserService;
import com.cntest.security.UserDetails;
import com.cntest.security.remote.IUserResourceService;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月28日
 * @version 1.0
 **/

@Controller
public class StudentCenterController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(StudentCenterController.class);

	@Autowired
	private URLResourceService myResourceService;

	@Autowired
	private IUserResourceService resourceService;
	
	@Autowired
	private RoleService roleService;
	
	
	@Autowired
	private UserService userService;
	
	/**
	 * 4a平台首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "student/center")
	public ModelAndView index(ServletRequest request, ServletResponse response) throws Exception {
		logger.debug("/student/center Method Get");
		UserDetails userDetails = userService.getCurrentLoginedUser();
		//student用户是系统默认的通用学生类用户
		User student = userService.findUserBy("student");
		
		//URLResource app = myResourceService.getUserDefaultApp(student);
		

		//List<UserResource> menus = resourceService.getResourcesFor(userDetails,getAppKey(), IUserResourceService.Type.MENU,
		//		IUserResourceService.Level.ALL);
		return ModelAndViewBuilder
				.newInstanceFor("/student/center")
				//.append("user", userDetails)
				//.append("app", app)
				//.append("menus", menus)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE)
								.code("4a.index").msg("student.center").build())
				.build();
	}
}
