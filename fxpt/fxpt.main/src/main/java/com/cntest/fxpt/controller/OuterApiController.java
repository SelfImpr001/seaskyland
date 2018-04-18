/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.service.IEducationService;
import com.cntest.fxpt.service.ISchoolService;
import com.cntest.web.view.JqueryZtree;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/** 
 * <pre>
 * 不需求安全控制就能访问的接口
 * </pre>
 *  
 * @author 李贵庆2014年11月26日
 * @version 1.0
 **/
@Controller
@RequestMapping(value = "/api")
public class OuterApiController {
	private static Logger logger = LoggerFactory.getLogger(OuterApiController.class);
	
	
	@Autowired(required = false)
	@Qualifier("IEducationService")
	private IEducationService educationService;

	@Autowired(required = false)
	@Qualifier("ISchoolService")
	private ISchoolService schoolService;

	
	@RequestMapping(value="/orgs")
	public ModelAndView orgTree(@RequestParam String code, @RequestParam int type)
			throws Exception {
		logger.debug("URL: /api/orgs method GET ");
		//List<OrganizationTreeNode> treeNodes = new ArrayList<OrganizationTreeNode>();
		List<Map<String,Object>>  treeNodes;
		  
		if (type == 0) {
			List<Education> educations = educationService.list(1,0,1);
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
		return ModelAndViewBuilder.newInstanceFor("/api/jqueryZtreeJsonData")
				.append("treeNodes", treeNodes).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("api.orgs")
						.msg("组织结构").build()).build();
	}

	private List<Map<String,Object>> fromEducationToOrganizationTreeNode(
			List<Education> educations) {

		ArrayList<Map<String,Object>> treeNodes = new ArrayList<>();
		for (Education org : educations) {
			String parentCode = "0";
			if (org.getParent() != null) {
				parentCode = org.getParent().getCode();
			}
			JqueryZtree ztree = new JqueryZtree().id(org.getId()).pId(org.getParent()!=null?org.getParent().getId():-1L).isParent(true);
			ztree.open(false).name(org.getName()).expendAttr("code", org.getCode()).expendAttr("parentCode", parentCode);
			ztree.expendAttr("type", org.getType());
			treeNodes.add(ztree.getZtreeNode());
		}
		return treeNodes;
	}

	private List<Map<String,Object>> fromSchoolToOrganizationTreeNode(
			List<School> schools) {
		ArrayList<Map<String,Object>> treeNodes = new ArrayList<>();
		for (School org : schools) {
			JqueryZtree ztree = new JqueryZtree().id(org.getId()).pId(org.getEducation().getId()).isParent(false);
			ztree.open(false).name(org.getName()).expendAttr("code", org.getCode()).expendAttr("parentCode", org.getEducation().getCode());
			ztree.expendAttr("type", 4);
			treeNodes.add(ztree.getZtreeNode());
		}
		return treeNodes;
		
	}
}

