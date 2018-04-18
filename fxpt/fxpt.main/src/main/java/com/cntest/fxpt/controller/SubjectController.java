/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.service.ISubjectService;
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
@RequestMapping("/subject")
public class SubjectController {

	@Autowired(required = false)
	@Qualifier("ISubjectService")
	private ISubjectService subjectService;
	
	@Autowired
	private UserService userService;


	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list(@RequestParam String subjectName, HttpServletRequest request) throws Exception {
		List<Subject> subjects = null;
		if(subjectName!=null && subjectName.length()>0){
			subjects = subjectService.findSubjectList(subjectName);	
		}else{
			subjects = subjectService.list();
		}
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/subject/list").append("title", title)
				.append("subjects", subjects).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("list").build()).build();
	}

	@RequestMapping(value="/newAdd",method = RequestMethod.GET)
	public ModelAndView newAdd() throws Exception {
		return ModelAndViewBuilder.newInstanceFor("/subject/newAdd").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody Subject subject) throws Exception {
		String status = "失败",info = "",erre="";
		try {
			boolean res = subjectService.add(subject);
			if (res) {
				throw new BusinessException("","科目已存在");
			}else {
				status = "成功";
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+subject.getName()+"</b>添加"+status+","+erre;
			LogUtil.log("系统设置>字典配置>科目设置","添加",subject.getName(),status, info,erre);
		}
		
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("添加成功").build()).build();
	}

	@RequestMapping("/newUpdate/{subjectId}")
	public ModelAndView newUpdate(@PathVariable Long subjectId) throws Exception {
		Subject subject = subjectService.get(subjectId);
		return ModelAndViewBuilder.newInstanceFor("/subject/newUpdate")
				.append("subject", subject).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody Subject subject) throws Exception {
		String status = "失败",info = "",erre="";
		try {
			Subject data1 = subjectService.get(subject.getId());
			info+= updatainfo(subject,data1);
			userService.evictSession(data1);
			boolean res = subjectService.update(subject);
			if (res) {
				throw new BusinessException("","科目已存在");
			}else {
				status = "成功";
			}
			 
		} catch (Exception e) {
			erre = LogUtil.e(e);	
			throw e;
		}finally {
			LogUtil.log("系统设置>字典配置>科目配置","修改",subject.getName(),status, "<p style='color:red;'>"+subject.getName()+"</p>修改"+status+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("更新成功").build()).build();
	}
	public String updatainfo(Subject resource,Subject temp) {
		//resource:新来的数据，temp:之前的数据
		String info="";
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+=("科目名称：<b style='color:red;'>"+(temp.getName()==null?"":temp.getName())+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>");
		}
		if(resource.getOrdernum()!=null && (!resource.getOrdernum().equals(temp.getOrdernum()))) {
			info+=("序号：<b style='color:red;'>"+(temp.getOrdernum()==0?"":temp.getOrdernum())+"</b> 改  <b style='color:red;'>"+resource.getOrdernum()+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody Subject subject) throws Exception {
		Subject newsubject = subjectService.get(subject.getId());
		String status = "失败",info = "",erre="",name=newsubject.getName();
		try {
			boolean res = subjectService.delete(newsubject);
			if (!res) {
				throw new BusinessException("","已有绑定科目，无法删除");
			}else {
				status = "成功";
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+name+"</b>删除"+status+","+erre;
			LogUtil.log("系统设置>字典配置>科目设置	","删除",name,status, info,erre);
		}
		
		return ModelAndViewBuilder.newInstanceFor("").append("subject", subject).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("删除成功").build()).build();
	}
	
	@RequestMapping("/newInit")
	public ModelAndView newInit() throws Exception {
		return ModelAndViewBuilder.newInstanceFor("/subject/newInit").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("").build()).build();
	}
	
	@RequestMapping(value="/initSubject",method = RequestMethod.POST)
	public ModelAndView initSubject(@RequestBody String[] arr){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < arr.length; i++) {
			if(arr[i].equals("1")){
				list.add("小学科目1");
				list.add("小学科目2");
				list.add("小学科目3");
			}if(arr[i].equals("2")){
				list.add("初中科目1");
				list.add("初中科目2");
				list.add("初中科目3");
			}if(arr[i].equals("3")){
				list.add("高中科目1");
				list.add("高中科目2");
				list.add("高中科目3");
			}	
		}
		for (int i = 0; i < list.size() && list!=null; i++) {
			Subject subject = new Subject();
			subject.setName(list.get(i));
			subjectService.add(subject);
		}
		return ModelAndViewBuilder.newInstanceFor("/subject/list").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("initSubject").build()).build();
	}

}
