/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.fxpt.bean.EtlLog;
import com.cntest.fxpt.service.IEtlLogService;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/logs")
public class EtlLogController extends BaseController {

	@Autowired(required = false)
	@Qualifier("IEtlLogService")
	private IEtlLogService etlLogService;

	@RequestMapping(value = "/list/{examId}/{logType}/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView examList(@PathVariable Long examId,
			@PathVariable int logType, @PathVariable int currentPage,
			@PathVariable int pageSize, ServletRequest request)
			throws Exception {

		Page<EtlLog> page = newPage(currentPage, pageSize, request);
		etlLogService.list(examId, logType, page);
		return ModelAndViewBuilder.newInstanceFor("/etlLog/listTBody")
				.append("logs", page.getList()).build();
	}

}
