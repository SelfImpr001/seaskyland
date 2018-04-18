/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamTypeService;
import com.cntest.fxpt.service.IGradeService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.security.UserDetails;
import com.cntest.security.UserOrg;
import com.cntest.security.remote.UserOrgService;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController {
	private static Logger logger = LoggerFactory
			.getLogger(ExamController.class);

	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	@Autowired(required = false)
	@Qualifier("IExamTypeService")
	private IExamTypeService examTypeService;
	@Autowired(required = false)
	@Qualifier("IGradeService")
	private IGradeService gradeService;

	@Autowired(required = false)
	private UserOrgService userOrgService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage,
			@PathVariable int pageSize, HttpServletRequest request)
			throws Exception {

		Page<Exam> page = newPage(currentPage, pageSize, request);
		List<Exam> exams = examService.list(page);
		
		String title = TitleUtil.getTitle(request.getServletPath());
		if (page.getParameter().isEmpty()) {
			List<Grade> grades = gradeService.list();
			List<ExamType> examTypes = examTypeService.list();

			return ModelAndViewBuilder.newInstanceFor("/exam/list")
					.append("page", page).append("examTypes", examTypes)
					.append("title",title)
					.append("grades", grades).build();
		} else {

			return ModelAndViewBuilder.newInstanceFor("/exam/listBody")
					.append("page", page).build();
		}

	}

	@RequestMapping("/newExams")
	public ModelAndView newExams() throws Exception {
		List<Grade> grades = gradeService.list();
		List<ExamType> examTypes = examTypeService.list(true);
		int year = getYear();
		int month = getMonth() + 1;
		Date date = new Date();

		UserDetails user = userService.getCurrentLoginedUser();
		List<UserOrg> userOrgs = userOrgService.getUserOrgs(user);
		UserOrg userOrg = null;
		for (UserOrg tmp : userOrgs) {
			if (userOrg == null) {
				userOrg = tmp;
			} else if (tmp.getType() < userOrg.getType()) {
				userOrg = tmp;
			}
		}
		
		return ModelAndViewBuilder.newInstanceFor("/exam/newExams")
				.append("grades", grades).append("examTypes", examTypes)
				.append("year", year).append("month", month)
				.append("date", date).append("user", user)
				.append("userOrg", userOrg).build();
	}

	@RequestMapping(value = "/createExams", method = RequestMethod.POST)
	public ModelAndView createExams(@RequestBody Map<String, Object> param)
			throws Exception {
		String examDate = param.get("examDate").toString();
		String examTypeId = param.get("examTypeId").toString();
		String examTypeName = param.get("examTypeName").toString();
		String schoolYear = param.get("schoolYear").toString();
		String schoolTerm = param.get("schoolTerm").toString();
		String levelName = param.get("levelName").toString();
		String levelCode = param.get("levelCode").toString();
		String ownerCode = param.get("ownerCode").toString();
		String ownerName = param.get("ownerName").toString();
		String createUserName = param.get("createUserName").toString();
		Map<String, String> gradeMap = (Map<String, String>) param
				.get("grades");

		ExamType examType = new ExamType();
		examType.setId(Long.parseLong(examTypeId));
		examType.setName(examTypeName);

		ArrayList<Grade> grades = new ArrayList<Grade>();
		for (String gradeId : gradeMap.keySet()) {
			String gradeName = gradeMap.get(gradeId);
			Grade grade = new Grade();
			grade.setId(Long.parseLong(gradeId));
			grade.setName(gradeName);
			grades.add(grade);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		examService.createExams(sdf.parse(examDate),
				Integer.parseInt(schoolYear), Integer.parseInt(schoolTerm),
				Integer.parseInt(levelCode), levelName, ownerCode, ownerName,
				createUserName, examType, grades);

		return ModelAndViewBuilder.newInstanceFor("").build();
	}

	@RequestMapping("/newAdd")
	public ModelAndView newAdd() throws Exception {
		List<Grade> grades = gradeService.list();
		List<ExamType> examTypes = examTypeService.list(true);
		int year = getYear();
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("year", year);
		model.put("grades", grades);
		model.put("examTypes", examTypes);
		return new ModelAndView("/exam/newAdd", model);
	}

	@RequestMapping("/add")
	public ModelAndView add(@RequestBody Exam exam) throws Exception {
		examService.add(exam);
		return new ModelAndView("", null);
	}

	@RequestMapping("/newUpdate/{examId}")
	public ModelAndView newUpdate(@PathVariable Long examId) throws Exception {
		List<Grade> grades = gradeService.list();
		List<ExamType> examTypes = examTypeService.list(true);
		int year = getYear();
		Exam exam = examService.findById(examId);
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("year", year);
		model.put("grades", grades);
		model.put("examTypes", examTypes);
		model.put("exam", exam);
		return new ModelAndView("/exam/newUpdate", model);
	}

	@RequestMapping("/update")
	public ModelAndView update(@RequestBody Exam exam) throws Exception {
		String status = "失败",info = "",erre="";
		Exam exams = examService.findById(exam.getId());
		try {
			info = updatainfo(exam,exams);
			userService.evictSession(exams);
			examService.update(exam);;
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("考试管理>考试列表", "修改",exam.getName(),status, "考试<b style='color:red;'>"+exam.getName()+"</b>修改"+status+"</br>修改项</br>"+info,erre);
		}
		
		
		return new ModelAndView("", null);
	}
	//查找修改项
	public String updatainfo(Exam resource,Exam temp) {
		//resource:新来的数据，temp:之前的数据
		
		String info="";
		
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+=("考试名字：<b style='color:red;'>"+(temp.getName()==null?"":temp.getName())+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>");
		}
		if(resource.getSortName()!=null && (!resource.getSortName().equals(temp.getSortName()))) {
			info+=("考试简称：<b style='color:red;'>"+(temp.getSortName()==null?"":temp.getSortName())+"</b> 改  <b style='color:red;'>"+resource.getSortName()+"</b><br/>");
		}
		if(resource.getSchoolYear()!=0 && (resource.getSchoolYear()!=temp.getSchoolYear())) {
			info+=("学年：<b style='color:red;'>"+(temp.getSchoolYearName()==null?"":temp.getSchoolYearName())+"</b> 改  <b style='color:red;'>"+((resource.getSchoolYear()-1)+"-"+resource.getSchoolYear()+"学年")+"</b><br/>");
		}
		
		if(resource.getGrade().getId()!=null && (!resource.getGrade().getId().equals(temp.getGrade().getId()))) {
			int gradeId = resource.getGrade().getId().intValue();
			String gradeLevel = "";
			int gradeNum = 0;
			String[] nums = {"零","一","二","三","四","五","六","一","二","三","一","二","三"};
			//生成界别，界别格式 ： 年级+学年   例如：小学2017届---12017，1代表小学   2代表初中  3代表高中
			String jiebie = "";
			if (gradeId <= 6) {
				gradeLevel = "年级";
				gradeNum = gradeId;
				jiebie= nums[gradeNum]+gradeLevel;
				
			} else if (gradeId > 6 && gradeId <= 9) {
				gradeLevel = "初";
				gradeNum = gradeId - 6;
				jiebie= gradeLevel+nums[gradeNum];
			} else if (gradeId > 9 && gradeId <= 12) {
				gradeLevel = "高";
				gradeNum = gradeId - 9;
				jiebie= gradeLevel+nums[gradeNum];
			}
			info+=("年级：<b style='color:red;'>"+(temp.getGrade().getName()==null?"":temp.getGrade().getName())+"</b> 改  <b style='color:red;'>"+jiebie +"</b><br/>");
		}
		
		if(LogUtil.datefor(resource.getExamDate())!=null && (!LogUtil.datefor(resource.getExamDate()).equals(LogUtil.datefor(temp.getExamDate())))) {
			info+=("考试日期：<b style='color:red;'>"+LogUtil.datefor(temp.getExamDate())+"</b> 改  <b style='color:red;'>"+LogUtil.datefor(resource.getExamDate())+"</b><br/>");
		}
		
		if(resource.getSchoolTerm()!=0 && (resource.getSchoolTerm()!= temp.getSchoolTerm())) {
			Map<String ,String> span = new HashMap<String,String>();
			span.put("1","上学期");
			span.put("2","下学期");
			info+=("学期:<b style='color:red;'>"+(span.get(temp.getSchoolTerm()+"")) +"</b> 改  <b style='color:red;'>"+(span.get(resource.getSchoolTerm()+""))+"</b><br/>");
		}
		if(resource.getExamType().getId()!=null && (!resource.getExamType().getId().equals(temp.getExamType().getId()))) {
			Map<Long ,String> span = new HashMap<Long,String>();
			List<ExamType> examTypes = examTypeService.list(true);
			for (ExamType examT : examTypes) {
				span.put(examT.getId(),examT.getName());
			}
			info+=("考试类型：<b style='color:red;'>"+(temp.getExamType().getName()==null?"":temp.getExamType().getName())+"</b> 改  <b style='color:red;'>"+(span.get(resource.getExamType().getId()))+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	

	@RequestMapping("/delete/{examId}")
	public ModelAndView delete(@PathVariable Long examId) throws Exception {
		// 检查是否还有报名库，试卷，成绩与他关联
		Exam exam = examService.findById(examId);
		boolean hasTestPaper = exam.getTestPaperSize() == 0 ? false : true;
		boolean hasExamStudent = exam.isHasExamStudent();
		String status = "失败",info = "删除考试<b style='color:red;'>",erre="";
		String examName = exam.getName();
		
		try{
			if (!hasTestPaper && !hasExamStudent) {
				examService.delete(exam);
				status = "成功";
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= examName+"</b>"+status;
			if( hasTestPaper||hasExamStudent ) {
				info+="，报名库，试卷，成绩与他关联";
			}
			LogUtil.log("考试管理>考试列表", "删除",examName,status, info,erre);
		}
		
		return ModelAndViewBuilder.newInstanceFor("").append("exam", exam)
				.append("hasTestPaper", hasTestPaper)
				.append("hasExamStudent", hasExamStudent).build();
	}

	// @RequestMapping("/view/{examId}")
	// public ModelAndView view(@PathVariable Long examId) throws Exception {
	// Exam exam = examService.findById(examId);
	// return new ModelAndView("/exam/view", "exam", exam);
	// }
	//
	// @RequestMapping("/info/{examId}")
	// public ModelAndView info(@PathVariable Long examId) throws Exception {
	// Exam exam = examService.findById(examId);
	// return new ModelAndView("/exam/info", "exam", exam);
	// }

	private int getYear() {
		Calendar c = Calendar.getInstance();
		int result = c.get(Calendar.YEAR);
		return result;
	}

	private int getMonth() {
		Calendar c = Calendar.getInstance();
		int result = c.get(Calendar.MONTH);
		return result;
	}
}
