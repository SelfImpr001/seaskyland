/*
 * @(#)com.cntest.fxpt.controller.StatisticSettingController.java	1.0 2014年10月27日:下午2:40:25
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.fxpt.domain.StatisticSetting;
import com.cntest.fxpt.domain.StudentBase;
import com.cntest.fxpt.service.IStatisticSettingService;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午2:40:25
 * @version 1.0
 */
@Controller
@RequestMapping("statisticSetting")
public class StatisticSettingController {
	@Autowired(required = false)
	@Qualifier("IStatisticSettingService")
	private IStatisticSettingService statisticSettingService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request) {
		List<StatisticSetting> list = statisticSettingService.list();
		int setStatus = 0; 
		if(list!=null && list.size()>0 &&list.get(0)!=null){
			setStatus = list.get(0).getStatus();
		}
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder
				.newInstanceFor("/statisticSetting/list")
				.append("list", list)
				.append("setStatus", setStatus)
				.append("title",title)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
								.msg("list").build()).build();
	}
	
	@RequestMapping(value="/update/{single}/{multi}")
	public String update(@PathVariable Integer single,@PathVariable Integer multi) throws Exception {
		statisticSettingService.update(single,multi);
		return "redirect:/statisticSetting/list/";
	}
}
