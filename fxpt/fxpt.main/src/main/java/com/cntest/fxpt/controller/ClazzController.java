/*
 * @(#)com.cntest.fxpt.controller.ClassController.java	1.0 2014年10月10日:下午2:57:16
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

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
import com.cntest.fxpt.domain.Clazz;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.service.IClazzService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月10日 下午2:57:16
 * @version 1.0
 */
@Controller
@RequestMapping("/clazz")
public class ClazzController {
		@Autowired(required = false)
		@Qualifier("IClazzService")
		private IClazzService clazzService;

		@RequestMapping(value="/list",method = RequestMethod.GET)
		public ModelAndView list(@RequestParam String clazzName, HttpServletRequest request) throws Exception {
			List<Clazz> clazzs = null;
			if(clazzName!=null && clazzName.length()>0){
				clazzs = clazzService.findByName(clazzName);	
			}else{
				clazzs = clazzService.list();
			}	
			String title = TitleUtil.getTitle(request.getServletPath());
			return ModelAndViewBuilder.newInstanceFor("/clazz/list")
					.append("title",title)
					.append("clazzs", clazzs).append(ResponseStatus.NAME,
							new ResponseStatus.Builder(Boolean.TRUE).code("list")
							.msg("list").build()).build();
		}

		@RequestMapping(value="/newAdd",method = RequestMethod.GET)
		public ModelAndView newAdd() throws Exception {
			return ModelAndViewBuilder.newInstanceFor("/clazz/newAdd").append(ResponseStatus.NAME,
							new ResponseStatus.Builder(Boolean.TRUE).code("")
							.msg("").build()).build();
		}

		public boolean hasCode(Clazz clazz){
			return clazzService.hasCode(clazz);
		}
		
		@RequestMapping(method = RequestMethod.POST)
		public ModelAndView add(@RequestBody Clazz clazz) throws Exception {
			String status = "失败",info = "新增班级<b style='color:red;'>",erre="";
			try {
				if (!hasCode(clazz)) {
					throw new BusinessException("","班级代码已存在");
				}
			    boolean res = clazzService.add(clazz);
				if (!res) {
					throw new BusinessException("","班级名称已存在");
				} 
				status = "成功";
			} catch (Exception e) {
				erre = LogUtil.e(e);
				throw e;
			}finally {
				info+=clazz.getName()+"</b>"+status;
				LogUtil.log("班级管理", "添加",clazz.getName(),status, info,erre);
			}
			return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
					new ResponseStatus.Builder(Boolean.TRUE).code("")
					.msg("").build()).build();
		}

		@RequestMapping("/newUpdate/{clazzId}")
		public ModelAndView newUpdate(@PathVariable Integer clazzId) throws Exception {
			Clazz clazz = clazzService.get(clazzId);
			return ModelAndViewBuilder.newInstanceFor("/clazz/newUpdate").append("clazz", clazz).append(ResponseStatus.NAME,
					new ResponseStatus.Builder(Boolean.TRUE).code("")
					.msg("").build()).build();
		}

		@RequestMapping(method = RequestMethod.PUT)
		public ModelAndView update(@RequestBody Clazz clazz) throws Exception {
			String status = "失败",info = "修改班级<b style='color:red;'>",erre="";
			try {
				if (!hasCode(clazz)) {
					throw new BusinessException("","班级代码已存在");
				}
				boolean res = clazzService.update(clazz);
				if (!res) {
					throw new BusinessException("","班级名称已存在");
				}
				status = "成功";
			} catch (Exception e) {
				erre = LogUtil.e(e);
				throw e;
			}finally {
				info+=clazz.getName()+"</b>"+status;
				LogUtil.log("班级管理", "修改",clazz.getName(),status, info,erre);
			}
			
			return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
					new ResponseStatus.Builder(Boolean.TRUE).code("")
					.msg("").build()).build();
		}

		@RequestMapping(method = RequestMethod.DELETE)
		public ModelAndView delete(@RequestBody Clazz clazz) throws Exception {
			String status = "失败",info = "删除班级<b style='color:red;'>",erre="",name="";
			try {
				Clazz clazzd = clazzService.get(clazz.getId());
				name = clazzd.getName();
				boolean res = clazzService.delete(clazz);
				if (!res) {
					throw new BusinessException("","已有绑定学生库信息，无法删除");
				}
				status = "成功";
			} catch (Exception e) {
				erre = LogUtil.e(e);
				throw e;
			}finally {
				info+=name+"</b>"+status;
				LogUtil.log("考试管理>产看", "删除",name,status, info,erre);
			}
			
			return ModelAndViewBuilder.newInstanceFor("").append("clazz", clazz).append(ResponseStatus.NAME,
					new ResponseStatus.Builder(Boolean.TRUE).code("")
					.msg("").build()).build();
		}

}
