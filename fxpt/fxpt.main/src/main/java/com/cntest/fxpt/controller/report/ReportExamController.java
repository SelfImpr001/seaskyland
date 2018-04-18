/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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
import com.cntest.common.query.Query;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.anlaysis.service.IReportExamService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamTypeService;
import com.cntest.fxpt.service.ISynUsersService;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.fxpt.web.ExamOfSchoolTerm;
import com.cntest.remote.domain.NanShanData;
import com.cntest.remote.service.INanShanDataService;
import com.cntest.security.RoleType;
import com.cntest.security.UserDetails;
import com.cntest.security.remote.UserDetailsService;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/report/exam")
public class ReportExamController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(ReportExamController.class);
	
	@Autowired(required = false)
	@Qualifier("INanShanDataService")
	private INanShanDataService nanShanDataService;
	
	@Autowired(required = false)
	@Qualifier("IReportExamService")
	private IReportExamService reportExamService;
	
//	@Autowired(required = false)
//	private IUserResourceService userResourceService;
	
	@Autowired(required = false)
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private IExamTypeService examTypeService;
	
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	
	@Autowired(required = false)
	private ISynUsersService synUsersService;

	@RequestMapping(value = "/list/{currentPage}/{pageSize}",method=RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage,@PathVariable int pageSize,HttpServletRequest request) throws Exception {
		logger.debug("URL exam/checkin/{}/{} METHOD:GET", currentPage, pageSize);

		boolean isStudent=false;
		UserDetails user = userService.getCurrentLoginedUser();
		UserDetails thisUser = null;
		if(user.roleOf(RoleType.student)) {
			thisUser = user;
		}else {
		    thisUser = userDetailsService.findUserDetailsBy(user.getUserName());
		}
		isStudent=user.roleOf(RoleType.student);
		//南山单点登录
		NanShanData nanShanData = nanShanDataService.findByUid(user.getUserName());
		if(nanShanData!=null){
			isStudent=true;
		}
//		int gradeid = synUsersService.loadByCode(thisUser.getUserName());
//		request.setAttribute("gradeid", gradeid);
		Query query = newQuery(currentPage, pageSize, request);
		
		reportExamService.getSamTermReports(thisUser, query);
		List<ExamType> types = examTypeService.list();
		List<Exam> schoolYears =examService.getExamAllSchoolYears();
		//boolean isStudent = user.roleOf(RoleType.student);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/exam/reportExamList")
				.append("examType", types)
				.append("title", title)
				.append("schoolYears", schoolYears)
				.append("query", query).append("isStudent", isStudent).append("user", user).build();
	}

	@RequestMapping("/recentlyList")
	public ModelAndView recentlyList() throws Exception {
		// TODO 根据用户角色不同请求不同 的考试
		UserDetails user = userService.getCurrentLoginedUser();
		boolean isStudent = false;
		//user.get
		List<Exam> exams = null;
		if(user.roleOf(RoleType.county)) {
			String[] codes = user.getOrgCodes();
			if(codes != null) {
				exams = reportExamService.countyRecentlyList(codes[0]);
			}
		}else if(user.roleOf(RoleType.school)) {
			String[] codes = user.getOrgCodes();
			if(codes != null) {
				exams = reportExamService.schoolRecentlyList(codes[0]);
			}
		}else if(user.roleOf(RoleType.student)) {
			isStudent = true;
			if(user != null) {
				exams = reportExamService.studentRecentlyList(user.getUserName());
			}
			
		}

		LinkedHashMap<String, ExamOfSchoolTerm> shoolTearMap = new LinkedHashMap<String, ExamOfSchoolTerm>();
		for (Exam exam : exams) {
			String key = exam.getSchoolYear() + "" + exam.getSchoolTerm();
			ExamOfSchoolTerm examOfSchoolTerm = shoolTearMap.get(key);
			if (examOfSchoolTerm == null) {
				examOfSchoolTerm = new ExamOfSchoolTerm();
				shoolTearMap.put(key, examOfSchoolTerm);
				examOfSchoolTerm.setSchoolTerm(exam.getSchoolTerm());
				examOfSchoolTerm.setSchoolYear(exam.getSchoolYear());
			}
			examOfSchoolTerm.addExam(exam);
		}

		ArrayList<ExamOfSchoolTerm> examOfSchoolTerms = new ArrayList<ExamOfSchoolTerm>();
		for (ExamOfSchoolTerm tmp : shoolTearMap.values()) {
			examOfSchoolTerms.add(tmp);
		}

		return ModelAndViewBuilder.newInstanceFor("/exam/recentlyList")
				.append("examOfSchoolTerms", examOfSchoolTerms).append("isStudent", isStudent).append("user", user).build();
	}

}
