/*
 * @(#)com.cntest.fxpt.controller.EtlConfigControoler.java	1.0 2014年5月14日:下午4:22:10
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
import com.cntest.fxpt.bean.DataCategory;
import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.service.etl.IDataCategoryService;
import com.cntest.fxpt.service.etl.IDataFieldService;
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
@RequestMapping("/dataField")
public class EtlDataFieldController {
	@Autowired(required = false)
	@Qualifier("etl.IDataFieldService")
	private IDataFieldService dataFieldService;
	@Autowired(required = false)
	@Qualifier("etl.IDataCategoryService")
	private IDataCategoryService dataCategoryService;
	@Autowired
	private UserService userService;

	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list(@RequestParam Long dataCategoryId, HttpServletRequest request) throws Exception {
		List<DataCategory> dataCategorys = dataCategoryService.list();
		if (dataCategoryId == -1 && dataCategorys != null
				&& !dataCategorys.isEmpty()) {
			dataCategoryId = dataCategorys.get(0).getId();
		}
		List<DataField> dataFields = dataFieldService.list(dataCategoryId);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/etl/dataField/list").append("title",title).append("dataCategorys", dataCategorys).append("dataCategoryId", dataCategoryId).append("dataFields", dataFields)
				.append(ResponseStatus.NAME,new ResponseStatus.Builder(Boolean.TRUE).code("status").msg("list").build()).build();
	}

	@RequestMapping("/newAdd")
	public ModelAndView newAdd(@RequestParam Long dataCategoryId) throws Exception {
		List<DataCategory> dataCategorys = dataCategoryService.list();
		return ModelAndViewBuilder.newInstanceFor("/etl/dataField/newAdd").append("dataCategorys", dataCategorys).append("dataCategoryId", dataCategoryId).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody DataField dataField)throws Exception {
	
		String status = "失败",info = "",erre="";
		try {
			if(hasFiled(dataField)){
				dataFieldService.add(dataField);
				status = "成功";
			}else{
				throw new BusinessException("","字段已存在");
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+dataField.getAsName()+"</b>添加"+status+","+erre;
			LogUtil.log("系统设置>导入配置>导入字段管理","添加",dataField.getAsName(),status, info,erre);
		}
		
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("添加成功").build()).build();
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody DataField dataField) throws Exception {
		dataField  = dataFieldService.findById(dataField.getId());
		
		String status = "失败",info = "",erre="",name=dataField.getAsName();
		try {
			dataFieldService.delete(dataField);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "<b style='color:red;'>"+name+"</b>删除"+status+","+erre;
			LogUtil.log("系统设置>导入配置>导入字段管理","删除",name,status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("删除成功").build()).build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody DataField dataField)
			throws Exception {
		String status = "失败",info = "",erre="";
		try {
			if(hasFiled(dataField)){
				info+= "</br>修改项</br>";
				DataField data1 = dataFieldService.findById(dataField.getId());
				info+= updatainfo(dataField,data1);
				userService.evictSession(data1);
				
				dataFieldService.update(dataField);
				status = "成功";
			}else{
				throw new BusinessException("","字段已存在");
			}
			 
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("系统设置>导入配置>导入数据项管理","修改",dataField.getAsName(),status, "<p style='color:red;'>"+dataField.getAsName()+"</p>修改"+status+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("更新成功").build()).build();
	}
	public String updatainfo(DataField resource,DataField temp) {
		//resource:新来的数据，temp:之前的数据
		String info="";
		if(resource.getFieldName()!=null && (!resource.getFieldName().equals(temp.getFieldName()))) {
			info+=("字段名称：<b style='color:red;'>"+(temp.getFieldName()==null?"":temp.getFieldName())+"</b> 改  <b style='color:red;'>"+resource.getFieldName()+"</b><br/>");
		}
		if(resource.getAsName()!=null && (!resource.getAsName().equals(temp.getAsName()))) {
			info+=("字段别名：<b style='color:red;'>"+(temp.getAsName()==null?"":temp.getAsName())+"</b> 改  <b style='color:red;'>"+resource.getAsName()+"</b><br/>");
		}
		if(resource.getDefaultName()!=null && (!resource.getDefaultName().equals(temp.getDefaultName()))) {
			info+=("默认字段：<b style='color:red;'>"+(temp.getDefaultName()==null?"":temp.getDefaultName())+"</b> 改  <b style='color:red;'>"+resource.getDefaultName()+"</b><br/>");
		}
		if(resource.getDescription()!=null && (!resource.getDescription().equals(temp.getDescription()))) {
			info+=("描述：<b style='color:red;'>"+(temp.getDescription()==null?"":temp.getDescription())+"</b> 改  <b style='color:red;'>"+resource.getDescription()+"</b><br/>");
		}
		if(resource.isValid()!= temp.isValid()) {
			Map<String ,String> span = new HashMap<String,String>();
			span.put("true","有效");
			span.put("false","无效");
			info+=("是否有效:<b style='color:red;'>"+span.get(temp.isValid()+"") +"</b> 改  <b style='color:red;'>"+span.get(resource.isValid()+"")+"</b><br/>");
		}
		if(resource.isValid()!= temp.isValid()) {
			Map<String ,String> span = new HashMap<String,String>();
			span.put("true","是");
			span.put("false","否");
			info+=("是否必须导入:<b style='color:red;'>"+span.get(temp.isValid()+"") +"</b> 改  <b style='color:red;'>"+span.get(resource.isValid()+"")+"</b><br/>");
		}
		if(resource.getSortNum()!=0 && (resource.getSortNum()!=temp.getSortNum())) {
			info+=("序号：<b style='color:red;'>"+(temp.getSortNum()==0?"":temp.getSortNum())+"</b> 改  <b style='color:red;'>"+resource.getSortNum()+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping("/newUpdate/{dataFieldId}")
	public ModelAndView newUpdate(@PathVariable Long dataFieldId)
			throws Exception {
		DataField dataField = dataFieldService.findById(dataFieldId);
		return ModelAndViewBuilder.newInstanceFor("/etl/dataField/newUpdate")
				.append("dataField", dataField).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("").build()).build();
	}
	

	/**
	 * 检查字段是否重复
	* <Pre>
	* </Pre>
	* 
	* @param dataCategory
	* @return void
	* @author:黄洪成 2014年10月30日 下午3:20:27
	 */
	public boolean hasFiled(DataField dataField){
		return dataFieldService.hasFiled(dataField);
	}
}
