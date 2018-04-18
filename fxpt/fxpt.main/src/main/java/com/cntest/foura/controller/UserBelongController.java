/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.DataPermission;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserBelong;
import com.cntest.foura.service.OrganizationService;
import com.cntest.foura.service.UserBelongService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.service.ISubjectService;
import com.cntest.security.UserDetails;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;
import org.apache.shiro.subject.Subject;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年7月4日
 * @version 1.0
 **/
@Controller
@RequestMapping("/belong")
public class UserBelongController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(URLResourceController.class);
	@Autowired
	private UserBelongService userBelongService;
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationService  organizationService;
	
	@Autowired
	private ISubjectService  subjectService;
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list() throws Exception{
		logger.info("list {}",UserBelong.class);
		Subject subject = SecurityUtils.getSubject();
		UserDetails userDetail = null;
		if(subject.getPrincipal() instanceof UserDetails) {
			userDetail = (UserDetails) subject.getPrincipal();
			
		} else if(subject.getPrincipal() instanceof String) {
			String username  = (String) subject.getPrincipal();
			User user = userService.findUserBy(username);
			userDetail = user.toUserDetails();
		}
		
		User user = User.currentUser(userDetail);
		
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.userBelong.list")
								.msg(user.getName()).build()).build();
		
	}
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody UserBelong belong) throws Exception{
		logger.info("add {}",UserBelong.class);
		
		Subject subject = SecurityUtils.getSubject();
		UserDetails userDetail = null;
		if(subject.getPrincipal() instanceof UserDetails) {
			userDetail = (UserDetails) subject.getPrincipal();
			
		} else if(subject.getPrincipal() instanceof String) {
			String username  = (String) subject.getPrincipal();
			User user = userService.findUserBy(username);
			userDetail = user.toUserDetails();
		}
		User user = User.currentUser(userDetail);
		
		userBelongService.create(belong);
		
		return ModelAndViewBuilder
				.newInstanceFor("userBelong/list")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.userBelong.add")
						.msg(user.getName()).build()).build();
		
	}
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody UserBelong belong) throws Exception{
		logger.info("update {}",UserBelong.class);
		Subject subject = SecurityUtils.getSubject();
		UserDetails userDetail = null;
		if(subject.getPrincipal() instanceof UserDetails) {
			userDetail = (UserDetails) subject.getPrincipal();
			
		} else if(subject.getPrincipal() instanceof String) {
			String username  = (String) subject.getPrincipal();
			User user = userService.findUserBy(username);
			userDetail = user.toUserDetails();
		}
		User user = User.currentUser(userDetail);
		
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.userBelong.update")
								.msg(user.getName()).build()).build();
		
	}
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody UserBelong belong) throws Exception{
		logger.info("delete {}",UserBelong.class);
		Subject subject = SecurityUtils.getSubject();
		UserDetails userDetail = null;
		if(subject.getPrincipal() instanceof UserDetails) {
			userDetail = (UserDetails) subject.getPrincipal();
			
		} else if(subject.getPrincipal() instanceof String) {
			String username  = (String) subject.getPrincipal();
			User user = userService.findUserBy(username);
			userDetail = user.toUserDetails();
		}
		User user = User.currentUser(userDetail);
		
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.userBelong.delete")
								.msg(user.getName()).build()).build();
		
	}
	@RequestMapping(value="/view",method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest request) throws Exception{
		logger.info("view {}",UserBelong.class);
		Subject subject = SecurityUtils.getSubject();
		UserDetails userDetail = null;
		if(subject.getPrincipal() instanceof UserDetails) {
			userDetail = (UserDetails) subject.getPrincipal();
			
		} else if(subject.getPrincipal() instanceof String) {
			String username  = (String) subject.getPrincipal();
			User user = userService.findUserBy(username);
			userDetail = user.toUserDetails();
		}
		User user = User.currentUser(userDetail);
		
		
		
		return ModelAndViewBuilder
				.newInstanceFor("organization/edit")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.userBelong.view")
						.msg(user.getName()).build()).build();
		
	}
	/**
	 * 
	 * @param userBelong
	 * @param ids  -1表示组织为空
	 * @param subjectIds   为0时表示科目为空
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/batch/{ids}/{subjectIds}",method = RequestMethod.POST)
	public ModelAndView batchAddOrUpdate(@RequestBody UserBelong userBelong,@PathVariable String ids,@PathVariable String subjectIds) throws Exception{
		logger.info("batchAddOrUpdate {} orgIds {}",UserBelong.class);
		Subject subject = SecurityUtils.getSubject();
		UserDetails userDetail = null;
		if(subject.getPrincipal() instanceof UserDetails) {
			userDetail = (UserDetails) subject.getPrincipal();
			
		} else if(subject.getPrincipal() instanceof String) {
			String username  = (String) subject.getPrincipal();
			User user = userService.findUserBy(username);
			userDetail = user.toUserDetails();
		}
		User user = User.currentUser(userDetail);
		user.setPk(userBelong.getUser().getPk());
		List<Organization> orgs = new ArrayList<Organization>();
		String idArray[] =null;
		//数组长度
		int size=0;
		if(!"-1".equals(ids)){
			idArray=  ids.split(",");
			size+=idArray.length;
		}
		String[] subs =subjectIds.split(",");
		if(!"0".equals(subjectIds)){
			subs =subjectIds.split(",");
			size+=subs.length;
		}
		DataAuthorized[] dataAuthorizeds = new DataAuthorized[size];
		int i = 0;
		DataPermission  permission = new DataPermission();
		permission.setPk(9L);//9代表省份
		if(idArray!=null)
		for (String orgId :idArray ) {
			Organization orgd= organizationService.load(Long.valueOf(orgId));
			if(orgId.equals("-1"))
				continue;
			Organization org = new Organization();
			org.setPk(Long.valueOf(orgId));
			orgs.add(org);
			
			DataAuthorized dataA = new DataAuthorized();
			dataA.setTargetPk(user.getPk());
			dataA.setTarget("user");
			dataA.setFromTable("4a_org");
			dataA.setFromPk(Long.valueOf(orgId));
			dataA.setPermissionValue(orgId);
			dataA.setPermission(permission);
			dataA.setPermissionName(orgd.getName());
			dataAuthorizeds[i]=dataA;
			i++;
		}
		
		if(!"0".equals(subjectIds)){
			int a=0;
			permission.setPk(1L);
			for(String sb:subs){
				DataAuthorized dataB = new DataAuthorized();
				//Subject subName =;
				dataB.setTargetPk(user.getPk());
				dataB.setTarget("user");
				dataB.setFromTable("kn_subject");
				dataB.setFromPk(Long.valueOf(sb));
				dataB.setPermissionValue(sb);
				dataB.setPermission(permission);
				dataB.setPermissionName(subjectService.get(Long.parseLong(sb)).getName());
				dataAuthorizeds[i]=dataB;
				i++;
			}
		}
		if(i>0){
			//组织权限
			userService.updateRoleDataAuthorized(user, dataAuthorizeds);
		}
		//查看报告的考试权限(自动指定考试)
		if(idArray!=null)
			userService.giveExamPowerToNewUser(idArray,user.getPk());
		userBelongService.updateBelongFor(userBelong.getUser(), orgs);
		
		return ModelAndViewBuilder
				.newInstanceFor("userBelong/list")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.userBelong.batch")
						.msg(user.getName()).build()).build();
		
	}
}

