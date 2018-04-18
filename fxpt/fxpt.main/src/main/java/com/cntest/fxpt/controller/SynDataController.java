/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java	1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.pool.DruidDataSource;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.etl.business.CacheMgr;
import com.cntest.fxpt.etl.business.SynData;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.statistical.CalculateExam;
import com.cntest.util.SpringContext;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/syndata")
public class SynDataController {
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView synAll(@RequestBody Exam exam) throws Exception {
		exam = examService.findById(exam.getId());
		synData(exam);
		CalculateExam calculateExam = new CalculateExam();
		calculateExam.calculateExam(exam);
		exam.setStatus(5);
		examService.update(exam);
		return ModelAndViewBuilder.newInstanceFor("").build();
	}

	private void synData(Exam exam) throws Exception {
		try {			
			SynData synData = new SynData();
			synData.synProvince(exam);
			synData.synCity(exam);
			synData.synCounty(exam);
			synData.synSchool(exam);
			synData.synGrade(exam);
			synData.synSubject(exam);
			synData.synExam(exam);
			synData.synExamClass(exam);
			synData.synExamStudent(exam);
			synData.synTestPaper(exam);
			synData.synItem(exam);
			synData.synCj(exam);
			synData.synParameter(exam);
			synData.synCombinationSubject(exam);

		} catch (Exception e) {
			throw e;
		} finally {
			CacheMgr.newInstance().clearCache();
		}
	}

}
