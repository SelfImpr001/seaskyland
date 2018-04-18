/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.exception.BusinessException;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.service.IGradeService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/grade")
public class GradeController {
	@Autowired(required = false)
	@Qualifier("IGradeService")
	private IGradeService gradeService;

	@Autowired
	private UserService userService;
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list(@RequestParam String gradeName, HttpServletRequest request) throws Exception {
		List<Grade> grades = null;
		if(gradeName!=null && gradeName.length()>0){
			grades = gradeService.findGradeList(gradeName);	
		}else{
			grades = gradeService.list();
		}
		
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/grade/list").append("title",title)
				.append("grades", grades).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("list")
						.msg("list").build()).build();
	}

	@RequestMapping(value="/newAdd",method = RequestMethod.GET)
	public ModelAndView newAdd() throws Exception {
		return ModelAndViewBuilder.newInstanceFor("/grade/newAdd").append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("")
						.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody Grade grade) throws Exception {
		String status = "失败",info = "",erre="";
		try {
			boolean res = gradeService.add(grade);
			if (!res) {
				throw new BusinessException("","年级已存在");
			}else {
				status = "成功";
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+grade.getName()+"</b>添加"+status+","+erre;
			LogUtil.log("系统设置>字典配置>年级设置","添加",grade.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping("/newUpdate/{gradeId}")
	public ModelAndView newUpdate(@PathVariable Long gradeId) throws Exception {
		Grade grade = gradeService.get(gradeId);
		return ModelAndViewBuilder.newInstanceFor("/grade/newUpdate").append("grade", grade).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody Grade grade) throws Exception {
		
		 
		String status = "失败",info = "",erre="";
		try {
			Grade data1 = gradeService.get(grade.getId());
			info+= updatainfo(grade,data1);
			userService.evictSession(data1);
			
			boolean res = gradeService.update(grade);
			if (!res) {
				throw new BusinessException("","年级已存在");
			}else {
				status = "成功";
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("系统设置>字典配置>年级配置","修改",grade.getName(),status, "<p style='color:red;'>"+grade.getName()+"</p>修改"+status+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	public String updatainfo(Grade resource,Grade temp) {
		//resource:新来的数据，temp:之前的数据
		String info="";
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+=("考试类型名称：<b style='color:red;'>"+(temp.getName()==null?"":temp.getName())+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody Grade grade) throws Exception {
		// TODO 检查是否有考试与他关联
		grade = gradeService.get(grade.getId());
		String status = "失败",info = "",erre="",name=grade.getName();
		boolean hasExam = gradeService.hasExam(grade);
		try {
			if (!hasExam) {
				gradeService.delete(grade);
				status = "成功";
			}else{
				throw new BusinessException("","已有考试与他关联，无法删除");
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+name+"</b>删除"+status+","+erre;
			LogUtil.log("系统设置>字典配置>年级设置","删除",name,status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append("hasExam", hasExam).append("grade", grade).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
}
