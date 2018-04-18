/*
 * @(#)com.cntest.fxpt.controller.PublishController.java	1.0 2014年11月26日:下午2:38:42
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.ISynUsersService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * 数据发布
 * 
 * @author 黄洪成 2014年11月26日 下午2:38:42
 * @version 1.0
 */
@Controller
@RequestMapping("/publish")
public class PublishController extends BaseController {
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	
	@Autowired
	private ISynUsersService synUsersService;

	@Autowired(required = false)
	private UserService userService;
	
	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage,
			@PathVariable int pageSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		@SuppressWarnings("deprecation")
		Page<Exam> page = newPage(currentPage, pageSize, request);
		
		boolean isEmpty = page.getParameter().isEmpty();
		//page.addParameter("isParamList", "true"); //注释于2017/08/02
		if (!page.hasParam("examStatus")) {
			page.addParameter("examStatus", "5,1");//5:发布状态,1分析成功
		}
		examService.list(page);
		String title = TitleUtil.getTitle(request.getServletPath());
		if (isEmpty) {
			return ModelAndViewBuilder.newInstanceFor("/publish/examList")
					.append("page", page).append("title",title).build();
		} else {
			return ModelAndViewBuilder.newInstanceFor("/publish/examListBody")
					.append("page", page).build();
		}
	}
	//发布
	@RequestMapping("/update/{examId}")
	public ModelAndView update(@PathVariable Long examId) throws Exception {
		String status = "失败",info = "",erre="";
		Exam exam = examService.findById(examId);
		try {
			examService.updateStatus(examId, 5);
			synUsersService.createUsers(examId);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<p style='color:red;'>"+exam.getName()+"</p>发布"+status;
			LogUtil.log("考试管理>数据发布", "发布",exam.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").build();
	}
	//取消发布
	@RequestMapping("/resetUpdate/{examId}")
	public ModelAndView resetUpdate(@PathVariable Long examId) throws Exception {
		String status = "失败",info = "",erre="";
		Exam exam = examService.findById(examId);
		try {
			examService.updateStatus(examId, 1);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<p style='color:red;'>"+exam.getName()+"</p>发布取消"+status;
			LogUtil.log("考试管理>数据发布", "取消发布",exam.getName(),status, info,erre);
		}
		
		return ModelAndViewBuilder.newInstanceFor("").build();
	}
	
}
