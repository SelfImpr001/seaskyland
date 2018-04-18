/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.service.UserService;
import com.cntest.security.UserDetails;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/** 
 * <pre>
 * 主控制器
 * </pre>
 *  
 * @author 李贵庆2014年6月12日
 * @version 1.0
 **/
@Controller
public class FxptController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(FxptController.class);
	
	@Autowired
	private IUserResourceService resourceService;
	
	@Autowired
	private UserService userService;

	
	/**
	 * 登录成功地址
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(ServletRequest request, ServletResponse response) throws Exception{
        logger.debug("URL: / method GET");
        UserDetails user = userService.getCurrentLoginedUser();
        UserResource myApp = getDefaultApp(resourceService);
        if(myApp == null)
        	return ModelAndViewBuilder
    				.newInstanceFor("redirect:/403").build();
        
        logger.debug("Current User: {}",user.getUserName());

		return ModelAndViewBuilder
				.newInstanceFor("/index")
				.append("user", user)
				.append("app", myApp)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("fxpt.index")
								.msg("登录成功").build()).build();
	}
	
	@RequestMapping(value="/home/menu",method=RequestMethod.GET)
	public ModelAndView homeMenu() throws Exception {
		UserDetails user = userService.getCurrentLoginedUser();
		String appKey = getAppKey();
		List<UserResource> menus = resourceService.getResourcesFor(user, appKey, IUserResourceService.Type.MENU,IUserResourceService.Level.FIRST);
		//List<UserResource> userResources = resourceService.getChildrenUserResourceByUrlAnType(null, "/home/menu", null);
		//userResources = resourceService.getChildrenUserResourceByUrlAnType(user, "/home/menu", null);
		//return ModelAndViewBuilder.newInstanceFor("homeMenu").append("userResources", userResources).build();
		return ModelAndViewBuilder.newInstanceFor("homeMenu")
				.append("menus", menus)
				.append("user", user)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("fxpt.index")
						.msg("分析平台首页").build())
				.build();
	}
	
}

