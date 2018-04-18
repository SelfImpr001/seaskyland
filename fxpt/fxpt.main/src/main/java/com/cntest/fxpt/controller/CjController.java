/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bean.EtlLog;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamCjDataSum;
import com.cntest.fxpt.domain.FileManage;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.ShowMessage;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.service.ICjService;
import com.cntest.fxpt.service.IEtlLogService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IFileManageService;
import com.cntest.fxpt.service.IItemService;
import com.cntest.fxpt.service.IShowMessageService;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/cj")
public class CjController extends BaseController {
	private static final Logger log = LoggerFactory
			.getLogger(CjController.class);
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	@Autowired(required = false)
	@Qualifier("ITestPaperService")
	private ITestPaperService testPaperService;
	@Autowired(required = false)
	@Qualifier("IItemService")
	private IItemService itemService;

	@Autowired(required = false)
	@Qualifier("etl.IDataFieldService")
	private IDataFieldService dataFieldService;

	@Autowired(required = false)
	@Qualifier("ICjService")
	private ICjService cjService;

	@Autowired(required = false)
	@Qualifier("IEtlLogService")
	private IEtlLogService etlLogService;

	@Autowired(required = false)
	@Qualifier("IFileManageService")
	private IFileManageService fileManageService;
	
	@Autowired(required = false)
	@Qualifier("IShowMessageService")
	private IShowMessageService showMessageService;
	


	@Autowired(required = false)
	private UserService userService;
	
	//导入数据汇总
	@RequestMapping(value = "/dataSumList/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView dataSumList(@PathVariable int currentPage,
			@PathVariable int pageSize, ServletRequest request)
			throws Exception {
		Page<ExamCjDataSum> page = newPage(currentPage, pageSize, request);
		boolean isEmpty = page.getParameter().isEmpty();
		String examData="";
		String examName="";
		if(!isEmpty) {
			examData=page.getParameter().get("examData");
			examName=page.getParameter().get("examName");
		}
		
		List<ExamCjDataSum> examDataList = cjService.dataSumList(examData,examName);
		cjService.setPageMsg(examDataList,page);
		if(isEmpty) {
			return ModelAndViewBuilder.newInstanceFor("/cj/dataSumList")
					.append("page", page).build();
		}else {
			return ModelAndViewBuilder.newInstanceFor("/cj/dataListBody")
					.append("page", page).build();

		}
		
	}
	
	@RequestMapping(value = "/list/exam/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView examList(@PathVariable int currentPage,
			@PathVariable int pageSize, HttpServletRequest request)
			throws Exception {

		Page<Exam> page = newPage(currentPage, pageSize, request);
		boolean isEmpty = page.getParameter().isEmpty();
		page.addParameter("isCjList", "true");

		List<Exam> exams = examService.list(page);

		String title = TitleUtil.getTitle(request.getServletPath());
		if (isEmpty) {
			return ModelAndViewBuilder.newInstanceFor("/cj/examList")
					.append("page", page).append("title",title).build();
		} else {
			return ModelAndViewBuilder.newInstanceFor("/cj/examListBody")
					.append("page", page).build();
		}
	}

	@RequestMapping("/viewFile/{examId}")
	public ModelAndView viewFile(@PathVariable Long examId,
			ServletRequest request) throws Exception {
			//根据examid 和  类型（此处为学生cj，类型为3） 多类型用逗号隔开  例如（0,1）
			List<FileManage> fileList = fileManageService.fileList(examId,"3");
			Exam exam = examService.findById(examId);
			return ModelAndViewBuilder.newInstanceFor("/cj/viewFile")
					.append("exam", exam)
					.append("fileList", fileList)
					.build();
		}
	@RequestMapping(value = "/view/{examId}", method = RequestMethod.GET)
	public ModelAndView examList(@PathVariable long examId,
			ServletRequest request) throws Exception {
		Exam exam = examService.findById(examId);
		return ModelAndViewBuilder.newInstanceFor("/cj/view")
				.append("exam", exam).build();
	}
	@RequestMapping(value = "/examJSON/{testPaperId}", method = RequestMethod.POST)
	public ModelAndView examJSON(@PathVariable long testPaperId,
			ServletRequest request) throws Exception {
		TestPaper testPaper = testPaperService.get(testPaperId);
		Exam exam = examService.findById(testPaper.getExam().getId());
		return ModelAndViewBuilder.newInstanceFor("")
				.append("exam", exam).build();
	}
 
	@RequestMapping(value = "/testPaper/stat/{examId}", method = RequestMethod.GET)
	public ModelAndView testPaperList(@PathVariable long examId,
			ServletRequest request) throws Exception {
		List<TestPaper> testPapers = cjService.statTestPaperExamCount(examId);
		return ModelAndViewBuilder.newInstanceFor("/cj/testPaperStatTBody")
				.append("testPapers", testPapers).build();
	}

	@RequestMapping(value = "/delete/{testPaperId}")
	public ModelAndView delete(@PathVariable long testPaperId,
			ServletRequest request) throws Exception {
		String status = "失败",info = "删除成绩<b style='color:red;'>",erre="";
		TestPaper testPaper = testPaperService.get(testPaperId);
		try {
			cjService.deleteCj(testPaper);
			EtlLog etlLog = new EtlLog();
			etlLog.setOptionUser(userService.getCurrentLoginedUser().getUserName());
			etlLog.setLogType(3);
			etlLog.setCreateDate(new Date());
			etlLog.setOptionContent("删除" + testPaper.getName() + "成绩");
			etlLog.setStatusMessage("删除成功");
			etlLog.setExam(testPaper.getExam());
			etlLogService.save(etlLog);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+=testPaper.getName()+"</b>"+status;
			LogUtil.log("考试管理>成绩信息导入>查看", "成绩删除",testPaper.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").build();
	}

	@RequestMapping(value = "/list/{examId}/{testPaperId}/{schoolId}/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable Long examId,
			@PathVariable Long testPaperId, @PathVariable Long schoolId,
			@PathVariable int currentPage, @PathVariable int pageSize,
			ServletRequest request) throws Exception {

		Page<Map<String, Object>> page = newPage(currentPage, pageSize, request);
		cjService.listCj(examId, testPaperId, schoolId, page);

		List<Item> items = itemService.listByTestPaperId(testPaperId);

		HashMap hashmap = new HashMap<>();
		//kn_show_message 1学生信息  2是明细表 3是成绩信息
		List<ShowMessage> showMessageList=showMessageService.findShowMessageByExamid(examId,testPaperId,3);
		if(showMessageList.size()>0){
			//按照导入时的设置字段显示
			hashmap=toHashMap(showMessageList);
		}else{
			//历史数据按照当前系统设置的字段显示
			hashmap=dataFieldService.getDateByType("7,10");
		}
		
		if (page.getParameter().isEmpty()) {
			return ModelAndViewBuilder.newInstanceFor("/cj/cjList")
					.append("map", hashmap)
					.append("page", page).append("items", items).build();
		} else {
			return ModelAndViewBuilder.newInstanceFor("/cj/cjListTBody")
					.append("map", hashmap)
					.append("page", page).append("items", items).build();
		}
	}
	public HashMap toHashMap(List<ShowMessage> showMessageList){
		HashMap hashmap = new HashMap<>();
		for(int i=0;i<showMessageList.size();i++){
			ShowMessage showMessage =showMessageList.get(i);
			hashmap.put(showMessage.getFieldName(), showMessage.getIsShow());
		}
	return hashmap;
	}
}
