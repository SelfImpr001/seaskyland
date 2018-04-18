/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.security.UserDetails;
import com.cntest.security.remote.RemoteSecuryInterface;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年7月5日
 * @version 1.0
 **/


public class UserController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private RemoteSecuryInterface remoteService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/setpwd",method=RequestMethod.GET)
	public ModelAndView getPwdModal() throws Exception{
        logger.debug("URL: /user/setpwd method PUT");

		return ModelAndViewBuilder
				.newInstanceFor("/pwdmodal")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("setpwd")
								.msg("修改密码").build()).build();
	}
	
	@RequestMapping(value = "/setpwd/{n}/{o}",method=RequestMethod.PUT)
	public ModelAndView setPwd(@PathVariable String n,@PathVariable String o) throws Exception{
        logger.debug("URL: /user/setpwd method PUT");
        UserDetails user =   userService.getCurrentLoginedUser();
        Boolean b = (Boolean)remoteService.updatePassword(user.getUserName(), o, n);
        if(!b)
        	throw new BusinessException("cntest-4a-0005","原密码错误");
		return ModelAndViewBuilder
				.newInstanceFor("/setpwd")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(b).code("login")
								.msg("修改密码").build()).build();
	}
}

