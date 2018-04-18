/*
 * @(#)com.cntest.fxpt.controller.FtpSettingController.java	1.0 2016年3月21日:上午11:38:30
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.exception.BusinessException;
import com.cntest.foura.controller.HttpDataSourceController;
import com.cntest.fxpt.domain.FtpSetting;
import com.cntest.fxpt.service.IFtpSettingService;
import com.cntest.fxpt.util.FileFactory;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2016年3月21日 上午11:38:30
 * @version 1.0
 */
@Controller
@RequestMapping("ftpSetting")
public class FtpSettingController {
	
	@Autowired(required = false)
	@Qualifier("IFtpSettingService")
	private IFtpSettingService ftpSettingService;
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		List<FtpSetting> list  = ftpSettingService.ftpList();
		return ModelAndViewBuilder
				.newInstanceFor("/ftp/list")
				.append("list", list)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
								.msg("list").build()).build();
	}
	
		
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String update(@RequestBody FtpSetting ftpSetting) throws Exception {
		ftpSettingService.updateFtp(ftpSetting);
		return "redirect:/ftpSetting/list/";
	}
	
	@RequestMapping(value = "/testFtpSuccess", method = RequestMethod.POST)
	public ModelAndView testFtpSuccess(@RequestBody FtpSetting ftpSetting) {
		boolean b = false;
		try {
			if (ftpSetting.getStatus() > 0) {
				b = FileFactory.testFtpSuccess(ftpSetting.getUrl(),
						ftpSetting.getPort(), ftpSetting.getUsername(),
						ftpSetting.getPassword(), ftpSetting.getPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ModelAndViewBuilder.newInstanceFor("").append("isSuccess", b).build();
	}
	
}
