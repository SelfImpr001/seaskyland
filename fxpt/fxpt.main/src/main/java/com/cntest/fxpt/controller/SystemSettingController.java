/*
 * @(#)com.cntest.fxpt.controller.EtlConfigControoler.java	1.0 2014年5月14日:下午4:22:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.service.UserService;
import com.cntest.security.UserDetails;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * 系统设置->etl参数设置
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 下午4:22:10
 * @version 1.0
 */
@Controller
@RequestMapping("/systemsetting")
public class SystemSettingController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(SystemSettingController.class);
	@Autowired(required = false)
	private UserService userService;
	@Autowired(required = false)
	@Qualifier("resourceService")
	private IUserResourceService userResourceService;

	@RequestMapping(value="/home/{uuid}",method=RequestMethod.GET)
	public ModelAndView home(@PathVariable String uuid) throws Exception {
		logger.debug("URL /systemsetting/hmome");
		UserDetails user = userService.getCurrentLoginedUser();
		return ModelAndViewBuilder.newInstanceFor("/systemsetting/home").append("user", user).append("uuid", uuid).build();
		
	}

	@RequestMapping(value="/menu/{uuid}",method=RequestMethod.GET)
	public ModelAndView menu(@PathVariable String uuid) throws Exception {
		logger.debug("URL /systemsetting/menu/{}",uuid);
		UserDetails user = userService.getCurrentLoginedUser();
		List<UserResource> menus = userResourceService.getResourcesFor(user, uuid, IUserResourceService.Type.MENU,IUserResourceService.Level.ALL);
		logger.debug("Get menus {} for {} ", menus==null?0:menus.size(),user);
		return ModelAndViewBuilder.newInstanceFor("/systemsetting/menu").append("menus", menus).build();
	}

}
