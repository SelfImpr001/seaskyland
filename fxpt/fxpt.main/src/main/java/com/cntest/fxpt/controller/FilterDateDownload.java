package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.cntest.foura.domain.Organization;
import com.cntest.foura.service.OrganizationService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.Clazz;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.service.IClazzService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.security.UserDetails;
import com.cntest.security.UserOrg;
import com.cntest.security.remote.UserDetailsService;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <Pre>
 * </Pre>
 * 
 * 数据分析
 * 
 * @author 黄洪成 2014年11月26日 下午2:18:32
 * @version 1.0
 */
@Controller
@RequestMapping("/dowload")
public class FilterDateDownload extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);
	@Autowired(required = false)
	private UserService userService;
	
	@Autowired(required = false)
	private UserDetailsService userDetailsService;
	
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired(required = false)
	@Qualifier("IClazzService")
	private IClazzService clazzService;
	
	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage, 
			@PathVariable int pageSize, HttpServletRequest request, 
			HttpServletResponse response ) throws Exception {
		int  type = 0;
		String name = "";
		String code = "";
		List<Organization> citys = new ArrayList<>();
		List<Organization> countys = new ArrayList<>();
		List<Organization> schools = new ArrayList<>();
		List<Organization> clazzs = new ArrayList<>();
		List<String> result = new ArrayList<>();
		
		UserDetails<?> user =   userService.getCurrentLoginedUser();
		 //获取权限
		List<UserOrg> userOrgs = userDetailsService.findUserOrgs(user);
		List<Organization> orgtmp = organizationService.getTopOrgList(null);
		List<Organization> orgList=organizationService.nextOrgCount(orgtmp); 
		//分级处理选项
		for (UserOrg userOrg : userOrgs) {
			name = userOrg.getName();
			type = userOrg.getType();
			code = userOrg.getCode();
		}
		Organization iorg =	organizationService.getOrgByCode(code);
		if(iorg.getType()==2){
			citys .add(iorg);
			countys = organizationService.getNextOrgList(iorg.getPk());
		}else if(iorg.getType()==3){
			citys .add(iorg.getParent());
			countys.add(iorg);
			schools = organizationService.getNextOrgList(iorg.getPk());
		}
		else if(iorg.getType()==4){
			citys .add(iorg.getParent().getParent());
			countys.add(iorg.getParent());
			schools.add(iorg);
			clazzs = organizationService.getNextOrgList(iorg.getPk());
		}
		else if(iorg.getType()==5){
			citys .add(iorg.getParent().getParent().getParent());
			countys.add(iorg.getParent().getParent());
			schools.add(iorg.getParent());
			clazzs.add(iorg);
		}
		
		iorg.getParent();
		
		
		
		
		//组合列表
		
		
		Page<Exam> page = newPage(currentPage, 0, request);
		//获取问卷信息
		List<Exam> exams = examService.list(page);
		
		return ModelAndViewBuilder.newInstanceFor("/doload/list")
				.append("org", userOrgs)
				.append("citys", citys)
				.append("countys", countys)
				.append("schools", schools)
				.append("clazzs", clazzs)
				.append("exams", exams)
				.build();
	}
	@RequestMapping(value="/tree/children",method = RequestMethod.GET)
	public ModelAndView childNodelist(HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("childNodelist {} select * From");
		String pk = request.getParameter("pk");
		String examid = request.getParameter("examid");
		List<Clazz> orgList = clazzService.getchildren(pk,examid);
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("orgJson",orgList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.tree.children")
						.msg("success").build()).build();
	}
}
