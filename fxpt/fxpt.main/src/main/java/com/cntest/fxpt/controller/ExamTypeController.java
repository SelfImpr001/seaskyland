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
import com.cntest.fxpt.service.IExamTypeService;
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
@RequestMapping("/examType")
public class ExamTypeController {

	@Autowired(required = false)
	@Qualifier("IExamTypeService")
	private IExamTypeService examTypeService;
	@Autowired
	private UserService userService;
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list(@RequestParam String examTypeName, HttpServletRequest request) throws Exception {
		List<ExamType> examTypes = null;
		if(examTypeName!=null && examTypeName.length()>0){
			examTypes = examTypeService.findExamTypeList(examTypeName);
		}else{
			examTypes = examTypeService.list();
		}
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/examType/list").append("title",title)
				.append("examTypes", examTypes).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("list")
						.msg("list").build()).build();
	}

	@RequestMapping(value="/newAdd",method = RequestMethod.GET)
	public ModelAndView newAdd() throws Exception {
		return ModelAndViewBuilder.newInstanceFor("/examType/newAdd").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody ExamType examType) throws Exception {
		String status = "失败",info = "",erre="";
		try {
			boolean res = examTypeService.add(examType);
			if (!res) {
				throw new BusinessException("","考试类型已存在");
			}else {
				status = "成功";
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+examType.getName()+"</b>添加"+status+","+erre;
			LogUtil.log("系统设置>字典配置>考试类型设置","添加",examType.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping("/newUpdate/{examTypeId}")
	public ModelAndView newUpdate(@PathVariable Long examTypeId)
			throws Exception {
		ExamType examType = examTypeService.get(examTypeId);
		return ModelAndViewBuilder.newInstanceFor("/examType/newUpdate").append("examType", examType).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody ExamType examType) throws Exception {
		String status = "失败",info = "",erre="";
		try {
			ExamType data1 = examTypeService.get(examType.getId());
			info+= updatainfo(examType,data1);
			userService.evictSession(data1);
			boolean res = examTypeService.update(examType);
			if (!res) {
				throw new BusinessException("","考试类型已存在");
			}else {
				status = "成功";
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);	
			throw e;
		}finally {
			LogUtil.log("系统设置>字典配置>考试类型配置","修改",examType.getName(),status, "<p style='color:red;'>"+examType.getName()+"</p>修改"+status+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	public String updatainfo(ExamType resource,ExamType temp) {
		//resource:新来的数据，temp:之前的数据
		String info="";
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+=("考试类型名称：<b style='color:red;'>"+(temp.getName()==null?"":temp.getName())+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>");
		}
		if(resource.isValid()!= temp.isValid()) {
			Map<String ,String> span = new HashMap<String,String>();
			span.put("true","有效");
			span.put("false","无效");
			info+=("是否有效:<b style='color:red;'>"+span.get(temp.isValid()+"") +"</b> 改  <b style='color:red;'>"+span.get(resource.isValid()+"")+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody ExamType examType) throws Exception {
		// TODO 检查是否有考试与他关联
		examType = examTypeService.get(examType.getId());
		String status = "失败",info = "",erre="",name=examType.getName();
		boolean hasExam = examTypeService.hasExam(examType);
		try {
			
			if (!hasExam) {
				examTypeService.delete(examType);
				status = "成功";
			}else{
				throw new BusinessException("","已有考试与他关联，无法删除");
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+name+"</b>删除"+status+","+erre;
			LogUtil.log("系统设置>字典配置>考试类型设置","删除",name,status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append("hasExam", hasExam).append("examType", examType).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
}
