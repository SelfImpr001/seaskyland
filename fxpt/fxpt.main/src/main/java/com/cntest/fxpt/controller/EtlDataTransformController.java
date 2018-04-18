/*
 * @(#)com.cntest.fxpt.controller.EtlConfigControoler.java	1.0 2014年5月14日:下午4:22:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.HashMap;
import java.util.List;

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
import com.cntest.fxpt.bean.DataCategory;
import com.cntest.fxpt.bean.DataTransform;
import com.cntest.fxpt.service.etl.IDataCategoryService;
import com.cntest.fxpt.service.etl.IDataTransformService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <Pre>
 * 系统设置->etl参数设置->导入字段管理
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 下午4:22:10
 * @version 1.0
 */
@Controller
@RequestMapping("/dataTransform")
public class EtlDataTransformController {

	@Autowired(required = false)
	@Qualifier("etl.IDataCategoryService")
	private IDataCategoryService dataCategoryService;
	@Autowired(required = false)
	@Qualifier("etl.IDataTransformService")
	private IDataTransformService dataTransformService;
	@Autowired
	private UserService userService;

	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list(@RequestParam Long dataCategoryId,HttpServletRequest request) throws Exception {
		List<DataCategory> dataCategorys = dataCategoryService.list();
		if (dataCategoryId == -1 && dataCategorys != null
				&& !dataCategorys.isEmpty()) {
			dataCategoryId = dataCategorys.get(0).getId();
		}
		List<DataTransform> dataTransforms = dataTransformService
				.list(dataCategoryId);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/etl/dataTransform/list").append("title",title).append("dataCategorys", dataCategorys).append("dataCategoryId", dataCategoryId).append("dataTransforms", dataTransforms)
				.append(ResponseStatus.NAME,new ResponseStatus.Builder(Boolean.TRUE).code("status").msg("list").build()).build();
	}

	@RequestMapping("/newAdd")
	public ModelAndView newAdd(@RequestParam Long dataCategoryId) throws Exception {
		List<DataCategory> dataCategorys = dataCategoryService.list();
		return ModelAndViewBuilder.newInstanceFor("/etl/dataTransform/newAdd").append("dataCategorys", dataCategorys).append("dataCategoryId", dataCategoryId).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody DataTransform dataTransform)
			throws Exception {
		String status = "失败",info = "",erre="";
		try {
			dataTransformService.add(dataTransform);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+dataTransform.getName()+"</b>添加"+status;
			LogUtil.log("系统设置>导入配置>转换规则管理","添加",dataTransform.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("添加成功").build()).build();
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody DataTransform dataTransform) throws Exception {
		dataTransform = dataTransformService.findById(dataTransform.getId());
		String status = "失败",info = "",erre="",name=dataTransform.getName();
		try {
			dataTransformService.delete(dataTransform);
			status="成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+name+"</b>删除"+status;
			LogUtil.log("系统设置>导入配置>>转换规则管理", "删除",name,status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("删除成功").build()).build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody DataTransform dataTransform)
			throws Exception {
		
		String status = "失败",info = "",erre="";
		DataTransform data1 = dataTransformService.findById(dataTransform.getId());
		try {
				info+= "</br>修改项</br>";
				info+= updatainfo(dataTransform,data1);
				userService.evictSession(data1);
				dataTransformService.update(dataTransform);
				status = "成功";
			 
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("系统设置>导入配置>转换规则管理","修改",dataTransform.getName(),status, "<p style='color:red;'>"+dataTransform.getName()+"</p>修改"+status+info,erre);
		}
		
		
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("更新成功").build()).build();
	}
	public String updatainfo(DataTransform resource,DataTransform temp) {
		//resource:新来的数据，temp:之前的数据
		String info="";
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+=("名称：<b style='color:red;'>"+(temp.getName()==null?"":temp.getName())+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>");
		}
		if(resource.getType()!=0 && (resource.getType()!=temp.getType())) {
			HashMap<Integer,String> span = new HashMap<Integer,String>();
			span.put(1, "验证");
			span.put(2, "转换");
			info+=("验证类型:<b style='color:red;'>"+span.get(temp.getType()) +"</b> 改  <b style='color:red;'>"+span.get(resource.getType()+"")+"</b><br/>");
		}
		if(resource.getType()!=0 && (resource.getType()!=temp.getType())) {
			HashMap<String,String> span = new HashMap<String,String>();
			span.put("true", "有效");
			span.put("false", "无效");
			info+=("是否有效:<b style='color:red;'>"+span.get(temp.isValid()+"") +"</b> 改  <b style='color:red;'>"+span.get(resource.isValid()+"")+"</b><br/>");
		}
		if(resource.getContent()!=null && (!resource.getContent().equals(temp.getContent()))) {
			info+=("验证/转换表达式：<b style='color:red;'>"+(temp.getContent()==null?"":temp.getContent())+"</b> 改  <b style='color:red;'>"+resource.getContent()+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping("/newUpdate/{dataTransformId}")
	public ModelAndView newUpdate(@PathVariable Long dataTransformId)
			throws Exception {
		DataTransform dataTransform = dataTransformService
				.findById(dataTransformId);
		List<DataCategory> dataCategorys = dataCategoryService.list();
		return ModelAndViewBuilder.newInstanceFor("/etl/dataTransform/newUpdate")
				.append("dataTransform", dataTransform).append("dataCategorys", dataCategorys).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("").build()).build();
	}
}
