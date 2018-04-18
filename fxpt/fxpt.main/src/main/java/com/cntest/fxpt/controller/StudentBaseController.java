/*
 * @(#)com.cntest.fxpt.controller.StudentBaseController.java	1.0 2014年10月9日:下午2:22:53
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.functors.ExceptionClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.Clazz;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.domain.StudentBase;
import com.cntest.fxpt.service.IClazzService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamTypeService;
import com.cntest.fxpt.service.IGradeService;
import com.cntest.fxpt.service.ISchoolService;
import com.cntest.fxpt.service.IStudentBaseService;
import com.cntest.fxpt.service.etl.impl.DataFieldServiceImpl;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月9日 下午2:22:53
 * @version 1.0
 */
@Controller
@RequestMapping("studentBase")
public class StudentBaseController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(DataFieldServiceImpl.class);
	
	@Autowired(required = false)
	@Qualifier("IStudentBaseService")
	private IStudentBaseService studentBaseService;
	
	@Autowired(required = false)
	@Qualifier("ISchoolService")
	private ISchoolService schoolService;
	
	@Autowired(required = false)
	@Qualifier("IClazzService")
	private IClazzService clazzService;
	
	
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	@Autowired(required = false)
	@Qualifier("IExamTypeService")
	private IExamTypeService examTypeService;
	@Autowired(required = false)
	@Qualifier("IGradeService")
	private IGradeService gradeService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/list/{currentPage}/{pageSize}",method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage, @PathVariable int pageSize,HttpServletRequest request){
		log.debug("进入学生库列表");
		Query<StudentBase> query = newQuery(currentPage, pageSize, request);
		query = studentBaseService.list(query);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/studentBase/list").append("title",title).append("query", query).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("list").build()).build();
	}
	
	@RequestMapping("/newUpdate/{studentBaseId}")
	public ModelAndView newUpdate(@PathVariable Integer studentBaseId) throws Exception {
		log.debug("进入修改学生库信息");
		StudentBase studentBase = studentBaseService.get(studentBaseId);
		List<School> listSchool = schoolService.list();
		List<Clazz> listClazz = clazzService.list();
		return ModelAndViewBuilder.newInstanceFor("/studentBase/newUpdate")
				.append("studentBase", studentBase).append("listSchool", listSchool).append("listClazz", listClazz).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("").build()).build();
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody StudentBase studentBase) throws Exception {
		log.debug("修改学生库信息");
		String status = "失败",info = " ",erre="";
		StudentBase student = studentBaseService.get(studentBase.getId());
		try {
			
			info+= "</br>修改项</br>";
			info+= updatainfo(studentBase,student);
			userService.evictSession(student);
			studentBaseService.update(studentBase);
			status = "成功";
			
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("系统设置>字典配置>学生库设置", "修改",studentBase.getName(),status, "学生<b style='color:red;'>"+studentBase.getName()+"</b>修改"+status+"</br>"+info,erre);
		}
		
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("").build()).build();
	}
	public String updatainfo(StudentBase resource,StudentBase temp) {
		//resource:新来的数据，temp:之前的数据
		String info="";
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+=("姓名：<b style='color:red;'>"+(temp.getName()==null?"":temp.getName())+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>");
		}
		
		if(resource.getSex() != temp.getSex()) {
			HashMap<Integer ,String> span = new HashMap<Integer,String>();
			span.put(null,"无");
			span.put(0,"无");
			span.put(1,"男");
			span.put(2,"女");
			info+=("性别:<b style='color:red;'>"+span.get(temp.getSex())+"</b> 改  <b style='color:red;'>"+span.get(resource.getSex())+"</b><br/>");
		}
		if(resource.getSchool().getCode()!=null && (!resource.getSchool().getCode().equals(temp.getSchool().getCode()))) {
			School school = schoolService.findCodeForSchool(resource.getSchool().getCode());
			info+=("所属学校:<b style='color:red;'>"+((temp.getSchool().getName()==null?"": temp.getSchool().getName())+"")+"</b> 改  <b style='color:red;'>"+school.getName()+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping(value = "/examList/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView examList(@PathVariable int currentPage,
			@PathVariable int pageSize, ServletRequest request)
			throws Exception {
		log.debug("进入学生库设置考试导入列表");
		Page<Exam> page = newPage(currentPage, pageSize, request);
		List<Exam> exams = examService.list(page);
			List<Grade> grades = gradeService.list();
			List<ExamType> examTypes = examTypeService.list();
			return ModelAndViewBuilder.newInstanceFor("/studentBase/examList").append("page", page).append("examTypes", examTypes).append("grades", grades).append(ResponseStatus.NAME,new ResponseStatus.Builder(Boolean.TRUE).code("status").msg("").build()).build();

	}
	@RequestMapping(value = "/importExamStudents/{currentPage}/{pageSize}",method = RequestMethod.POST)
	public String importExamStudents(@RequestBody Integer[] examids,@PathVariable int currentPage,@PathVariable int pageSize,ServletRequest request){
		log.debug("学生库考试导入");
		String op ="<div style='width:100%; max-height:60px; overflow-y:auto; overflow-x:auto;'>";
		String status = "失败",info = "",erre="";
		try {
			for (int i = 0; i < examids.length; i++) {
				Exam exam = examService.findById(Long.valueOf(examids[i]));
				op+=(exam.getName()+",");
			}
			op+="</div>";
			studentBaseService.importExamStudents(examids);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info = "<b style='color:red;'>"+op+"</b>导入库"+status+"</br>";
			LogUtil.log("系统设置>字典配置>学生库设置", "考试学生导入",op,status, info,erre);
		}
		return "redirect:/studentBase/examList/"+currentPage+"/"+pageSize;
	}
}
