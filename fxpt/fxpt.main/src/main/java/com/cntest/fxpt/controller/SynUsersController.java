/*
 * @(#)com.cntest.fxpt.controller.CreateTeachers.java	1.0 2016年4月7日:上午10:07:15
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.cntest.foura.domain.Role;
import com.cntest.foura.service.RoleService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.SynUsers;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.ISynUsersService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2016年4月7日 上午10:07:15
 * @version 1.0
 */
@Controller
@RequestMapping("synusers")
public class SynUsersController {
	
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	
	@Autowired
	private RoleService roleService;
	
	
	@Autowired
	private ISynUsersService synUsersService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request) {
		List<Role> listRole = roleService.list();
		List<SynUsers> list  = synUsersService.list();
//		synUsersService.createUsers(2003L);
		
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder
				.newInstanceFor("/synUsers/list")
				.append("listRole", listRole)
				.append("list", list)
				.append("title",title)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
								.msg("list").build()).build();
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@RequestBody SynUsers synUsers) {
		Role role = roleService.findRoleById(Long.valueOf(synUsers.getRoleid()));
		String status = "失败",info = " ",erre="";
		try {
			synUsersService.save(synUsers);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="同步用户 角色：<b style='color:red;'>"+role.getName()+"</b>添加"+status+"</br>";
			LogUtil.log("系统设置>字典配置>同步用户设置", "添加",role.getName(),status,info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").build();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@RequestBody SynUsers synUsers) {
		String status = "失败",info = " ",erre="";
		SynUsers syn = synUsersService.load(Long.valueOf(synUsers.getId()));
		Role role = roleService.findRoleById(Long.valueOf(synUsers.getRoleid()));
		try {
			info+= "</br>修改项</br>";
			info+= updatainfo(synUsers,syn);
			userService.evictSession(syn);
			synUsersService.update(synUsers);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("系统设置>字典配置>同步用户设置", "修改",role.getName(),status, "同步用户 角色<b style='color:red;'>"+role.getName()+"</b>修改"+status+"</br>"+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").build();
	}
	
	public String updatainfo(SynUsers resource,SynUsers temp) {
		//resource:新来的数据，temp :之前的数据
		String info="";
		if(resource.getRoleid()!=temp.getRoleid()) {
			Role role = roleService.findRoleById(Long.valueOf(resource.getRoleid()));
			Role trole = roleService.findRoleById(Long.valueOf(temp.getRoleid()));
			info+="用户对应角色：<b style='color:red;'>"+trole.getName()+"</b> 改  <b style='color:red;'>"+role.getName()+"</b><br/>";
		}
		if(resource.getPasswordrules()!=null && (!resource.getPasswordrules().equals(temp.getPasswordrules()))) {
			Map<String ,String> span = new HashMap<String,String>();
			span.put("","未知 ");
			span.put("any","随机产生");
			span.put("one","固定密码");
			info+=("密码规则:<b style='color:red;'>"+span.get((temp.getPasswordrules()==null?"": temp.getPasswordrules())+"")+"</b> 改  <b style='color:red;'>"+span.get(resource.getPasswordrules()+"")+"</b><br/>");
		}
		if(resource.getIsSyn()!= temp.getIsSyn()) {
			Map<String ,String> span = new HashMap<String,String>();
			span.put(null,"锁定 ");
			span.put("1","启用");
			span.put("0","停用");
			info+=("数据发布时自动同步:<b style='color:red;'>"+span.get(temp.getIsSyn()+"") +"</b> 改  <b style='color:red;'>"+span.get(resource.getIsSyn()+"")+"</b><br/>");
		}
		
		if(resource.getNamingrules()!= temp.getNamingrules()) {
			String newA = "",newB="";
			Map<String ,String> span = new HashMap<String,String>();
			span.put("provinceCode","省");
			span.put("cityCode","市");
			span.put("countyCode","区");
			span.put("schoolCode","学校");
			span.put("gradeCode","年级");
			span.put("classCode","班级");
			span.put("subjectCode","学科");
			for (String key :resource.getNamingrules().split("、")) {
				newA+=(span.get(key)+",");
			}
			for (String key :temp.getNamingrules().split("、")) {
				newB+=(span.get(key)+",");
			}
			info+=("用户命名规则:<b style='color:red;'>"+newB.substring(0,newB.length()) +"</b> 改  <b style='color:red;'>"+newA.substring(0,newA.length())+"</b><br/>");
		}
		
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	
	
	@RequestMapping(value = "/newAdd", method = RequestMethod.GET)
	public ModelAndView newAdd() {
		List<SynUsers> list  = synUsersService.list();
		List<Role> listRole = roleService.list();
		for(SynUsers synUsers : list){
			for (int i = 0; i < listRole.size(); i++) {
				Role role = listRole.get(i);
				if(role.getPk()==synUsers.getRoleid()){
					listRole.remove(i);
				}
			}
		}
		return ModelAndViewBuilder.newInstanceFor("/synUsers/newAdd").append("listRole", listRole).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	
	@RequestMapping(value = "/newupdate/{id}")
	public ModelAndView newupdate(@PathVariable Long id) {
		List<Role> listRole = roleService.list();
		SynUsers synUsers = synUsersService.load(id);
		return ModelAndViewBuilder.newInstanceFor("/synUsers/newUpdate").append("listRole", listRole).append("syn", synUsers).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody SynUsers synUsers) throws Exception {
		SynUsers syn = synUsersService.load(Long.valueOf(synUsers.getId()));
		Role role = roleService.findRoleById(Long.valueOf(syn.getRoleid()));
		String status = "失败",info = " ",erre="",name = role.getName();
		try {
			synUsersService.delete(syn);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="用户对应角色<b style='color:red;'>"+name+"</b>删除"+status+"</br>";
			LogUtil.log("系统设置>字典配置>用户同步设置", "删除",name,status,info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("删除成功").build()).build();
	}
	
	@RequestMapping(value="/download")
	public void download(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String status = "失败",info = " ",erre="";
		try {
			synUsersService.downLoadExcelByZip(request, response);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="操作<b style='color:red;'>导出同步用户数据</b>"+status+"</br>";
			LogUtil.log("系统设置>字典配置>用户同步设置", "导出同步用户数据","全部",status,info,erre);
		}
	}
	
}
