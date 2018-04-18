/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.controller.report;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.fxpt.anlaysis.customize.AnalysisService;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisResource;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisResult;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.service.IAnalysisTestPaperService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2015年4月22日
 * @version 1.0
 **/
@Controller
@RequestMapping("/report/selfdefined")
public class SelfdefinedReportExamController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(SelfdefinedReportExamController.class);

	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperService")
	private IAnalysisTestPaperService paperService;

	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage, @PathVariable int pageSize, @RequestParam Long examId,HttpServletRequest request) throws Exception {
		logger.debug("URL report/selfdefined/{}/{} METHOD:GET", currentPage, pageSize);
		Page<Exam> page = newPage(currentPage, pageSize, request);
		List<Exam> exams = examService.list(page);
		Exam exam = examService.findById(examId);
		if (exam == null && exams != null && exams.size() > 0) {
			exam = exams.get(0);
		}
		Education edu = examService.getExamRootEducation(exam);
		List<AnalysisTestpaper> papers = paperService.listAllWith(exam.getId());
		String[] nations = examService.getExamNations(exam);
		// List<School> schools = examService.getExamSchools(exam);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/exam/selfdefined")
				.append("exams", exams)
				.append("exam", exam)
				.append("rootEdu", edu)
				.append("nations", nations)
				.append("title",title)
				.append("papers", papers).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("fxpt.analyse.selfdefined").msg("fxpt.analyse.selfdefined").build()).build();
	}
	
	@RequestMapping(value = "/analyse/{tpl}/{examId}", method = RequestMethod.POST)
	public ModelAndView analyse(@RequestBody AnalysisResource analysisResource ,@PathVariable Long examId,@PathVariable String tpl, ServletRequest request) throws Exception {
		Exam exam = examService.findById(examId);
		AnalysisService analysisService = new AnalysisService();
		AnalysisResult analysisResult = analysisService.analysis(exam, analysisResource);
		return ModelAndViewBuilder.newInstanceFor("/exam/selfdefined")
				.append("result", analysisResult.getResult())
				.append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("fxpt.analyse.selfdefined").msg("fxpt.analyse.selfdefined").build()).build();
	}
}
