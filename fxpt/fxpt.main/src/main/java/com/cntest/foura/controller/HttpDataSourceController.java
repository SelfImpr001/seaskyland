/*
 * @(#)com.cntest.fxpt.controller.HttpDataSourceController.java	1.0 2016年3月9日:上午10:38:56
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.foura.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cntest.common.controller.BaseController;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.FtpSetting;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.service.IFtpSettingService;
import com.cntest.fxpt.util.FileFactory;
import com.cntest.fxpt.util.Json;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2016年3月9日 上午10:38:56
 * @version 1.0
 */
@Controller
public class HttpDataSourceController extends BaseController{
	
	@Autowired(required = false)
	@Qualifier("IExamStudentService")
	private IExamStudentService examStudentService;
	
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService ;
	
	@Autowired(required = false)
	@Qualifier("IFtpSettingService")
	private IFtpSettingService ftpSettingService;
	
	/**
	 * 
	* <Pre>
	* 在线返回JSON格式学生初始成绩数据
	* </Pre>
	* 
	* @param request
	* @param response
	* @return
	* @throws Exception
	* @return String 
	* @author:黄洪成 2016年3月28日 下午2:38:23
	 */
	@ResponseBody
	@RequestMapping(value = "/httpResult",method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public String httpResult(ServletRequest request, ServletResponse response) throws Exception{
		if (request.getParameter("examid") != null) {
			Long examid = Long.parseLong(request.getParameter("examid"));
			if (examService.hasStudentsAndSubjcetsAndCj(examid)) {
				FtpSetting ftpSetting = ftpSettingService.findFtpSetByStatus(1);
				if (ftpSetting != null && request.getParameter("examid") != null) {
					List<Map<String, String>> listStudent = examStudentService.getStudentList(examid);
					String result = Json.toJson(listStudent);
					return result;
				}
			}else{
				return "考试ID为："+examid+"的考试不存在或者没有导入学生成绩";
			}
		}
		return null;
	}

	/**
	 * 
	* <Pre>
	* 学生初始成绩数据上传到FTP
	* </Pre>
	* 
	* @param request
	* @param response
	* @return
	* @throws Exception
	* @return String
	* @author:黄洪成 2016年3月28日 下午2:39:17
	 */
	@ResponseBody
	@RequestMapping(value = "/downResult",method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public String downResult(ServletRequest request, ServletResponse response) throws Exception{
		FtpSetting ftpSetting = ftpSettingService.findFtpSetByStatus(1);
		if (request.getParameter("examid") != null && ftpSetting!=null) {
			Long examid = Long.parseLong(request.getParameter("examid"));
			if(examService.hasStudentsAndSubjcetsAndCj(examid)){
				ftpSettingService.upload(examid);
				return "导出成功";
			}else{
				return "考试ID为："+examid+"的考试不存在或者没有导入学生成绩";
			}
		}else{
			return "导出失败";
		}
	}
	
}
