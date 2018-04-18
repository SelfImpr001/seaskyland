/*
 * @(#)com.cntest.fxpt.controller.AnalysisController.java	1.0 2014年11月26日:下午2:18:32
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.anlaysis.service.impl.ExamMgr;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.service.IAanalysisService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * 数据分析
 * 
 * @author 黄洪成 2014年11月26日 下午2:18:32
 * @version 1.0
 */
@Controller
@RequestMapping("/analysis")
public class AnalysisController extends BaseController {
	
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;

	@Autowired(required = false)
	@Qualifier("ITestPaperService")
	private ITestPaperService testPaperService;
	
	@Autowired(required = false)
	@Qualifier("IAanalysisService")
	private IAanalysisService analysisService;
	
	@Autowired(required = false)
	private UserService userService;

	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage, @PathVariable int pageSize, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Page<Exam> page = newPage(currentPage, pageSize, request);
		boolean isEmpty = page.getParameter().isEmpty();
		page.addParameter("isParamList", "true");
		List<Exam> exams = examService.list(page);
		String title = TitleUtil.getTitle(request.getServletPath());
		if (isEmpty) {
			return ModelAndViewBuilder.newInstanceFor("/analysis/examList").append("page", page).append("title",title).build();
		} else {
			return ModelAndViewBuilder.newInstanceFor("/analysis/examListBody").append("page", page).build();
		}
	}

	@RequestMapping(value = "/{examId}", method = RequestMethod.POST)
	public ModelAndView anlaysis(@PathVariable Long examId, ServletRequest request, HttpServletResponse response) throws Exception {
		ExamMgr emgr = ExamMgr.getInstance();
		String status = "失败",info = "",erre="";
		Exam exam = examService.findById(examId);
		
		try {
			if (emgr.size() > 0) {
				//把状态置为等待分析
				examService.updateStatus(examId, 6);
			} else {
				//删除之前分析的数据
				analysisService.clarenAnalysisResult(exam.getId());
				//把状态置为正在分析，合成各种总分
				analysisService.prepareAnalysis(examId);
				analysisService.startAnalysis(examId);
			}
			status="成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			
			throw e;
		}finally {
			info+= "操作执行成功";
			LogUtil.log("考试管理>数据分析", "分析",exam.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").build();
	}
}
