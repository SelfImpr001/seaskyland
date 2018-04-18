/*
 * e * @(#)com.cntest.fxpt.controller.UploadFileController.java 1.0 2014年10月15日:上午9:42:41
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cntest.common.controller.BaseController;
import com.cntest.fxpt.etl.business.IImportTemplateService;
import com.cntest.fxpt.util.SaveEtlProcessResultToFile;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午9:42:41
 * @version 1.0
 */
@Controller
@RequestMapping("/download")
public class DownloadController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(DownloadController.class);

	@RequestMapping("/org")
	public void org(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/excel;charset=UTF-8"); // 下载文件类型
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("组织架构模板.xls".getBytes("UTF-8"), "ISO-8859-1")); // 下载文件命名
		ServletOutputStream out = response.getOutputStream();
		IImportTemplateService tmplate = SpringContext.getBean("Org.TemplateService");
		tmplate.template(out);
		out.flush();
	}

	@RequestMapping("/xmb")
	public void xmb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/excel;charset=UTF-8"); // 下载文件类型
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("双向细目表模板.xls".getBytes("UTF-8"), "ISO-8859-1")); // 下载文件命名
		ServletOutputStream out = response.getOutputStream();
		IImportTemplateService tmplate = SpringContext.getBean("Item.TemplateService");
		tmplate.template(out);
		out.flush();
	}

	@RequestMapping("/bmk")
	public void bmk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/excel;charset=UTF-8"); // 下载文件类型
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("学生信息模板.xls".getBytes("UTF-8"), "ISO-8859-1")); // 下载文件命名
		ServletOutputStream out = response.getOutputStream();
		IImportTemplateService tmplate = SpringContext.getBean("Student.TemplateService");
		tmplate.template(out);
		out.flush();
	}

	@RequestMapping("/cj")
	public void cj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/excel;charset=UTF-8"); // 下载文件类型
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("试卷成绩模板.xls".getBytes("UTF-8"), "ISO-8859-1")); // 下载文件命名
		ServletOutputStream out = response.getOutputStream();
		IImportTemplateService tmplate = SpringContext.getBean("Cj.TemplateService");
		tmplate.template(out);
		out.flush();
	}

	@RequestMapping("/studentBase")
	public void studentBase(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/excel;charset=UTF-8"); // 下载文件类型
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("学生库模板.xls".getBytes("UTF-8"), "ISO-8859-1")); // 下载文件命名
		ServletOutputStream out = response.getOutputStream();
		IImportTemplateService tmplate = SpringContext.getBean("StudentBase.TemplateService");
		tmplate.template(out);
		out.flush();
	}

	@RequestMapping("/log/{examId}")
	public void dowloadLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String logFile = request.getParameter("logFile");
		int logType = Integer.parseInt(request.getParameter("logType"));
		String fileName = "tmp.csv";
		if (logType == 1) {
			fileName = "学生信息导入日志.csv";
		} else if (logType == 2) {
			fileName = "细目表导入日志.csv";
		} else if (logType == 3) {
			fileName = "成绩导入日志.csv";
		} else if (logType == 5) {
			fileName = "学生库导入日志.csv";
		}
		response.setContentType("text/csv;charset=UTF-8"); // 下载文件类型
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1")); // 下载文件命名

		ServletOutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(SaveEtlProcessResultToFile.getDir() + logFile);
		byte[] bs = new byte[1024];
		int len = 0;
		while ((len = in.read(bs)) != -1) {
			out.write(bs, 0, len);
		}
		in.close();
		out.flush();
		out.close();
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void mytest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setHeader("Cache-control", "private");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Connection", "Keep-Alive");
		response.setHeader("Proxy-Connection", "Keep-Alive");
		response.setContentType("text/html;charset=utf-8");

		PrintWriter out = response.getWriter();

		for (int i = 0; i < 30; i++) {
			Thread.sleep(1000);
			out.println(i + "====fdkjdkjfdkjfjkfdjkfdjk");
			out.println("<script type='text/javascript'>window.parent.myFun();</script>");
			System.out.println(i);
			out.flush();
		}
	}

	@RequestMapping("/down")
	public void data(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filename = request.getParameter("filename") + ".xls";
		response.setContentType("application/excel;charset=UTF-8"); // 下载文件类型
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(filename.getBytes("UTF-8"), "ISO-8859-1")); // 下载文件命名
		ServletOutputStream out = response.getOutputStream();
		IImportTemplateService tmplate = SpringContext.getBean("Data.TemplateService");
		tmplate.template(out, request, response);
		out.flush();
	}

	@RequestMapping("/downLog")
	public void downLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName="日志信息表.xls";
		String userAgent = request.getHeader("user-agent").toLowerCase();
		if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/excel;charset=UTF-8"); // 下载文件类型
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + fileName);
		ServletOutputStream out = response.getOutputStream();
		IImportTemplateService tmplate = SpringContext.getBean("Log.TemplateService");
		tmplate.template(out, request, response);
		out.flush();
	}

}
