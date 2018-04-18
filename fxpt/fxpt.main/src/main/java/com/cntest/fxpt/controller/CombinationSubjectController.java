/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.fxpt.domain.CombinationSubject;
import com.cntest.fxpt.domain.CombinationSubjectXTestPaper;
import com.cntest.fxpt.domain.ExamPaprameter;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.service.ICombinationSubjectService;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/combinationSubject")
public class CombinationSubjectController {
	@Autowired(required = false)
	@Qualifier("ICombinationSubjectService")
	private ICombinationSubjectService combinationSubjectService;

	@Autowired(required = false)
	@Qualifier("ITestPaperService")
	private ITestPaperService testPaperService;

	@RequestMapping("/list/{examId}")
	public ModelAndView list(@PathVariable Long examId) throws Exception {
		List<CombinationSubject> combinationSubjects = combinationSubjectService
				.listByExamId(examId);

		return ModelAndViewBuilder.newInstanceFor("/combinationSubject/list")
				.append("combinationSubjects", combinationSubjects).build();
	}

	@RequestMapping("/newAdd/{examId}")
	public ModelAndView newAdd(@PathVariable Long examId) throws Exception {
		List<TestPaper> testPapers = testPaperService.listByExamId(examId);
		return ModelAndViewBuilder.newInstanceFor("/combinationSubject/newAdd")
				.append("testPapers", testPapers).build();
	}

	@RequestMapping("/add")
	public ModelAndView add(@RequestBody CombinationSubject combinationSubject)
			throws Exception {
		combinationSubjectService.add(combinationSubject);
		return ModelAndViewBuilder.newInstanceFor("/combinationSubject/list")
				.build();
	}

	@RequestMapping("/newUpdate/{examId}/{combinationSubjectId}")
	public ModelAndView newUpdate(@PathVariable Long examId,
			@PathVariable Long combinationSubjectId) throws Exception {
		CombinationSubject combinationSubject = combinationSubjectService
				.get(combinationSubjectId);

		HashMap<String, Integer> testPaperMarks = new HashMap<String, Integer>();
		for (CombinationSubjectXTestPaper csXtp : combinationSubject
				.getChildTestPaper()) {
			testPaperMarks.put(csXtp.getTestPaper().getId()+"", 1);
		}

		List<TestPaper> testPapers = testPaperService.listByExamId(examId);
		return ModelAndViewBuilder
				.newInstanceFor("/combinationSubject/newUpdate")
				.append("testPapers", testPapers)
				.append("combinationSubject", combinationSubject)
				.append("testPaperMarks", testPaperMarks).build();
	}

	@RequestMapping("/update")
	public ModelAndView update(
			@RequestBody CombinationSubject combinationSubject)
			throws Exception {
		String status = "失败",info = "新增用户<b style='color:red;'>",erre="";
		try {
			combinationSubjectService.update(combinationSubject);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+=combinationSubject.getName()+"</b>"+status;
			LogUtil.log("考试管理>查看", "删除",combinationSubject.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("/combinationSubject/list")
				.build();
	}

	@RequestMapping("/delete/{combinationSubjectId}")
	public ModelAndView delete(@PathVariable Long combinationSubjectId)
			throws Exception {
		
		
		CombinationSubject combinationSubject = new CombinationSubject();
		combinationSubject.setId(combinationSubjectId);
		combinationSubjectService.delete(combinationSubject);
		
		
		return ModelAndViewBuilder.newInstanceFor("/combinationSubject/list")
				.build();
	}

}
