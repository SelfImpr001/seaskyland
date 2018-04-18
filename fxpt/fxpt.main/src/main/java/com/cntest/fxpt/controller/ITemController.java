/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bean.EtlLog;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.ShowMessage;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.service.IEtlLogService;
import com.cntest.fxpt.service.IExamService;
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
@RequestMapping("/item")
public class ITemController extends BaseController {

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
	@Qualifier("IEtlLogService")
	private IEtlLogService etlLogService;

	@Autowired(required = false)
	private UserService userService;
	
	@Autowired(required = false)
	@Qualifier("IShowMessageService")
	private IShowMessageService showMessageService;
	
	
	@Autowired(required = false)
	@Qualifier("etl.IDataFieldService")
	private IDataFieldService dataFieldService;
	
	@RequestMapping(value = "/list/exam/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView examList(@PathVariable int currentPage,
			@PathVariable int pageSize, HttpServletRequest request)
			throws Exception {

		Page<Exam> page = newPage(currentPage, pageSize, request);
		List<Exam> exams = examService.list(page);
		String title = TitleUtil.getTitle(request.getServletPath());
		if (page.getParameter().isEmpty()) {
			return ModelAndViewBuilder.newInstanceFor("/item/examList")
					.append("page", page).append("title",title).build();
		} else {
			return ModelAndViewBuilder.newInstanceFor("/item/examListBody")
					.append("page", page).build();
		}
	}

	@RequestMapping("/list/{testPaperId}")
	public ModelAndView list(@PathVariable Long testPaperId) throws Exception {
		List<Item> items = itemService.listByTestPaperId(testPaperId);
		return new ModelAndView("/item/list", "items", items);
	}

	@RequestMapping("/delete/{testPaperId}")
	public ModelAndView delete(@PathVariable Long testPaperId) throws Exception {
		
		String status = "失败",info = "删除双向细目表 <b style='color:red;'>",erre="";
		TestPaper testPaper = testPaperService.get(testPaperId);
		EtlLog etlLog = new EtlLog();
		try {
			etlLog.setOptionUser(userService.getCurrentLoginedUser().getUserName());
			etlLog.setLogType(2);
			etlLog.setCreateDate(new Date());
			etlLog.setExam(testPaper.getExam());
			boolean isSuccess = false;
			if (testPaper.isHasCj()) {
				etlLog.setOptionContent("删除" + testPaper.getName() + "细目表");
				etlLog.setLogContent("成绩已存在，无法删除。");
				etlLog.setStatusMessage("删除失败");
			} else {
				testPaperService.delete(testPaper);
				etlLog.setOptionContent("删除" + testPaper.getName() + "细目表");
				etlLog.setStatusMessage("删除成功");
				isSuccess = true;
			}
			status = "成功";
			etlLogService.save(etlLog);
			
			return ModelAndViewBuilder.newInstanceFor("").append("isSuccess", isSuccess).build();
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+=testPaper.getName()+"</b>" +status;
			LogUtil.log("考试管理>双向细目表信息导入>查看", "双向细目表删除",testPaper.getName(),status, info,erre);
		}
		
	}

	@RequestMapping("/view/{examId}")
	public ModelAndView view(@PathVariable Long examId) throws Exception {
		Exam exam = examService.findById(examId);
		List<TestPaper> testPapers = testPaperService.listByExamId(examId);
		Page<EtlLog> etlLogs = new Page<EtlLog>().setCurpage(0).setPagesize(10);
		etlLogService.list(examId, 2, etlLogs);
		return ModelAndViewBuilder.newInstanceFor("/item/view")
				.append("exam", exam).append("testPapers", testPapers)
				.append("etlLogs", etlLogs.getList()).build();
	}

	@RequestMapping("/list/testPaper/{examId}")
	public ModelAndView testPaperList(@PathVariable Long examId)
			throws Exception {
		List<TestPaper> testPapers = testPaperService.listByExamId(examId);
		return ModelAndViewBuilder.newInstanceFor("/item/testPaperListTBody")
				.append("testPapers", testPapers).build();
	}

	@RequestMapping("/detail/{isInit}/{examId}/{testPaperId}")
	public ModelAndView view(@PathVariable boolean isInit,
			@PathVariable Long examId, @PathVariable Long testPaperId)
			throws Exception {
		if (isInit) {
			Exam exam = examService.findById(examId);
			List<TestPaper> testPapers = testPaperService.listByExamId(examId);
			TestPaper testPaper = null;
			List<Item> items = null;
			List<AnalysisTestpaper> analysisTestpapers = null;

			for (TestPaper tp : testPapers) {
				if (tp.getId().equals(testPaperId)) {
					testPaper = tp;
					break;
				}
			}

			items = itemService.listByTestPaperId(testPaper.getId());
			for(int i=0;i<items.size();i++){
				if(items.get(i).isChoice()){
					items.get(i).setChoiceOrNot(1);
				}else{
					items.get(i).setChoiceOrNot(0);
				}
				
			}
			analysisTestpapers = calcualteAnalysisTestpaperWith(items);
			
			HashMap hashmap = new HashMap<>();
			//kn_show_message 1学生信息  2是明细表 3是成绩信息
			List<ShowMessage> showMessageList=showMessageService.findShowMessageByExamid(examId,testPaper.getId(),2);
			if(showMessageList.size()>0){
				//按照导入时的设置字段显示
				hashmap=toHashMap(showMessageList);
			}else{
				//历史数据按照当前系统设置的字段显示
				hashmap=dataFieldService.getDateByType("6");
			}
			
			return ModelAndViewBuilder.newInstanceFor("/item/detail")
					.append("exam", exam).append("testPapers", testPapers)
					.append("items", items)
					.append("map", hashmap)
					.append("analysisTestpapers", analysisTestpapers)
					.append("testPaper", testPaper).build();
		} else {
			TestPaper testPaper = testPaperService.get(testPaperId);
			List<Item> items = itemService.listByTestPaperId(testPaper.getId());
			for(int i=0;i<items.size();i++){
				if(items.get(i).isChoice()){
					items.get(i).setChoiceOrNot(1);
				}
				
			}
			//细目表的type是6：表kn_etl_dataField的字段dataCategoryId=6
			HashMap hashmap = new HashMap<>();
			//kn_show_message 1学生信息  2是明细表 3是成绩信息
			List<ShowMessage> showMessageList=showMessageService.findShowMessageByExamid(examId,testPaper.getId(),2);
			if(showMessageList.size()>0){
				//按照导入时的设置字段显示
				hashmap=toHashMap(showMessageList);
			}else{
				//历史数据按照当前系统设置的字段显示
				hashmap=dataFieldService.getDateByType("6");
			}
			List<AnalysisTestpaper> analysisTestpapers = calcualteAnalysisTestpaperWith(items);
			return ModelAndViewBuilder.newInstanceFor("/item/itemList")
					.append("map", hashmap)
					.append("items", items)
					.append("analysisTestpapers", analysisTestpapers)
					.append("testPaper", testPaper).build();
		}

	}

	private List<AnalysisTestpaper> calcualteAnalysisTestpaperWith(
			List<Item> items) {
		LinkedHashMap<Long, AnalysisTestpaper> analysisTestpaperMap = new LinkedHashMap<Long, AnalysisTestpaper>();
		for (Item item : items) {
			AnalysisTestpaper analysisTestpaper = item.getAnalysisTestpaper();
			if (!analysisTestpaperMap.containsKey(analysisTestpaper.getId())) {
				analysisTestpaperMap.put(analysisTestpaper.getId(),
						analysisTestpaper);
			}

			if (analysisTestpaper.getItems() == null) {
				analysisTestpaper.setItems(new ArrayList<Item>());
			}
			analysisTestpaper.getItems().add(item);
		}

		List<AnalysisTestpaper> analysisTestpapers = null;
		analysisTestpapers = new ArrayList<AnalysisTestpaper>();
		for (AnalysisTestpaper tmp : analysisTestpaperMap.values()) {
			analysisTestpapers.add(tmp);
		}
		return analysisTestpapers;
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
