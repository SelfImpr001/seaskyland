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
import org.springframework.web.servlet.ModelAndView;

import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.service.IEducationService;
import com.cntest.fxpt.service.ISchoolService;
import com.cntest.common.page.Page;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/school")
public class SchoolController {

	@Autowired(required = false)
	@Qualifier("ISchoolService")
	private ISchoolService schoolService;

	@Autowired(required = false)
	@Qualifier("IEducationService")
	private IEducationService educationService;

	@RequestMapping("/list")
	public ModelAndView list(@RequestBody Page<School> page) throws Exception {
		List<School> schools = schoolService.list(page);
		return ModelAndViewBuilder.newInstanceFor("/school/list")
				.append("page", page).build();
	}

	// @RequestMapping("/list/educationSchool/{educationCode}")
	// public ModelAndView educationSchool(@PathVariable String educationCode)
	// throws Exception {
	// List<School> schools = schoolService
	// .listWithEducationCode(educationCode);
	// return ModelAndViewBuilder.newInstanceFor("/school/list")
	// .append("schools", schools).build();
	// }

	@RequestMapping("/newAdd")
	public ModelAndView newAdd() throws Exception {
		List<Education> provinceEducations = educationService.list(1);
		return ModelAndViewBuilder.newInstanceFor("/school/newAdd")
				.append("provinceEducations", provinceEducations).build();
	}

	@RequestMapping("/add")
	public ModelAndView add(@RequestBody School school) throws Exception {
		boolean isExistSchool = schoolService.isExistSchool(school);
		if (!isExistSchool) {
			schoolService.add(school);
		}
		return ModelAndViewBuilder.newInstanceFor("")
				.append("isExistSchool", isExistSchool).build();
	}

	@RequestMapping("/newUpdate/{schoolId}")
	public ModelAndView newUpdate(@PathVariable Long schoolId) throws Exception {
		List<Education> provinceEducations = educationService.list(1);
		School school = schoolService.get(schoolId);
		return ModelAndViewBuilder.newInstanceFor("/school/newUpdate")
				.append("provinceEducations", provinceEducations)
				.append("school", school).build();
	}

	@RequestMapping("/update")
	public ModelAndView update(@RequestBody School school) throws Exception {
		schoolService.update(school);
		return ModelAndViewBuilder.newInstanceFor("").build();
	}

	@RequestMapping("/delete/{schoolId}")
	public ModelAndView delete(@PathVariable Long schoolId) throws Exception {
		School school = schoolService.get(schoolId);
		boolean hasExamStudent = schoolService.hasExamStudent(school);
		if (!hasExamStudent) {
			schoolService.delete(school);
		}

		return ModelAndViewBuilder.newInstanceFor("")
				.append("hasExamStudent", hasExamStudent)
				.append("school", school).build();
	}
}
