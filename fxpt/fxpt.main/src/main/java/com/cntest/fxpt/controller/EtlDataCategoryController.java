/*
 * @(#)com.cntest.fxpt.controller.EtlConfigControoler.java	1.0 2014年5月14日:下午4:22:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.exception.BusinessException;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bean.DataCategory;
import com.cntest.fxpt.service.etl.IDataCategoryService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <Pre>
 * 系统设置->etl参数设置->导入数据项管理
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 下午4:22:10
 * @version 1.0
 */
@Controller
@RequestMapping("/dataCategory")
public class EtlDataCategoryController {
	@Autowired(required = false)
	@Qualifier("etl.IDataCategoryService")
	private IDataCategoryService dataCategoryService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list() throws Exception {
		List<DataCategory> dataCategorys = dataCategoryService.list();
		return ModelAndViewBuilder.newInstanceFor("/etl/dataCategory/list").append("dataCategorys", dataCategorys)
				.append(ResponseStatus.NAME,new ResponseStatus.Builder(Boolean.TRUE).code("status").msg("list").build()).build();
	}

	@RequestMapping("/newAdd")
	public ModelAndView newAdd() throws Exception {
		return ModelAndViewBuilder.newInstanceFor("/etl/dataCategory/newAdd").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody DataCategory dataCategory)
			throws Exception {
		String status = "失败",info = "",erre="";
		try {
			if(hasTableName(dataCategory)){
				dataCategoryService.add(dataCategory);
				status = "成功";
			}else{
				throw new BusinessException("","对应的表已存在");
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+dataCategory.getName()+"</b>添加"+status+","+erre;
			LogUtil.log("系统设置>导入配置>导入数据项管理","添加",dataCategory.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("添加成功").build()).build();
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody DataCategory dataCategory)
			throws Exception {
		dataCategory = dataCategoryService.findById(dataCategory.getId());
		
		//有关联的字段
		boolean hasDataField = dataCategoryService.hasDataField(dataCategory);
		//有关联的验证
		boolean hasTransform = dataCategoryService.hasTransform(dataCategory);
		String status = "失败",info = "",erre="",name=dataCategory.getName();
		try {
			if (!hasDataField && !hasTransform) {
				dataCategoryService.delete(dataCategory);
				status="成功";
			}else{
				throw new BusinessException("","已有绑定信息，无法删除");
			}
			dataCategory.setDataFields(null);
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+name+"</b>删除"+status+","+erre;
			LogUtil.log("系统设置>导入配置>导入数据项管理", "删除",name,status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append("dataCategory", dataCategory).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("删除成功").build()).build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody DataCategory dataCategory)
			throws Exception {
		String status = "失败",info = "",erre="";
		try {
			if(hasTableName(dataCategory)){
				DataCategory data1 = dataCategoryService.findById(dataCategory.getId());
				
				info+= "</br>修改项</br>";
				info+= updatainfo(dataCategory,data1);
				userService.evictSession(data1);
				dataCategoryService.update(dataCategory);
				status = "成功";
			}else{
				throw new BusinessException("","对应的表已存在");
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("系统设置>导入配置>导入数据项管理","修改",dataCategory.getName(),status, "<p style='color:red;'>"+dataCategory.getName()+"</p>修改"+status+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("更新成功").build()).build();
	}
	public String updatainfo(DataCategory resource,DataCategory temp) {
		//resource:新来的数据，temp:之前的数据
		String info="";
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+=("名称：<b style='color:red;'>"+(temp.getName()==null?"":temp.getName())+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>");
		}
		if(resource.getTableName()!=null && (!resource.getTableName().equals(temp.getTableName()))) {
			info+=("对应的表：<b style='color:red;'>"+(temp.getTableName()==null?"":temp.getTableName())+"</b> 改  <b style='color:red;'>"+resource.getTableName()+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping("/newUpdate/{dataCategoryId}")
	public ModelAndView newUpdate(@PathVariable Long dataCategoryId)
			throws Exception {
		DataCategory dataCategory = dataCategoryService
				.findById(dataCategoryId);
		return ModelAndViewBuilder.newInstanceFor("/etl/dataCategory/newUpdate")
				.append("dataCategory", dataCategory).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("").build()).build();
	}
	
	/**
	 * 检查表名是否重复
	* <Pre>
	* </Pre>
	* 
	* @param dataCategory
	* @return void
	* @author:黄洪成 2014年10月30日 下午3:20:27
	 */
	public boolean hasTableName(DataCategory dataCategory){
		return dataCategoryService.hasTableName(dataCategory);
	}
}
