/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.controller;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamCheckin;
import com.cntest.fxpt.service.ExamCheckinService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <pre>
 * 考试的学生信息核对
 * </pre>
 * 
 * @author 李贵庆2014年10月13日
 * @version 1.0
 **/
@Controller
@RequestMapping("/exam/checkin")
public class ExamCheckinController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(ExamCheckinController.class);

	@Autowired
	private ExamCheckinService checkinService;

	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;

	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage, @PathVariable int pageSize, HttpServletRequest request, ServletResponse response)
			throws Exception {
		logger.debug("URL exam/checkin/{}/{} METHOD:GET", currentPage, pageSize);

		Page page = newPage(currentPage, pageSize, request);
		checkinService.listExamCheckin(page);
		//page.setList(checkins);
		logger.debug("Student checkin list size is {}", page.getList().size());
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/exam/checkinList").append("query", page).append("title", title)
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("status").msg("Exam.chickin.list").build()).build();
	}
	
	@RequestMapping(value = "/checked/{examId}/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView checkedList(@PathVariable Long examId,@PathVariable int currentPage, @PathVariable int pageSize, ServletRequest request, ServletResponse response)
			throws Exception {
		logger.debug("URL exam//checked/{}/{} METHOD:GET", examId,currentPage+"/"+ pageSize);
		Page page = newPage(currentPage, pageSize, request);
		checkinService.getCheckedExamniees(examId,page);
		
		Page page2 = newPage(1, 1, request);
		checkinService.getCheckedlessExamniees(examId,page2);	
		
		Exam exam = examService.findById(examId);
		ExamCheckin checkin = checkinService.getOf(exam);
		//logger.debug("Student checkin list size is {}", checkins.size());
		return ModelAndViewBuilder.newInstanceFor("/exam/checkedList").append("checkin", checkin).append("exam", exam).append("query", page).append("uc", page2.getTotalRows())
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("status").msg("Exam.chickined.list").build()).build();
	}
	
	@RequestMapping(value = "/checkedless/{examId}/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView checkedlessList(@PathVariable Long examId,@PathVariable int currentPage, @PathVariable int pageSize, ServletRequest request, ServletResponse response)
			throws Exception {
		logger.debug("URL exam//checked/{}/{} METHOD:GET", examId,currentPage+"/"+ pageSize);
		Page page = newPage(currentPage, pageSize, request);
		checkinService.getCheckedlessExamniees(examId,page);
		Exam exam = examService.findById(examId);

		Page page2 = newPage(1, 1, request);
		//page2.setPagesize(1);
		checkinService.getCheckedExamniees(examId,page2);
		ExamCheckin checkin = checkinService.getOf(exam);
		return ModelAndViewBuilder.newInstanceFor("/exam/checkedlessList").append("checkin", checkin).append("exam", exam).append("query", page).append("uc", page2.getTotalRows())
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("status").msg("Exam.chickined.list").build()).build();
	}
	
	@RequestMapping(value = "/rule/{pk}", method = RequestMethod.GET)
	public ModelAndView showCheckinRule(@PathVariable Long pk,  ServletRequest request, ServletResponse response)
			throws Exception {
		
		logger.debug("URL exam/checkin/{}", pk);
		return ModelAndViewBuilder.newInstanceFor("/exam/checkinRule")
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("status").msg("Exam.chickin.list").build()).build();
	}
	
	@RequestMapping(value = "/rule/{examId}", method = RequestMethod.POST)
	public ModelAndView checkin(@PathVariable Long examId,  ServletRequest request, ServletResponse response)
			throws Exception {
		logger.debug("URL exam/checkin/{} method=POST", examId);
		String status = "失败",info = "",erre="";
		Exam exam = examService.findById(examId); 
		try {
			
			String[] specs = request.getParameterValues("spec");
			examService.createCheckin(exam,specs);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+= "操作成功！";
			LogUtil.log("考试管理>学生信息核对", "核对学生历次信息",exam.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("/exam/checkinRule")
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("status").msg("Exam.chickin.list").build()).build();
	}
}
