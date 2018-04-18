/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.cntest.fxpt.bean.WebLevelScore;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.LeveScoreSetting;
import com.cntest.fxpt.service.IAnalysisTestPaperService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.ILeveScoreSettingService;
import com.cntest.fxpt.service.IStandardScoreSettingService;
import com.cntest.fxpt.service.IStatisticSettingService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/report/param")
public class ReportParamController extends BaseController {
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperService")
	private IAnalysisTestPaperService atpService;

	@Autowired(required = false)
	@Qualifier("LeveScoreSettingService")
	private ILeveScoreSettingService lssService;
	
	@Autowired(required = false)
	@Qualifier("StandardScoreSettingService")
	private IStandardScoreSettingService standardScoreSettingService;
	
	@Autowired(required = false)
	private UserService userService;
	
	@RequestMapping(value = "/exams/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage,
			@PathVariable int pageSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Page<Exam> page = newPage(currentPage, pageSize, request);
		boolean isEmpty = page.getParameter().isEmpty();
		
		page.addParameter("examStatus", "5");

		List<Exam> exams = examService.list(page);
		String title = TitleUtil.getTitle(request.getServletPath());
		if (isEmpty) {
			return ModelAndViewBuilder.newInstanceFor("/report/param/examList")
					.append("page", page).append("title",title).build();
		} else {
			return ModelAndViewBuilder
					.newInstanceFor("/report/param/examListBody")
					.append("page", page).build();
		}
	}

	@RequestMapping(value = "/levelScore/view/{examId}", method = RequestMethod.GET)
	public ModelAndView levelScoreView(@PathVariable Long examId,
			ServletRequest request, HttpServletResponse response)
			throws Exception {
		Exam exam = examService.findById(examId);
		Map<String, List<WebLevelScore>> lssMap = lssService.find(exam);
		return ModelAndViewBuilder
				.newInstanceFor("/report/param/levelScoreView")
				.append("lssMap", lssMap).append("exam", exam).build();
	}

	@RequestMapping(value = "/levelScore/update/view/{examId}/{wl}/{atpId}", method = RequestMethod.GET)
	public ModelAndView updateView(@PathVariable Long examId,
			@PathVariable int wl, @PathVariable Long atpId,
			ServletRequest request, HttpServletResponse response)
			throws Exception {
		Exam exam = examService.findById(examId);
		List<AnalysisTestpaper> tmpAtpList = atpService.listAllWith(examId);
		ArrayList<AnalysisTestpaper> atpList = new ArrayList<>();
		AnalysisTestpaper curAtp = null;
		for (AnalysisTestpaper atp : tmpAtpList) {
			if (atp.getPaperType() == 0 || atp.getPaperType() == wl) {
				atpList.add(atp);
			}
			if (atp.getId().equals(atpId)) {
				curAtp = atp;
			}
		}

		return ModelAndViewBuilder
				.newInstanceFor("/report/param/levelScoreUpdate")
				.append("exam", exam).append("wl", wl)
				.append("atpList", atpList).append("curAtp", curAtp).build();
	}

	@RequestMapping(value = "/levelScore/atpList/{examId}/{wl}", method = RequestMethod.GET)
	public ModelAndView updateView(@PathVariable Long examId,
			@PathVariable int wl, ServletRequest request,
			HttpServletResponse response) throws Exception {
		Exam exam = examService.findById(examId);
		List<AnalysisTestpaper> tmpAtpList = atpService.listAllWith(examId);
		ArrayList<AnalysisTestpaper> atpList = new ArrayList<>();
		for (AnalysisTestpaper atp : tmpAtpList) {
			if (atp.getPaperType() == 0 || atp.getPaperType() == wl) {
				atp.setCombinationSubject(null);
				atpList.add(atp);
			}
		}
		return ModelAndViewBuilder.newInstanceFor("")
				.append("atpList", atpList).build();
	}

	@RequestMapping(value = "/levelScore/data/{wl}/{atpId}", method = RequestMethod.GET)
	public ModelAndView levelScoreData(@PathVariable int wl,
			@PathVariable Long atpId, ServletRequest request,
			HttpServletResponse response) {
		List<LeveScoreSetting> lsses = lssService.find(atpId, wl);
		return ModelAndViewBuilder.newInstanceFor("").append("lsses", lsses)
				.build();
	}
	@RequestMapping(value = "/levelScore/save/{wl}/{atpId}", method = RequestMethod.POST)
	public ModelAndView saveLevelScore(@PathVariable int wl,
			@PathVariable Long atpId,
			@RequestBody List<LeveScoreSetting> lsses, ServletRequest request,
			HttpServletResponse response) {
		
		List<LeveScoreSetting> lssesList  = null;
		String status = "失败",info = "",erre="",name="";
		try {
			AnalysisTestpaper atp = atpService.get(atpId);
			name = atp.getName();
			lssService.adds(atp, wl, lsses);
			lssesList = lssService.find(atpId, wl);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info = "保存<b style='color:red;'>"+name+"</b>参数"+status;
			LogUtil.log("报告管理>参数设置", "等级设置及赋分",name,status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("")
				.append("lsses", lssesList).build();
	}
	
	/****************************StandardScore********************************/
	
	@RequestMapping(value = "/standardScore/view/{examId}", method = RequestMethod.GET)
	public ModelAndView standardScoreView(@PathVariable Long examId,
			ServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, List<Object[]>> resMap = standardScoreSettingService.findList(examId);
		return ModelAndViewBuilder
				.newInstanceFor("/report/param/standardScoreView")
				.append("resMap", resMap)
				.build();
	}
	
	@RequestMapping(value = "/standardScore/update/view/{examId}/{wl}", method = RequestMethod.GET)
	public ModelAndView standardScoreUpdate(@PathVariable Long examId,
			@PathVariable int wl,
			ServletRequest request, HttpServletResponse response)
			throws Exception {
		Exam exam = examService.findById(examId);
		Map<String, List<Object[]>> resMap = standardScoreSettingService.findList(examId);
		List<Object[]> reslist =resMap.get(String.valueOf(wl));
		String zValue = standardScoreSettingService.getZvalue(examId,wl);
		String defaultvalue = null;
		String zvalue =null;
		if(zValue!=null){
		 defaultvalue = zValue.split(":")[0];
		 zvalue = zValue.split(":")[1];
		}
		return ModelAndViewBuilder
				.newInstanceFor("/report/param/standardScoreUpdate")
				.append("exam", exam).append("wl", wl)
				.append("reslist", reslist)
				.append("defaultvalue", defaultvalue)
				.append("zvalue", zvalue)
				.build();
	}
	
	@RequestMapping(value = "/standardScore/save/view/{res}", method = RequestMethod.POST)
	public ModelAndView standardScoreUpdate(@PathVariable String res,
			ServletRequest request, HttpServletResponse response)
			throws Exception {
		String status = "失败",info = "",erre="",name="";
		try {
			String[] str = res.split(",");
			standardScoreSettingService.save(str);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info = "操作<b style='color:red;'>"+name+"</b>"+status;
			LogUtil.log("报告管理>参数设置", "导出分数设置",name,status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").build();
	}
	


	

}
