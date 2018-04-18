package com.cntest.foura.shiro;



import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.cntest.foura.application.permission.PasswordService;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.util.SpringContext;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月18日
 * @version 1.0
 **/
public class IsloginAuthenticationFilter extends FormAuthenticationFilter {
	private UserService userService;
	private String captchaParam = "kaptcha";//前台提交的验证码参数名
	private PasswordService passwordService;
	private boolean captchaEbabled = true;//是否开启验证码支持

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response,Object mappedValue) throws Exception {
		
		
		HttpServletRequest re = (HttpServletRequest) request;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(userService == null) {
			userService = SpringContext.getBean(UserService.class);
		}
		
		if(passwordService == null) {
			passwordService = SpringContext.getBean(PasswordService.class);
		}
		if(username!=null&&password!=null){
			String thepassword = passwordService.encryptPassword(password, username);
			User user = 	userService.findUserBy(username);
			//	boolean b =  super.onAccessDenied(request, response, mappedValue);
			if(user!=null) {
				
				if(user.getIsup()==null||user.getIsup().equals("")) {
					return true;
				}
				if(thepassword.equals(user.getPassword())&&("1").equals(user.getIsup())) {
					re.removeAttribute("username");
					re.removeAttribute("password");
					request.setAttribute("islogin", user.getPk());
					re.getSession().setAttribute("islogin", user.getPk());
//		request.getRequestDispatcher("/login").forward(re, response);
					saveRequestAndRedirectToLogin(re, response);
//		  saveRequest(request);
//	      redirectToLogin(request, response);
//	      Map map = new HashMap();
//	      map.put("dilogUser", pro.getId());
//	      WebUtils.issueRedirect(request, response, getLoginUrl(), map);
					return false;
				}
		}
		}
		return true;
	}
	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	public void setCaptchaEbabled(boolean captchaEbabled) {
		this.captchaEbabled = captchaEbabled;
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	public boolean isCaptchaEbabled() {
		return captchaEbabled;
	}
	 protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
	        String username = getUsername(request);
	        String password = getPassword(request);
	        return createToken(username, password, request, response);
	 }

	
	

	
}
