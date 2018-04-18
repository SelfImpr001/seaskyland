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
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.FileManage;
import com.cntest.fxpt.domain.ShowMessage;
import com.cntest.fxpt.service.IEtlLogService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.service.IFileManageService;
import com.cntest.fxpt.service.IShowMessageService;
import com.cntest.fxpt.service.etl.IDataFieldService;
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
@RequestMapping("/examStudent")
public class ExamStudentController extends BaseController {
	private static Logger logger = LoggerFactory
			.getLogger(ExamStudentController.class);
	@Autowired(required = false)
	private UserService userService;
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	@Autowired(required = false)
	@Qualifier("IExamStudentService")
	private IExamStudentService examStudentService;

	@Autowired(required = false)
	@Qualifier("IFileManageService")
	private IFileManageService fileManageService;

	@Autowired(required = false)
	@Qualifier("IEtlLogService")
	private IEtlLogService etlLogService;
	
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
		logger.debug("URL /examStudent/list/exam/{}/{}", currentPage, pageSize);
		Page<Exam> page = newPage(currentPage, pageSize, request);
		List<Exam> exams = examService.list(page);
		
		String title = TitleUtil.getTitle(request.getServletPath());
		if (page.getParameter().isEmpty()) {
			return ModelAndViewBuilder.newInstanceFor("/examStudent/examList")
					.append("page", page).append("title",title).build();
		} else {
			if (page.getParameter().get("rollin") != null)
				return ModelAndViewBuilder
						.newInstanceFor("/examStudent/examListRollin")
						.append("page", page).append("title",title).build();

			if (page.getParameter().get("examId") != null)
				return ModelAndViewBuilder
						.newInstanceFor("/examStudent/examRollinListBody")
						.append("page", page).append("title",title).build();

			return ModelAndViewBuilder
					.newInstanceFor("/examStudent/examListBody")
					.append("page", page).append("title",title).build();
		}
	}

	@RequestMapping("/view/{examId}/{currentPage}/{pageSize}")
	public ModelAndView view(@PathVariable Long examId,
			@PathVariable int currentPage, @PathVariable int pageSize,
			ServletRequest request) throws Exception {
		Page<Map<String, Object>> page = newPage(currentPage, pageSize, request);
		examStudentService.statSchoolImportPersonNum(examId, page);
		if (page.getParameter().isEmpty()) {
			Exam exam = examService.findById(examId);
			Page<EtlLog> tmpPage = new Page<EtlLog>().setCurpage(0)
					.setPagesize(10);
			etlLogService.list(examId, 1, tmpPage);
			int totalNum =examStudentService.getExamStudentNum(examId);
			int totalSchoolNum = examStudentService.getExamSchoolNum(examId);
			return ModelAndViewBuilder.newInstanceFor("/examStudent/view")
					.append("exam", exam).append("page", page)
					.append("etlLogs", tmpPage.getList()).append("totalNum", totalNum).append("totalSchoolNum", totalSchoolNum).build();
		} else {
			return ModelAndViewBuilder
					.newInstanceFor("/examStudent/statSchoolPersonTBody")
					.append("page", page).build();
		}
	}
/**
 * 导入学生原始文件管理
 * @param type
 * @param examId
 * @param currentPage
 * @param pageSize
 * @param request
 * @return
 * @throws Exception
 */
	@RequestMapping("/viewFile/student/{examId}")
	public ModelAndView viewFile(@PathVariable Long examId,
			ServletRequest request) throws Exception {
			//根据examid 和  类型（此处为学生库信息，类型为1） 多类型用逗号隔开  例如（0,1）
			List<FileManage> fileList = fileManageService.fileList(examId,"1");
			Exam exam = examService.findById(examId);
			return ModelAndViewBuilder.newInstanceFor("/examStudent/viewFile")
					.append("exam", exam)
					.append("fileList", fileList)
					.build();
		}
	@RequestMapping(value = "/rollin/from/{srcExamId}/to/{targetExamId}", method = RequestMethod.PUT)
	public ModelAndView rollStdudent(@PathVariable Long srcExamId,
			@PathVariable Long targetExamId) throws Exception {
		logger.debug("URL /examStudent/list/exam/from/{}/to/{} method=PUT ",
				targetExamId, srcExamId);
		String status = "失败",info = "",erre="";
		Exam e1 = examService.findById(targetExamId);
		Exam e2 = examService.findById(srcExamId);
		
		try {
			examStudentService.saveRollinStudent(targetExamId, srcExamId);
			EtlLog etlLog = new EtlLog();
			etlLog.setOptionUser(userService.getCurrentLoginedUser().getUserName());
			etlLog.setLogType(1);
			etlLog.setCreateDate(new Date());
			etlLog.setOptionContent("转入考生信息");
			etlLog.setStatusMessage("转入成功");
			Exam exam = new Exam();
			exam.setId(targetExamId);
			etlLog.setExam(exam);
			etlLogService.save(etlLog);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="从考试 <b style='color:red;'>"+e2.getName()+"</b>的数据 转入 <b style='color:red;'>"+e1.getName()+"</b>"+status;
			LogUtil.log("考试管理>报名信息导入", "转入",e1.getName(),status, info,erre);
		}	
			
		return ModelAndViewBuilder
			.newInstanceFor("/examStudent/rollin")
			.append(ResponseStatus.NAME,
					new ResponseStatus.Builder(Boolean.TRUE).code("status")
							.msg("ExamStudent.rollin").build()).build();
	}

	@RequestMapping("/delete/{examId}")
	public ModelAndView delete(@PathVariable Long examId) throws Exception {
		String status = "失败",info = "",erre="";
		Exam exam = examService.findById(examId);
		boolean isSuccess = false;
		EtlLog etlLog = new EtlLog();
		try {
			
			etlLog.setOptionUser(userService.getCurrentLoginedUser().getUserName());
			etlLog.setLogType(1);
			etlLog.setCreateDate(new Date());
			etlLog.setExam(exam);
			if (exam.getImpCjCount() > 0) {
				etlLog.setOptionContent("删除学生信息");
				etlLog.setLogContent("已经导入成绩，无法删除。");
				etlLog.setStatusMessage("删除失败");
			} else {
				examStudentService.deleteAndUpdateExam(examId);
				etlLog.setOptionContent("删除学生信息");
				etlLog.setStatusMessage("删除成功");
				isSuccess = true;
				status ="成功";
			}
			etlLogService.save(etlLog);
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info=etlLog.getStatusMessage();
			LogUtil.log("考试管理>报名信息导入", "删除学生信息",exam.getName(),status, info,erre);
		}
		
		return ModelAndViewBuilder.newInstanceFor("")
				.append("isSuccess", isSuccess).build();
	}
	/**
	 * 查看学生列表信息
	 */
	@RequestMapping("/studentList/{examId}/{schoolCode}/{currentPage}/{pageSize}")
	public ModelAndView studentList(@PathVariable Long examId,
			@PathVariable String schoolCode, @PathVariable int currentPage,
			@PathVariable int pageSize, ServletRequest request)
			throws Exception {
		Page<ExamStudent> page = newPage(currentPage, pageSize, request);
		//学生信息的type是5：表kn_etl_dataField的字段dataCategoryId=5
		HashMap hashmap = new HashMap<>();
		//kn_show_message 1学生信息  2是明细表 3是成绩信息
		List<ShowMessage> showMessageList=showMessageService.findShowMessageByExamid(examId,null, 1);
		if(showMessageList.size()>0){
			//按照导入时的设置字段显示
			hashmap=toHashMap(showMessageList);
		}else{
			//历史数据按照当前系统设置的字段显示
			hashmap=dataFieldService.getDateByType("5");
		}
		
		examStudentService .findStudentListBySchoolCode(examId, schoolCode, page);
		ExamStudent sds = page.getList().get(0);
		if (page.getParameter().isEmpty()) {
			return ModelAndViewBuilder
					.newInstanceFor("/examStudent/studentList")
					.append("map", hashmap)
					.append("page", page).append("schoolCode", schoolCode)
					.build();
		} else {
			return ModelAndViewBuilder
					.newInstanceFor("/examStudent/studentListBody")
					.append("map", hashmap)
					.append("page", page).append("schoolCode", schoolCode)
					.append("examId", examId).build();
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
