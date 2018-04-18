/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.exception.BusinessException;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.domain.SchoolType;
import com.cntest.fxpt.domain.StudentBase;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.service.ISchoolTypeService;
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
@RequestMapping("/schoolType")
public class schoolTypeController  extends BaseController{

	@Autowired(required = false)
	@Qualifier("ISchoolTypeService")
	private ISchoolTypeService schoolTypeService;
	
	@Autowired
	private UserService userService;


	@RequestMapping(value="/list/{currentPage}/{pageSize}",method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage,
			@PathVariable int pageSize, HttpServletRequest request,
			HttpServletResponse respons) throws Exception {
		
		Page<SchoolType> page = newPage(currentPage, pageSize, request);
		boolean isEmpty = page.getParameter().isEmpty();
		List<SchoolType> schoolTypes = null;
		String schoolTypeName = request.getParameter("schoolTypeName");
		if(schoolTypeName!=null && schoolTypeName.length()>0){
			schoolTypes = schoolTypeService.findSchoolTypeList(schoolTypeName,page);	
		}else{
			schoolTypes = schoolTypeService.list(page);
		}
		
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/schoolType/list").append("title",title)
				.append("page", page).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("list").build()).build();
	}
	
	public List<SchoolType> getAllSchoolType(){
		List<SchoolType> schoolTypeList = new ArrayList<>();
		schoolTypeList = schoolTypeService.getAllSchoolType();
		return schoolTypeList;
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody SchoolType schoolType) throws Exception {
		SchoolType schoolType1 = schoolTypeService.get(schoolType.getId());
		String status = "失败",info = " ",erre="",name = schoolType1.getName();
		try {
			schoolTypeService.delete(schoolType1);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="学校类型<b style='color:red;'>"+name+"</b>删除"+status+"</br>";
			LogUtil.log("系统设置>字典配置>学生类型管理", "删除",name,status,info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append("schoolType", schoolType).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("删除成功").build()).build();
	}
	
	@RequestMapping(value="/newAdd",method = RequestMethod.GET)
	public ModelAndView newAdd() throws Exception {
		return ModelAndViewBuilder.newInstanceFor("/schoolType/newAdd").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody SchoolType schoolType) throws Exception {
		String status = "失败",info = " ",erre="";
		try {
			schoolTypeService.add(schoolType);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="学校类型<b style='color:red;'>"+schoolType.getName()+"</b>添加"+status+"</br>";
			LogUtil.log("系统设置>字典配置>学生类型管理", "添加",schoolType.getName(),status,info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("添加成功").build()).build();
	}
	
	@RequestMapping("/newUpdate/{schoolTypeId}")
	public ModelAndView newUpdate(@PathVariable Long schoolTypeId) throws Exception {
		SchoolType schoolType = schoolTypeService.get(schoolTypeId);
		return ModelAndViewBuilder.newInstanceFor("/schoolType/newUpdate")
				.append("schoolType", schoolType).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody SchoolType schoolType) throws Exception {
		String status = "失败",info = " ",erre="";
		SchoolType type = schoolTypeService.get(schoolType.getId());
		try {
			
			info+= "</br>修改项</br>";
			info+= updatainfo(schoolType,type);
			userService.evictSession(type);
			schoolTypeService.update(schoolType);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("系统设置>字典配置>学生类型管理", "修改",schoolType.getName(),status, "学校类型<b style='color:red;'>"+schoolType.getName()+"</b>修改"+status+"</br>"+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("更新成功").build()).build();
	}
	public String updatainfo(SchoolType resource,SchoolType temp) {
		//resource:新来的数据，temp :之前的数据
		String info="";
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+=("学校类别名称：<b style='color:red;'>"+(temp.getName()==null?"":temp.getName())+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>");
		}
		if(resource.getOrdernum()!=null && (!resource.getOrdernum().equals(temp.getOrdernum()))) {
			info+=("排序:<b style='color:red;'>"+((temp.getOrdernum()==null?"": temp.getOrdernum())+"")+"</b> 改  <b style='color:red;'>"+resource.getOrdernum()+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
}
