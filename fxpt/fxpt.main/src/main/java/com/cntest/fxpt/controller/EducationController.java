/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java 1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.service.IEducationService;
import com.cntest.fxpt.service.ISchoolService;
import com.cntest.fxpt.web.OrganizationTreeNode;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/education")
public class EducationController {
  @Autowired(required = false)
  @Qualifier("IEducationService")
  private IEducationService educationService;

  @Autowired(required = false)
  @Qualifier("ISchoolService")
  private ISchoolService schoolService;

  @RequestMapping("/list/{educationType}")
  public ModelAndView list(@PathVariable int educationType, @RequestBody Page<Education> page)
      throws Exception {
    List<Education> educations = educationService.list(educationType, page);
    return ModelAndViewBuilder.newInstanceFor("/education/list")
        .append("educationType", educationType).append("page", page).build();
  }

  @RequestMapping("/newAdd/{educationType}")
  public ModelAndView newAdd(@PathVariable int educationType) throws Exception {

    List<Education> provinceEducations = null;
    if (educationType > 1) {
      provinceEducations = educationService.list(1);
    }
    List<Education> cityEducations = null;
    if (educationType > 2) {
      if (provinceEducations != null && !provinceEducations.isEmpty()) {
        cityEducations = educationService.childList(provinceEducations.get(0));
      }
    }

    return ModelAndViewBuilder.newInstanceFor("/education/newAdd")
        .append("provinceEducations", provinceEducations).append("cityEducations", cityEducations)
        .append("educationType", educationType).build();
  }

  @RequestMapping("/add")
  public ModelAndView add(@RequestBody Education education) throws Exception {
    boolean isExistEducation = educationService.isExistEducation(education);
    if (!isExistEducation) {
      educationService.add(education);
    }
    return ModelAndViewBuilder.newInstanceFor("").append("isExistEducation", isExistEducation)
        .build();
  }

  @RequestMapping("/newUpdate/{educationId}")
  public ModelAndView newUpdate(@PathVariable Long educationId) throws Exception {
    Education education = educationService.get(educationId);
    return ModelAndViewBuilder.newInstanceFor("/education/newUpdate").append("education", education)
        .build();
  }

  @RequestMapping("/update")
  public ModelAndView update(@RequestBody Education education) throws Exception {
    educationService.update(education);
    return ModelAndViewBuilder.newInstanceFor("").build();
  }

  @RequestMapping("/delete/{educationId}")
  public ModelAndView delete(@PathVariable Long educationId) throws Exception {
    Education education = educationService.get(educationId);
    boolean hasEducation = educationService.hasEducation(education);
    boolean hasSchool = educationService.hasSchool(education);

    if (!hasEducation && !hasSchool) {
      educationService.delete(education);
    }

    return ModelAndViewBuilder.newInstanceFor("").append("hasEducation", hasEducation)
        .append("hasSchool", hasSchool).append("education", education).build();
  }

  @RequestMapping("/child/{code}")
  public ModelAndView child(@PathVariable String code) throws Exception {
    Education education = new Education();
    education.setCode(code);
    List<Education> educations = educationService.childList(education);
    return ModelAndViewBuilder.newInstanceFor("").append("educations", educations).build();
  }

  @RequestMapping("/tree")
  public ModelAndView tree(@RequestParam String code, @RequestParam int type) throws Exception {
    List<OrganizationTreeNode> treeNodes = new ArrayList<OrganizationTreeNode>();

		if (type == 0) {
			List<Education> educations = educationService.list(1);
			treeNodes = fromEducationToOrganizationTreeNode(educations);
		} else if (type == 3) {
			List<School> schools = schoolService.listWithEducationCode(code);
			treeNodes = fromSchoolToOrganizationTreeNode(schools);
		} else {
			Education education = new Education();
			education.setCode(code);
			List<Education> educations = educationService.childList(education);
			treeNodes = fromEducationToOrganizationTreeNode(educations);
		}
		return ModelAndViewBuilder.newInstanceFor("/education/jsonData")
				.append("treeNodes", treeNodes).build();
	}

  private List<OrganizationTreeNode> fromEducationToOrganizationTreeNode(
      List<Education> educations) {
    ArrayList<OrganizationTreeNode> treeNodes = new ArrayList<OrganizationTreeNode>();
    for (Education education : educations) {
      String parentCode = "0";
      if (education.getParent() != null) {
        parentCode = education.getParent().getCode();
      }
      OrganizationTreeNode tmp = new OrganizationTreeNode();
      tmp.setCode(education.getCode());
      tmp.setName(education.getName());
      tmp.setParent(true);
      tmp.setParentCode(parentCode);
      tmp.setType(education.getType());
      treeNodes.add(tmp);
    }
    return treeNodes;
  }

  private List<OrganizationTreeNode> fromSchoolToOrganizationTreeNode(List<School> schools) {
    ArrayList<OrganizationTreeNode> treeNodes = new ArrayList<OrganizationTreeNode>();
    for (School school : schools) {
      OrganizationTreeNode tmp = new OrganizationTreeNode();
      tmp.setCode(school.getCode());
      tmp.setName(school.getName());
      tmp.setParent(false);
      tmp.setParentCode(school.getEducation().getCode());
      tmp.setType(4);
      treeNodes.add(tmp);
    }
    return treeNodes;
  }
}
