/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.ApplySet;
import com.cntest.fxpt.service.IApplySetService;
import com.cntest.fxpt.util.ShiroClientConfig;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.security.RoleType;
import com.cntest.security.UserDetails;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService;
import com.cntest.security.shiroimpl.CaptchaValidateFilter;
import com.cntest.util.SpringContext;
import com.cntest.web.util.ServletUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.Progress;
import com.cntest.web.view.ProgressListener;
import com.cntest.web.view.ResponseStatus;

/**
 * <pre>
 * 4a平台首页
 * </pre>
 * 
 * @author 李贵庆2014年6月11日
 * @version 1.0
 **/

@Controller
public class IndexController extends BaseController {
	private static Logger logger = LoggerFactory
			.getLogger(IndexController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private URLResourceService myResourceService;

	@Autowired
	private IUserResourceService resourceService;
	
	@Autowired
	private IApplySetService applySetService;
	

	/**
	 * 首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/home")
	public ModelAndView index(ServletRequest request, ServletResponse response)
			throws Exception {
		logger.debug("URL: /home");
		UserDetails userDetails = userService.getCurrentLoginedUser();
		User user = User.from(userDetails);
		List<ApplySet> applySeList=applySetService.findApplyByStatus("1");
		
		User theUser  = userService.findUserBy(userDetails.getUserName());
		//获取系统名称
		String title="";
		//获取系统操作界面图标
		String logoImage="";
		if(applySeList.size()>0){
			title=applySeList.get(0).getSystemName();
			logoImage=applySeList.get(0).getHandleIcon();
			if(logoImage!=""){
				logoImage="style='background: no-repeat center; background-image:url("+logoImage+")'";
			}
		}else{
			//黄洪成2015-03-25 新增关于（系统版本）
			title = SystemConfig.newInstance().getValue("cntest.title");
		}
		String version = SystemConfig.newInstance().getValue("cntest.version");
		String date = SystemConfig.newInstance().getValue("cntest.date");
		//------
	
		String appKey = getAppKey();
		URLResource app = myResourceService.getUserDefaultApp(user);
		//获取系统退出路径
		Properties prop = new Properties();
		InputStream fis =ShiroClientConfig.class.getResourceAsStream("/properties/shiro-client-default.properties");
        prop.load(fis);  
        //一定要在修改值之前关闭fis  
        fis.close();  
		String sysLoginOut=(String) prop.get("shiro.sso.loginOut.url");
		// 暂时这样实现
		if ("admin".equals(user.getName())) {
			// List<URLResource> menus =
			// myResourceService.getAppMenuResources(user,getAppKey());

			/* 不调用该方法, 菜单将会把与角色没关联的资源也显示出来 */
			// myResourceService.filterNonRole(menus);
			List<UserResource> menus = resourceService.getResourcesFor(
					userDetails, appKey, IUserResourceService.Type.MENU,
					IUserResourceService.Level.ALL);
			
		
			return ModelAndViewBuilder
					.newInstanceFor("index")
					.append("user", userDetails)
					.append("theUser", theUser)
					.append("logoImage", logoImage)
					.append("sysLoginOut", sysLoginOut)
					.append("app", app)
					.append("menus", menus)
					.append("title", title)
					.append("version", version)
					.append("date", date)
					.append("priview", false)
					.append(ResponseStatus.NAME,
							new ResponseStatus.Builder(Boolean.TRUE)
									.code("4a.index").msg(app.getName())
									.build()).build();
		}
		
		List<UserResource> menus = resourceService.getResourcesFor(userDetails,
				appKey, IUserResourceService.Type.MENU,
				IUserResourceService.Level.ALL);
		// logger.info("User {} default app is {}",user.getName(),app.getUrl());
		// List<URLResource> menus =
		// resourceService.getAppMenuResources(user,getAppKey());

		/* 不调用该方法, 菜单将会把与角色没关联的资源也显示出来 */
		// resourceService.filterNonRole(menus);
		return ModelAndViewBuilder
				.newInstanceFor("index")
				.append("logoImage", logoImage)
				.append("theUser", theUser)
				.append("sysLoginOut", sysLoginOut)
				.append("user", userDetails)
				.append("app", app)	
				.append("menus", menus)
				.append("title", title)
				.append("version", version)
				.append("date", date)
				.append("priview", false)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE)
								.code("4a.index").msg(app.getName()).build())
				.build();
	}

	/**
	 * 4a平台首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard")
	public ModelAndView dashboard(ServletRequest request,
			ServletResponse response) throws Exception {
		logger.debug("/dashboard");
		UserDetails user = userService.getCurrentLoginedUser();;
		String forward = "dashboard";
		if(user.roleOf(RoleType.manager))
			forward = "forward:/manager/home";
		if(user.roleOf(RoleType.student))
			forward = "forward:/student/center";
		return ModelAndViewBuilder
				.newInstanceFor(forward)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE)
								.code("dashboard").msg("我的首页").build()).build();
	}

	/**
	 * 4a平台首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/executor/progress/{entry}")
	public ModelAndView executor(@PathVariable String entry,
			ServletRequest request, ServletResponse response) throws Exception {
		logger.debug("/executor/progress/{}", entry);
		ProgressListener progressListener = SpringContext.getBean(entry);
		Progress progress = null;
		if (progressListener != null)
			progress = progressListener.on(ServletUtil.getRequestMap(request));
		else
			progress = new Progress(100, 100);
		return ModelAndViewBuilder
				.newInstanceFor("share/progressbar")
				.append("progress", progress)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE)
								.code("dashboard").msg("我的首页").build()).build();
	}
	
	/**
	 * 登录页面预览
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/priview/loginLY/{logoImage}/{systemName}")
	public ModelAndView indexYL(@PathVariable String logoImage,@PathVariable String systemName,ServletRequest request, ServletResponse response) throws Exception {
		 logger.debug("URL: /login method GET");
	        HttpServletResponse res = (HttpServletResponse)response;
	        CaptchaValidateFilter filter = SpringContext.getBean("captchaFilter");
	        boolean showCode = filter.isCaptchaEbabled();//是否启用验证码
	       
	  		List<ApplySet> applySeList=applySetService.findApplyByStatus("1");
	  		//获取系统名称
	  		String title="";
	  		//登录页面图标
	  		String logoX="";
	  		if(applySeList.size()>0){
	  			title=applySeList.get(0).getSystemName();
	  			logoX=applySeList.get(0).getLoginIcon();
	  		}
  			//取页面的值
			if(logoImage=="-1" || "-1".equals(logoImage)){
				logoImage=logoX;
			}else  if("update".equalsIgnoreCase(logoImage)){
				ApplySet apply=applySetService.get(Long.parseLong(systemName!=""?systemName:"0"));
				logoImage=apply.getLoginIcon();
				systemName=apply.getSystemName();
			}else{
				List<ApplySet> applySeList3=applySetService.findApplyByStatus("3");
				logoImage=applySeList3.size()>0?applySeList3.get(0).getLoginIcon():"";
			}
			if(logoImage!=""){
				logoImage="style='background: no-repeat center 10px; background-image:url("+logoImage+")'";
  			}
			if(systemName=="-1" || "-1".equals(systemName)){
				systemName=title;
			}
	  		if("".equals(systemName) || "-1".equals(systemName)){
	  			//黄洪成2015-03-25 新增关于（系统版本）
	  			systemName = SystemConfig.newInstance().getValue("cz.systemName");
	  		}
	  		//获取系统退出路径
			Properties prop = new Properties();
			InputStream fis =ShiroClientConfig.class.getResourceAsStream("/properties/shiro-client-default.properties");
	        prop.load(fis);  
	        //一定要在修改值之前关闭fis  
	        fis.close();  
			String sysLoginOut=(String) prop.get("shiro.sso.loginOut.url");	
	        request.setAttribute("systemName", systemName);
	        Cookie rememberMeCookie = SpringContext.getBean("rememberMeCookie");
	        int maxAge = rememberMeCookie.getMaxAge();
			return ModelAndViewBuilder
					.newInstanceFor("/login")
					.append("timeOut",false)
					.append("logoImage",logoImage)
					.append("sysLoginOut",sysLoginOut)
					.append("showCode",showCode)
					.append("expires", maxAge/60/60/24)
					.append("isStudent",false)
					.append("priview", true)
					.append(ResponseStatus.NAME,
							new ResponseStatus.Builder(Boolean.TRUE).code("common.login")
									.msg("登录表单").build()).build();
		//return ModelAndViewBuilder.newInstanceFor("/loginForm2").append("request", request).build();
	}
	
	/**
	 * 操作页面预览
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/priview/mainLY/{logoImage}/{systemName}")
	public ModelAndView mainYL(@PathVariable String logoImage,@PathVariable String systemName,
			ServletRequest request, ServletResponse response) throws Exception {
		logger.debug("URL: /home");
		UserDetails userDetails = userService.getCurrentLoginedUser();
		User user = User.from(userDetails);
		List<ApplySet> applySeList=applySetService.findApplyByStatus("1");
		//获取系统名称
  		String title="";
  		//登录页面图标
  		String logoX="";
  		if(applySeList.size()>0){
  			title=applySeList.get(0).getSystemName();
  			logoX=applySeList.get(0).getLoginIcon();
  		}
		//取页面的值
		if(logoImage=="-1" || "-1".equals(logoImage)){
			logoImage=logoX;
		}else  if("update".equalsIgnoreCase(logoImage)){
			ApplySet apply=applySetService.get(Long.parseLong(systemName!=""?systemName:"0"));
			logoImage=apply.getHandleIcon();
			systemName=apply.getSystemName();
		}else{
			List<ApplySet> applySeList3=applySetService.findApplyByStatus("3");
			logoImage=applySeList3.size()>0?applySeList3.get(0).getHandleIcon():"";
		}
		if(logoImage!=""){
			logoImage="style='background: no-repeat center 0px; background-image:url("+logoImage+")'";
		}
		if(systemName=="-1" || "-1".equals(systemName)){
			systemName=title;
		}
		if(systemName==""){
			systemName=SystemConfig.newInstance().getValue("cntest.title");
		}
		String version = SystemConfig.newInstance().getValue("cntest.version");
		String date = SystemConfig.newInstance().getValue("cntest.date");
		//------
		//获取系统退出路径
		Properties prop = new Properties();
		InputStream fis =ShiroClientConfig.class.getResourceAsStream("/properties/shiro-client-default.properties");
        prop.load(fis);  
        //一定要在修改值之前关闭fis  
        fis.close();  
		String sysLoginOut=(String) prop.get("shiro.sso.loginOut.url");
		String appKey = getAppKey();
		URLResource app = myResourceService.getUserDefaultApp(user);
		app.setName(systemName);
		// 暂时这样实现
		if ("admin".equals(user.getName())) {
			List<UserResource> menus = resourceService.getResourcesFor(
					userDetails, appKey, IUserResourceService.Type.MENU,
					IUserResourceService.Level.ALL);
			
		
			return ModelAndViewBuilder
					.newInstanceFor("index")
					.append("user", userDetails)
					.append("logoImage", logoImage)
					.append("sysLoginOut", sysLoginOut)
					.append("app", app)
					.append("menus", menus)
					.append("title", systemName)
					.append("version", version)
					.append("date", date)
					.append("priview", true)
					.append(ResponseStatus.NAME,
							new ResponseStatus.Builder(Boolean.TRUE)
									.code("4a.index").msg(app.getName())
									.build()).build();
		}
		
		List<UserResource> menus = resourceService.getResourcesFor(userDetails,
				appKey, IUserResourceService.Type.MENU,
				IUserResourceService.Level.ALL);
		return ModelAndViewBuilder
				.newInstanceFor("index")
				.append("logoImage", logoImage)
				.append("sysLoginOut", sysLoginOut)
				.append("user", userDetails)
				.append("app", app)	
				.append("menus", menus)
				.append("title", systemName)
				.append("version", version)
				.append("date", date)
				.append("priview", true)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE)
								.code("4a.index").msg(app.getName()).build())
				.build();
	}

}