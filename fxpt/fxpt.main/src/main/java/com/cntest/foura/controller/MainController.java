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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserAction;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.ApplySet;
import com.cntest.fxpt.service.IApplySetService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.ShiroClientConfig;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.remote.domain.NanShanData;
import com.cntest.remote.service.INanShanDataService;
import com.cntest.security.RoleType;
import com.cntest.security.UserDetails;
import com.cntest.security.shiroimpl.CaptchaValidateFilter;
import com.cntest.security.shiroimpl.MyLogoutFilter;
import com.cntest.util.SpringContext;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/** @author 李贵庆2014年6月7日
 * @version 1.0
 **/

@Controller
public class MainController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired(required = false)
	@Qualifier("INanShanDataService")
	private INanShanDataService nanShanDataService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IApplySetService applySetService;
	/**
	 * 登录地址
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login")
	public ModelAndView login(ServletRequest request,ServletResponse response,HttpSession session) throws Exception{
        logger.debug("URL: /login method GET");
        HttpServletResponse res = (HttpServletResponse)response;
        HttpServletRequest req = (HttpServletRequest)request;
        String sessionstatus = res.getHeader("sessionstatus");
        Object isloin =    req.getSession().getAttribute("islogin");
        req.getSession().removeAttribute("islogin");
        Boolean timeout = Boolean.FALSE;
        if(sessionstatus!=null && "timeout".equalsIgnoreCase(sessionstatus) ) {
        	timeout = Boolean.TRUE;
        }
    	//获取系统退出路径
		Properties prop = new Properties();
		InputStream fis =ShiroClientConfig.class.getResourceAsStream("/properties/shiro-client-default.properties");
        prop.load(fis);  
        //一定要在修改值之前关闭fis  
        fis.close();  
		String sysLoginOut=(String) prop.get("shiro.sso.loginOut.url");
        CaptchaValidateFilter filter = SpringContext.getBean("captchaFilter");
        boolean showCode = filter.isCaptchaEbabled();//是否启用验证码
       
  		List<ApplySet> applySeList=applySetService.findApplyByStatus("1");
  		//获取系统名称
  		String systemName="";
  		//登录页面图标
  		String logoImage="";
  		if(applySeList.size()>0){
  			systemName=applySeList.get(0).getSystemName();
  			logoImage=applySeList.get(0).getLoginIcon();
  			if(logoImage!=""){
  				logoImage="style='background: no-repeat center 10px; background-image:url("+logoImage+")'";
  			}
  		}else{
  			//黄洪成2015-03-25 新增关于（系统版本）
  			systemName = SystemConfig.newInstance().getValue("cz.systemName");
  		}
      		
        request.setAttribute("systemName", systemName);
        Cookie rememberMeCookie = SpringContext.getBean("rememberMeCookie");
        int maxAge = rememberMeCookie.getMaxAge();
        //boolean isStudent = session.getAttribute("isStudent") != null;
        //session.removeAttribute("isStudent");
		return ModelAndViewBuilder
				.newInstanceFor("/login")
				.append("timeOut",timeout)
				.append("isloin",isloin)
				.append("logoImage",logoImage)
				.append("sysLoginOut",sysLoginOut)
				.append("showCode",showCode)
				.append("expires", maxAge/60/60/24)
				.append("isStudent",false)
				.append("priview", false)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("common.login")
								.msg("登录表单").build()).build();
	}
	
	@RequestMapping(value = "/student/login")
	public ModelAndView loginOfStudent(ServletRequest request,ServletResponse response) throws Exception{
        logger.debug("URL: /student/login method GET");
        HttpServletResponse res = (HttpServletResponse)response;
        String sessionstatus = res.getHeader("sessionstatus");
        Boolean timeout = Boolean.FALSE;
        if(sessionstatus!=null && "timeout".equalsIgnoreCase(sessionstatus) ) {
        	timeout = Boolean.TRUE;
        }
        
        CaptchaValidateFilter filter = SpringContext.getBean("captchaFilter");
        boolean showCode = filter.isCaptchaEbabled();//是否启用验证码
        
        Cookie rememberMeCookie = SpringContext.getBean("rememberMeCookie");
        int maxAge = rememberMeCookie.getMaxAge();
        //获取系统名称
  		List<ApplySet> applySeList=applySetService.findApplyByStatus("1");
  		String systemName="";
  		if(applySeList.size()>0){
  			systemName=applySeList.get(0).getSystemName();
  		}else{
  			//黄洪成2015-03-25 新增关于（系统版本）
  			systemName = SystemConfig.newInstance().getValue("cz.systemName");
  		}
        request.setAttribute("systemName", systemName);
        
		return ModelAndViewBuilder
				.newInstanceFor("/login")
				.append("timeOut",timeout)
				.append("showCode",showCode)
				.append("expires", maxAge/60/60/24)
				.append("isStudent",true)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("student.login")
								.msg("登录表单").build()).build();
	}
	
	/**
	 * 登录成功地址
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public ModelAndView index(ServletRequest request, ServletResponse response) throws Exception{
        logger.debug("URL: / method GET");
        String status = "失败",name="",info = "用户<b style='color:red;'>",erre="";
        boolean isStudent=false;
        try {
	        Subject subject = SecurityUtils.getSubject(); 
	        logger.debug("Login User: {}",subject);
	        String appKey = getAppKey();
	        
	        UserDetails userDetails = userService.getCurrentLoginedUser();
	        User user = User.from(userDetails);
	        name=user.getName();
	        UserAction action = user.login();
	        logger.info(action.toString());
	        
	        isStudent=userDetails.roleOf(RoleType.student);
			
	        //南山单点登录
			NanShanData nanShanData = nanShanDataService.findByUid(userDetails.getUserName());
			if(nanShanData!=null){
				isStudent=true;
			}
	        
	        if(!isStudent) {
	        	WebUtils.toHttp(request).getSession().removeAttribute(MyLogoutFilter.LOGOUT_BACK_URL_PRAMA_NAME);
	        }
	        status = "成功";
        } catch (Exception e) {
        	erre = LogUtil.e(e);
        	throw e;
        }finally {
        	info+=name+"</b>登录"+status;
        	LogUtil.log("系统操作", "登录",name,status, info,erre);
        }
        
        //URLResource resource =  userService.getDefaultUrlResource(user);
		return ModelAndViewBuilder
				.newInstanceFor("redirect:/home")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("login")
								.msg("登录成功").build()).build();
	}
	
	/**
	 * 退出系统地址
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout")
	public ModelAndView logout(ServletRequest request, ServletResponse response) throws Exception{
        logger.debug("URL: /logout");
		return ModelAndViewBuilder
				.newInstanceFor("redirect:/login")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("login")
								.msg("退出成功").build()).build();
	}

	/**
	 * 退出系统地址
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/html/{}")
	public ModelAndView html(ServletRequest request, ServletResponse response) throws Exception{
        logger.debug("URL: /logout");

		return ModelAndViewBuilder
				.newInstanceFor("redirect:/login")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("login")
								.msg("退出成功").build()).build();
	}
	
	
	@RequestMapping(value = "/500")
	public ModelAndView error(ServletRequest request, ServletResponse response) throws Exception{
        logger.debug("URL: /500");

		return ModelAndViewBuilder
				.newInstanceFor("/500")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("login")
								.msg("系统内部错误，请稍候再试").build()).build();
	}
	
	@RequestMapping(value = "/401")
	public ModelAndView unauthorized(ServletRequest request, ServletResponse response) throws Exception{
        logger.debug("URL: /401");

		return ModelAndViewBuilder
				.newInstanceFor("/401")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("login")
								.msg("身份未认证，请重新登录").build()).build();
	}
	
	@RequestMapping(value = "/403")
	public ModelAndView forbidden(ServletRequest request, ServletResponse response) throws Exception{
        logger.debug("URL: /403");

		return ModelAndViewBuilder
				.newInstanceFor("/403")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("login")
								.msg("无权限访问").build()).build();
	}
	
	@RequestMapping(value = "/404")
	public ModelAndView notFond(ServletRequest request, ServletResponse response) throws Exception{
//        logger.debug("URL: /404");
		return ModelAndViewBuilder
				.newInstanceFor("/404")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("login")
								.msg("访问资源不存在").build()).build();
	}
	
	@RequestMapping(value = "/405")
	public ModelAndView methodNotAllowed(ServletRequest request, ServletResponse response) throws Exception{
        logger.debug("URL: /405");

		return ModelAndViewBuilder
				.newInstanceFor("/405")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("login")
								.msg("访问的方法不存在 ").build()).build();
	}
	
	/**
	 * 初始化密码用户修改
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUser/{pk}", method=RequestMethod.GET)
	public ModelAndView getPro(@PathVariable Long pk,HttpServletRequest request) throws Exception{
		logger.debug("URL: /getName  method GET  getName {}",pk);
		User user = userService.load(pk);
		
		return ModelAndViewBuilder
				.newInstanceFor("/user/updatePro")
				.append("user",user)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.getPassword")
								.msg("用户密码").build()).build();
	}
	/**
	 * 修改初始密码
	 * @throws Exception 
	 */
	@RequestMapping(value="/updateIslogin",method=RequestMethod.PUT) 
	public ModelAndView updateIslogin(@RequestBody User  user,HttpServletRequest request) throws Exception {
			logger.debug("URL: /user  method PUT  updateIslogin {}",user);	
			String code = user.getPassword();
			Long pk = user.getPk();
			User  thepro = new User();
			user.setPk(pk);
			user.setPassword(code);
			User userpro =	userService.load(pk);
			String status ="失败",errorinfo="",info="";
			try {
				if(userpro.getIsup()!=null&&userpro.getIsup().equals("1")) {
				//修改密码 --coding
					userService.updateIsupPassword(userpro, code,"");
				//修改pro code
					request.getSession().removeAttribute("islogin");
				}
				
				status="成功";
			} catch (Exception e) {
				throw e;
			}finally {
				info="";
				info = "用户"+userpro.getName() +" 修改初始化密码"+status;
			}
			return ModelAndViewBuilder.newInstanceFor("/login")
					.append(ResponseStatus.NAME,
					 new ResponseStatus.Builder(Boolean.TRUE).code("").msg("修改成功").build()).build();
	}
}

